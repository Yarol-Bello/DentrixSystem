package com.dentrix.service;

import com.dentrix.model.Cita;
import com.dentrix.util.ValidacionException;
import org.junit.jupiter.api.Test;
import java.sql.Time;
import static org.junit.jupiter.api.Assertions.*;

public class CitaServiceTest {

    @Test
    public void testGuardarCita_SinPaciente_LanzaExcepcion() {
        CitaService service = new CitaService();
        Cita c = new Cita();
        c.setPacienteId(0); // Inválido
        c.setOdontologoId(1);

        Exception exception = assertThrows(ValidacionException.class, () -> {
            service.guardarCita(c, false);
        });

        assertTrue(exception.getMessage().contains("Debe seleccionar paciente"));
    }

    @Test
    public void testGuardarCita_HorarioInvalido_LanzaExcepcion() {
        CitaService service = new CitaService();
        Cita c = new Cita();
        c.setPacienteId(1);
        c.setOdontologoId(1);
        c.setFecha(java.sql.Date.valueOf("2026-10-25"));
        
        // ASIGNAMOS HORAS PARA QUE NO DE NULL POINTER
        c.setHoraInicio(Time.valueOf("11:00:00"));
        c.setHoraFin(Time.valueOf("10:00:00"));

        Exception exception = assertThrows(ValidacionException.class, () -> {
            service.guardarCita(c, false);
        });

        assertTrue(exception.getMessage().toLowerCase().contains("horario"));
    }
}