package ru.otus.DAO;


import org.springframework.stereotype.Repository;
import ru.otus.model.SupplyReport;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Repository
public class SupplyReportDaoImpl implements SupplyReportDao {

    private final DataSource dataSource;

    public SupplyReportDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(SupplyReport report) throws SQLException {
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

    @Override
    public List<SupplyReport> findAll() throws SQLException {
        List<SupplyReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM SupplyReport";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                reports.add(mapRowToReport(resultSet));
            }
        }
        return reports;
    }

    @Override
    public List<SupplyReport> findBySupplier(String supplier) throws SQLException {
        List<SupplyReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM SupplyReport WHERE Supplier = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, supplier);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    reports.add(mapRowToReport(resultSet));
                }
            }
        }
        return reports;
    }

    private SupplyReport mapRowToReport(ResultSet resultSet) throws SQLException {
        SupplyReport report = new SupplyReport();
        report.setSupplier(resultSet.getString("Supplier"));
        report.setProduct(resultSet.getString("Product"));
        report.setQuantity(resultSet.getInt("Quantity"));
        report.setPrice(resultSet.getDouble("Price"));
        return report;
    }

    @Override
    public void update(SupplyReport report) throws SQLException {
        String sql = "UPDATE SupplyReport SET Quantity = ?, Price = ? WHERE Supplier = ? AND Product = ?";

        System.out.println("SQL-запрос: " + sql);
        System.out.println("Данные: " + report.getQuantity() + ", " + report.getPrice() + ", " + report.getSupplier() + ", " + report.getProduct());

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, report.getQuantity());
            statement.setDouble(2, report.getPrice());
            statement.setString(3, report.getSupplier());
            statement.setString(4, report.getProduct());

            int rowsUpdated = statement.executeUpdate();
            System.out.println("Обновлено строк: " + rowsUpdated);
        }
    }

    @Override
    public void delete(String supplier, String product) throws SQLException {

    }
}
