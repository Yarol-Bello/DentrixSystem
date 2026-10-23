package com.dentrix.model;

import java.sql.Date;
import java.sql.Timestamp;

public class TurnoCaja {
    private int id;
    private Date fecha;
    private String turno;
    private int cajeroAperturaId;
    private int cajeroCierreId;
    private double fondoInicial;
    private Timestamp horaApertura;
    private Timestamp horaCierre;
    private String estado;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
    public int getCajeroAperturaId() { return cajeroAperturaId; }
    public void setCajeroAperturaId(int cajeroAperturaId) { this.cajeroAperturaId = cajeroAperturaId; }
    public int getCajeroCierreId() { return cajeroCierreId; }
    public void setCajeroCierreId(int cajeroCierreId) { this.cajeroCierreId = cajeroCierreId; }
    public double getFondoInicial() { return fondoInicial; }
    public void setFondoInicial(double fondoInicial) { this.fondoInicial = fondoInicial; }
    public Timestamp getHoraApertura() { return horaApertura; }
    public void setHoraApertura(Timestamp horaApertura) { this.horaApertura = horaApertura; }
    public Timestamp getHoraCierre() { return horaCierre; }
    public void setHoraCierre(Timestamp horaCierre) { this.horaCierre = horaCierre; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}