package com.dentrix.dao.impl;

import com.dentrix.dao.ConsultorioDAO;
import com.dentrix.model.Consultorio;
import com.dentrix.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultorioDAOImpl implements ConsultorioDAO {

    @Override
    public List<Consultorio> listarTodos() {
        List<Consultorio> lista = new ArrayList<>();
        String sql = "SELECT * FROM consultorios ORDER BY nombre";
        try (Connection conn = ConexionDB.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Consultorio c = new Consultorio();
                c.setId(rs.getInt("id")); c.setNombre(rs.getString("nombre")); c.setEspecialidad(rs.getString("especialidad"));
                c.setOdontologoId(rs.getInt("odontologo_id")); c.setEstado(rs.getString("estado"));
                lista.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean guardar(Consultorio c) {
        String sql = "INSERT INTO consultorios (nombre, especialidad, odontologo_id, estado) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre()); ps.setString(2, c.getEspecialidad());
            ps.setInt(3, c.getOdontologoId()); ps.setString(4, c.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    @Override
    public boolean actualizarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE consultorios SET estado = ? WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado); ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}