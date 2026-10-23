package com.dentrix.service;

import com.dentrix.dao.impl.FichaClinicaDAOImpl;
import com.dentrix.dao.impl.ProcedimientoDAOImpl;
import com.dentrix.dao.impl.Cie10DAOImpl;
import com.dentrix.model.FichaClinica;
import com.dentrix.model.FichaDetalle;
import com.dentrix.model.Procedimiento;
import com.dentrix.model.Cie10;
import com.dentrix.util.ValidacionException; // IMPORTANTE: Importar ValidacionException
import java.util.List;

public class FichaClinicaService {
    private final FichaClinicaDAOImpl fichaDAO;

    public FichaClinicaService() {
        this.fichaDAO = new FichaClinicaDAOImpl();
    }

    public void guardarFicha(FichaClinica ficha) {
        if (ficha.getPacienteId() <= 0) {
            // CAMBIADO A ValidacionException
            throw new ValidacionException("Debe seleccionar un paciente válido.");
        }
        if (ficha.getDetalles() == null || ficha.getDetalles().isEmpty()) {
            // CAMBIADO A ValidacionException
            throw new ValidacionException("Debe registrar al menos un tratamiento en la ficha.");
        }

        if (!fichaDAO.guardarFicha(ficha)) {
            throw new RuntimeException("Error al guardar la ficha clínica en la base de datos.");
        }
    }

    public List<FichaClinica> listarFichasPorPaciente(int pacienteId) {
        return fichaDAO.listarFichasPorPaciente(pacienteId);
    }
    
    public List<Procedimiento> obtenerProcedimientos() {
        return new ProcedimientoDAOImpl().listarTodos();
    }

    public List<Cie10> obtenerCie10() {
        return new Cie10DAOImpl().listarTodos();
    }
    
    public List<FichaDetalle> obtenerDetallesFicha(int fichaId) {
        return fichaDAO.obtenerDetallesFicha(fichaId);
    }
}