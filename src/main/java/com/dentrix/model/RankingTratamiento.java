package com.dentrix.model;

public class RankingTratamiento {
    private String codigo;
    private String nombre;
    private int cantidad;
    private double facturacion;

    public RankingTratamiento(String codigo, String nombre, int cantidad, double facturacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.facturacion = facturacion;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public double getFacturacion() { return facturacion; }
}