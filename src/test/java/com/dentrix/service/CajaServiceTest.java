package com.dentrix.service;

import com.dentrix.model.FichaClinica;
import com.dentrix.util.ValidacionException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CajaServiceTest {

    @Test
    public void testCobrarFicha_MontoExcedeSaldo_LanzaExcepcion() {
        CajaService service = new CajaService();
        FichaClinica ficha = new FichaClinica();
        ficha.setId(1);
        ficha.setSaldo(150.00);

        Exception exception = assertThrows(ValidacionException.class, () -> {
            service.cobrarFicha(ficha, 200.00, "EFECTIVO", "", "B001-123");
        });

        assertTrue(exception.getMessage().contains("excede el saldo pendiente"));
    }

    @Test
    public void testCobrarFicha_MontoCero_LanzaExcepcion() {
        CajaService service = new CajaService();
        FichaClinica ficha = new FichaClinica();
        ficha.setId(1);
        ficha.setSaldo(150.00);

        Exception exception = assertThrows(ValidacionException.class, () -> {
            service.cobrarFicha(ficha, 0.00, "EFECTIVO", "", "B001-123");
        });

        assertTrue(exception.getMessage().contains("El monto debe ser mayor a 0"));
    }
}