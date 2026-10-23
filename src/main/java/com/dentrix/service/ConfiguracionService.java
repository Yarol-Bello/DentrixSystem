package com.dentrix.service;

import com.dentrix.dao.impl.ConfiguracionClinicaDAOImpl;
import com.dentrix.model.ConfiguracionClinica;

public class ConfiguracionService {
    private final ConfiguracionClinicaDAOImpl dao;

    public ConfiguracionService() {
        this.dao = new ConfiguracionClinicaDAOImpl();
    }

    public ConfiguracionClinica obtenerConfiguracion() {
        return dao.obtener();
    }

    public void guardarConfiguracion(ConfiguracionClinica config) {
        if (!dao.actualizar(config)) {
            throw new RuntimeException("Error al guardar la configuración en la base de datos.");
        }
    }
}