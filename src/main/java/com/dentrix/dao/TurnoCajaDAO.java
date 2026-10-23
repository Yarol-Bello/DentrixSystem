package com.dentrix.dao;
import com.dentrix.model.TurnoCaja;

public interface TurnoCajaDAO {
    TurnoCaja obtenerTurnoAbierto();
    boolean abrirTurno(TurnoCaja turno);
    boolean cerrarTurno(int turnoId, int cajeroCierreId);
}