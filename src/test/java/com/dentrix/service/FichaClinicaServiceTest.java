package com.dentrix.service;

import com.dentrix.model.FichaClinica;
import com.dentrix.util.ValidacionException;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class FichaClinicaServiceTest {

    @Test
    public void testGuardarFicha_SinDetalles_LanzaExcepcion() {
        FichaClinicaService service = new FichaClinicaService();
        FichaClinica ficha = new FichaClinica();
        ficha.setPacienteId(1);
        ficha.setDetalles(new ArrayList<>()); // Lista vacía

        Exception exception = assertThrows(ValidacionException.class, () -> {
            service.guardarFicha(ficha);
        });

        assertTrue(exception.getMessage().contains("al menos un tratamiento"));
    }

    @Test
    public void testGuardarFicha_SinPaciente_LanzaExcepcion() {
        FichaClinicaService service = new FichaClinicaService();
        FichaClinica ficha = new FichaClinica();
        ficha.setPacienteId(0); // ID inválido

        Exception exception = assertThrows(ValidacionException.class, () -> {
            service.guardarFicha(ficha);
        });

        assertTrue(exception.getMessage().contains("paciente válido"));
    }
}