package com.dentrix.dao;

import com.dentrix.model.Paciente;
import java.util.List;

public interface PacienteDAO {
    boolean guardar(Paciente paciente);
    boolean actualizar(Paciente paciente);
    boolean eliminar(int id);
    Paciente buscarPorDni(String dni);
    List<Paciente> listarTodos();
    String generarNuevoCodigo();
}