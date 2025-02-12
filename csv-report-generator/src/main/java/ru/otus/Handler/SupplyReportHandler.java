package ru.otus.Handler;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import ru.otus.DAO.SupplyReportDaoImpl;
import ru.otus.model.SupplyReport;

import java.sql.SQLException;
import java.util.List;
@SpringBootApplication
@ComponentScan(basePackages = "ru.otus")
public class SupplyReportHandler {

    private final SupplyReportDaoImpl supplyReportDao;

    public SupplyReportHandler(SupplyReportDaoImpl supplyReportDao) {
        this.supplyReportDao = supplyReportDao;
    }

    public void saveReport(SupplyReport report) throws SQLException {
        supplyReportDao.save(report);
    }

    public List<SupplyReport> getAllReports() throws SQLException {
        return supplyReportDao.findAll();
    }

    public List<SupplyReport> getReportsBySupplier(String supplier) throws SQLException {
        return supplyReportDao.findBySupplier(supplier);
    }
}