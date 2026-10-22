package com.dentrix.app;

import com.dentrix.util.ConexionDB;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        // Intentamos conectar
        Connection conn = ConexionDB.getConnection();
        
        if (conn != null) {
            System.out.println("✅ ¡EXCELENTE! Java se conectó correctamente a PostgreSQL.");
        } else {
            System.out.println("❌ ERROR: No se pudo conectar. Revisa tu contraseña o si PostgreSQL está encendido.");
        }
    }
}