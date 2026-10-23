package com.dentrix.dao;

import com.dentrix.model.Paciente;
import java.util.List;

public interface PacienteDAO {
    boolean guardar(Paciente p);
    boolean actualizar(Paciente p);
    boolean eliminar(int id);
    Paciente buscarPorDni(String dni);
    Paciente buscarPorId(int id);
    List<Paciente> listarTodos();
    String generarNuevoCodigo();
}