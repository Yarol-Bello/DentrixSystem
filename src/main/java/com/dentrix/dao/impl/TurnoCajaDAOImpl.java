package com.dentrix.dao.impl;

import com.dentrix.dao.TurnoCajaDAO;
import com.dentrix.model.TurnoCaja;
import com.dentrix.util.ConexionDB;
import java.sql.*;

public class TurnoCajaDAOImpl implements TurnoCajaDAO {

    @Override
    public TurnoCaja obtenerTurnoAbierto() {
        String sql = "SELECT * FROM turnos_caja WHERE estado = 'ABIERTO' ORDER BY id DESC LIMIT 1";
        try (Connection conn = ConexionDB.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                TurnoCaja t = new TurnoCaja();
                t.setId(rs.getInt("id")); t.setFecha(rs.getDate("fecha")); t.setTurno(rs.getString("turno"));
                t.setFondoInicial(rs.getDouble("fondo_inicial")); t.setEstado(rs.getString("estado"));
                return t;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean abrirTurno(TurnoCaja turno) {
        String sql = "INSERT INTO turnos_caja (fecha, turno, cajero_apertura_id, fondo_inicial, estado) VALUES (?, ?, ?, ?, 'ABIERTO')";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, turno.getFecha()); ps.setString(2, turno.getTurno());
            ps.setInt(3, turno.getCajeroAperturaId()); ps.setDouble(4, turno.getFondoInicial());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    @Override
    public boolean cerrarTurno(int turnoId, int cajeroCierreId) {
        String sql = "UPDATE turnos_caja SET estado = 'CERRADO', hora_cierre = CURRENT_TIMESTAMP, cajero_cierre_id = ? WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cajeroCierreId); ps.setInt(2, turnoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}