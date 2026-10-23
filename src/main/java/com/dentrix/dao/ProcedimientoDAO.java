package com.dentrix.dao;

import com.dentrix.model.Procedimiento;
import java.util.List;

public interface ProcedimientoDAO {
    List<Procedimiento> listarTodos();
    Procedimiento buscarPorId(int id);
    double obtenerPrecio(int id);
    boolean guardar(Procedimiento p);      // <-- NUEVO
    boolean actualizar(Procedimiento p);   // <-- NUEVO
    boolean eliminar(int id);              // <-- NUEVO
}