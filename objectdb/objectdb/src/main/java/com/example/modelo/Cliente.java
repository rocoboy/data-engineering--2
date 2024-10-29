package com.example.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.example.exceptions.CuentaException;

@Entity
public class Cliente {

    @Id
    private String nroDocumento;

    @ManyToMany(mappedBy = "clientes")
    private List<Cuenta> cuentas;

    private String nombreCompleto;

    public Cliente(String nombre, String documento) {
        this.nombreCompleto = nombre;
        this.nroDocumento = documento;
        this.cuentas = new ArrayList<Cuenta>();
    }
	public float saldoEnCuenta(String nroCuenta) throws CuentaException {
		for(Cuenta c : cuentas)
			if(c.soyLaCuenta(nroCuenta))
				return c.obtenerSaldo();
	    throw new CuentaException("No existe esa cuenta para ese cliente");
	}
	
	public float posicion() {
		float resultado = 0;
		for(Cuenta c : cuentas)
			resultado += c.obtenerSaldo();
		return resultado;
	}
	
	public List<Movimiento> movimientosMes(int mes, String nroCuenta) throws CuentaException{
		for(Cuenta c : cuentas)
			if(c.soyLaCuenta(nroCuenta)) {
				return c.movimientosDelMes(mes);
			}
		throw new CuentaException("No existe esa cuenta para ese cliente");
	}
	
	public String getNombre() {
		return nombreCompleto;
	}

	public String getDocumento() {
		return nroDocumento;
	}
	
	public boolean tieneDocumento(String documento) {
		return this.nroDocumento.equalsIgnoreCase(documento);
	}

	public void agregarCuenta(Cuenta cuenta) {
		if (clientePerteneceCuenta(cuenta)) {
			System.out.println("\nEl cliente ya esta asociado a esta cuenta! \n");
		} else {
			cuentas.add(cuenta);
			cuenta.agregarClienteCuenta(this);
		}
	}

	public void vincularCuenta(Cuenta cuenta) {
		cuentas.add(cuenta);
	}

	public void setNombre(String nombre) {
		this.nombreCompleto = nombre;
	}

	private boolean clientePerteneceCuenta(Cuenta cuenta) {
		return cuentas.contains(cuenta);
	}

	public String obtenerCuentas(){

		StringBuilder nroCuentas = new StringBuilder();
		for (Cuenta cuenta : cuentas){
			nroCuentas.append(cuenta.nroCuenta).append(", ");
		}

		return nroCuentas.toString();
	}
}
