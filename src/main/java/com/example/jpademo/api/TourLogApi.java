package com.example.jpademo.api;

import com.example.jpademo.service.TourLogService;
import com.example.jpademo.service.dtos.TourLogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/tourlogs")
public class TourLogApi {

    private static final Logger log = LoggerFactory.getLogger(TourLogApi.class);

    @Autowired
    private TourLogService tourLogService;

    @PostMapping
    public ResponseEntity<TourLogDto> createTourLog(@RequestBody TourLogDto tourLogDto) {
        log.info("Received TourLogDto: {}", tourLogDto);
        TourLogDto savedTourLog = tourLogService.saveTourLog(tourLogDto);
        log.info("Saved TourLogDto: {}", savedTourLog);
        return ResponseEntity.ok(savedTourLog);
    }

    @GetMapping
    public ResponseEntity<List<TourLogDto>> getAllTourLogs() {
        return ResponseEntity.ok(tourLogService.getAllTourLogs());
    }

    @GetMapping("/tour/{tourId}")
    public ResponseEntity<List<TourLogDto>> getTourLogsByTourId(@PathVariable Long tourId) {
        return ResponseEntity.ok(tourLogService.getTourLogsByTourId(tourId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TourLogDto> updateTourLog(@PathVariable Long id, @RequestBody TourLogDto tourLogDto) {
        log.info("Received TourLogDto to update: {}", tourLogDto);
        TourLogDto updatedTourLog = tourLogService.updateTourLog(id, tourLogDto);
        log.info("Updated TourLogDto: {}", updatedTourLog);
        return ResponseEntity.ok(updatedTourLog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTourLog(@PathVariable Long id) {
        tourLogService.deleteTourLog(id);
        return ResponseEntity.noContent().build();
    }
}
