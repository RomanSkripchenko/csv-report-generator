package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.model.ReportData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class HtmlReportService {

    public void generateHtmlReport(List<ReportData> data) throws IOException {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Report</title></head><body>");
        html.append("<h1>Отчет по поставкам</h1>");
        html.append("<table>");
        for (ReportData reportData : data) {
            html.append("<tr><td>").append(reportData.toString()).append("</td></tr>");
        }
        html.append("</table></body></html>");
        Files.writeString(Path.of("report.html"), html.toString(), StandardCharsets.UTF_8);
    }
}
