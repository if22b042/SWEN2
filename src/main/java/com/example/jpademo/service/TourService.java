package com.example.jpademo.service;

import com.example.jpademo.service.dtos.TourDto;
import java.util.List;
import java.util.Optional;

public interface TourService {
    TourDto saveTour(TourDto tourDto);
    List<TourDto> getAllTours();

    Optional<TourDto> findTourById(long l);
    // Add more methods as needed
}
