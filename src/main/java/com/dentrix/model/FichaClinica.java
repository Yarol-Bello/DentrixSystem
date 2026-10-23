package com.dentrix.model;

import java.sql.Date;
import java.util.List;

public class FichaClinica {
    private int id;
    private int pacienteId;
    private int citaId;
    private int odontologoId;
    private Date fecha;
    private String anamnesis;
    private String diagnosticoCie10;
    private String planTratamiento;
    private String prescripcion;
    private double totalPresupuesto;
    private double montoPagado;
    private double saldo;
    private String estadoPresupuesto;
    private String pacienteNombre;
    private List<FichaDetalle> detalles;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPacienteId() { return pacienteId; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }
    public int getCitaId() { return citaId; }
    public void setCitaId(int citaId) { this.citaId = citaId; }
    public int getOdontologoId() { return odontologoId; }
    public void setOdontologoId(int odontologoId) { this.odontologoId = odontologoId; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public String getAnamnesis() { return anamnesis; }
    public void setAnamnesis(String anamnesis) { this.anamnesis = anamnesis; }
    public String getDiagnosticoCie10() { return diagnosticoCie10; }
    public void setDiagnosticoCie10(String diagnosticoCie10) { this.diagnosticoCie10 = diagnosticoCie10; }
    public String getPlanTratamiento() { return planTratamiento; }
    public void setPlanTratamiento(String planTratamiento) { this.planTratamiento = planTratamiento; }
    public String getPrescripcion() { return prescripcion; }
    public void setPrescripcion(String prescripcion) { this.prescripcion = prescripcion; }
    public double getTotalPresupuesto() { return totalPresupuesto; }
    public void setTotalPresupuesto(double totalPresupuesto) { this.totalPresupuesto = totalPresupuesto; }
    public double getMontoPagado() { return montoPagado; }
    public void setMontoPagado(double montoPagado) { this.montoPagado = montoPagado; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
    public String getEstadoPresupuesto() { return estadoPresupuesto; }
    public void setEstadoPresupuesto(String estadoPresupuesto) { this.estadoPresupuesto = estadoPresupuesto; }
    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }
    public List<FichaDetalle> getDetalles() { return detalles; }
    public void setDetalles(List<FichaDetalle> detalles) { this.detalles = detalles; }
}