package com.dentrix.dao.impl;

import com.dentrix.dao.UsuarioDAO;
import com.dentrix.model.Usuario;
import com.dentrix.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public Usuario buscarPorUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND activo = true";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id")); u.setDni(rs.getString("dni")); u.setUsername(rs.getString("username"));
                    u.setPasswordHash(rs.getString("password_hash")); u.setNombres(rs.getString("nombres"));
                    u.setApellidos(rs.getString("apellidos")); u.setRol(rs.getString("rol"));
                    u.setConsultorioAsignado(rs.getString("consultorio_asignado")); u.setActivo(rs.getBoolean("activo"));
                    return u;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY apellidos";
        try (Connection conn = ConexionDB.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id")); u.setDni(rs.getString("dni")); u.setUsername(rs.getString("username"));
                u.setNombres(rs.getString("nombres")); u.setApellidos(rs.getString("apellidos"));
                u.setRol(rs.getString("rol")); u.setConsultorioAsignado(rs.getString("consultorio_asignado"));
                u.setActivo(rs.getBoolean("activo"));
                lista.add(u);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean registrar(Usuario u) {
        String sql = "INSERT INTO usuarios (dni, username, password_hash, nombres, apellidos, rol, consultorio_asignado, activo) VALUES (?, ?, ?, ?, ?, ?, ?, true)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getDni()); ps.setString(2, u.getUsername()); ps.setString(3, u.getPasswordHash());
            ps.setString(4, u.getNombres()); ps.setString(5, u.getApellidos()); ps.setString(6, u.getRol());
            ps.setString(7, u.getConsultorioAsignado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
    
        @Override
    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET dni=?, username=?, password_hash=?, nombres=?, apellidos=?, rol=?, consultorio_asignado=?, activo=? WHERE id=?";
        try (java.sql.Connection conn = com.dentrix.util.ConexionDB.getConnection(); 
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getDni());
            ps.setString(2, u.getUsername());
            ps.setString(3, u.getPasswordHash());
            ps.setString(4, u.getNombres());
            ps.setString(5, u.getApellidos());
            ps.setString(6, u.getRol());
            ps.setString(7, u.getConsultorioAsignado());
            ps.setBoolean(8, u.isActivo());
            ps.setInt(9, u.getId());
            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLException e) { e.printStackTrace(); return false; }
    }
    
        @Override
    public boolean cambiarEstado(int id, boolean activo) {
        String sql = "UPDATE usuarios SET activo = ? WHERE id = ?";
        try (java.sql.Connection conn = com.dentrix.util.ConexionDB.getConnection(); 
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, activo);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLException e) { e.printStackTrace(); return false; }
    }
}