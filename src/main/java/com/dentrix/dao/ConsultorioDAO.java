package com.dentrix.dao;
import com.dentrix.model.Consultorio;
import java.util.List;

public interface ConsultorioDAO {
    List<Consultorio> listarTodos();
    boolean guardar(Consultorio c);
    boolean actualizarEstado(int id, String nuevoEstado);
}