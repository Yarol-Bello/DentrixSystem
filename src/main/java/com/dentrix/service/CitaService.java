package com.dentrix.service;

import com.dentrix.dao.impl.CitaDAOImpl;
import com.dentrix.dao.impl.ProcedimientoDAOImpl;
import com.dentrix.model.Cita;
import com.dentrix.model.Procedimiento;
import com.dentrix.model.Usuario;
import com.dentrix.util.ValidacionException;
import java.sql.Date;
import java.util.List;

public class CitaService {
    private final CitaDAOImpl dao;

    public CitaService() {
        this.dao = new CitaDAOImpl();
    }

    public List<Cita> listarCitasDelDia(Date fecha) {
        return dao.listarPorFecha(fecha);
    }

    public void guardarCita(Cita c, boolean esEdicion) {
        if (c.getPacienteId() <= 0) {
            throw new ValidacionException("Debe seleccionar paciente.");
        }
        if (c.getOdontologoId() <= 0) {
            throw new ValidacionException("Debe seleccionar odontólogo.");
        }

        // NUEVA VALIDACIÓN: Control de horarios
        if (c.getHoraInicio() != null && c.getHoraFin() != null) {
            if (c.getHoraInicio().after(c.getHoraFin())) {
                throw new ValidacionException("El horario es inválido, la hora de fin debe ser mayor.");
            }
        }

        if (esEdicion) {
            if (!dao.actualizar(c)) {
                throw new RuntimeException("Error al actualizar la cita en la base de datos.");
            }
        } else {
            if (!dao.guardar(c)) {
                throw new RuntimeException("Error al guardar la nueva cita.");
            }
        }
    }

    public List<Usuario> obtenerOdontologos() {
        return dao.listarOdontologos();
    }

    public List<Procedimiento> obtenerProcedimientos() {
        return new ProcedimientoDAOImpl().listarTodos();
    }
    
    public void actualizarEstadoCita(int citaId, String estado) {
        if (!dao.actualizarEstadoCita(citaId, estado)) {
            throw new RuntimeException("Error al actualizar el estado de la cita.");
        }
    }
}