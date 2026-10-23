package com.dentrix.dao;
import com.dentrix.model.ConfiguracionClinica;

public interface ConfiguracionClinicaDAO {
    ConfiguracionClinica obtener();
    boolean actualizar(ConfiguracionClinica config);
}