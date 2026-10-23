package com.dentrix.dao;

import com.dentrix.model.FichaClinica;
import com.dentrix.model.FichaDetalle;
import java.util.List;

public interface FichaClinicaDAO {
    boolean guardarFicha(FichaClinica ficha);
    FichaClinica buscarFichaPendientePorPaciente(int pacienteId);
    List<FichaDetalle> obtenerDetallesFicha(int fichaId);
    List<FichaClinica> listarFichasPorPaciente(int pacienteId); // <-- NUEVO
}