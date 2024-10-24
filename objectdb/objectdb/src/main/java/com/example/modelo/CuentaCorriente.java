package com.example.modelo;

import java.util.Calendar;

import com.example.exceptions.CuentaException;

public class CuentaCorriente extends Cuenta {

	private float descubiertoHabilitado;
	private float costoMantenimiento;
	private float tasaDiariaDescubierto;

	public CuentaCorriente(Cliente cliente,float descubiertoHabilitado, float costoMantenimiento, float tasaDiariaDescubierto) {
		super(cliente);
		this.descubiertoHabilitado = descubiertoHabilitado;
		this.costoMantenimiento = costoMantenimiento;
		this.tasaDiariaDescubierto = tasaDiariaDescubierto;
	}

	@Override
	public int extraer(float importe) throws CuentaException {
		if(this.saldo + this.descubiertoHabilitado > importe) {
			this.saldo -= importe;
			Movimiento movimiento = new Movimiento(Calendar.getInstance().getTime(), "Extraccion", importe);
			this.movimientos.add(movimiento);
			return movimiento.getNroMovimiento();
		}
		else
			throw new CuentaException("El saldo es insuficiente");
	}

	@Override
	public float disponible() {
		return this.saldo + this.descubiertoHabilitado;
	}

	public float getDescubiertoHabilitado() {
		return descubiertoHabilitado;
	}

	public float getCostoMantenimiento() {
		return costoMantenimiento;
	}

	public float getTasaDiariaDescubierto() {
		return tasaDiariaDescubierto;
	}

}
