package com.dentrix.model;

public class ProductividadOdontologo {
    private String nombre;
    private String consultorio;
    private int atenciones;
    private double produccion;

    public ProductividadOdontologo(String nombre, String consultorio, int atenciones, double produccion) {
        this.nombre = nombre;
        this.consultorio = consultorio;
        this.atenciones = atenciones;
        this.produccion = produccion;
    }

    public String getNombre() { return nombre; }
    public String getConsultorio() { return consultorio; }
    public int getAtenciones() { return atenciones; }
    public double getProduccion() { return produccion; }
}