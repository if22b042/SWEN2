package com.example.jpademo.api;

import com.example.jpademo.service.TourService;
import com.example.jpademo.service.dtos.TourDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/tours")
public class TourApi {

    private static final Logger log = LoggerFactory.getLogger(TourApi.class);
    @Autowired
    private TourService tourService;

    @PostMapping
    public ResponseEntity<TourDto> createTour(@RequestBody TourDto tourDto) {
        log.info("Received TourDto: {}", tourDto);
        TourDto savedTour = tourService.saveTour(tourDto);
        log.info("Saved TourDto: {}", savedTour);
        return ResponseEntity.ok(savedTour);
    }

    @GetMapping
    public ResponseEntity<List<TourDto>> getAllTours() {
        return ResponseEntity.ok(tourService.getAllTours());
    }

}
