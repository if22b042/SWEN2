package com.example.jpademo.api;

import com.example.jpademo.service.TourService;
import com.example.jpademo.service.dtos.TourDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tours")
public class TourApi {

    @Autowired
    private TourService tourService;

    @PostMapping
    public ResponseEntity<TourDto> createTour(@RequestBody TourDto tourDto) {
        return ResponseEntity.ok(tourService.saveTour(tourDto));
    }

    @GetMapping
    public ResponseEntity<List<TourDto>> getAllTours() {
        return ResponseEntity.ok(tourService.getAllTours());
    }

    // Add more endpoints as needed
}
