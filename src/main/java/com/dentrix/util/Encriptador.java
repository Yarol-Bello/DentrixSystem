package com.dentrix.util;

import org.mindrot.jbcrypt.BCrypt;

public class Encriptador {
    // Genera un hash seguro para guardar en la BD
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Verifica si la contraseña ingresada coincide con el hash de la BD
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            return false; // Evita errores si hay texto plano antiguo
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}