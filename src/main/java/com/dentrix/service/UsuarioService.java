package com.dentrix.service;

import com.dentrix.dao.impl.UsuarioDAOImpl;
import com.dentrix.model.Usuario;
import com.dentrix.util.Encriptador;
import com.dentrix.util.ValidacionException;
import java.util.List;

public class UsuarioService {
    private final UsuarioDAOImpl dao;

    public UsuarioService() {
        this.dao = new UsuarioDAOImpl();
    }

    public Usuario login(String username, String password) {
        Usuario u = dao.buscarPorUsername(username);
        if (u == null) throw new ValidacionException("Usuario no encontrado o inactivo.");
        
        if (!u.getPasswordHash().startsWith("$2a$")) {
            if (password.equals(u.getPasswordHash())) {
                String nuevoHash = Encriptador.hashPassword(password);
                actualizarPasswordEnBD(u.getId(), nuevoHash);
                return u;
            } else {
                throw new ValidacionException("Contraseña incorrecta.");
            }
        }
        if (!Encriptador.checkPassword(password, u.getPasswordHash())) {
            throw new ValidacionException("Contraseña incorrecta.");
        }
        return u;
    }

    public List<Usuario> listarTodos() {
        return dao.listarTodos();
    }

    public void registrar(Usuario u, String passwordPlana) {
        if (u.getDni() == null || !u.getDni().matches("\\d{8}")) 
            throw new ValidacionException("El DNI debe tener 8 dígitos.");
        if (u.getUsername() == null || u.getUsername().trim().isEmpty())
            throw new ValidacionException("El nombre de usuario es obligatorio.");
        if (passwordPlana == null || passwordPlana.trim().isEmpty())
            throw new ValidacionException("La contraseña es obligatoria.");

        if (dao.buscarPorUsername(u.getUsername()) != null) 
            throw new ValidacionException("El nombre de usuario ya está en uso.");
        
        u.setPasswordHash(Encriptador.hashPassword(passwordPlana));
        u.setActivo(true);

        if (!dao.registrar(u)) throw new RuntimeException("Error al guardar el usuario en BD.");
    }

    public void actualizar(Usuario u, String nuevaPassword) {
        if (nuevaPassword != null && !nuevaPassword.trim().isEmpty()) {
            u.setPasswordHash(Encriptador.hashPassword(nuevaPassword));
        } else {
            // Si no cambió la contraseña, conservamos la anterior
            Usuario old = dao.buscarPorUsername(u.getUsername());
            if(old != null) u.setPasswordHash(old.getPasswordHash());
        }

        if (!dao.actualizar(u)) throw new RuntimeException("Error al actualizar el usuario.");
    }

    private void actualizarPasswordEnBD(int usuarioId, String nuevoHash) {
        String sql = "UPDATE usuarios SET password_hash = ? WHERE id = ?";
        try (java.sql.Connection conn = com.dentrix.util.ConexionDB.getConnection(); 
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoHash);
            ps.setInt(2, usuarioId);
            ps.executeUpdate();
        } catch (java.sql.SQLException e) {
            System.err.println("Error al actualizar hash: " + e.getMessage());
        }
    }
    
        public void cambiarEstadoUsuario(int id, boolean activo) {
        if (!dao.cambiarEstado(id, activo)) {
            throw new RuntimeException("Error al cambiar el estado del usuario.");
        }
    }
}