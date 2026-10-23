package com.dentrix.dao;
import com.dentrix.model.Pago;
import com.dentrix.model.TurnoCaja;
import java.util.List;

public interface ProcesoPagoDAO {
    // Turnos
    TurnoCaja obtenerTurnoAbierto();
    boolean abrirTurno(TurnoCaja turno);
    boolean cerrarTurno(int turnoId, int cajeroCierreId);
    
    // Pagos
    boolean registrarPago(Pago pago, int fichaId, double nuevoSaldo, String nuevoEstado);
    List<Pago> listarPagosPorTurno(int turnoId);
}