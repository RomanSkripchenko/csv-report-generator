package ru.otus.DTO;

public class SupplierDistributionDTO {
    private String supplier;
    private int totalQuantity;

    public SupplierDistributionDTO(String supplier, int totalQuantity) {
        this.supplier = supplier;
        this.totalQuantity = totalQuantity;
    }

    public String getSupplier() { return supplier; }
    public int getTotalQuantity() { return totalQuantity; }
}

