package com.dentrix.model;

import java.sql.Date;
import java.sql.Timestamp;

public class CierreCaja {
    private int id;
    private int turnoId;
    private Date fecha;
    private String turno;
    private int cajeroId;
    private double fondoInicial;
    private double totalEfectivo;
    private double totalDigital;
    private double esperadoGaveta;
    private double contado;
    private double diferencia;
    private String numeroActa;
    private Timestamp fechaCierre;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getTurnoId() { return turnoId; }
    public void setTurnoId(int turnoId) { this.turnoId = turnoId; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
    public int getCajeroId() { return cajeroId; }
    public void setCajeroId(int cajeroId) { this.cajeroId = cajeroId; }
    public double getFondoInicial() { return fondoInicial; }
    public void setFondoInicial(double fondoInicial) { this.fondoInicial = fondoInicial; }
    public double getTotalEfectivo() { return totalEfectivo; }
    public void setTotalEfectivo(double totalEfectivo) { this.totalEfectivo = totalEfectivo; }
    public double getTotalDigital() { return totalDigital; }
    public void setTotalDigital(double totalDigital) { this.totalDigital = totalDigital; }
    public double getEsperadoGaveta() { return esperadoGaveta; }
    public void setEsperadoGaveta(double esperadoGaveta) { this.esperadoGaveta = esperadoGaveta; }
    public double getContado() { return contado; }
    public void setContado(double contado) { this.contado = contado; }
    public double getDiferencia() { return diferencia; }
    public void setDiferencia(double diferencia) { this.diferencia = diferencia; }
    public String getNumeroActa() { return numeroActa; }
    public void setNumeroActa(String numeroActa) { this.numeroActa = numeroActa; }
    public Timestamp getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(Timestamp fechaCierre) { this.fechaCierre = fechaCierre; }
}