package com.example.modelo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.exceptions.CuentaException;

public abstract class Cuenta {

	protected String nroCuenta;
	protected float saldo;
	protected List<Cliente> clientes;
	protected List<Movimiento> movimientos;
	
	
	public Cuenta(Cliente cliente) {
		this.nroCuenta = null;
		this.saldo = 0;
		this.clientes = new ArrayList<Cliente>();
		this.clientes.add(cliente);
		this.movimientos = new ArrayList<Movimiento>();
	}

	public void agregarClienteCuenta(Cliente cliente){
		this.clientes.add(cliente);
	}
	
	public int depositar(float importe) {
		if(importe > 0) {
			this.saldo += importe;
			Movimiento movimiento = new Movimiento(Calendar.getInstance().getTime(), "Deposito", importe);
			movimientos.add(movimiento);
			return movimiento.getNroMovimiento();
		}
		return 0;
	}
	
	public float obtenerSaldo() {
		return this.saldo;
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
}
