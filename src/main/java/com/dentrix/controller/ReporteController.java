package com.dentrix.controller;

import com.dentrix.model.ProductividadOdontologo;
import com.dentrix.model.RankingTratamiento;
import com.dentrix.service.ReporteService;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

public class ReporteController {
    private ReporteService service;

    private Label lblRecaudacion, lblPacientes, lblDigital, lblSaldos;
    private Label lblEfectivoTotal, lblYapeTotal, lblEfectivoCount, lblYapeCount;
    private TableView<RankingTratamiento> tablaTratamientos;
    private TableView<ProductividadOdontologo> tablaProductividad;

    public ReporteController(Label lblRecaudacion, Label lblPacientes, Label lblDigital, Label lblSaldos,
                             Label lblEfectivoTotal, Label lblYapeTotal, Label lblEfectivoCount, Label lblYapeCount,
                             TableView<RankingTratamiento> tablaTratamientos,
                             TableView<ProductividadOdontologo> tablaProductividad) {
        this.service = new ReporteService();
        this.lblRecaudacion = lblRecaudacion; this.lblPacientes = lblPacientes; 
        this.lblDigital = lblDigital; this.lblSaldos = lblSaldos;
        this.lblEfectivoTotal = lblEfectivoTotal; this.lblYapeTotal = lblYapeTotal; 
        this.lblEfectivoCount = lblEfectivoCount; this.lblYapeCount = lblYapeCount;
        this.tablaTratamientos = tablaTratamientos; this.tablaProductividad = tablaProductividad;

        configurarTablas();
        cargarDatos();
    }

    public void cargarDatos() {
        double recaudacion = service.getRecaudacionTotal();
        double digital = service.getRecaudacionDigital();
        double efectivo = service.getRecaudacionEfectivo();
        double saldos = service.getSaldosPorCobrar();
        int pacientes = service.getPacientesAtendidosMes();

        lblRecaudacion.setText("S/ " + String.format("%.2f", recaudacion));
        lblPacientes.setText(String.valueOf(pacientes));
        lblDigital.setText("S/ " + String.format("%.2f", digital));
        lblSaldos.setText("S/ " + String.format("%.2f", saldos));

        lblEfectivoTotal.setText("S/ " + String.format("%.2f", efectivo));
        lblYapeTotal.setText("S/ " + String.format("%.2f", digital));
        
        lblEfectivoCount.setText("(Cobros en Caja)");
        lblYapeCount.setText("(Billeteras digitales)");

        tablaTratamientos.setItems(FXCollections.observableArrayList(service.getRankingTratamientos()));
        tablaProductividad.setItems(FXCollections.observableArrayList(service.getProductividadOdontologos()));
    }

    private void configurarTablas() {
        TableColumn<RankingTratamiento, String> colCod = new TableColumn<>("Código");
        colCod.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        TableColumn<RankingTratamiento, String> colNom = new TableColumn<>("Procedimiento Dental");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<RankingTratamiento, Integer> colCant = new TableColumn<>("Cant.");
        colCant.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        TableColumn<RankingTratamiento, Double> colFact = new TableColumn<>("Facturación");
        colFact.setCellValueFactory(new PropertyValueFactory<>("facturacion"));
        tablaTratamientos.getColumns().addAll(colCod, colNom, colCant, colFact);

        TableColumn<ProductividadOdontologo, String> colProf = new TableColumn<>("Profesional");
        colProf.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<ProductividadOdontologo, String> colCons = new TableColumn<>("Consultorio");
        colCons.setCellValueFactory(new PropertyValueFactory<>("consultorio"));
        TableColumn<ProductividadOdontologo, Integer> colAt = new TableColumn<>("Atenciones");
        colAt.setCellValueFactory(new PropertyValueFactory<>("atenciones"));
        TableColumn<ProductividadOdontologo, Double> colProd = new TableColumn<>("Producción");
        colProd.setCellValueFactory(new PropertyValueFactory<>("produccion"));
        tablaProductividad.getColumns().addAll(colProf, colCons, colAt, colProd);
    }

    public void exportarExcel(String ruta) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte Mensual");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("TRATAMIENTO");
            header.createCell(1).setCellValue("CANTIDAD");
            header.createCell(2).setCellValue("FACTURACIÓN (S/)");

            int rowNum = 1;
            for (RankingTratamiento rt : tablaTratamientos.getItems()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rt.getNombre());
                row.createCell(1).setCellValue(rt.getCantidad());
                row.createCell(2).setCellValue(rt.getFacturacion());
            }
            try (FileOutputStream fileOut = new FileOutputStream(ruta)) {
                workbook.write(fileOut);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}