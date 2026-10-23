package com.dentrix.dao;

import com.dentrix.model.Cie10;
import java.util.List;

public interface Cie10DAO {
    List<Cie10> listarTodos();
    Cie10 buscarPorCodigo(String codigo);
    boolean guardar(Cie10 c);
    boolean actualizar(Cie10 c);
    boolean eliminar(int id);
}