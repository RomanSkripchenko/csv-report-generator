package ru.otus.DTO;

public class SupplyVolumeDTO {
    private String month;
    private int volume;

    public SupplyVolumeDTO(String month, int volume) {
        this.month = month;
        this.volume = volume;
    }

    public String getMonth() {
        return month;
    }

    public int getVolume() {
        return volume;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
