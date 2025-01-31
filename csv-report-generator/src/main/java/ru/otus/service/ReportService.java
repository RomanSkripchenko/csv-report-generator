package ru.otus.service;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import java.util.Base64;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.DAO.SupplyReportDao;
import ru.otus.DTO.PriceTrendDTO;
import ru.otus.DTO.SupplierDistributionDTO;
import ru.otus.DTO.SupplyVolumeDTO;
import ru.otus.Handler.SupplyReportHandler;
import ru.otus.model.SupplyReport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final List<SupplyReport> reports = new ArrayList<>();
    private final SupplyReportHandler supplyReportHandler;
    private final SupplyReportDao supplyReportDao;

    public List<SupplyReport> parseCsv(MultipartFile file) throws Exception {
        System.out.println("Processing file: " + file.getOriginalFilename());
        try (var reader = new InputStreamReader(file.getInputStream())) {
            List<SupplyReport> parsedReports = new CsvToBeanBuilder<SupplyReport>(reader)
                    .withType(SupplyReport.class)
                    .withSeparator(';')
                    .build()
                    .parse();
            reports.addAll(parsedReports);
            return parsedReports;
        }
    }

    public List<SupplyReport> getReports() {
        return new ArrayList<>(reports);
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
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Отчет по поставкам").setBold().setFontSize(18));

            Table table = new Table(4);
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

    @Async
    public void processFile(MultipartFile file) {
        // Логика обработки файла
        System.out.println("Processing file: " + file.getOriginalFilename());
    }
    public ReportService(SupplyReportHandler supplyReportHandler, SupplyReportDao supplyReportDao) {
        this.supplyReportHandler = supplyReportHandler;
        this.supplyReportDao = supplyReportDao;
    }

    public void updateReport(SupplyReport report) throws SQLException {
        System.out.println("Передаем в DAO: " + report);
        supplyReportDao.update(report);
    }

    @Transactional
    public void saveReport(SupplyReport report) throws SQLException {
        supplyReportHandler.saveReport(report);
    }

    @Transactional(readOnly = true)
    public List<SupplyReport> getAllReports() throws SQLException {
        return supplyReportHandler.getAllReports();
    }

    @Transactional(readOnly = true)
    public List<SupplyReport> getReportsBySupplier(String supplier) throws SQLException {
        return supplyReportHandler.getReportsBySupplier(supplier);
    }

    public List<SupplyVolumeDTO> calculateSupplyVolumeByMonth() throws SQLException {
        List<SupplyReport> reports = supplyReportDao.findAll();
        Map<String, Integer> volumeByMonth = new HashMap<>();

        for (SupplyReport report : reports) {
            String month = LocalDate.now().getMonth().toString(); // Заглушка, можно парсить дату из отчета
            volumeByMonth.put(month, volumeByMonth.getOrDefault(month, 0) + report.getQuantity());
        }

        return volumeByMonth.entrySet().stream()
                .map(entry -> new SupplyVolumeDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<PriceTrendDTO> calculatePriceTrend() throws SQLException {
        List<SupplyReport> reports = supplyReportDao.findAll();
        Map<String, Double> averagePrices = new HashMap<>();

        for (SupplyReport report : reports) {
            averagePrices.put(report.getProduct(),
                    averagePrices.getOrDefault(report.getProduct(), 0.0) + report.getPrice());
        }

        return averagePrices.entrySet().stream()
                .map(entry -> new PriceTrendDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<SupplierDistributionDTO> calculateSupplierDistribution() throws SQLException {
        List<SupplyReport> reports = supplyReportDao.findAll();
        Map<String, Integer> supplierCounts = new HashMap<>();

        for (SupplyReport report : reports) {
            supplierCounts.put(report.getSupplier(),
                    supplierCounts.getOrDefault(report.getSupplier(), 0) + report.getQuantity());
        }

        return supplierCounts.entrySet().stream()
                .map(entry -> new SupplierDistributionDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public ByteArrayInputStream exportToExcelWithCharts(List<String> base64Images) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Supply Report");

            // Заголовки
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Supplier");
            header.createCell(1).setCellValue("Product");
            header.createCell(2).setCellValue("Quantity");
            header.createCell(3).setCellValue("Price");

            // Данные
            List<SupplyReport> reports = supplyReportDao.findAll();
            int rowIndex = 1;
            for (SupplyReport report : reports) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(report.getSupplier());
                row.createCell(1).setCellValue(report.getProduct());
                row.createCell(2).setCellValue(report.getQuantity());
                row.createCell(3).setCellValue(report.getPrice());
            }

            // Вставляем изображения графиков
            int imageRow = rowIndex + 2; // Стартовая строка для первого графика
            CreationHelper helper = workbook.getCreationHelper();
            Drawing<?> drawing = sheet.createDrawingPatriarch();

            for (String base64Image : base64Images) {
                byte[] imageBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);
                int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);

                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setCol1(0); // Первый столбец
                anchor.setRow1(imageRow); // Двигаем вниз
                anchor.setCol2(8); // Конечный столбец, чтобы изображение было шире
                anchor.setRow2(imageRow + 15); // Размещаем каждый график на 15 строк

                Picture pict = drawing.createPicture(anchor, pictureIdx);
                pict.resize(1.5); // Увеличиваем размер

                imageRow += 35; // Сдвигаем следующий график вниз, чтобы они не накладывались
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
    public ByteArrayInputStream exportToPdfWithCharts(List<String> base64Images) throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(out));
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Отчет по поставкам").setBold().setFontSize(18));

            // Добавляем таблицу с данными
            Table table = new Table(4);
            table.addCell("Поставщик").setBold();
            table.addCell("Продукт").setBold();
            table.addCell("Количество").setBold();
            table.addCell("Цена").setBold();

            List<SupplyReport> reports = supplyReportDao.findAll();
            for (SupplyReport report : reports) {
                table.addCell(report.getSupplier());
                table.addCell(report.getProduct());
                table.addCell(String.valueOf(report.getQuantity()));
                table.addCell(String.valueOf(report.getPrice()));
            }

            document.add(table);
            document.add(new Paragraph("\nГрафики и диаграммы:\n"));

            // Вставляем изображения графиков
            for (String base64Image : base64Images) {
                byte[] imageBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);
                ImageData imageData = ImageDataFactory.create(imageBytes);
                Image image = new Image(imageData);
                image.setAutoScale(true);
                document.add(image);
                document.add(new Paragraph("\n"));
            }

            document.close();
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

}
