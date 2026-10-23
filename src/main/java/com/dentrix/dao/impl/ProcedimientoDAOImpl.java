package com.dentrix.dao.impl;

import com.dentrix.dao.ProcedimientoDAO;
import com.dentrix.model.Procedimiento;
import com.dentrix.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProcedimientoDAOImpl implements ProcedimientoDAO {

    @Override
    public List<Procedimiento> listarTodos() {
        List<Procedimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM procedimientos ORDER BY codigo";
        try (Connection conn = ConexionDB.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Procedimiento p = new Procedimiento();
                p.setId(rs.getInt("id")); p.setCodigo(rs.getString("codigo")); p.setNombre(rs.getString("nombre"));
                p.setEspecialidad(rs.getString("especialidad")); p.setCalculo(rs.getString("calculo"));
                p.setPrecio(rs.getDouble("precio"));
                lista.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public Procedimiento buscarPorId(int id) {
        String sql = "SELECT * FROM procedimientos WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Procedimiento p = new Procedimiento();
                    p.setId(rs.getInt("id")); p.setCodigo(rs.getString("codigo")); p.setNombre(rs.getString("nombre"));
                    p.setEspecialidad(rs.getString("especialidad")); p.setPrecio(rs.getDouble("precio"));
                    return p;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public double obtenerPrecio(int id) {
        String sql = "SELECT precio FROM procedimientos WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return rs.getDouble("precio"); }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }
    
        @Override
    public boolean guardar(Procedimiento p) {
        String sql = "INSERT INTO procedimientos (codigo, nombre, especialidad, calculo, precio) VALUES (?, ?, ?, ?, ?)";
        try (java.sql.Connection conn = com.dentrix.util.ConexionDB.getConnection(); 
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo()); ps.setString(2, p.getNombre());
            ps.setString(3, p.getEspecialidad()); ps.setString(4, p.getCalculo());
            ps.setDouble(5, p.getPrecio());
            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean actualizar(Procedimiento p) {
        String sql = "UPDATE procedimientos SET nombre=?, especialidad=?, calculo=?, precio=? WHERE codigo=?";
        try (java.sql.Connection conn = com.dentrix.util.ConexionDB.getConnection(); 
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre()); ps.setString(2, p.getEspecialidad());
            ps.setString(3, p.getCalculo()); ps.setDouble(4, p.getPrecio());
            ps.setString(5, p.getCodigo());
            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM procedimientos WHERE id = ?";
        try (java.sql.Connection conn = com.dentrix.util.ConexionDB.getConnection(); 
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLException e) { e.printStackTrace(); return false; }
    }
}