package com.dentrix.service;

import com.dentrix.dao.impl.ProcedimientoDAOImpl;
import com.dentrix.model.Procedimiento;
import com.dentrix.util.ValidacionException;
import java.util.List;

public class ProcedimientoService {
    private final ProcedimientoDAOImpl dao;

    public ProcedimientoService() { 
        this.dao = new ProcedimientoDAOImpl(); 
    }

    public List<Procedimiento> listarTodos() { 
        return dao.listarTodos(); 
    }
    
    public double obtenerPrecio(int id) { 
        return dao.obtenerPrecio(id); 
    }

    public void guardar(Procedimiento p) {
        // VALIDACIÓN AGREGADA PARA EL TEST DE TDD
        if (p.getPrecio() < 0) {
            throw new ValidacionException("El precio no puede ser negativo.");
        }
        
        if (dao.buscarPorId(p.getId()) != null) {
            throw new ValidacionException("Ya existe un procedimiento con ese código.");
        }
        if (!dao.guardar(p)) {
            throw new RuntimeException("Error al guardar en BD.");
        }
    }

    public void actualizar(Procedimiento p) {
        // VALIDACIÓN AGREGADA POR SI ACASO
        if (p.getPrecio() < 0) {
            throw new ValidacionException("El precio no puede ser negativo.");
        }
        
        if (!dao.actualizar(p)) {
            throw new RuntimeException("Error al actualizar en BD.");
        }
    }

    public void eliminar(int id) {
        if (!dao.eliminar(id)) {
            throw new RuntimeException("Error al eliminar. Puede estar en uso en alguna ficha clínica.");
        }
    }
}