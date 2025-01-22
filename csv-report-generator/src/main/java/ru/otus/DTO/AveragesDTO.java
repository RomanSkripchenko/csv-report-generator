package ru.otus.DTO;

public class AveragesDTO {
    private double averageCost;
    private double averageDeliveryTime;

    public AveragesDTO(double averageCost, double averageDeliveryTime) {
        this.averageCost = averageCost;
        this.averageDeliveryTime = averageDeliveryTime;
    }

    public double getAverageCost() {
        return averageCost;
    }

    public double getAverageDeliveryTime() {
        return averageDeliveryTime;
    }

    public void setAverageCost(double averageCost) {
        this.averageCost = averageCost;
    }

    public void setAverageDeliveryTime(double averageDeliveryTime) {
        this.averageDeliveryTime = averageDeliveryTime;
    }
}
