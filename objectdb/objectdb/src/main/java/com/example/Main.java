package com.example;

import java.util.List;

import com.example.controlador.Controlador;
import com.example.exceptions.CuentaException;
import com.example.modelo.Cliente;
import com.example.views.MovimientoView;

public class Main {
    public static void main(String[] args) {

		Controlador controlador = new Controlador();

		// Creo 2 clientes, modifico a uno y obtengo todos los clientes

		controlador.crearCliente("Peter Scherz", "43723950");

		controlador.crearCliente("Juanjo Alonso", "12345678");

		controlador.obtenerTodosLosClientes();

		controlador.modificarCliente("43723950", "Max Scherz");

		controlador.obtenerTodosLosClientes();

		// Creo 2 cuentas corrientes

		controlador.crearCuenta("43723950", "Cuenta Corriente", "12345");

		controlador.crearCuenta("43723950", "Cuenta Corriente", "56789");

		// Obtengo el saldo inicial de ambas

		System.out.println("-----------------------------------------------------------------------------");

		System.out.println("Saldo Inicial de la cuenta 12345: $" + controlador.saldoEnCuenta("12345") + "\n");

		controlador.depositar("12345", 1000);

		System.out.println("Saldo después del deposito la cuenta 12345: $" + controlador.saldoEnCuenta("12345") + "\n");

		System.out.println(("\nOperacion de deposito con ID: " + controlador.depositar("56789", 5000)));

        try {
            controlador.extraer("12345", 500);
        } catch (CuentaException e) {
            // Manejar la excepción aquí, por ejemplo:
            System.out.println("Error al extraer: " + e.getMessage());
        }

		System.out.println(
				"Saldo después de la extracción en la cuenta 12345: $" + controlador.saldoEnCuenta("12345") + "\n");

		System.out.println(
				"Saldo después del depósito en la cuenta 56789: $" + controlador.saldoEnCuenta("56789") + "\n");

		System.out.println("-----------------------------------------------------------------------------\n");

		// Obtengo los movimientos de la primera cuenta y los listo

		List<MovimientoView> MovimientosdeCuenta = controlador.obtenerMovimientos("12345", 9);

		System.out.println("\nListando Transacciones de la cuenta 12345\n");

		for (MovimientoView movimiento : MovimientosdeCuenta) {
			System.out.println(
					"\nNro de Movimiento: " + movimiento.getNroMovimiento() + " Tipo de Movimiento: "
							+ movimiento.getTipoMovimiento() + " Monto: $" + movimiento.getImporte() + "\n");
		}

		System.out.println("-----------------------------------------------------------------------------\n");

		// Obtengo los clientes de la cuenta 12345

		List<Cliente> ClientesdeCuenta = controlador.obtenerClientes("12345");

		for (Cliente ClienteActual : ClientesdeCuenta) {
			System.out.println(
					"Nombre del Cliente: " + ClienteActual.getNombre() + " Documento: " + ClienteActual.getDocumento()
							+ " Posición: $" + ClienteActual.posicion()
							+ "\n");
		}
		// Agrego el cliente Juanjo Alonso a la cuenta 12345

		controlador.agregarCuentaCliente("12345678", "12345");

		// Obtengo devuelta los clientes para verificar que estén bien

		ClientesdeCuenta = controlador.obtenerClientes("12345");

		for (Cliente ClienteActual : ClientesdeCuenta) {
			System.out.println(
					"Nombre del Cliente: " + ClienteActual.getNombre() + " Documento: " +
							ClienteActual.getDocumento()
							+ " Posición: $" + ClienteActual.posicion()
							+ "\n");
		}

	}
}