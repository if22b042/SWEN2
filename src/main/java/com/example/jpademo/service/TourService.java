package com.example.jpademo.service;

import com.example.jpademo.service.dtos.TourDto;
import java.util.List;
import java.util.Optional;
import com.example.jpademo.persistence.entities.TourEntity;
import com.example.jpademo.service.dtos.TourDto;
import org.mapstruct.Mapper;

public interface TourService {


    TourDto saveTour(TourDto tourDto);
    List<TourDto> getAllTours();

    TourDto updateTour(Long id, TourDto tourDto);

    Optional<TourDto> findTourById(long id);

}
