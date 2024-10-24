package com.example.modelo;

import java.util.Calendar;

import com.example.exceptions.CuentaException;

public class CajaAhorro extends Cuenta {
	private float tasaInteres;
	
	public CajaAhorro(Cliente cliente) {
		super(cliente);
		this.tasaInteres = 1.8f;
	}

	@Override
	public int extraer(float importe) throws CuentaException {
		if(this.saldo > importe) {
			this.saldo -= importe;
			Movimiento movimiento = new Movimiento(Calendar.getInstance().getTime(), "Extraccion", importe);
			this.movimientos.add(movimiento);
			return movimiento.getNroMovimiento();
		}
		else
			throw new CuentaException("Saldo Insuficiente");
	}

	@Override
	public float disponible() {
		return this.saldo;
	}

	public float getTasaInteres() {
		return tasaInteres;
	}

}
