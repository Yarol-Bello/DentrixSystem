package com.dentrix.model;

public class FichaDetalle {
    private int id;
    private int fichaId;
    private int procedimientoId;
    private String piezaDental;
    private String caraDental;
    private String diagnostico;
    private String estado;
    private String procedimientoNombre; // Útil para mostrar en tablas
    private double precioAplicado;
    private double subtotal;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getFichaId() { return fichaId; }
    public void setFichaId(int fichaId) { this.fichaId = fichaId; }
    public int getProcedimientoId() { return procedimientoId; }
    public void setProcedimientoId(int procedimientoId) { this.procedimientoId = procedimientoId; }
    public String getPiezaDental() { return piezaDental; }
    public void setPiezaDental(String piezaDental) { this.piezaDental = piezaDental; }
    public String getCaraDental() { return caraDental; }
    public void setCaraDental(String caraDental) { this.caraDental = caraDental; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getProcedimientoNombre() { return procedimientoNombre; }
    public void setProcedimientoNombre(String procedimientoNombre) { this.procedimientoNombre = procedimientoNombre; }
    public double getPrecioAplicado() { return precioAplicado; }
    public void setPrecioAplicado(double precioAplicado) { this.precioAplicado = precioAplicado; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}