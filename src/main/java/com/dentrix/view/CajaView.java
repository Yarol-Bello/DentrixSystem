package com.dentrix.view;

import com.dentrix.controller.CajaController;
import com.dentrix.model.FichaClinica;
import com.dentrix.model.FichaDetalle;
import com.dentrix.model.Paciente;
import com.dentrix.model.Pago;
import com.dentrix.service.FichaClinicaService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class CajaView {
    public Pane getView() {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #F1F5F9;");

        // --- BARRA SUPERIOR ---
        VBox topBar = new VBox(10);
        topBar.setPadding(new Insets(10, 15, 10, 15));
        topBar.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        HBox fila1 = new HBox(10);
        fila1.setAlignment(Pos.CENTER_LEFT);
        TextField txtDni = new TextField(); txtDni.setPromptText("DNI del paciente"); txtDni.setPrefWidth(200);
        Button btnCargar = new Button("Cargar"); btnCargar.setStyle("-fx-background-color: #00A896; -fx-text-fill: white; -fx-font-weight: bold;");
        
        Label lblCodigo = new Label("P-000"); lblCodigo.setStyle("-fx-background-color: #E0F7F6; -fx-text-fill: #0F766E; -fx-padding: 3 8; -fx-background-radius: 4; -fx-font-weight: bold;");
        Label lblPaciente = new Label("Ningún paciente cargado"); lblPaciente.setStyle("-fx-font-weight: bold; -fx-text-fill: #0F172A;");
        Label lblPorCobrar = new Label("Por Cobrar: S/ 0.00"); lblPorCobrar.setStyle("-fx-background-color: #FEF2F2; -fx-text-fill: #DC2626; -fx-padding: 3 8; -fx-background-radius: 4; -fx-font-weight: bold;");
        
        fila1.getChildren().addAll(txtDni, btnCargar, lblCodigo, lblPaciente, lblPorCobrar);

        HBox fila2 = new HBox(10);
        fila2.setStyle("-fx-border-color: #E2E8F0 transparent transparent transparent; -fx-border-width: 1 0 0 0; -fx-padding: 10 0 0 0;");
        Button btnCierre = new Button("🔒 Cierre de Caja"); btnCierre.setStyle("-fx-background-color: #1E293B; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        fila2.getChildren().addAll(btnCierre);

        topBar.getChildren().addAll(fila1, fila2);

        // --- CONTENIDO PRINCIPAL ---
        HBox contentBox = new HBox(20);
        HBox.setHgrow(contentBox, Priority.ALWAYS);

        // --- PANEL IZQUIERDO: REGISTRO DE COBRO (40%) ---
        VBox panelCobro = new VBox(10);
        panelCobro.setPadding(new Insets(15));
        panelCobro.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        panelCobro.setMinWidth(400);
        panelCobro.setMaxWidth(450);

        Label tituloCobro = new Label("Registro de Cobro");
        tituloCobro.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        // Banner de Derivación
        VBox bannerFicha = new VBox(2);
        bannerFicha.setStyle("-fx-background-color: #EFF6FF; -fx-border-color: #BFDBFE; -fx-padding: 10; -fx-background-radius: 6; -fx-border-radius: 6;");
        Label lblFichaTitulo = new Label("Presupuesto Derivado");
        lblFichaTitulo.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E3A8A; -fx-font-size: 11px;");
        Label lblFichaDetalle = new Label("Desde Ficha Clínica — Consultorio 01");
        lblFichaDetalle.setStyle("-fx-text-fill: #1E40AF; -fx-font-size: 10px;");
        bannerFicha.getChildren().addAll(lblFichaTitulo, lblFichaDetalle);

        // Tabla de Tratamientos a Cobrar
        Label lblTratamientos = new Label("Tratamientos a Cobrar:");
        lblTratamientos.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #475569;");
        
        TableView<FichaDetalle> tablaTratamientos = new TableView<>();
        tablaTratamientos.setPrefHeight(120);
        TableColumn<FichaDetalle, String> colCod = new TableColumn<>("Código");
        colCod.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty("TRAT-" + String.format("%02d", cell.getValue().getProcedimientoId())));
        TableColumn<FichaDetalle, String> colDesc = new TableColumn<>("Descripción");
        colDesc.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("diagnostico"));
        TableColumn<FichaDetalle, String> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty("S/ " + String.format("%.2f", cell.getValue().getSubtotal())));
        tablaTratamientos.getColumns().addAll(colCod, colDesc, colPrecio);
        tablaTratamientos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Banner Total
        HBox boxTotal = new HBox(10);
        boxTotal.setStyle("-fx-background-color: #ECFDF5; -fx-border-color: #A7F3D0; -fx-padding: 10; -fx-background-radius: 6; -fx-border-radius: 6;");
        boxTotal.setAlignment(Pos.CENTER_LEFT);
        Label lblTotalText = new Label("TOTAL A PAGAR:");
        lblTotalText.setStyle("-fx-font-weight: bold; -fx-text-fill: #065F46;");
        Label lblTotalACobrar = new Label("S/ 0.00");
        lblTotalACobrar.setStyle("-fx-font-weight: bold; -fx-text-fill: #047857; -fx-font-size: 16px;");
        Region spacerTotal = new Region(); HBox.setHgrow(spacerTotal, Priority.ALWAYS);
        boxTotal.getChildren().addAll(lblTotalText, spacerTotal, lblTotalACobrar);

        // Formulario de Pago
        ComboBox<String> cbModalidad = new ComboBox<>(); cbModalidad.getItems().addAll("Cancelación Total", "Abono / Pago Parcial"); cbModalidad.setValue("Cancelación Total");
        cbModalidad.setMaxWidth(Double.MAX_VALUE);

        // ESTE ES EL COMBO QUE FALTABA Y QUE EL CONTROLADOR NECESITA
        ComboBox<String> cbMetodoOculto = new ComboBox<>(); 
        cbMetodoOculto.getItems().addAll("EFECTIVO", "YAPE", "PLIN"); 
        cbMetodoOculto.setValue("EFECTIVO");

        HBox botonesMetodo = new HBox(10);
        Button btnEfectivo = new Button("💵 Efectivo");
        btnEfectivo.setStyle("-fx-background-color: #E0F7F6; -fx-border-color: #00A896; -fx-text-fill: #0F766E; -fx-font-weight: bold; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10; -fx-cursor: hand;");
        btnEfectivo.setMaxWidth(Double.MAX_VALUE); HBox.setHgrow(btnEfectivo, Priority.ALWAYS);
        
        Button btnYape = new Button("📱 Yape / Plin");
        btnYape.setStyle("-fx-background-color: white; -fx-border-color: #CBD5E1; -fx-text-fill: #475569; -fx-font-weight: bold; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10; -fx-cursor: hand;");
        btnYape.setMaxWidth(Double.MAX_VALUE); HBox.setHgrow(btnYape, Priority.ALWAYS);
        
        botonesMetodo.getChildren().addAll(btnEfectivo, btnYape);

        VBox boxYape = new VBox(5);
        boxYape.setVisible(false); boxYape.setManaged(false);
        boxYape.setStyle("-fx-background-color: #F5F3FF; -fx-border-color: #DDD6FE; -fx-padding: 10; -fx-background-radius: 6; -fx-border-radius: 6;");
        Label lblYape = new Label("N° Operación Yape / Plin *");
        lblYape.setStyle("-fx-font-weight: bold; -fx-text-fill: #5B21B6; -fx-font-size: 10px;");
        TextField txtOperacion = new TextField(); txtOperacion.setPromptText("Ej: 849201");
        boxYape.getChildren().addAll(lblYape, txtOperacion);

        HBox boxMontos = new HBox(10);
        VBox boxMontoPagado = new VBox(5);
        Label lblMontoPagado = new Label("Monto Pagado (S/) *");
        lblMontoPagado.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #475569;");
        TextField txtMonto = new TextField("0.00");
        boxMontoPagado.getChildren().addAll(lblMontoPagado, txtMonto);
        HBox.setHgrow(boxMontoPagado, Priority.ALWAYS);

        VBox boxSaldo = new VBox(5);
        Label lblSaldoText = new Label("Saldo Deudor");
        lblSaldoText.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #475569;");
        Label lblSaldoDeudor = new Label("S/ 0.00 (Al día)");
        lblSaldoDeudor.setStyle("-fx-background-color: #ECFDF5; -fx-border-color: #A7F3D0; -fx-padding: 8; -fx-text-fill: #047857; -fx-font-weight: bold; -fx-border-radius: 6; -fx-background-radius: 6;");
        boxSaldo.getChildren().addAll(lblSaldoText, lblSaldoDeudor);
        HBox.setHgrow(boxSaldo, Priority.ALWAYS);

        boxMontos.getChildren().addAll(boxMontoPagado, boxSaldo);

        Button btnConfirmar = new Button("💳 Confirmar Pago y Emitir Boleta");
        btnConfirmar.setMaxWidth(Double.MAX_VALUE);
        btnConfirmar.setStyle("-fx-background-color: #00A896; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-cursor: hand;");

        panelCobro.getChildren().addAll(tituloCobro, bannerFicha, lblTratamientos, tablaTratamientos, 
            boxTotal, new Label("Modalidad de Cobro *"), cbModalidad,
            new Label("Método de Pago *"), botonesMetodo, boxYape, 
            boxMontos, btnConfirmar);

        // --- PANEL DERECHO: HISTORIAL Y BOLETA (60%) ---
        VBox panelBoleta = new VBox(15);
        panelBoleta.setPadding(new Insets(15));
        panelBoleta.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        HBox.setHgrow(panelBoleta, Priority.ALWAYS);

        Label tituloHistorial = new Label("Historial de Pagos");
        tituloHistorial.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");
        
        TableView<Pago> tablaHistorial = new TableView<>();
        VBox.setVgrow(tablaHistorial, Priority.ALWAYS);
        tablaHistorial.setMinHeight(150);

        Label tituloBoleta = new Label("Comprobante Electrónico");
        tituloBoleta.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        VBox boletaVisual = new VBox(10);
        boletaVisual.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-padding: 15; -fx-background-radius: 8; -fx-border-radius: 8;");
        
        Label lblEmpresa = new Label("CENTRO ODONTOLÓGICO DENTRIX");
        lblEmpresa.setStyle("-fx-font-weight: bold; -fx-text-fill: #0F766E;");
        Label lblRuc = new Label("RUC: 20608945123");
        lblRuc.setStyle("-fx-font-size: 10px; -fx-text-fill: #64748B;");
        
        VBox boxCliente = new VBox(3);
        boxCliente.setStyle("-fx-border-color: #E2E8F0 transparent transparent transparent; -fx-border-width: 1 0 0 0; -fx-padding: 10 0 0 0;");
        Label lblBoletaCliente = new Label("Cliente: --");
        Label lblBoletaDni = new Label("DNI: --");
        Label lblBoletaMetodo = new Label("Método: --");
        boxCliente.getChildren().addAll(lblBoletaCliente, lblBoletaDni, lblBoletaMetodo);

        Label lblBoletaNum = new Label("B001-000000");
        lblBoletaNum.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #DC2626; -fx-border-color: #0F172A; -fx-padding: 5 15; -fx-border-radius: 4;");

        Label lblBoletaTotal = new Label("TOTAL: S/ 0.00");
        lblBoletaTotal.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0F766E;");

        boletaVisual.getChildren().addAll(lblEmpresa, lblRuc, boxCliente, lblBoletaNum, new Separator(), lblBoletaTotal);

        panelBoleta.getChildren().addAll(tituloHistorial, tablaHistorial, new Separator(), tituloBoleta, boletaVisual);

        contentBox.getChildren().addAll(panelCobro, panelBoleta);
        mainLayout.getChildren().addAll(topBar, contentBox);

        // --- CONECTAR CON EL CONTROLADOR ---
        // AHORA LE PASAMOS EL COMBO OCULTO EN LUGAR DEL NULL
        CajaController controller = new CajaController(txtDni, lblCodigo, lblPaciente, lblPorCobrar, 
                lblTotalACobrar, lblSaldoDeudor, cbModalidad, cbMetodoOculto, txtMonto, txtOperacion, tablaHistorial);

        // Eventos
        btnCargar.setOnAction(e -> {
            controller.cargarPaciente();
            // Actualizar la tabla de tratamientos de la vista
            if (controller.getFichaACobrar() != null) {
                FichaClinicaService fs = new FichaClinicaService();
                List<FichaDetalle> detalles = fs.obtenerDetallesFicha(controller.getFichaACobrar().getId());
                tablaTratamientos.getItems().setAll(detalles);
                
                // Actualizar Boleta Preview
                Paciente p = controller.getPacienteActual();
                lblBoletaCliente.setText("Cliente: " + p.getApellidos() + ", " + p.getNombres());
                lblBoletaDni.setText("DNI: " + p.getDni());
                lblBoletaTotal.setText("TOTAL: S/ " + String.format("%.2f", controller.getFichaACobrar().getSaldo()));
            }
        });

        btnConfirmar.setOnAction(e -> {
            controller.confirmarCobro();
            // Actualizar Boleta con el último pago
            Pago ultimo = controller.getUltimoPago();
            if (ultimo != null) {
                lblBoletaNum.setText(ultimo.getComprobante());
                lblBoletaMetodo.setText("Método: " + ultimo.getMetodoPago());
                lblBoletaTotal.setText("TOTAL: S/ " + String.format("%.2f", ultimo.getMonto()));
            }
        });

        // Selección visual de método de pago
        btnEfectivo.setOnAction(e -> {
            btnEfectivo.setStyle("-fx-background-color: #E0F7F6; -fx-border-color: #00A896; -fx-text-fill: #0F766E; -fx-font-weight: bold; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10; -fx-cursor: hand;");
            btnYape.setStyle("-fx-background-color: white; -fx-border-color: #CBD5E1; -fx-text-fill: #475569; -fx-font-weight: bold; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10; -fx-cursor: hand;");
            boxYape.setVisible(false); boxYape.setManaged(false);
            cbMetodoOculto.setValue("EFECTIVO"); // Actualizamos el combo oculto
        });

        btnYape.setOnAction(e -> {
            btnYape.setStyle("-fx-background-color: #E0F7F6; -fx-border-color: #00A896; -fx-text-fill: #0F766E; -fx-font-weight: bold; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10; -fx-cursor: hand;");
            btnEfectivo.setStyle("-fx-background-color: white; -fx-border-color: #CBD5E1; -fx-text-fill: #475569; -fx-font-weight: bold; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10; -fx-cursor: hand;");
            boxYape.setVisible(true); boxYape.setManaged(true);
            cbMetodoOculto.setValue("YAPE"); // Actualizamos el combo oculto
        });

        // Modal de Cierre de Caja
        btnCierre.setOnAction(e -> {
            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Cierre de Caja y Arqueo");
            Scene scene = new Scene(new CierreCajaView().getView(), 1000, 650);
            modal.setScene(scene);
            modal.showAndWait();
        });

        return mainLayout;
    }
}