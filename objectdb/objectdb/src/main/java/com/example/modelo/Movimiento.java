package com.example.modelo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.example.views.MovimientoView;

@Entity
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cuenta cuenta;

    private int nroMovimiento;
    private Date fecha;  // Si prefieres usar LocalDate, puedes cambiar esto.
    private String tipoMovimiento;
    private float importe;

    public Movimiento(Cuenta cuenta, Date fecha, String tipoMovimiento, float importe) {
        this.cuenta = cuenta;
        this.nroMovimiento = cuenta.getMovimientos();
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.importe = importe;
    }

    public Movimiento() {
    }

    public int getNroMovimiento() {
        return this.nroMovimiento;
    }

    public String getTipoMovimiento() {
        return this.tipoMovimiento;
    }

    public boolean soyDeEseMes(int mes) {
        LocalDate localDate = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getMonthValue() == mes;
    }

    public boolean soyDeposito() {
        return this.tipoMovimiento.equalsIgnoreCase("Deposito");
    }

    public float obtenerImporte() {
        return this.importe;
    }

    public MovimientoView toView() {
        return new MovimientoView(nroMovimiento, fecha, tipoMovimiento, importe);
    }
}
