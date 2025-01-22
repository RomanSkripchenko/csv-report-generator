package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.model.SupplyReport;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class JdbcService {

    private final DataSource dataSource;

    public JdbcService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Метод для сохранения отчета в базу данных
    public void saveSupplyReport(SupplyReport report) throws SQLException {
        String sql = "INSERT INTO SupplyReport (Supplier, Product, Quantity, Price) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, report.getSupplier());
            statement.setString(2, report.getProduct());
            statement.setInt(3, report.getQuantity());
            statement.setDouble(4, report.getPrice());
            statement.executeUpdate();
        }
    }

    // Метод для получения всех отчетов из базы данных
    public List<SupplyReport> getAllSupplyReports() throws SQLException {
        List<SupplyReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM SupplyReport";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                SupplyReport report = new SupplyReport();
                report.setSupplier(resultSet.getString("Supplier"));
                report.setProduct(resultSet.getString("Product"));
                report.setQuantity(resultSet.getInt("Quantity"));
                report.setPrice(resultSet.getDouble("Price"));
                reports.add(report);
            }
        }
        return reports;
    }

    // Метод для получения отчетов по поставщику
    public List<SupplyReport> getReportsBySupplier(String supplier) throws SQLException {
        List<SupplyReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM SupplyReport WHERE supplier = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, supplier);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    SupplyReport report = new SupplyReport();
                    report.setSupplier(resultSet.getString("Supplier"));
                    report.setProduct(resultSet.getString("Product"));
                    report.setQuantity(resultSet.getInt("Quantity"));
                    report.setPrice(resultSet.getDouble("Price"));
                    reports.add(report);
                }
            }
        }
        return reports;
    }
}