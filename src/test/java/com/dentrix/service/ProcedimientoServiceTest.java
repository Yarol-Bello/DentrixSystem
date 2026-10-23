package com.dentrix.service;

import com.dentrix.model.Procedimiento;
import com.dentrix.util.ValidacionException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProcedimientoServiceTest {

    @Test
    public void testGuardarProcedimiento_PrecioNegativo_LanzaExcepcion() {
        // NOTA: Este test requiere que agregues una validación en tu ProcedimientoService
        // Si tu ProcedimientoService no tiene la validación, este test fallará.
        // (Puedes agregarla en el método guardar: if(p.getPrecio() < 0) throw new ValidacionException(...))
        
        ProcedimientoService service = new ProcedimientoService();
        Procedimiento p = new Procedimiento();
        p.setCodigo("TRAT-99");
        p.setNombre("Prueba");
        p.setPrecio(-50.0); // Precio inválido

        Exception exception = assertThrows(ValidacionException.class, () -> {
            service.guardar(p);
        });

        assertTrue(exception.getMessage().contains("precio") || exception.getMessage().contains("negativo"));
    }
}