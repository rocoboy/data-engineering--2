package com.example.modelo;

import java.util.Calendar;

import javax.persistence.Entity;

import com.example.exceptions.CuentaException;

@Entity

public class CajaAhorro extends Cuenta {
	private float tasaInteres;

	public CajaAhorro(Cliente cliente, String nroCuenta) {
		super(cliente, nroCuenta);
		this.tasaInteres = 1.8f;
	}

	public CajaAhorro() {
	}

	@Override
	public int extraer(float importe) throws CuentaException {
		if(this.saldo > importe) {
			this.saldo -= importe;
			Movimiento movimiento = new Movimiento(this, Calendar.getInstance().getTime(), "Extraccion", importe);
			this.movimientos.add(movimiento);
			System.out.println("Extracción a la cuenta: " + nroCuenta + " de $" + importe + " pesos\n");
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
