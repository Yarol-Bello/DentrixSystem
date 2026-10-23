package com.dentrix.service;

import com.dentrix.util.Encriptador;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioServiceTest {

    @Test
    public void testEncriptador_GeneraHashSeguro() {
        String passwordPlana = "admin123";
        String passwordHasheada = Encriptador.hashPassword(passwordPlana);

        // 1. El hash no debe ser igual al texto plano (está encriptado)
        assertNotEquals(passwordPlana, passwordHasheada);
        
        // 2. El sistema debe validar correctamente la contraseña original
        assertTrue(Encriptador.checkPassword(passwordPlana, passwordHasheada));
        
        // 3. El sistema debe rechazar una contraseña incorrecta
        assertFalse(Encriptador.checkPassword("clave_falsa", passwordHasheada));
    }
}