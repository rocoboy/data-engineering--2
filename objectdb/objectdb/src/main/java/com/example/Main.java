package com.example;

import java.util.List;

import com.example.controlador.Controlador;
import com.example.exceptions.CuentaException;
import com.example.modelo.Cliente;
import com.example.views.MovimientoView;

public class Main {
    public static void main(String[] args) {

		// inicializamos un controlador
		Controlador controlador = new Controlador();

		//
		System.err.println("\ncreamos 3 clientes para tener datos");
		System.out.println("-----------------------------------------------------------------------------");
		controlador.crearCliente("Juan Perez", "1234567");
		controlador.crearCliente("Ricardo Montaner", "3444555");
		controlador.crearCliente("Juan Carlos Messi", "6777888");

		//
		System.err.println("\nmuestro los clientes que tengo");
		System.out.println("-----------------------------------------------------------------------------");
		controlador.obtenerTodosLosClientes();

		// 
		System.err.println("\nmodifico un cliente y muestro todos nuevamente");
		System.out.println("-----------------------------------------------------------------------------");
		controlador.modificarCliente("1234567", "Juan Martinez");
		controlador.obtenerTodosLosClientes();

		// 
		System.err.println("\nCreo 3 cuentas corrientes");
		System.out.println("-----------------------------------------------------------------------------");
		controlador.crearCuenta("1234567", "Cuenta Corriente", "123");
		controlador.crearCuenta("3444555", "Cuenta Corriente", "456");
		controlador.crearCuenta("6777888", "Cuenta Corriente", "789");

		// 
		System.err.println("\nVeo el estado de cada cuenta");
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("Saldo Inicial de la cuenta 123: $" + controlador.saldoEnCuenta("123") + "\n");
		System.out.println("Saldo Inicial de la cuenta 456: $" + controlador.saldoEnCuenta("456") + "\n");
		System.out.println("Saldo Inicial de la cuenta 789: $" + controlador.saldoEnCuenta("789") + "\n");


		// 
		System.err.println("\nRealizo un depósito de prueba");
		System.out.println("-----------------------------------------------------------------------------");
		controlador.depositar("456", 1000);
		System.out.println("Saldo después del deposito la cuenta 123: $" + controlador.saldoEnCuenta("123") + "\n");
		System.out.println(("\nOperacion de deposito con ID: " + controlador.depositar("123", 5000)));
        
		
		//
		System.err.println("\n trato de realizar una extracción");
		System.out.println("-----------------------------------------------------------------------------");
		try {
            controlador.extraer("123", 500);
        } catch (CuentaException e) {
            System.out.println("Error al extraer: " + e.getMessage());
        }
		System.out.println(
				"Saldo después de la extracción en la cuenta 123: $" + controlador.saldoEnCuenta("123") + "\n");
		System.out.println(
				"Saldo después del depósito en la cuenta 456: $" + controlador.saldoEnCuenta("456") + "\n");


		// 
		System.err.println("\nVeo los movimientos de la primera cuenta");
		System.out.println("-----------------------------------------------------------------------------\n");
		List<MovimientoView> MovimientosdeCuenta = controlador.obtenerMovimientos("123", 9);
		System.out.println("\nListando Transacciones de la cuenta 123\n");
		for (MovimientoView movimiento : MovimientosdeCuenta) {
			System.out.println(
					"\nNro de Movimiento: " + movimiento.getNroMovimiento() + " Tipo de Movimiento: "
							+ movimiento.getTipoMovimiento() + " Monto: $" + movimiento.getImporte() + "\n");
		}

		// 
		System.err.println("\nObtengo los clientes de la cuenta 123");
		System.out.println("-----------------------------------------------------------------------------\n");
		List<Cliente> ClientesdeCuenta = controlador.obtenerClientes("123");
		for (Cliente ClienteActual : ClientesdeCuenta) {
			System.out.println(
					"Nombre del Cliente: " + ClienteActual.getNombre() + " Documento: " + ClienteActual.getDocumento()
							+ " Posición: $" + ClienteActual.posicion()
							+ "\n");
		}


		// 
		System.err.println("\nObtengo los clientes de la cuenta 123");
		System.out.println("-----------------------------------------------------------------------------\n");
		controlador.agregarCuentaCliente("6777888", "456");

		ClientesdeCuenta = controlador.obtenerClientes("456");

		for (Cliente ClienteActual : ClientesdeCuenta) {
			System.out.println(
					"Nombre del Cliente: " + ClienteActual.getNombre() + " Documento: " +
							ClienteActual.getDocumento()
							+ " Posición: $" + ClienteActual.posicion()
							+ "\n");
		}

	}
}