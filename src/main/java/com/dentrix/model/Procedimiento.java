package com.dentrix.model;

public class Procedimiento {
    private int id;
    private String codigo;
    private String nombre;
    private String especialidad;
    private String calculo;
    private double precio;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getCalculo() { return calculo; }
    public void setCalculo(String calculo) { this.calculo = calculo; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}