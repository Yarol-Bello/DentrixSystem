package com.dentrix.dao.impl;

import com.dentrix.dao.CierreCajaDAO;
import com.dentrix.model.CierreCaja;
import com.dentrix.util.ConexionDB;
import java.sql.*;

public class CierreCajaDAOImpl implements CierreCajaDAO {

    @Override
    public boolean guardar(CierreCaja c) {
        String sql = "INSERT INTO cierres_caja (turno_id, fecha, turno, cajero_id, fondo_inicial, total_efectivo, total_digital, esperado_gaveta, contado, diferencia, numero_acta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getTurnoId()); ps.setDate(2, c.getFecha()); ps.setString(3, c.getTurno());
            ps.setInt(4, c.getCajeroId()); ps.setDouble(5, c.getFondoInicial()); ps.setDouble(6, c.getTotalEfectivo());
            ps.setDouble(7, c.getTotalDigital()); ps.setDouble(8, c.getEsperadoGaveta()); ps.setDouble(9, c.getContado());
            ps.setDouble(10, c.getDiferencia()); ps.setString(11, c.getNumeroActa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public CierreCaja buscarPorTurno(int turnoId) {
        String sql = "SELECT * FROM cierres_caja WHERE turno_id = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, turnoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CierreCaja c = new CierreCaja();
                    c.setId(rs.getInt("id"));
                    c.setTurnoId(rs.getInt("turno_id"));
                    c.setNumeroActa(rs.getString("numero_acta"));
                    c.setTotalEfectivo(rs.getDouble("total_efectivo"));
                    c.setTotalDigital(rs.getDouble("total_digital"));
                    c.setDiferencia(rs.getDouble("diferencia"));
                    return c;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}