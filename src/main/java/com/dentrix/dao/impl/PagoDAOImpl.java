package com.dentrix.dao.impl;

import com.dentrix.dao.PagoDAO;
import com.dentrix.model.Pago;
import com.dentrix.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoDAOImpl implements PagoDAO {

    @Override
    public boolean registrarPago(Pago pago, int fichaId, double nuevoSaldo, String nuevoEstado) {
        String sqlPago = "INSERT INTO pagos (ficha_id, paciente_id, turno_id, monto, metodo_pago, numero_operacion, comprobante, saldo_restante) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlFicha = "UPDATE fichas_clinicas SET monto_pagado = monto_pagado + ?, saldo = ?, estado_presupuesto = ? WHERE id = ?";

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psPago = conn.prepareStatement(sqlPago);
                 PreparedStatement psFicha = conn.prepareStatement(sqlFicha)) {
                 
                psPago.setInt(1, pago.getFichaId()); psPago.setInt(2, pago.getPacienteId()); psPago.setInt(3, pago.getTurnoId());
                psPago.setDouble(4, pago.getMonto()); psPago.setString(5, pago.getMetodoPago()); psPago.setString(6, pago.getNumeroOperacion());
                psPago.setString(7, pago.getComprobante()); psPago.setDouble(8, pago.getSaldoRestante());
                psPago.executeUpdate();

                psFicha.setDouble(1, pago.getMonto()); psFicha.setDouble(2, nuevoSaldo);
                psFicha.setString(3, nuevoEstado); psFicha.setInt(4, fichaId);
                psFicha.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) { conn.rollback(); e.printStackTrace(); return false; }
        } catch (SQLException e) { return false; }
    }

    @Override
    public List<Pago> listarPagosPorPaciente(int pacienteId) {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagos WHERE paciente_id = ? ORDER BY fecha_pago DESC";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pacienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pago p = new Pago();
                    p.setId(rs.getInt("id")); p.setMonto(rs.getDouble("monto")); p.setMetodoPago(rs.getString("metodo_pago"));
                    p.setComprobante(rs.getString("comprobante")); p.setFechaPago(rs.getTimestamp("fecha_pago"));
                    p.setSaldoRestante(rs.getDouble("saldo_restante"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public List<Pago> listarPagosPorTurno(int turnoId) {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagos WHERE turno_id = ? ORDER BY fecha_pago ASC";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, turnoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pago p = new Pago();
                    p.setId(rs.getInt("id")); p.setMonto(rs.getDouble("monto")); p.setMetodoPago(rs.getString("metodo_pago"));
                    p.setComprobante(rs.getString("comprobante")); p.setFechaPago(rs.getTimestamp("fecha_pago"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}