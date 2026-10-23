package com.dentrix.view;

import com.dentrix.controller.FichaClinicaController;
import com.dentrix.model.Cie10;
import com.dentrix.model.FichaDetalle;
import com.dentrix.model.Procedimiento;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class FichaClinicaView {
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
        Label lblNombre = new Label(); lblNombre.setStyle("-fx-font-weight: bold; -fx-text-fill: #0F172A;");
        fila1.getChildren().addAll(txtDni, btnCargar, lblCodigo, lblNombre);

        HBox fila2 = new HBox(10);
        fila2.setStyle("-fx-border-color: #E2E8F0 transparent transparent transparent; -fx-border-width: 1 0 0 0; -fx-padding: 10 0 0 0;");
        Button btnVerFichas = new Button("📂 Ver Fichas"); btnVerFichas.setStyle("-fx-background-color: white; -fx-border-color: #CBD5E1; -fx-cursor: hand;");
        Button btnImprimir = new Button("🖨 Imprimir"); btnImprimir.setStyle("-fx-background-color: white; -fx-border-color: #CBD5E1; -fx-cursor: hand;");
        Button btnGuardar = new Button("💾 Guardar Ficha"); btnGuardar.setStyle("-fx-background-color: #00A896; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        fila2.getChildren().addAll(btnVerFichas, btnImprimir, btnGuardar);

        topBar.getChildren().addAll(fila1, fila2);

        HBox contentBox = new HBox(20);
        HBox.setHgrow(contentBox, Priority.ALWAYS);

        // --- PANEL IZQUIERDO: ODONTOGRAMA ---
        VBox panelOdontograma = new VBox(10);
        panelOdontograma.setPadding(new Insets(15));
        panelOdontograma.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        panelOdontograma.setMinWidth(550);

        Label tituloOdon = new Label("Odontograma Adulto (FDI)");
        tituloOdon.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        GridPane gridDientes = new GridPane();
        gridDientes.setHgap(2); gridDientes.setVgap(10);
        gridDientes.setAlignment(Pos.CENTER);
        gridDientes.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 15; -fx-background-radius: 8;");

        String[] supDer = {"18", "17", "16", "15", "14", "13", "12", "11"};
        String[] supIzq = {"21", "22", "23", "24", "25", "26", "27", "28"};
        String[] infDer = {"48", "47", "46", "45", "44", "43", "42", "41"};
        String[] infIzq = {"31", "32", "33", "34", "35", "36", "37", "38"};

        FichaClinicaController[] controllerRef = new FichaClinicaController[1];

        for (int i = 0; i < 8; i++) {
            gridDientes.add(crearBotonDiente(supDer[i], controllerRef), i, 0);
            gridDientes.add(crearBotonDiente(supIzq[i], controllerRef), i + 9, 0);
            gridDientes.add(crearBotonDiente(infDer[i], controllerRef), i, 1);
            gridDientes.add(crearBotonDiente(infIzq[i], controllerRef), i + 9, 1);
        }

        Label lblHallazgos = new Label("Hallazgos Registrados en Sesión:");
        lblHallazgos.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #475569;");
        TableView<FichaDetalle> tabla = new TableView<>();
        VBox.setVgrow(tabla, Priority.ALWAYS);

        panelOdontograma.getChildren().addAll(tituloOdon, gridDientes, lblHallazgos, tabla);

        // --- PANEL DERECHO: ANAMNESIS Y PRESUPUESTO ---
        VBox panelDiagnostico = new VBox(10);
        panelDiagnostico.setPadding(new Insets(15));
        panelDiagnostico.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        HBox.setHgrow(panelDiagnostico, Priority.ALWAYS);

        Label tituloDiag = new Label("Anamnesis, CIE-10 y Presupuesto");
        tituloDiag.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        Label lblAlergias = new Label("Sin alergias registradas.");
        lblAlergias.setStyle("-fx-text-fill: #DC2626; -fx-font-weight: bold; -fx-font-size: 11px;");
        VBox alertaBox = new VBox(2, new Label("⚠️ AFECCIONES MÉDICAS"), lblAlergias);
        alertaBox.setStyle("-fx-background-color: #FEF2F2; -fx-border-color: #FECACA; -fx-padding: 8; -fx-background-radius: 6;");

        TextArea txtAnamnesis = new TextArea(); txtAnamnesis.setPromptText("Motivo de consulta..."); txtAnamnesis.setPrefRowCount(2);
        ComboBox<Cie10> cbCie10 = new ComboBox<>(); cbCie10.setPromptText("Seleccione Diagnóstico CIE-10"); cbCie10.setMaxWidth(Double.MAX_VALUE);
        
        ComboBox<Procedimiento> cbPlan = new ComboBox<>(); cbPlan.setPromptText("Seleccione Plan de Tratamiento"); cbPlan.setMaxWidth(Double.MAX_VALUE);
        Button btnAddTratamiento = new Button("+ Añadir");
        btnAddTratamiento.setStyle("-fx-background-color: #0F172A; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        
        HBox boxPlan = new HBox(10, cbPlan, btnAddTratamiento);
        HBox.setHgrow(cbPlan, Priority.ALWAYS);

        TextArea txtPrescripcion = new TextArea(); txtPrescripcion.setPromptText("Indicaciones médicas..."); txtPrescripcion.setPrefRowCount(2);

        Label lblTotal = new Label("TOTAL: S/ 0.00");
        lblTotal.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0F766E;");

        Button btnAceptar = new Button("✔ Aceptar → Caja"); btnAceptar.setStyle("-fx-background-color: #059669; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        Button btnPendiente = new Button("⏸ Lo Piensa"); btnPendiente.setStyle("-fx-background-color: #D97706; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        HBox botonesPresupuesto = new HBox(10, btnAceptar, btnPendiente);

        VBox boxPresupuesto = new VBox(10, lblTotal, botonesPresupuesto);
        boxPresupuesto.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-padding: 10; -fx-background-radius: 8;");

        panelDiagnostico.getChildren().addAll(tituloDiag, alertaBox, new Label("Anamnesis *"), txtAnamnesis, 
                new Label("CIE-10 *"), cbCie10, new Label("Plan *"), boxPlan, 
                new Label("Prescripción"), txtPrescripcion, boxPresupuesto);

        contentBox.getChildren().addAll(panelOdontograma, panelDiagnostico);
        mainLayout.getChildren().addAll(topBar, contentBox);

        // --- CONECTAR CONTROLADOR ---
        FichaClinicaController controller = new FichaClinicaController(
                txtDni, lblCodigo, lblNombre, lblAlergias, lblTotal, 
                txtAnamnesis, txtPrescripcion, cbCie10, cbPlan, tabla
        );
        controllerRef[0] = controller; 

        btnCargar.setOnAction(e -> controller.cargarPaciente());
        btnVerFichas.setOnAction(e -> controller.mostrarModalFichas());
        btnAceptar.setOnAction(e -> controller.guardarFicha("ACEPTADO"));
        btnPendiente.setOnAction(e -> controller.guardarFicha("PENDIENTE"));
        
        btnAddTratamiento.setOnAction(e -> {
            Procedimiento p = cbPlan.getValue();
            if(p != null) {
                FichaDetalle det = new FichaDetalle();
                det.setPiezaDental("Gral");
                det.setDiagnostico(p.getNombre());
                det.setProcedimientoId(p.getId());
                det.setEstado("Curado");
                det.setPrecioAplicado(p.getPrecio());
                det.setSubtotal(p.getPrecio());
                controller.agregarTratamientoALista(det);
            }
        });

        return mainLayout;
    }

    private Button crearBotonDiente(String numero, FichaClinicaController[] controllerRef) {
        Button btn = new Button(numero);
        btn.setPrefSize(35, 45);
        btn.setStyle("-fx-background-color: white; -fx-border-color: #CBD5E1; -fx-border-radius: 4; -fx-cursor: hand; -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #475569;");
        btn.setOnAction(e -> {
            if (controllerRef[0] != null) {
                controllerRef[0].abrirDialogoDiente(numero, btn); 
            }
        });
        return btn;
    }
}