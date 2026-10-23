package com.dentrix.dao.impl;

import com.dentrix.dao.PacienteDAO;
import com.dentrix.model.Paciente;
import com.dentrix.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAOImpl implements PacienteDAO {

    @Override
    public boolean guardar(Paciente p) {
        String sql = "INSERT INTO pacientes (codigo_hc, dni, apellidos, nombres, fecha_nacimiento, sexo, celular, correo, direccion, alergias) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, generarNuevoCodigo());
            ps.setString(2, p.getDni());
            ps.setString(3, p.getApellidos());
            ps.setString(4, p.getNombres());
            ps.setDate(5, p.getFechaNacimiento());
            ps.setString(6, p.getSexo());
            ps.setString(7, p.getCelular());
            ps.setString(8, p.getCorreo());
            ps.setString(9, p.getDireccion());
            ps.setString(10, p.getAlergias());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar paciente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Paciente p) {
        String sql = "UPDATE pacientes SET apellidos=?, nombres=?, fecha_nacimiento=?, sexo=?, celular=?, correo=?, direccion=?, alergias=? WHERE id=?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, p.getApellidos());
            ps.setString(2, p.getNombres());
            ps.setDate(3, p.getFechaNacimiento());
            ps.setString(4, p.getSexo());
            ps.setString(5, p.getCelular());
            ps.setString(6, p.getCorreo());
            ps.setString(7, p.getDireccion());
            ps.setString(8, p.getAlergias());
            ps.setInt(9, p.getId()); // Usamos el ID único
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar paciente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM pacientes WHERE id=?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Paciente buscarPorDni(String dni) {
        String sql = "SELECT * FROM pacientes WHERE dni=?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPaciente(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar paciente: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Paciente> listarTodos() {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacientes ORDER BY apellidos ASC";
        try (Connection conn = ConexionDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearPaciente(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pacientes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public String generarNuevoCodigo() {
        String sql = "SELECT MAX(id) FROM pacientes";
        try (Connection conn = ConexionDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                int maxId = rs.getInt(1);
                return String.format("P-%03d", maxId + 1);
            }
        } catch (SQLException e) {}
        return "P-001";
    }

    private Paciente mapearPaciente(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getInt("id"));
        p.setCodigoHc(rs.getString("codigo_hc"));
        p.setDni(rs.getString("dni"));
        p.setApellidos(rs.getString("apellidos"));
        p.setNombres(rs.getString("nombres"));
        p.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
        p.setSexo(rs.getString("sexo"));
        p.setCelular(rs.getString("celular"));
        p.setCorreo(rs.getString("correo"));
        p.setDireccion(rs.getString("direccion"));
        p.setAlergias(rs.getString("alergias"));
        return p;
    }
}