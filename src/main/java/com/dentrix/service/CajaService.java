package com.dentrix.service;

import com.dentrix.dao.impl.*;
import com.dentrix.model.*;
import com.dentrix.util.ValidacionException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class CajaService {
    private final PagoDAOImpl pagoDAO;
    private final TurnoCajaDAOImpl turnoDAO;
    private final FichaClinicaDAOImpl fichaDAO;
    private final CierreCajaDAOImpl cierreDAO;

    public CajaService() {
        this.pagoDAO = new PagoDAOImpl();
        this.turnoDAO = new TurnoCajaDAOImpl();
        this.fichaDAO = new FichaClinicaDAOImpl();
        this.cierreDAO = new CierreCajaDAOImpl();
    }

    public TurnoCaja obtenerTurnoActual() {
        return turnoDAO.obtenerTurnoAbierto();
    }

    public FichaClinica obtenerFichaPendiente(int pacienteId) {
        return fichaDAO.buscarFichaPendientePorPaciente(pacienteId);
    }

    public List<Pago> listarPagosPaciente(int pacienteId) {
        return pagoDAO.listarPagosPorPaciente(pacienteId);
    }

    public List<Pago> obtenerPagosTurnoActual() {
        TurnoCaja turno = turnoDAO.obtenerTurnoAbierto();
        if (turno == null) return List.of();
        return pagoDAO.listarPagosPorTurno(turno.getId());
    }

    public void cobrarFicha(FichaClinica ficha, double montoPagar, String metodo, String nroOperacion, String comprobante) {
        TurnoCaja turno = turnoDAO.obtenerTurnoAbierto();
        if (turno == null) throw new ValidacionException("No hay un turno de caja abierto.");

        if (montoPagar <= 0) throw new ValidacionException("El monto debe ser mayor a 0.");
        if (montoPagar > ficha.getSaldo()) throw new ValidacionException("El monto excede el saldo pendiente.");

        double nuevoSaldo = ficha.getSaldo() - montoPagar;
        String nuevoEstado = nuevoSaldo == 0 ? "COBRADO" : "ABONO";

        Pago pago = new Pago();
        pago.setFichaId(ficha.getId());
        pago.setPacienteId(ficha.getPacienteId());
        pago.setTurnoId(turno.getId());
        pago.setMonto(montoPagar);
        pago.setMetodoPago(metodo);
        pago.setNumeroOperacion(nroOperacion);
        pago.setComprobante(comprobante);
        pago.setSaldoRestante(nuevoSaldo);

        if (!pagoDAO.registrarPago(pago, ficha.getId(), nuevoSaldo, nuevoEstado)) {
            throw new RuntimeException("Error al procesar el pago.");
        }
    }

    public void consolidarCierreCaja(TurnoCaja turno, double fondoInicial, double contadoFisico, double totalEfectivo, double totalDigital, double diferencia, int cajeroId) {
        CierreCaja cierre = new CierreCaja();
        cierre.setTurnoId(turno.getId());
        cierre.setFecha(Date.valueOf(LocalDate.now()));
        cierre.setTurno(turno.getTurno());
        cierre.setCajeroId(cajeroId);
        cierre.setFondoInicial(fondoInicial);
        cierre.setTotalEfectivo(totalEfectivo);
        cierre.setTotalDigital(totalDigital);
        cierre.setEsperadoGaveta(fondoInicial + totalEfectivo);
        cierre.setContado(contadoFisico);
        cierre.setDiferencia(diferencia);
        
        String numActa = "CIE-" + LocalDate.now().getYear() + "-" + String.format("%04d", turno.getId());
        cierre.setNumeroActa(numActa);

        if (!cierreDAO.guardar(cierre)) {
            throw new RuntimeException("Error al guardar el acta de cierre.");
        }
        
        if (!turnoDAO.cerrarTurno(turno.getId(), cajeroId)) {
            throw new RuntimeException("Error al cerrar el turno en el sistema.");
        }
    }
}