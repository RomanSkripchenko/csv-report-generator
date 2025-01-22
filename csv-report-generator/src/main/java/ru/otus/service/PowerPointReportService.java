package ru.otus.service;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.springframework.stereotype.Service;
import ru.otus.model.ReportData;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PowerPointReportService {

    public void generatePptReport(List<ReportData> data) throws IOException {
        XMLSlideShow ppt = new XMLSlideShow();
        for (ReportData reportData : data) {
            XSLFSlide slide = ppt.createSlide();
            XSLFTextBox textBox = slide.createTextBox();
            textBox.setText(reportData.toString());
        }
        try (FileOutputStream out = new FileOutputStream("report.pptx")) {
            ppt.write(out);
        }
    }
}
