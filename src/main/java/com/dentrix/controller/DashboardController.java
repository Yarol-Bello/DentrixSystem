package com.dentrix.controller;

import com.dentrix.model.Usuario;
import com.dentrix.view.*;
import javafx.scene.layout.BorderPane;

public class DashboardController {
    private BorderPane root;
    private Usuario usuarioLogueado;

    public DashboardController(BorderPane root, Usuario usuario) {
        this.root = root;
        this.usuarioLogueado = usuario;
    }

    public void abrirModulo(String modulo) {
        switch (modulo) {
            case "INICIO":
                // Cargamos la vista real del Dashboard (M2)
                root.setCenter(new DashboardHomeView().getView());
                break;
            case "RECEPCION":
                root.setCenter(new RecepcionView().getView());
                break;
            case "PACIENTES":
                root.setCenter(new PacientesView().getView());
                break;
            case "AGENDA":
                root.setCenter(new AgendaView().getView());
                break;
            case "FICHA":
                root.setCenter(new FichaClinicaView().getView());
                break;
            case "CAJA":
                root.setCenter(new CajaView().getView());
                break;
            case "REPORTES":
                root.setCenter(new ReportesView().getView());
                break;
            case "CONFIG":
                root.setCenter(new ConfiguracionView().getView());
                break;
            default:
                break;
        }
    }
}