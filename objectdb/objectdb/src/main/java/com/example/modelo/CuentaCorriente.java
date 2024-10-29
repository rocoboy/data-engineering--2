package com.example.modelo;

import java.util.Calendar;

import javax.persistence.Entity;

import com.example.exceptions.CuentaException;

@Entity
public class CuentaCorriente extends Cuenta {

    private float descubiertoHabilitado;
    private float costoMantenimiento;
    private float tasaDiariaDescubierto;

    public CuentaCorriente(Cliente cliente, String nroCuenta, float descubiertoHabilitado, float costoMantenimiento,
                           float tasaDiariaDescubierto) {
        super(cliente, nroCuenta);
        this.descubiertoHabilitado = descubiertoHabilitado;
        this.costoMantenimiento = costoMantenimiento;
        this.tasaDiariaDescubierto = tasaDiariaDescubierto;
    }

    public CuentaCorriente() {
        // Aquí podrías inicializar los valores por defecto
    }

    @Override
    public int extraer(float importe) throws CuentaException {
        if (this.saldoDisponible  + this.descubiertoHabilitado >= importe) {
            this.saldoDisponible  -= importe;  // Asegúrate de que "saldo" está definido en la superclase
            Movimiento movimiento = new Movimiento(this, Calendar.getInstance().getTime(), "Extracción", importe);
            this.movimientos.add(movimiento);
            System.out.println("Extracción a la cuenta: " + nroCuenta + " de $" + importe + "\n");
            return movimiento.getNroMovimiento();
        } else {
            throw new CuentaException("El saldo es insuficiente");
        }
    }

    @Override
    public float disponible() {
        return this.saldoDisponible  + this.descubiertoHabilitado;
    }

    // Getters para los atributos adicionales
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
