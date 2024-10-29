package com.example.controlador;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.example.exceptions.CuentaException;
import com.example.modelo.CajaAhorro;
import com.example.modelo.Cliente;
import com.example.modelo.Cuenta;
import com.example.modelo.CuentaCorriente;
import com.example.modelo.Movimiento;
import com.example.views.MovimientoView;

public class Controlador {
private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;

	public Controlador() {
		this.entityManagerFactory = javax.persistence.Persistence.createEntityManagerFactory("Cuentas.odb");
		this.entityManager = entityManagerFactory.createEntityManager();
	}
	

	// CRUD de Cliente
	// ----------------------------------------------------------------------------------------------------------------------------

	public void crearCliente(String nombre, String documento) {
		if (!existeCliente(documento)) {
			entityManager.getTransaction().begin();
			Cliente nuevoCliente = new Cliente(nombre, documento);
			entityManager.persist(nuevoCliente);
			entityManager.getTransaction().commit();
			System.out.println("Cliente creado con exito\n");
		} else {
			System.out.println("Ya existe un cliente con ese documento!\n");
		}
	}

	public void obtenerTodosLosClientes() {
		List<Cliente> clientes = entityManager.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
		System.out.println("Listando Clientes:\n");
		for (Cliente cliente : clientes) {
			System.out.println(cliente.getNombre() + "\n");
		}
	}

	public void borrarCliente(String documento) {
		Cliente cliente = this.buscarCliente(documento);
		if (cliente != null) {
			System.out.println("Borrando cliente " + cliente.getNombre());
			entityManager.getTransaction().begin();
			entityManager.remove(cliente);
			entityManager.getTransaction().commit();
			System.out.println("Cliente eliminado con exito\n");
		} else {
			System.out.println("No se encontro el cliente\n");
		}
	}

	public void modificarCliente(String documento, String nombre) {
		Cliente cliente = this.buscarCliente(documento);
		if (cliente != null) {
			System.out.println("Modificando cliente " + cliente.getNombre() + "\n");
			entityManager.getTransaction().begin();
			cliente.setNombre(nombre);
			entityManager.getTransaction().commit();
			System.out.println("Cliente modificado con exito\n");
		} else {
			System.out.println("No se encontro el cliente\n");
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	// CRUD de Cuenta y Operaciones
	// ----------------------------------------------------------------------------------------------------------------------------

	public void crearCuenta(String documento, String tipoCuenta, String nroCuenta) {
		Cliente cliente = this.buscarCliente(documento);
		if (cliente != null) {
			Cuenta cuenta = this.buscarCuenta(nroCuenta);
			if (cuenta != null) {
				System.out.println("Ya existe una cuenta con el numero: " + nroCuenta + "!\n");
			} else {
				// Comenzar una transacción a la DB
				entityManager.getTransaction().begin();
				if (tipoCuenta.equalsIgnoreCase("Cuenta Corriente")) {
					CuentaCorriente nuevaCuenta = new CuentaCorriente(cliente, nroCuenta, 50000f, 1500f, 0.023f);
					entityManager.persist(nuevaCuenta);
					System.out.println("Cuenta Corriente creada con exito\n");
				} else {
					CajaAhorro nuevaCajaAhorro = new CajaAhorro(cliente, nroCuenta);
					entityManager.persist(nuevaCajaAhorro);
					System.out.println("Caja de Ahorro creada con exito\n");
				}
				entityManager.getTransaction().commit();
			}
		}
	}

	public void agregarCuentaCliente(String documento, String nroCuenta) {
		Cliente cliente = this.buscarCliente(documento);
		if (cliente != null) {
			Cuenta cuenta = this.buscarCuenta(nroCuenta);
			if (cuenta != null) {
				System.out.println(
						"Agregando el cliente con documento: " + documento + " a la cuenta con número: "
								+ nroCuenta);
				entityManager.getTransaction().begin();
				cliente.agregarCuenta(cuenta);
				entityManager.getTransaction().commit();
			} else {
				System.out.println("La cuenta con número: " + nroCuenta + " no fue encontrada");
			}
		} else {
			System.out.println("El cliente con documento: " + documento + " no fue encontrado");
		}
	}

	public int depositar(String nroCuenta, float importe) {
		Cuenta cuenta = this.buscarCuenta(nroCuenta);
		if (cuenta != null) {
			entityManager.getTransaction().begin();
			int nroMovimiento = cuenta.depositar(importe);
			entityManager.getTransaction().commit();
			return nroMovimiento;
		} else {
			System.out.println("No se encontro la cuenta\n");
			return 0;
		}
	}

	public int extraer(String nroCuenta, float importe) throws CuentaException {
		Cuenta cuenta = this.buscarCuenta(nroCuenta);
		if (cuenta != null) {
			entityManager.getTransaction().begin();
			int nroMovimiento = cuenta.extraer(importe);
			entityManager.getTransaction().commit();
			return nroMovimiento;
		} else {
			System.out.println("No se encontro la cuenta\n");
			throw new CuentaException("Error de Extracción");
		}
	}

	public float disponible(String nroCuenta) {
		Cuenta cuenta = this.buscarCuenta(nroCuenta);
		return cuenta.disponible();
	}

	public float saldoEnCuenta(String nroCuenta) {
		Cuenta cuenta = this.buscarCuenta(nroCuenta);
		return cuenta != null ? cuenta.obtenerSaldo() : 0.0f;
	}

	public float posicionCliente(String documento) {
		Cliente cliente = entityManager.find(Cliente.class, documento);
		if (cliente != null) {
			return cliente.posicion();
		} else {
			return -1;
		}
	}

	public List<MovimientoView> obtenerMovimientos(String nroCuenta, int mes) {
		List<MovimientoView> resultado = new ArrayList<>();
		Cuenta cuenta = this.buscarCuenta(nroCuenta);
		if (cuenta != null) {
			List<Movimiento> movimientos = cuenta.movimientosDelMes(mes);
			for (Movimiento movimiento : movimientos) {
				resultado.add(movimiento.toView());
			}
			return resultado;
		} else {
			System.out.println("No se encontro la cuenta\n");
			return new ArrayList<>();
		}
	}

	public List<Cliente> obtenerClientes(String nroCuenta) {
		Cuenta cuenta = entityManager.find(Cuenta.class, nroCuenta);
		if (cuenta != null) {
			List<Cliente> resultado = cuenta.verClientesCuenta();
			System.out.println("\nCuenta encontrada, obteniendo clientes... \n");
			return resultado;
		} else {
			System.out.println("Cuenta no encontrada! \n");
			return new ArrayList<>();
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	// --------------- Métodos privados ---------------- ---------------------

	// Busca en la DB el cliente por documento con el metodo find de EntityManager
	// (El documento es la primary key de Cliente)

	private Cliente buscarCliente(String documento) {
		Cliente cliente = entityManager.find(Cliente.class, documento);
		if (cliente != null) {
			return cliente;
		} else {
			return null;
		}
	}

	// Determina si existe un cliente o no mediante el documento
	private boolean existeCliente(String documento) {
		return this.buscarCliente(documento) != null;
	}

	// Busca en la DB la cuenta por nroCuenta con el metodo find de EntityManager
	private Cuenta buscarCuenta(String nroCuenta) {
		Cuenta cuenta = entityManager.find(Cuenta.class, nroCuenta);
		if (cuenta != null) {
			return cuenta;
		} else {
			// System.out.println("No se encontro la cuenta");
			return null;
		}
	}}
