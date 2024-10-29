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
            try {
                Cliente nuevoCliente = new Cliente(nombre, documento);
                entityManager.persist(nuevoCliente);
                entityManager.getTransaction().commit();
                System.out.println("Cliente creado con éxito");
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                System.out.println("Error al crear cliente: " + e.getMessage());
            }
        } else {
            System.out.println("Ya existe un cliente con ese documento!");
        }
    }

    public void obtenerTodosLosClientes() {
        List<Cliente> clientes = entityManager.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        System.out.println("Listando Clientes:\n");
        for (Cliente cliente : clientes) {
            System.out.println(cliente.getNombre() + ", ");
        }
    }

    public void borrarCliente(String documento) {
        Cliente cliente = this.buscarCliente(documento);
        if (cliente != null) {
            System.out.println("Borrando cliente " + cliente.getNombre());
            entityManager.getTransaction().begin();
            try {
                entityManager.remove(cliente);
                entityManager.getTransaction().commit();
                System.out.println("Cliente eliminado con éxito\n");
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                System.out.println("Error al eliminar cliente: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró el cliente\n");
        }
    }

    public void modificarCliente(String documento, String nombre) {
        Cliente cliente = this.buscarCliente(documento);
        if (cliente != null) {
            System.out.println("Modificando cliente " + cliente.getNombre() + "\n");
            entityManager.getTransaction().begin();
            try {
                cliente.setNombre(nombre);
                entityManager.getTransaction().commit();
                System.out.println("Cliente modificado con éxito\n");
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                System.out.println("Error al modificar cliente: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró el cliente\n");
        }
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    // CRUD de Cuenta y Operaciones
    // ----------------------------------------------------------------------------------------------------------------------------

    public void crearCuenta(String documento, String tipoCuenta, String nroCuenta) {
        Cliente cliente = this.buscarCliente(documento);
        if (cliente != null) {
            Cuenta cuentaExistente = this.buscarCuenta(nroCuenta);
            if (cuentaExistente != null) {
                System.out.println("Ya existe una cuenta con el número: " + nroCuenta + "!\n");
            } else {
                entityManager.getTransaction().begin();
                try {
                    Cuenta nuevaCuenta;
                    if (tipoCuenta.equalsIgnoreCase("Cuenta Corriente")) {
                        nuevaCuenta = new CuentaCorriente(cliente, nroCuenta, 50000f, 1500f, 0.023f);
                    } else {
                        nuevaCuenta = new CajaAhorro(cliente, nroCuenta); // Asegúrate de que esto esté bien
                    }
                    entityManager.persist(nuevaCuenta); // Aquí persistes la nueva cuenta
                    entityManager.getTransaction().commit();
                    System.out.println("Cuenta creada con éxito\n");
                } catch (Exception e) {
                    if (entityManager.getTransaction().isActive()) {
                        entityManager.getTransaction().rollback();
                    }
                    System.out.println("Error al crear cuenta: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Cliente no encontrado");
        }
    }
        
    
    public void agregarCuentaCliente(String documento, String nroCuenta) {
        Cliente cliente = this.buscarCliente(documento);
        if (cliente != null) {
            Cuenta cuenta = this.buscarCuenta(nroCuenta);
            if (cuenta != null) {
                System.out.println("Agregando el cliente con documento: " + documento + " a la cuenta con número: " + nroCuenta);
                entityManager.getTransaction().begin();
                try {
                    cliente.agregarCuenta(cuenta);
                    entityManager.getTransaction().commit();
                } catch (Exception e) {
                    entityManager.getTransaction().rollback();
                    System.out.println("Error al agregar cuenta al cliente: " + e.getMessage());
                }
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
            try {
                int nroMovimiento = cuenta.depositar(importe);
                entityManager.getTransaction().commit();
                return nroMovimiento;
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                System.out.println("Error al depositar: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró la cuenta\n");
        }
        return 0;
    }

    public int extraer(String nroCuenta, float importe) throws CuentaException {
        Cuenta cuenta = this.buscarCuenta(nroCuenta);
        if (cuenta != null) {
            entityManager.getTransaction().begin();
            try {
                int nroMovimiento = cuenta.extraer(importe);
                entityManager.getTransaction().commit();
                return nroMovimiento;
            } catch (CuentaException e) {
                entityManager.getTransaction().rollback();
                throw e; // Propaga la excepción
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                System.out.println("Error al extraer: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró la cuenta\n");
            throw new CuentaException("Error de Extracción");
        }
        return 0;
    }

    public float disponible(String nroCuenta) {
        Cuenta cuenta = this.buscarCuenta(nroCuenta);
        if (cuenta != null) {
            return cuenta.disponible();
        } else {
            System.out.println("Cuenta no encontrada");
            return 0.0f; // O lanza una excepción si lo prefieres
        }
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
        List<MovimientoView> resultado = new ArrayList<MovimientoView>();
        Cuenta cuenta = this.buscarCuenta(nroCuenta);
        if (cuenta != null) {
            List<Movimiento> movimientos = cuenta.movimientosDelMes(mes);
            for (Movimiento movimiento : movimientos) {
                resultado.add(movimiento.toView());
            }
            return resultado;
        } else {
            System.out.println("No se encontró la cuenta\n");
            return new ArrayList<MovimientoView>();
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
            return new ArrayList<Cliente>();
        }
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    // --------------- Métodos privados ---------------- ---------------------

    // Busca en la DB el cliente por documento con el método find de EntityManager
    // (El documento es la primary key de Cliente)

    private Cliente buscarCliente(String documento) {
        return entityManager.find(Cliente.class, documento);
    }

    // Determina si existe un cliente o no mediante el documento
    private boolean existeCliente(String documento) {
        return this.buscarCliente(documento) != null;
    }

    // Busca en la DB la cuenta por nroCuenta con el método find de EntityManager
    public Cuenta buscarCuenta(String nroCuenta) {
        return entityManager.find(Cuenta.class, nroCuenta);
    }

    public void cerrar() {
        if (entityManager != null) {
            entityManager.close();
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
