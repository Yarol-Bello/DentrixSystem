package com.dentrix.util;

import java.lang.System.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConexionDB {
    
    private static final Logger logger = LoggerFactory.getLogger(ConexionDB.class);
    
    // Aquí va el nombre de tu BD, el usuario por defecto y la contraseña que pusiste al instalar PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/dentrix_db";
    private static final String USER = "postgres"; 
    private static final String PASSWORD = "162001"; // ¡CAMBIA ESTO POR TU CONTRASEÑA DE POSTGRES!

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info("Conexión exitosa a la base de datos PostgreSQL.");
            return connection;
        } catch (SQLException e) {
            logger.error("Error al conectar a la base de datos: {}", e.getMessage());
            return null;
        }
    }
}