package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.DTO.AveragesDTO;
import ru.otus.DTO.SupplyVolumeDTO;

import java.util.List;

@Service
public class AnalyticsService {
    public List<SupplyVolumeDTO> calculateSupplyVolumeByMonth() {
        return List.of(
                new SupplyVolumeDTO("January", 120),
                new SupplyVolumeDTO("February", 90)
        );
    }

    public AveragesDTO calculateAverages() {
        return new AveragesDTO(150.75, 5.3);
    }
}
