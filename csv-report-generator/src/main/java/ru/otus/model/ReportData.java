package ru.otus.model;

public class ReportData {
    private String supplier;
    private String product;
    private int quantity;
    private double price;

    public ReportData(String supplier, String product, int quantity, double price) {
        this.supplier = supplier;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Supplier: " + supplier + ", Product: " + product + ", Quantity: " + quantity + ", Price: " + price;
    }
}
