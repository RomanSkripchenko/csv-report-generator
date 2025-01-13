package ru.otus.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.model.SupplyReport;

@Service
public class ReportService {

    public List<SupplyReport> parseCsv(MultipartFile file) throws Exception {
        System.out.println("Processing file: " + file.getOriginalFilename());
        try (var reader = new InputStreamReader(file.getInputStream())) {
            return new CsvToBeanBuilder<SupplyReport>(reader)
                    .withType(SupplyReport.class)
                    .withSeparator(';')
                    .build()
                    .parse();
        }
    }

    public ByteArrayInputStream exportToExcel(List<SupplyReport> reports) throws Exception {
        try (var workbook = new XSSFWorkbook();
                var out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Supply Report");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Supplier");
            header.createCell(1).setCellValue("Product");
            header.createCell(2).setCellValue("Quantity");
            header.createCell(3).setCellValue("Price");

            int rowIndex = 1;
            for (SupplyReport report : reports) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(report.getSupplier());
                row.createCell(1).setCellValue(report.getProduct());
                row.createCell(2).setCellValue(report.getQuantity());
                row.createCell(3).setCellValue(report.getPrice());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream exportToPdf(List<SupplyReport> reports) throws Exception {
        try (var out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            PdfPTable table = new PdfPTable(4);
            table.addCell("Supplier");
            table.addCell("Product");
            table.addCell("Quantity");
            table.addCell("Price");

            for (SupplyReport report : reports) {
                table.addCell(report.getSupplier());
                table.addCell(report.getProduct());
                table.addCell(String.valueOf(report.getQuantity()));
                table.addCell(String.valueOf(report.getPrice()));
            }

            document.add(table);
            document.close();
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
