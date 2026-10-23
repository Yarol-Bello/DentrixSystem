package com.dentrix.dao.impl;

import com.dentrix.dao.Cie10DAO;
import com.dentrix.model.Cie10;
import com.dentrix.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Cie10DAOImpl implements Cie10DAO {

    @Override
    public List<Cie10> listarTodos() {
        List<Cie10> lista = new ArrayList<>();
        String sql = "SELECT * FROM cie10 ORDER BY codigo";
        try (Connection conn = ConexionDB.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Cie10 c = new Cie10();
                c.setId(rs.getInt("id")); c.setCodigo(rs.getString("codigo")); c.setNombre(rs.getString("nombre"));
                c.setGrupo(rs.getString("grupo")); c.setPrioridad(rs.getString("prioridad")); c.setDescripcion(rs.getString("descripcion"));
                lista.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public Cie10 buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM cie10 WHERE codigo = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cie10 c = new Cie10();
                    c.setId(rs.getInt("id")); c.setCodigo(rs.getString("codigo")); c.setNombre(rs.getString("nombre"));
                    c.setGrupo(rs.getString("grupo")); c.setPrioridad(rs.getString("prioridad")); c.setDescripcion(rs.getString("descripcion"));
                    return c;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean guardar(Cie10 c) {
        String sql = "INSERT INTO cie10 (codigo, nombre, grupo, prioridad, descripcion) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCodigo()); ps.setString(2, c.getNombre());
            ps.setString(3, c.getGrupo()); ps.setString(4, c.getPrioridad());
            ps.setString(5, c.getDescripcion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean actualizar(Cie10 c) {
        String sql = "UPDATE cie10 SET nombre=?, grupo=?, prioridad=?, descripcion=? WHERE id=?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre()); ps.setString(2, c.getGrupo());
            ps.setString(3, c.getPrioridad()); ps.setString(4, c.getDescripcion());
            ps.setInt(5, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM cie10 WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}