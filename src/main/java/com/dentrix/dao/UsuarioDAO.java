package com.dentrix.dao;

import com.dentrix.model.Usuario;
import java.util.List;

public interface UsuarioDAO {
    Usuario buscarPorUsername(String username);
    List<Usuario> listarTodos();
    boolean registrar(Usuario u);
    boolean actualizar(Usuario u);
    boolean cambiarEstado(int id, boolean activo); // <-- NUEVO
}