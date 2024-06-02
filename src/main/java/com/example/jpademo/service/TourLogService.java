package com.example.jpademo.service;

import com.example.jpademo.service.dtos.TourLogDto;

import java.util.List;
import java.util.Optional;

public interface TourLogService {

    TourLogDto saveTourLog(TourLogDto tourLogDto);

    List<TourLogDto> getAllTourLogs();

    List<TourLogDto> getTourLogsByTourId(Long tourId);

    TourLogDto updateTourLog(Long id, TourLogDto tourLogDto);

    void deleteTourLog(Long id);

    void updateImagePath(Long id, String imagePath);

    Optional<TourLogDto> findTourLogById(Long id);
}
