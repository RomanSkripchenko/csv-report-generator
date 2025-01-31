package ru.otus.DTO;

public class PriceTrendDTO {
    private String product;
    private double averagePrice;

    public PriceTrendDTO(String product, double averagePrice) {
        this.product = product;
        this.averagePrice = averagePrice;
    }

    public String getProduct() { return product; }
    public double getAveragePrice() { return averagePrice; }
}

