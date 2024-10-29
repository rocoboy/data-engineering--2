package com.example.modelo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.example.exceptions.CuentaException;

@Entity
public abstract class Cuenta {

    @Id
    public String nroCuenta;

    protected float saldoDisponible;

    @ManyToMany(cascade = CascadeType.ALL)
    protected List<Cliente> clientes;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, orphanRemoval = true) // Asegúrate de que esto coincida con el nombre del campo en Movimiento
    protected List<Movimiento> movimientos;

    // Constructor
    protected Cuenta() {
        this.clientes = new ArrayList<Cliente>();
        this.movimientos = new ArrayList<Movimiento>();
    }

    public Cuenta(Cliente cliente, String nroCuenta) {
        this();
        this.nroCuenta = nroCuenta;
        this.saldoDisponible = 0;
        this.clientes.add(cliente);
        cliente.vincularCuenta(this);
    }

	public void agregarClienteCuenta(Cliente cliente){
		this.clientes.add(cliente);
		System.out.println("Cuenta asociada a cliente: " + cliente.getNombre());
	}
	
	public int depositar(float importe) {
		if(importe > 0) {
			this.saldoDisponible += importe;
			Movimiento movimiento = new Movimiento(this, Calendar.getInstance().getTime(), "Deposito", importe);
			movimientos.add(movimiento);
			System.out.println("Se ha depositado en: " + this.nroCuenta + " $" + importe + " pesos\n");
			return movimiento.getNroMovimiento();
		}
		return 0;
	}
	
	public float obtenerSaldo() {
		return this.saldoDisponible;
	} 
	
	public abstract int extraer(float importe) throws CuentaException;
	
	public abstract float disponible();
	
	public List<Movimiento> verDepositosEntreFechas(Date fechaDesde, Date fechaHasta){
		List<Movimiento> resultado = new ArrayList<Movimiento>();
		for(Movimiento m : movimientos)
			if(m.soyDeposito())
				resultado.add(m);
		return resultado;
	}
	
	public List<Cliente> verClientesCuenta() {
		List<Cliente> resultado = new ArrayList<Cliente>();
		for (Cliente c : clientes)
			resultado.add(c);
		return resultado;
	}

	public List<Movimiento> verExtraccionesEntreFechas(Date fechaDesde, Date fechaHasta){
		List<Movimiento> resultado = new ArrayList<Movimiento>();
		for(Movimiento m : movimientos)
			if(!m.soyDeposito())
				resultado.add(m);
		return resultado;
	}
	
	public boolean soyLaCuenta(String nroCuenta) {
		return this.nroCuenta.equalsIgnoreCase(nroCuenta);
	}

	public List<Movimiento> movimientosDelMes(int mes) {
		List<Movimiento> resultado = new ArrayList<Movimiento>();
		for(Movimiento m : movimientos)
			if(m.soyDeEseMes(mes))
				resultado.add(m);
		return resultado;
	}

	public boolean tieneNumero(String nroCuenta) {
		return this.nroCuenta.equalsIgnoreCase(nroCuenta);
	}

	public int getMovimientos () {
		return movimientos.size();
	}
}
