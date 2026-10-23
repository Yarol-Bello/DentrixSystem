package com.dentrix.dao;

import com.dentrix.model.Pago;
import java.util.List;

public interface PagoDAO {
    boolean registrarPago(Pago pago, int fichaId, double nuevoSaldo, String nuevoEstado);
    List<Pago> listarPagosPorPaciente(int pacienteId);
    List<Pago> listarPagosPorTurno(int turnoId);
}