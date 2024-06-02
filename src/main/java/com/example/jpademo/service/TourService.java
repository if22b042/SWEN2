package com.example.jpademo.service;

import com.example.jpademo.service.dtos.TourDto;
import java.util.List;
import java.util.Optional;

public interface TourService {

    TourDto saveTour(TourDto tourDto);
    List<TourDto> getAllTours();
    TourDto updateTour(Long id, TourDto tourDto);
    Optional<TourDto> findTourById(long id);
    void deleteTour(Long id); // Add this method
}
