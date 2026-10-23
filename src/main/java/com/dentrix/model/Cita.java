package com.dentrix.model;

import java.sql.Date;
import java.sql.Time;

public class Cita {
    private int id;
    private int pacienteId;
    private String pacienteNombre; // Para mostrar en la tabla sin hacer JOIN extra
    private int odontologoId;
    private String odontologoNombre;
    private int procedimientoId;
    private String procedimientoNombre;
    private String consultorio;
    private Date fecha;
    private Time horaInicio;
    private Time horaFin;
    private String estado;
    private String observaciones;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPacienteId() { return pacienteId; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }
    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }
    public int getOdontologoId() { return odontologoId; }
    public void setOdontologoId(int odontologoId) { this.odontologoId = odontologoId; }
    public String getOdontologoNombre() { return odontologoNombre; }
    public void setOdontologoNombre(String odontologoNombre) { this.odontologoNombre = odontologoNombre; }
    public int getProcedimientoId() { return procedimientoId; }
    public void setProcedimientoId(int procedimientoId) { this.procedimientoId = procedimientoId; }
    public String getProcedimientoNombre() { return procedimientoNombre; }
    public void setProcedimientoNombre(String procedimientoNombre) { this.procedimientoNombre = procedimientoNombre; }
    public String getConsultorio() { return consultorio; }
    public void setConsultorio(String consultorio) { this.consultorio = consultorio; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public Time getHoraInicio() { return horaInicio; }
    public void setHoraInicio(Time horaInicio) { this.horaInicio = horaInicio; }
    public Time getHoraFin() { return horaFin; }
    public void setHoraFin(Time horaFin) { this.horaFin = horaFin; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}