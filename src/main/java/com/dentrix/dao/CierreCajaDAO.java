package com.dentrix.dao;
import com.dentrix.model.CierreCaja;

public interface CierreCajaDAO {
    boolean guardar(CierreCaja cierre);
    CierreCaja buscarPorTurno(int turnoId);
}