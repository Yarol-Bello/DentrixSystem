package com.dentrix.dao.impl;

import com.dentrix.dao.FichaClinicaDAO;
import com.dentrix.model.FichaClinica;
import com.dentrix.model.FichaDetalle;
import com.dentrix.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FichaClinicaDAOImpl implements FichaClinicaDAO {

    @Override
    public boolean guardarFicha(FichaClinica ficha) {
        String sqlFicha = "INSERT INTO fichas_clinicas (paciente_id, cita_id, odontologo_id, fecha, anamnesis, diagnostico_cie10, plan_tratamiento, prescripcion, total_presupuesto, monto_pagado, saldo, estado_presupuesto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO ficha_detalles (ficha_id, pieza_dental, cara_dental, diagnostico, procedimiento_id, estado, precio_aplicado, subtotal) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psFicha = conn.prepareStatement(sqlFicha, Statement.RETURN_GENERATED_KEYS)) {
                psFicha.setInt(1, ficha.getPacienteId()); 
                
                // VALIDACIÓN CLAVE: Si el citaId es null, enviamos NULL a la BD
                if (ficha.getCitaId() != null && ficha.getCitaId() > 0) {
                    psFicha.setInt(2, ficha.getCitaId());
                } else {
                    psFicha.setNull(2, Types.INTEGER);
                }
                
                psFicha.setInt(3, ficha.getOdontologoId());
                psFicha.setDate(4, ficha.getFecha()); 
                psFicha.setString(5, ficha.getAnamnesis()); 
                psFicha.setString(6, ficha.getDiagnosticoCie10());
                psFicha.setString(7, ficha.getPlanTratamiento()); 
                psFicha.setString(8, ficha.getPrescripcion());
                psFicha.setDouble(9, ficha.getTotalPresupuesto()); 
                psFicha.setDouble(10, ficha.getMontoPagado());
                psFicha.setDouble(11, ficha.getSaldo()); 
                psFicha.setString(12, ficha.getEstadoPresupuesto());
                psFicha.executeUpdate();

                try (ResultSet rs = psFicha.getGeneratedKeys()) {
                    if (rs.next()) {
                        int fichaId = rs.getInt(1);
                        try (PreparedStatement psDet = conn.prepareStatement(sqlDetalle)) {
                            for (FichaDetalle det : ficha.getDetalles()) {
                                psDet.setInt(1, fichaId); 
                                psDet.setString(2, det.getPiezaDental()); 
                                psDet.setString(3, det.getCaraDental());
                                psDet.setString(4, det.getDiagnostico()); 
                                psDet.setInt(5, det.getProcedimientoId());
                                psDet.setString(6, det.getEstado()); 
                                psDet.setDouble(7, det.getPrecioAplicado()); 
                                psDet.setDouble(8, det.getSubtotal());
                                psDet.addBatch();
                            }
                            psDet.executeBatch();
                        }
                    }
                }
                conn.commit();
                return true;
            } catch (SQLException e) { conn.rollback(); e.printStackTrace(); return false; }
        } catch (SQLException e) { return false; }
    }

    // ... (los demás métodos se mantienen igual)
    @Override
    public FichaClinica buscarFichaPendientePorPaciente(int pacienteId) {
        String sql = "SELECT * FROM fichas_clinicas WHERE paciente_id = ? AND estado_presupuesto IN ('ACEPTADO', 'ABONO') ORDER BY fecha DESC LIMIT 1";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pacienteId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    FichaClinica f = new FichaClinica();
                    f.setId(rs.getInt("id")); f.setPacienteId(rs.getInt("paciente_id")); 
                    f.setTotalPresupuesto(rs.getDouble("total_presupuesto")); f.setMontoPagado(rs.getDouble("monto_pagado"));
                    f.setSaldo(rs.getDouble("saldo")); f.setEstadoPresupuesto(rs.getString("estado_presupuesto"));
                    return f;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<FichaDetalle> obtenerDetallesFicha(int fichaId) {
        List<FichaDetalle> lista = new ArrayList<>();
        String sql = "SELECT fd.*, p.nombre AS proc_nombre FROM ficha_detalles fd LEFT JOIN procedimientos p ON fd.procedimiento_id = p.id WHERE fd.ficha_id = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fichaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FichaDetalle det = new FichaDetalle();
                    det.setId(rs.getInt("id")); det.setFichaId(rs.getInt("ficha_id")); det.setPiezaDental(rs.getString("pieza_dental"));
                    det.setCaraDental(rs.getString("cara_dental")); det.setDiagnostico(rs.getString("diagnostico"));
                    det.setProcedimientoId(rs.getInt("procedimiento_id")); det.setEstado(rs.getString("estado"));
                    det.setPrecioAplicado(rs.getDouble("precio_aplicado")); det.setSubtotal(rs.getDouble("subtotal"));
                    det.setProcedimientoNombre(rs.getString("proc_nombre"));
                    lista.add(det);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
    
    @Override
    public List<FichaClinica> listarFichasPorPaciente(int pacienteId) {
        List<FichaClinica> lista = new ArrayList<>();
        String sql = "SELECT * FROM fichas_clinicas WHERE paciente_id = ? ORDER BY fecha DESC";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pacienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FichaClinica f = new FichaClinica();
                    f.setId(rs.getInt("id"));
                    f.setPacienteId(rs.getInt("paciente_id"));
                    f.setFecha(rs.getDate("fecha"));
                    f.setDiagnosticoCie10(rs.getString("diagnostico_cie10"));
                    f.setTotalPresupuesto(rs.getDouble("total_presupuesto"));
                    f.setSaldo(rs.getDouble("saldo"));
                    f.setEstadoPresupuesto(rs.getString("estado_presupuesto"));
                    lista.add(f);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}