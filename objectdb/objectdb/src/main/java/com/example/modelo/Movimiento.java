package com.example.modelo;

import java.util.Date;

import com.example.views.MovimientoView;

public class Movimiento {
	
	private static int numerador = 0;
	private int nroMovimiento;
	private Date fecha;
	private String tipoMovimiento;
	private float importe;
	
	public Movimiento(Date fecha, String tipoMovimiento, float importe) {
		this.nroMovimiento = numerador++;
		this.fecha = fecha;
		this.tipoMovimiento = tipoMovimiento;
		this.importe = importe;
	}

	public int getNroMovimiento() {
		return this.nroMovimiento;
	}
	public boolean soyDeEseMes(int mes) {
		 return this.fecha.getMonth() == mes;
	}
	
	public boolean soyDeposito() {
		return this.tipoMovimiento.equalsIgnoreCase("Deposito");
	}
	
	public float obtenerImporte() {
		return this.importe;
	}

	public MovimientoView toView() {
		return new MovimientoView(nroMovimiento,fecha, tipoMovimiento, importe);
	}
}
