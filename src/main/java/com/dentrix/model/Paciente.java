package com.dentrix.model;

import java.sql.Date;

public class Paciente {
    private int id;
    private String codigoHc;
    private String dni;
    private String apellidos;
    private String nombres;
    private Date fechaNacimiento;
    private String sexo;
    private String celular;
    private String correo;
    private String direccion;
    private String alergias;

    // Constructor vacío, getters y setters
    public Paciente() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCodigoHc() { return codigoHc; }
    public void setCodigoHc(String codigoHc) { this.codigoHc = codigoHc; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }
}