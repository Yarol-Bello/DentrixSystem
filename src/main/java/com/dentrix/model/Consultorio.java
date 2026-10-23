package com.dentrix.model;

public class Consultorio {
    private int id;
    private String nombre;
    private String especialidad;
    private int odontologoId;
    private String estado;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public int getOdontologoId() { return odontologoId; }
    public void setOdontologoId(int odontologoId) { this.odontologoId = odontologoId; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}