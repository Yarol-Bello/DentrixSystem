package com.dentrix.model;

import java.sql.Timestamp;

public class Pago {
    private int id;
    private int fichaId;
    private int pacienteId;
    private int turnoId;
    private double monto;
    private String metodoPago;
    private String numeroOperacion;
    private String comprobante;
    private double saldoRestante;
    private Timestamp fechaPago;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getFichaId() { return fichaId; }
    public void setFichaId(int fichaId) { this.fichaId = fichaId; }
    public int getPacienteId() { return pacienteId; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }
    public int getTurnoId() { return turnoId; }
    public void setTurnoId(int turnoId) { this.turnoId = turnoId; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getNumeroOperacion() { return numeroOperacion; }
    public void setNumeroOperacion(String numeroOperacion) { this.numeroOperacion = numeroOperacion; }
    public String getComprobante() { return comprobante; }
    public void setComprobante(String comprobante) { this.comprobante = comprobante; }
    public double getSaldoRestante() { return saldoRestante; }
    public void setSaldoRestante(double saldoRestante) { this.saldoRestante = saldoRestante; }
    public Timestamp getFechaPago() { return fechaPago; }
    public void setFechaPago(Timestamp fechaPago) { this.fechaPago = fechaPago; }
}