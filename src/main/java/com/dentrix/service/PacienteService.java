package com.dentrix.service;

import com.dentrix.dao.impl.PacienteDAOImpl;
import com.dentrix.model.Paciente;
import com.dentrix.util.ValidacionException;
import java.util.List;

public class PacienteService {
    private final PacienteDAOImpl dao;

    public PacienteService() { 
        this.dao = new PacienteDAOImpl(); 
    }

    public void registrarPaciente(Paciente p) {
        if (p.getDni() == null || !p.getDni().matches("\\d{8}")) 
            throw new ValidacionException("El DNI debe tener 8 dígitos.");
        if (dao.buscarPorDni(p.getDni()) != null) 
            throw new ValidacionException("Ya existe un paciente con ese DNI.");
        
        if (!dao.guardar(p)) throw new RuntimeException("Error al guardar el paciente.");
    }

    // ESTE ES EL MÉTODO QUE TE FALTA
    public void actualizarPaciente(Paciente p) {
        if (!dao.actualizar(p)) {
            throw new RuntimeException("Error al actualizar el paciente en la base de datos.");
        }
    }

    // ESTE TAMBIÉN FALTA
    public void eliminarPaciente(int id) {
        if (!dao.eliminar(id)) {
            throw new RuntimeException("Error al eliminar el paciente. Puede tener citas asociadas.");
        }
    }

    public List<Paciente> listarTodos() { 
        return dao.listarTodos(); 
    }
    
    public Paciente buscarPorDni(String dni) { 
        return dao.buscarPorDni(dni); 
    }
}