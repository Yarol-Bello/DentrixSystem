package com.dentrix.service;

import com.dentrix.model.Cita;
import com.dentrix.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DashboardService {

    public int countCitasHoy(LocalDate fecha) {
        String sql = "SELECT COUNT(*) FROM citas WHERE fecha = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public int countCitasPorEstado(String estado, LocalDate fecha) {
        String sql = "SELECT COUNT(*) FROM citas WHERE estado = ? AND fecha = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setDate(2, java.sql.Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public List<Cita> listarCitasHoy(LocalDate fecha) {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT c.*, p.dni, p.apellidos || ', ' || p.nombres AS pac_nombre, " +
                     "u.apellidos || ', ' || u.nombres AS odo_nombre, pr.nombre AS proc_nombre " +
                     "FROM citas c JOIN pacientes p ON c.paciente_id = p.id " +
                     "JOIN usuarios u ON c.odontologo_id = u.id " +
                     "LEFT JOIN procedimientos pr ON c.procedimiento_id = pr.id " +
                     "WHERE c.fecha = ? ORDER BY c.hora_inicio ASC";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita c = new Cita();
                    c.setId(rs.getInt("id"));
                    c.setDniPaciente(rs.getString("dni"));
                    c.setPacienteNombre(rs.getString("pac_nombre"));
                    c.setOdontologoNombre(rs.getString("odo_nombre"));
                    c.setProcedimientoNombre(rs.getString("proc_nombre"));
                    c.setHoraInicio(rs.getTime("hora_inicio"));
                    c.setEstado(rs.getString("estado"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}