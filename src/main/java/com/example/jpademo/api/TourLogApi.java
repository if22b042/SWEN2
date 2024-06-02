package com.example.jpademo.api;

import com.example.jpademo.service.TourLogService;
import com.example.jpademo.service.dtos.TourLogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/tourlogs")
public class TourLogApi {

    private static final Logger log = LoggerFactory.getLogger(TourLogApi.class);

    @Autowired
    private TourLogService tourLogService;

    private static final String IMAGE_UPLOAD_DIR = "uploads/tourlog-images/";

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

    @GetMapping("/{id}")
    public ResponseEntity<TourLogDto> getTourLogById(@PathVariable Long id) {
        TourLogDto tourLogDto = tourLogService.findTourLogById(id).orElse(null);
        if (tourLogDto != null) {
            return ResponseEntity.ok(tourLogDto);
        } else {
            return ResponseEntity.notFound().build();
        }
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

    @PostMapping("/{id}/image")
    public ResponseEntity<Void> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                // Create the directory if it doesn't exist
                Path uploadDir = Paths.get(IMAGE_UPLOAD_DIR);
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                // Save the file
                Path path = uploadDir.resolve(id + "_" + file.getOriginalFilename());
                Files.write(path, file.getBytes());
                tourLogService.updateImagePath(id, path.toString());
            }
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            log.error("Failed to upload image", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        TourLogDto tourLogDto = tourLogService.findTourLogById(id).orElse(null);
        if (tourLogDto != null && tourLogDto.getImagePath() != null) {
            try {
                Path path = Paths.get(tourLogDto.getImagePath());
                byte[] imageBytes = Files.readAllBytes(path);
                return ResponseEntity.ok(imageBytes);
            } catch (IOException e) {
                log.error("Failed to read image", e);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
