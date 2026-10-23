package com.dentrix.service;

import com.dentrix.dao.PacienteDAO;
import com.dentrix.dao.impl.PacienteDAOImpl;
import com.dentrix.model.Paciente;
import com.google.common.base.Preconditions;
import java.util.List;

public class PacienteService {

    private final PacienteDAO pacienteDAO;

    public PacienteService() {
        this.pacienteDAO = new PacienteDAOImpl();
    }

    public void registrarPaciente(Paciente p) {
        validarDatos(p);
        if (pacienteDAO.buscarPorDni(p.getDni()) != null) {
            throw new IllegalArgumentException("Ya existe un paciente con ese DNI.");
        }
        if (!pacienteDAO.guardar(p)) {
            throw new RuntimeException("Error al guardar en BD.");
        }
    }

    public void actualizarPaciente(Paciente p) {
        validarDatos(p);
        // Verificar si el DNI ya le pertenece a otro paciente
        Paciente existente = pacienteDAO.buscarPorDni(p.getDni());
        if (existente != null && existente.getId() != p.getId()) {
            throw new IllegalArgumentException("El DNI ya está registrado por otro paciente.");
        }
        if (!pacienteDAO.actualizar(p)) {
            throw new RuntimeException("Error al actualizar en BD.");
        }
    }

    public void eliminarPaciente(int id) {
        if (!pacienteDAO.eliminar(id)) {
            throw new RuntimeException("Error al eliminar el paciente.");
        }
    }

    public List<Paciente> obtenerTodos() {
        return pacienteDAO.listarTodos();
    }

    public Paciente buscarPorDni(String dni) {
        return pacienteDAO.buscarPorDni(dni);
    }

    // Validaciones estrictas con Guava
    private void validarDatos(Paciente p) {
        Preconditions.checkArgument(p.getDni() != null && p.getDni().matches("\\d{8}"), "El DNI debe contener exactamente 8 números.");
        Preconditions.checkArgument(p.getNombres() != null && !p.getNombres().trim().isEmpty(), "El nombre es obligatorio.");
        Preconditions.checkArgument(p.getApellidos() != null && !p.getApellidos().trim().isEmpty(), "El apellido es obligatorio.");
        Preconditions.checkArgument(p.getCelular() == null || p.getCelular().matches("\\d{9}"), "El celular debe contener exactamente 9 números.");
        Preconditions.checkArgument(p.getCorreo() == null || p.getCorreo().isEmpty() || p.getCorreo().matches("^[A-Za-z0-9+_.-]+@(.+)$"), "El formato del correo no es válido.");
    }
}