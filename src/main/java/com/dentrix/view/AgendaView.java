package com.dentrix.view;

import com.dentrix.controller.CitaController;
import com.dentrix.model.Cita;
import com.dentrix.model.Procedimiento;
import com.dentrix.model.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.time.LocalDate;

public class AgendaView {
    public Pane getView() {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #F1F5F9;");

        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 15, 10, 15));
        topBar.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        Label lblFecha = new Label("📅 Fecha:");
        lblFecha.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569;");
        
        DatePicker dpFechaTabla = new DatePicker(LocalDate.now());
        dpFechaTabla.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #CBD5E1; -fx-border-radius: 6;");
        
        Label lblHoy = new Label("🟢 HOY");
        lblHoy.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-padding: 3 8; -fx-background-radius: 12; -fx-font-size: 10px; -fx-font-weight: bold;");
        
        topBar.getChildren().addAll(lblFecha, dpFechaTabla, lblHoy);

        HBox contentBox = new HBox(20);
        HBox.setHgrow(contentBox, Priority.ALWAYS);

        // --- PANEL IZQUIERDO ---
        VBox formPanel = new VBox(10);
        formPanel.setPadding(new Insets(15));
        formPanel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        formPanel.setMinWidth(350);
        formPanel.setMaxWidth(380);

        Label lblTituloForm = new Label("Nueva Cita");
        lblTituloForm.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        TextField txtDniBuscar = new TextField();
        txtDniBuscar.setPromptText("8 dígitos");
        Button btnBuscar = new Button("🔍 Buscar");
        btnBuscar.setStyle("-fx-background-color: #00A896; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        HBox buscarBox = new HBox(10, txtDniBuscar, btnBuscar);
        HBox.setHgrow(txtDniBuscar, Priority.ALWAYS);

        VBox cardPaciente = new VBox(5);
        cardPaciente.setStyle("-fx-background-color: #ECFDF5; -fx-border-color: #A7F3D0; -fx-border-radius: 8; -fx-padding: 10;");
        cardPaciente.setVisible(false);
        cardPaciente.setManaged(false);

        HBox topCard = new HBox(10);
        topCard.setAlignment(Pos.CENTER_LEFT);
        Circle avatar = new Circle(15, Color.web("#059669"));
        Label lblIniciales = new Label();
        lblIniciales.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11px;");
        StackPane avatarPane = new StackPane(avatar, lblIniciales);
        
        VBox infoBasica = new VBox(2);
        Label lblNombre = new Label();
        lblNombre.setStyle("-fx-font-weight: bold; -fx-text-fill: #0F172A; -fx-font-size: 12px;");
        HBox subInfo = new HBox(10);
        Label lblDni = new Label();
        lblDni.setStyle("-fx-text-fill: #475569; -fx-font-size: 10px;");
        Label lblEdad = new Label();
        lblEdad.setStyle("-fx-text-fill: #475569; -fx-font-size: 10px;");
        subInfo.getChildren().addAll(lblDni, lblEdad);
        infoBasica.getChildren().addAll(lblNombre, subInfo);
        topCard.getChildren().addAll(avatarPane, infoBasica);

        Label lblCelular = new Label();
        lblCelular.setStyle("-fx-text-fill: #475569; -fx-font-size: 10px;");
        cardPaciente.getChildren().addAll(topCard, lblCelular);

        ComboBox<Usuario> cbOdontologo = new ComboBox<>();
        cbOdontologo.setPromptText("Odontólogo");
        cbOdontologo.setMaxWidth(Double.MAX_VALUE);
        cbOdontologo.setCellFactory(param -> new ListCell<>() {
            @Override protected void updateItem(Usuario u, boolean empty) {
                super.updateItem(u, empty);
                setText(empty || u == null ? null : u.getApellidos() + ", " + u.getNombres());
            }
        });
        cbOdontologo.setButtonCell(cbOdontologo.getCellFactory().call(null));

        ComboBox<Procedimiento> cbTratamiento = new ComboBox<>();
        cbTratamiento.setPromptText("Tratamiento");
        cbTratamiento.setMaxWidth(Double.MAX_VALUE);
        cbTratamiento.setCellFactory(param -> new ListCell<>() {
            @Override protected void updateItem(Procedimiento p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null : p.getNombre());
            }
        });
        cbTratamiento.setButtonCell(cbTratamiento.getCellFactory().call(null));

        ComboBox<String> cbConsultorio = new ComboBox<>();
        cbConsultorio.setMaxWidth(Double.MAX_VALUE);
        ComboBox<String> cbHorario = new ComboBox<>();
        cbHorario.setMaxWidth(Double.MAX_VALUE);

        DatePicker dpFechaCita = new DatePicker(LocalDate.now());
        dpFechaCita.setMaxWidth(Double.MAX_VALUE);

        Button btnAgendar = new Button("💾 Agendar Cita");
        btnAgendar.setMaxWidth(Double.MAX_VALUE);
        btnAgendar.setStyle("-fx-background-color: #00A896; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        formPanel.getChildren().addAll(lblTituloForm, new Separator(),
                new Label("DNI del Paciente *"), buscarBox, cardPaciente,
                new Label("Odontólogo *"), cbOdontologo,
                new Label("Tratamiento *"), cbTratamiento,
                new Label("Consultorio *"), cbConsultorio,
                new Label("Fecha *"), dpFechaCita,
                new Label("Horario *"), cbHorario, btnAgendar);

        // --- PANEL DERECHO ---
        VBox tablePanel = new VBox(10);
        tablePanel.setPadding(new Insets(15));
        tablePanel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        HBox.setHgrow(tablePanel, Priority.ALWAYS);

        HBox headerTabla = new HBox(10);
        headerTabla.setAlignment(Pos.CENTER_LEFT);
        Label tituloTabla = new Label("Citas del Día");
        tituloTabla.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label lblContadorCitas = new Label("0 citas");
        lblContadorCitas.setStyle("-fx-text-fill: #0F766E; -fx-font-weight: bold; -fx-font-size: 11px;");
        headerTabla.getChildren().addAll(tituloTabla, spacer, lblContadorCitas);

        TextField txtBuscarTabla = new TextField();
        txtBuscarTabla.setPromptText("Buscar cita por DNI o nombre...");
        txtBuscarTabla.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #CBD5E1; -fx-border-radius: 6; -fx-padding: 8;");

        TableView<Cita> tabla = new TableView<>();
        VBox.setVgrow(tabla, Priority.ALWAYS);

        tablePanel.getChildren().addAll(headerTabla, txtBuscarTabla, tabla);
        contentBox.getChildren().addAll(formPanel, tablePanel);
        mainLayout.getChildren().addAll(topBar, contentBox);

        CitaController controller = new CitaController(txtDniBuscar, cardPaciente, lblIniciales, lblNombre, 
                lblDni, lblEdad, lblCelular, cbOdontologo, cbTratamiento, cbConsultorio, 
                dpFechaCita, cbHorario, tabla, lblContadorCitas, txtBuscarTabla, dpFechaTabla,
                lblTituloForm, btnAgendar);

        btnBuscar.setOnAction(e -> controller.buscarPaciente());
        btnAgendar.setOnAction(e -> controller.guardarCita());

        return mainLayout;
    }
}