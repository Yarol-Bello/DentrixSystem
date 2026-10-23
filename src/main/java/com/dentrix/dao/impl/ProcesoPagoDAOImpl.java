package com.dentrix.dao.impl;

import com.dentrix.dao.ProcesoPagoDAO;
import com.dentrix.model.Pago;
import com.dentrix.model.TurnoCaja;
import com.dentrix.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProcesoPagoDAOImpl implements ProcesoPagoDAO {

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

    @Override
    public boolean registrarPago(Pago pago, int fichaId, double nuevoSaldo, String nuevoEstado) {
        String sqlPago = "INSERT INTO pagos (ficha_id, paciente_id, turno_id, monto, metodo_pago, numero_operacion, comprobante, saldo_restante) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlFicha = "UPDATE fichas_clinicas SET monto_pagado = monto_pagado + ?, saldo = ?, estado_presupuesto = ? WHERE id = ?";

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psPago = conn.prepareStatement(sqlPago);
                 PreparedStatement psFicha = conn.prepareStatement(sqlFicha)) {
                 
                // 1. Insertar Pago
                psPago.setInt(1, pago.getFichaId()); psPago.setInt(2, pago.getPacienteId()); psPago.setInt(3, pago.getTurnoId());
                psPago.setDouble(4, pago.getMonto()); psPago.setString(5, pago.getMetodoPago()); psPago.setString(6, pago.getNumeroOperacion());
                psPago.setString(7, pago.getComprobante()); psPago.setDouble(8, pago.getSaldoRestante());
                psPago.executeUpdate();

                // 2. Actualizar Ficha
                psFicha.setDouble(1, pago.getMonto()); psFicha.setDouble(2, nuevoSaldo);
                psFicha.setString(3, nuevoEstado); psFicha.setInt(4, fichaId);
                psFicha.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) { conn.rollback(); e.printStackTrace(); return false; }
        } catch (SQLException e) { return false; }
    }

    @Override
    public List<Pago> listarPagosPorTurno(int turnoId) {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT p.*, pac.apellidos || ', ' || pac.nombres AS pac_nombre FROM pagos p JOIN pacientes pac ON p.paciente_id = pac.id WHERE p.turno_id = ? ORDER BY p.fecha_pago ASC";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, turnoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pago p = new Pago();
                    p.setId(rs.getInt("id")); p.setMonto(rs.getDouble("monto")); p.setMetodoPago(rs.getString("metodo_pago"));
                    p.setComprobante(rs.getString("comprobante")); p.setFechaPago(rs.getTimestamp("fecha_pago"));
                    // Nota: Para mostrar el nombre en la tabla de caja, usaríamos un DTO, pero por ahora lo dejamos simple.
                    lista.add(p);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}