package com.dentrix.dao.impl;

import com.dentrix.model.Cita;
import com.dentrix.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAOImpl {

    public List<Cita> listarCitasPorFecha(Date fecha) {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT c.*, p.apellidos || ', ' || p.nombres AS pac_nombre, " +
                     "u.apellidos || ', ' || u.nombres AS odo_nombre, pr.nombre AS proc_nombre " +
                     "FROM citas c " +
                     "JOIN pacientes p ON c.paciente_id = p.id " +
                     "JOIN usuarios u ON c.odontologo_id = u.id " +
                     "LEFT JOIN procedimientos pr ON c.procedimiento_id = pr.id " +
                     "WHERE c.fecha = ? ORDER BY c.hora_inicio ASC";
                     
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, fecha);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita c = new Cita();
                    c.setId(rs.getInt("id"));
                    c.setPacienteId(rs.getInt("paciente_id"));
                    c.setPacienteNombre(rs.getString("pac_nombre"));
                    c.setOdontologoId(rs.getInt("odontologo_id"));
                    c.setOdontologoNombre(rs.getString("odo_nombre"));
                    c.setProcedimientoId(rs.getInt("procedimiento_id"));
                    c.setProcedimientoNombre(rs.getString("proc_nombre"));
                    c.setConsultorio(rs.getString("consultorio"));
                    c.setFecha(rs.getDate("fecha"));
                    c.setHoraInicio(rs.getTime("hora_inicio"));
                    c.setHoraFin(rs.getTime("hora_fin"));
                    c.setEstado(rs.getString("estado"));
                    c.setObservaciones(rs.getString("observaciones"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar citas: " + e.getMessage());
        }
        return lista;
    }

    public boolean guardar(Cita c) {
        String sql = "INSERT INTO citas (paciente_id, odontologo_id, procedimiento_id, consultorio, fecha, hora_inicio, hora_fin, estado, observaciones) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getPacienteId());
            ps.setInt(2, c.getOdontologoId());
            ps.setInt(3, c.getProcedimientoId());
            ps.setString(4, c.getConsultorio());
            ps.setDate(5, c.getFecha());
            ps.setTime(6, c.getHoraInicio());
            ps.setTime(7, c.getHoraFin());
            ps.setString(8, c.getEstado());
            ps.setString(9, c.getObservaciones());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar cita: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Cita c) {
        String sql = "UPDATE citas SET paciente_id=?, odontologo_id=?, procedimiento_id=?, consultorio=?, fecha=?, hora_inicio=?, hora_fin=?, estado=?, observaciones=? WHERE id=?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getPacienteId());
            ps.setInt(2, c.getOdontologoId());
            ps.setInt(3, c.getProcedimientoId());
            ps.setString(4, c.getConsultorio());
            ps.setDate(5, c.getFecha());
            ps.setTime(6, c.getHoraInicio());
            ps.setTime(7, c.getHoraFin());
            ps.setString(8, c.getEstado());
            ps.setString(9, c.getObservaciones());
            ps.setInt(10, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar cita: " + e.getMessage());
            return false;
        }
    }

    // Para llenar los ComboBox de la vista
    public List<String[]> listarOdontologos() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT id, apellidos || ', ' || nombres AS nombre FROM usuarios WHERE rol IN ('ADMIN', 'ODONTOLOGO') AND activo = true";
        try (Connection conn = ConexionDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new String[]{String.valueOf(rs.getInt("id")), rs.getString("nombre")});
            }
        } catch (SQLException e) { }
        return lista;
    }

    public List<String[]> listarProcedimientos() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM procedimientos";
        try (Connection conn = ConexionDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new String[]{String.valueOf(rs.getInt("id")), rs.getString("nombre")});
            }
        } catch (SQLException e) { }
        return lista;
    }
}