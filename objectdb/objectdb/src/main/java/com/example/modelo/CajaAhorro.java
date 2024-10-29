package com.example.modelo;

import java.util.Calendar;

import javax.persistence.Entity;

import com.example.exceptions.CuentaException;

@Entity
public class CajaAhorro extends Cuenta {
	private float tasaDeInteres;

	public CajaAhorro(Cliente cliente, String nroCuenta) {
		super(cliente, nroCuenta);
		this.tasaDeInteres = 1.12f;
	}

	public CajaAhorro() {
	}

	@Override
	public int extraer(float importe) throws CuentaException {
		if(this.saldoDisponible > importe) {
			this.saldoDisponible -= importe;
			Movimiento movimiento = new Movimiento(this, Calendar.getInstance().getTime(), "Extraccion", importe);
			this.movimientos.add(movimiento);
			System.out.println("Extracción a la cuenta: " + nroCuenta + " de $" + importe + "\n");
			return movimiento.getNroMovimiento();
		}
		else
			throw new CuentaException("No hay saldo suficiente para realizar la extracción");
	}

	@Override
	public float disponible() {
		return this.saldoDisponible;
	}

	public float getTasaDeInteres() {
		return tasaDeInteres;
	}

}
