package ru.otus.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;
import ru.otus.model.ReportData;

import java.io.IOException;
import java.util.List;

@Service
public class PdfReportService {

    public void generateDetailedPdf(List<ReportData> data) throws IOException {
        try (PdfWriter writer = new PdfWriter("report.pdf")) {
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.add(new Paragraph("Отчет по поставкам"));
            for (ReportData item : data) {
                document.add(new Paragraph(item.toString()));
            }
        }
    }
}

