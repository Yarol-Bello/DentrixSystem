package com.dentrix.service;

import com.dentrix.model.ProductividadOdontologo;
import com.dentrix.model.RankingTratamiento;
import com.dentrix.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReporteService {

    public double getRecaudacionTotal() {
        String sql = "SELECT SUM(monto) FROM pagos WHERE DATE_TRUNC('month', fecha_pago) = DATE_TRUNC('month', CURRENT_DATE)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    public int getPacientesAtendidosMes() {
        String sql = "SELECT COUNT(DISTINCT paciente_id) FROM fichas_clinicas WHERE DATE_TRUNC('month', fecha) = DATE_TRUNC('month', CURRENT_DATE)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public double getRecaudacionDigital() {
        String sql = "SELECT SUM(monto) FROM pagos WHERE metodo_pago IN ('YAPE', 'PLIN') AND DATE_TRUNC('month', fecha_pago) = DATE_TRUNC('month', CURRENT_DATE)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    public double getSaldosPorCobrar() {
        String sql = "SELECT SUM(saldo) FROM fichas_clinicas WHERE estado_presupuesto IN ('ACEPTADO', 'ABONO')";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    public double getRecaudacionEfectivo() {
        String sql = "SELECT SUM(monto) FROM pagos WHERE metodo_pago = 'EFECTIVO' AND DATE_TRUNC('month', fecha_pago) = DATE_TRUNC('month', CURRENT_DATE)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    public List<RankingTratamiento> getRankingTratamientos() {
        List<RankingTratamiento> lista = new ArrayList<>();
        String sql = "SELECT p.codigo, p.nombre, COUNT(fd.id) as cant, SUM(fd.subtotal) as total " +
                     "FROM ficha_detalles fd " +
                     "JOIN procedimientos p ON fd.procedimiento_id = p.id " +
                     "JOIN fichas_clinicas f ON fd.ficha_id = f.id " +
                     "WHERE DATE_TRUNC('month', f.fecha) = DATE_TRUNC('month', CURRENT_DATE) " +
                     "GROUP BY p.codigo, p.nombre ORDER BY cant DESC LIMIT 5";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new RankingTratamiento(rs.getString("codigo"), rs.getString("nombre"), rs.getInt("cant"), rs.getDouble("total")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<ProductividadOdontologo> getProductividadOdontologos() {
        List<ProductividadOdontologo> lista = new ArrayList<>();
        String sql = "SELECT u.apellidos || ', ' || u.nombres as nombre, u.consultorio_asignado as consultorio, " +
                     "COUNT(f.id) as atenciones, SUM(f.total_presupuesto) as produccion " +
                     "FROM fichas_clinicas f " +
                     "JOIN usuarios u ON f.odontologo_id = u.id " +
                     "WHERE DATE_TRUNC('month', f.fecha) = DATE_TRUNC('month', CURRENT_DATE) " +
                     "GROUP BY u.id, u.apellidos, u.nombres, u.consultorio_asignado ORDER BY produccion DESC";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new ProductividadOdontologo(rs.getString("nombre"), rs.getString("consultorio"), rs.getInt("atenciones"), rs.getDouble("produccion")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}