package ru.otus.analytics;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.DTO.AveragesDTO;
import ru.otus.DTO.SupplyVolumeDTO;
import ru.otus.service.AnalyticsService;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/supply-volume")
    public ResponseEntity<List<SupplyVolumeDTO>> getSupplyVolumeByMonth() {
        List<SupplyVolumeDTO> volumes = analyticsService.calculateSupplyVolumeByMonth();
        return ResponseEntity.ok(volumes);
    }

    @GetMapping("/averages")
    public ResponseEntity<AveragesDTO> getAverages() {
        AveragesDTO averages = analyticsService.calculateAverages();
        return ResponseEntity.ok(averages);
    }
}

