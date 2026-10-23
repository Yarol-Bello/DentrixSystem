package com.dentrix.dao.impl;

import com.dentrix.dao.CitaDAO;
import com.dentrix.model.Cita;
import com.dentrix.model.Usuario;
import com.dentrix.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAOImpl implements CitaDAO {

    @Override
    public List<Cita> listarPorFecha(Date fecha) {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT c.*, p.dni, p.apellidos || ', ' || p.nombres AS pac_nombre, " +
                     "u.apellidos || ', ' || u.nombres AS odo_nombre, pr.nombre AS proc_nombre " +
                     "FROM citas c JOIN pacientes p ON c.paciente_id = p.id " +
                     "JOIN usuarios u ON c.odontologo_id = u.id " +
                     "LEFT JOIN procedimientos pr ON c.procedimiento_id = pr.id " +
                     "WHERE c.fecha = ? ORDER BY c.hora_inicio ASC";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, fecha);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita c = new Cita();
                    c.setId(rs.getInt("id")); c.setPacienteId(rs.getInt("paciente_id"));
                    c.setDniPaciente(rs.getString("dni")); c.setPacienteNombre(rs.getString("pac_nombre"));
                    c.setOdontologoId(rs.getInt("odontologo_id")); c.setOdontologoNombre(rs.getString("odo_nombre"));
                    c.setProcedimientoId(rs.getInt("procedimiento_id")); c.setProcedimientoNombre(rs.getString("proc_nombre"));
                    c.setConsultorio(rs.getString("consultorio")); c.setFecha(rs.getDate("fecha"));
                    c.setHoraInicio(rs.getTime("hora_inicio")); c.setHoraFin(rs.getTime("hora_fin"));
                    c.setTurno(rs.getString("turno")); c.setEstado(rs.getString("estado"));
                    c.setObservaciones(rs.getString("observaciones"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean guardar(Cita c) {
        String sql = "INSERT INTO citas (paciente_id, odontologo_id, procedimiento_id, consultorio, fecha, hora_inicio, hora_fin, turno, estado, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'PENDIENTE', ?)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getPacienteId()); ps.setInt(2, c.getOdontologoId()); ps.setInt(3, c.getProcedimientoId());
            ps.setString(4, c.getConsultorio()); ps.setDate(5, c.getFecha());
            ps.setTime(6, c.getHoraInicio()); ps.setTime(7, c.getHoraFin());
            ps.setString(8, c.getTurno()); ps.setString(9, c.getObservaciones());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean actualizar(Cita c) {
        String sql = "UPDATE citas SET paciente_id=?, odontologo_id=?, procedimiento_id=?, consultorio=?, fecha=?, hora_inicio=?, hora_fin=?, turno=?, observaciones=? WHERE id=?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getPacienteId()); ps.setInt(2, c.getOdontologoId()); ps.setInt(3, c.getProcedimientoId());
            ps.setString(4, c.getConsultorio()); ps.setDate(5, c.getFecha());
            ps.setTime(6, c.getHoraInicio()); ps.setTime(7, c.getHoraFin());
            ps.setString(8, c.getTurno()); ps.setString(9, c.getObservaciones());
            ps.setInt(10, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean actualizarEstadoCita(int citaId, String nuevoEstado) {
        String sql = "UPDATE citas SET estado = ? WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado); ps.setInt(2, citaId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public List<Usuario> listarOdontologos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE rol = 'ODONTOLOGO' AND activo = true ORDER BY apellidos";
        try (Connection conn = ConexionDB.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombres(rs.getString("nombres"));
                u.setApellidos(rs.getString("apellidos"));
                u.setConsultorioAsignado(rs.getString("consultorio_asignado"));
                lista.add(u);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}