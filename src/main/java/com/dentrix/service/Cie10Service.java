package com.dentrix.service;

import com.dentrix.dao.impl.Cie10DAOImpl;
import com.dentrix.model.Cie10;
import java.util.List;

public class Cie10Service {
    private final Cie10DAOImpl dao;

    public Cie10Service() { 
        this.dao = new Cie10DAOImpl(); 
    }

    public List<Cie10> listarTodos() { 
        return dao.listarTodos(); 
    }
    
    public Cie10 buscarPorCodigo(String codigo) { 
        return dao.buscarPorCodigo(codigo); 
    }
}