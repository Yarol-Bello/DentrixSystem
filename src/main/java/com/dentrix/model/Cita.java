package com.dentrix.model;

import java.sql.Date;
import java.sql.Time;

public class Cita {
    private int id;
    private int pacienteId;
    private int odontologoId;
    private int procedimientoId;
    private String pacienteNombre;
    private String odontologoNombre;
    private String procedimientoNombre;
    private String dniPaciente;
    private String consultorio;
    private Date fecha;
    private Time horaInicio;
    private Time horaFin;
    private String turno;
    private String estado;
    private String observaciones;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPacienteId() { return pacienteId; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }
    public int getOdontologoId() { return odontologoId; }
    public void setOdontologoId(int odontologoId) { this.odontologoId = odontologoId; }
    public int getProcedimientoId() { return procedimientoId; }
    public void setProcedimientoId(int procedimientoId) { this.procedimientoId = procedimientoId; }
    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }
    public String getOdontologoNombre() { return odontologoNombre; }
    public void setOdontologoNombre(String odontologoNombre) { this.odontologoNombre = odontologoNombre; }
    public String getProcedimientoNombre() { return procedimientoNombre; }
    public void setProcedimientoNombre(String procedimientoNombre) { this.procedimientoNombre = procedimientoNombre; }
    public String getDniPaciente() { return dniPaciente; }
    public void setDniPaciente(String dniPaciente) { this.dniPaciente = dniPaciente; }
    public String getConsultorio() { return consultorio; }
    public void setConsultorio(String consultorio) { this.consultorio = consultorio; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public Time getHoraInicio() { return horaInicio; }
    public void setHoraInicio(Time horaInicio) { this.horaInicio = horaInicio; }
    public Time getHoraFin() { return horaFin; }
    public void setHoraFin(Time horaFin) { this.horaFin = horaFin; }
    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}