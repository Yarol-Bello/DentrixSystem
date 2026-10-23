package com.dentrix.view;

import com.dentrix.controller.ReporteController;
import com.dentrix.model.ProductividadOdontologo;
import com.dentrix.model.RankingTratamiento;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import java.io.File;

public class ReportesView {
    public Pane getView() {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #F1F5F9;");

        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 15, 10, 15));
        topBar.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        Label lblPeriodo = new Label("Periodo: Mes Actual");
        lblPeriodo.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569;");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        Button btnExportar = new Button("📊 Exportar Excel");
        btnExportar.setStyle("-fx-background-color: white; -fx-border-color: #CBD5E1; -fx-font-weight: bold; -fx-cursor: hand;");
        topBar.getChildren().addAll(lblPeriodo, spacer, btnExportar);

        HBox contentBox = new HBox(20);
        HBox.setHgrow(contentBox, Priority.ALWAYS);

        // PANEL IZQUIERDO: KPIs
        VBox panelKPIs = new VBox(15);
        panelKPIs.setPadding(new Insets(15));
        panelKPIs.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        panelKPIs.setMinWidth(350);

        Label tituloKPIs = new Label("Indicadores del Mes (KPIs)");
        tituloKPIs.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        GridPane gridKPIs = new GridPane();
        gridKPIs.setHgap(10); gridKPIs.setVgap(10);

        VBox kpi1 = crearTarjetaKPI("Recaudación Total", "#ECFDF5", "#047857", "#10B981");
        Label lblRecaudacion = (Label) kpi1.getChildren().get(1);
        
        VBox kpi2 = crearTarjetaKPI("Pacientes Atendidos", "#EFF6FF", "#1D4ED8", "#3B82F6");
        Label lblPacientes = (Label) kpi2.getChildren().get(1);
        
        VBox kpi3 = crearTarjetaKPI("Cobranza Yape/Plin", "#F5F3FF", "#6D28D9", "#8B5CF6");
        Label lblDigital = (Label) kpi3.getChildren().get(1);
        
        VBox kpi4 = crearTarjetaKPI("Saldos por Cobrar", "#FEF2F2", "#B91C1C", "#EF4444");
        Label lblSaldos = (Label) kpi4.getChildren().get(1);

        gridKPIs.add(kpi1, 0, 0); gridKPIs.add(kpi2, 1, 0);
        gridKPIs.add(kpi3, 0, 1); gridKPIs.add(kpi4, 1, 1);

        VBox boxDesglose = new VBox(5);
        boxDesglose.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-padding: 10; -fx-background-radius: 8;");
        Label lblTitDesglose = new Label("Cobranza por Medio de Pago:");
        lblTitDesglose.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #475569;");
        
        HBox rowEfectivo = new HBox();
        Label lblEfectivoCount = new Label("(Cobros en Caja)");
        lblEfectivoCount.setStyle("-fx-font-size: 10px; -fx-text-fill: #64748B;");
        Label lblEfectivoTotal = new Label("S/ 0.00");
        lblEfectivoTotal.setStyle("-fx-font-weight: bold; -fx-text-fill: #0F766E;");
        Region sp1 = new Region(); HBox.setHgrow(sp1, Priority.ALWAYS);
        rowEfectivo.getChildren().addAll(new Label("Efectivo:"), lblEfectivoCount, sp1, lblEfectivoTotal);

        HBox rowDigital = new HBox();
        Label lblYapeCount = new Label("(Billeteras digitales)");
        lblYapeCount.setStyle("-fx-font-size: 10px; -fx-text-fill: #64748B;");
        Label lblYapeTotal = new Label("S/ 0.00");
        lblYapeTotal.setStyle("-fx-font-weight: bold; -fx-text-fill: #6B21A8;");
        Region sp2 = new Region(); HBox.setHgrow(sp2, Priority.ALWAYS);
        rowDigital.getChildren().addAll(new Label("Yape/Plin:"), lblYapeCount, sp2, lblYapeTotal);

        boxDesglose.getChildren().addAll(lblTitDesglose, rowEfectivo, rowDigital);
        panelKPIs.getChildren().addAll(tituloKPIs, gridKPIs, boxDesglose);

        // PANEL DERECHO: TABLAS
        VBox panelTablas = new VBox(15);
        panelTablas.setPadding(new Insets(15));
        panelTablas.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        HBox.setHgrow(panelTablas, Priority.ALWAYS);

        Label tituloTrat = new Label("Tratamientos Más Demandados");
        tituloTrat.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
        
        TableView<RankingTratamiento> tablaTratamientos = new TableView<>();
        VBox.setVgrow(tablaTratamientos, Priority.ALWAYS);

        Label tituloProd = new Label("Productividad por Odontólogo");
        tituloProd.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
        
        TableView<ProductividadOdontologo> tablaProductividad = new TableView<>();
        VBox.setVgrow(tablaProductividad, Priority.ALWAYS);

        panelTablas.getChildren().addAll(tituloTrat, tablaTratamientos, new Separator(), tituloProd, tablaProductividad);

        contentBox.getChildren().addAll(panelKPIs, panelTablas);
        mainLayout.getChildren().addAll(topBar, contentBox);

        ReporteController controller = new ReporteController(
                lblRecaudacion, lblPacientes, lblDigital, lblSaldos,
                lblEfectivoTotal, lblYapeTotal, lblEfectivoCount, lblYapeCount,
                tablaTratamientos, tablaProductividad
        );

        btnExportar.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar Reporte Excel");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                controller.exportarExcel(file.getAbsolutePath());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Reporte exportado exitosamente a: " + file.getAbsolutePath());
                alert.showAndWait();
            }
        });

        return mainLayout;
    }

    private VBox crearTarjetaKPI(String titulo, String bgColor, String textColor, String borderColor) {
        VBox box = new VBox(5);
        box.setStyle("-fx-background-color: " + bgColor + "; -fx-border-color: " + borderColor + "; -fx-padding: 10; -fx-background-radius: 8; -fx-border-radius: 8;");
        Label lblTit = new Label(titulo);
        lblTit.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: " + textColor + ";");
        Label lblVal = new Label("S/ 0.00");
        lblVal.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + textColor + ";");
        box.getChildren().addAll(lblTit, lblVal);
        return box;
    }
}