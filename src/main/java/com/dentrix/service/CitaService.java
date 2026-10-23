package com.dentrix.service;

import com.dentrix.dao.impl.CitaDAOImpl;
import com.dentrix.model.Cita;
import com.google.common.base.Preconditions;
import java.sql.Date;
import java.util.List;

public class CitaService {

    private final CitaDAOImpl citaDAO;

    public CitaService() {
        this.citaDAO = new CitaDAOImpl();
    }

    public List<Cita> obtenerCitasDelDia(Date fecha) {
        return citaDAO.listarCitasPorFecha(fecha);
    }

    public void guardarCita(Cita c, boolean esEdicion) {
        Preconditions.checkArgument(c.getPacienteId() > 0, "Debe seleccionar un paciente.");
        Preconditions.checkArgument(c.getOdontologoId() > 0, "Debe seleccionar un odontólogo.");
        Preconditions.checkArgument(c.getProcedimientoId() > 0, "Debe seleccionar un tratamiento.");
        Preconditions.checkArgument(c.getFecha() != null, "Debe seleccionar una fecha.");
        Preconditions.checkArgument(c.getHoraInicio() != null, "Debe seleccionar una hora.");

        // Validar cruce de horarios
        List<Cita> citasDelDia = citaDAO.listarCitasPorFecha(c.getFecha());
        for (Cita existente : citasDelDia) {
            // Si estamos editando, ignoramos la misma cita
            if (esEdicion && existente.getId() == c.getId()) continue;

            if (existente.getOdontologoId() == c.getOdontologoId()) {
                if (c.getHoraInicio().before(existente.getHoraFin()) && c.getHoraFin().after(existente.getHoraInicio())) {
                    throw new IllegalArgumentException("El odontólogo ya tiene una cita en ese horario.");
                }
            }
        }

        boolean exito = esEdicion ? citaDAO.actualizar(c) : citaDAO.guardar(c);
        if (!exito) {
            throw new RuntimeException("Error al guardar la cita en la BD.");
        }
    }

    public List<String[]> obtenerOdontologos() { return citaDAO.listarOdontologos(); }
    public List<String[]> obtenerProcedimientos() { return citaDAO.listarProcedimientos(); }
}