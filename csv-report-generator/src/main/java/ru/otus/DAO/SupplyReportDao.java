package ru.otus.DAO;

import ru.otus.model.SupplyReport;

import java.sql.SQLException;
import java.util.List;

public interface SupplyReportDao {
    void save(SupplyReport report) throws SQLException;
    void update(SupplyReport report) throws SQLException;
    void delete(String supplier, String product) throws SQLException;
    List<SupplyReport> findAll() throws SQLException;
    List<SupplyReport> findBySupplier(String supplier) throws SQLException;
}
