package ru.otus.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.DTO.ChartExportRequest;
import ru.otus.DTO.PriceTrendDTO;
import ru.otus.DTO.SupplierDistributionDTO;
import ru.otus.DTO.SupplyVolumeDTO;
import ru.otus.model.SupplyReport;
import ru.otus.service.ReportService;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private List<SupplyReport> cachedReports;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        try {
            // Парсим CSV
            List<SupplyReport> parsedReports = reportService.parseCsv(file);

            // Сохраняем в базу
            for (SupplyReport report : parsedReports) {
                reportService.saveReport(report);
            }

            // Обновляем cachedReports
            cachedReports = parsedReports;

            model.addAttribute("reports", parsedReports);
            return "report";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при обработке файла: " + e.getMessage());
            return "upload";
        }
    }

    @GetMapping("/all")
    public String getAllReports(Model model) throws SQLException {
        List<SupplyReport> reports = reportService.getAllReports();
        model.addAttribute("reports", reports);
        return "report";
    }

    @GetMapping("/supplier")
    public String getReportsBySupplier(@RequestParam String supplier, Model model) throws SQLException {
        List<SupplyReport> reports = reportService.getReportsBySupplier(supplier);
        model.addAttribute("reports", reports);
        return "report";
    }

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportToExcel() throws Exception {
        if (cachedReports == null || cachedReports.isEmpty()) {
            throw new IllegalStateException("Нет данных для экспорта. Сначала загрузите файл.");
        }

        var excelStream = reportService.exportToExcel(cachedReports);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excelStream));
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<InputStreamResource> exportToPdf() throws Exception {
        if (cachedReports == null) {
            throw new IllegalStateException("No data available to export.");
        }

        var pdfStream = reportService.exportToPdf(cachedReports);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateReport(@RequestBody SupplyReport report) {
        System.out.println("Обновляемый отчет: " + report);
        try {
            reportService.updateReport(report);
            return ResponseEntity.ok("{\"message\": \"Отчет успешно обновлен\"}");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Ошибка обновления: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/chart-data")
    @ResponseBody
    public List<SupplyVolumeDTO> getChartData() throws SQLException {
        return reportService.calculateSupplyVolumeByMonth();
    }

    @GetMapping("/chart/price-trend")
    @ResponseBody
    public List<PriceTrendDTO> getPriceTrend() throws SQLException {
        return reportService.calculatePriceTrend();
    }

    @GetMapping("/chart/supplier-distribution")
    @ResponseBody
    public List<SupplierDistributionDTO> getSupplierDistribution() throws SQLException {
        return reportService.calculateSupplierDistribution();
    }

    @PostMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportToExcelWithCharts(@RequestBody ChartExportRequest request) throws Exception {
        ByteArrayInputStream excelStream = reportService.exportToExcelWithCharts(request.getImages());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_with_charts.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(excelStream));
    }
    @PostMapping("/export/pdf")
    public ResponseEntity<InputStreamResource> exportToPdfWithCharts(@RequestBody ChartExportRequest request) throws Exception {
        ByteArrayInputStream pdfStream = reportService.exportToPdfWithCharts(request.getImages());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_with_charts.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }

}