package ru.otus.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.model.SupplyReport;
import ru.otus.service.ReportService;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;
    private List<SupplyReport> cachedReports;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        try {
            // Используем ReportService для парсинга CSV
            List<SupplyReport> parsedReports = reportService.parseCsv(file);
            for (SupplyReport report : parsedReports) {
                reportService.saveReport(report);
            }
            model.addAttribute("reports", parsedReports);
            return "report";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to process the file: " + e.getMessage());
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
        if (cachedReports == null) {
            throw new IllegalStateException("No data available to export.");
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
}