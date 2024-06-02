package com.example.jpademo.service.impl;

import com.example.jpademo.persistence.entities.TourEntity;
import com.example.jpademo.persistence.repositories.TourRepository;
import com.example.jpademo.service.TourService;
import com.example.jpademo.service.dtos.TourDto;
import com.example.jpademo.service.mapper.TourMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final TourMapper tourMapper;

    @Autowired
    public TourServiceImpl(TourRepository tourRepository, TourMapper tourMapper) {
        this.tourRepository = tourRepository;
        this.tourMapper = tourMapper;
    }

    @Override
    public TourDto saveTour(TourDto tourDto) {
        System.out.println("DTO before mapping: " + tourDto);
        if (tourDto==null) {
            System.err.println("Error data is null");
            return null;
        }
        TourEntity tourEntity = tourMapper.toEntity(tourDto);
        System.out.println("Entity after manual mapping: " + tourEntity);

        tourEntity = tourRepository.save(tourEntity);
        System.out.println("Entity after saving: " + tourEntity);

        return tourMapper.toDto(tourEntity);
    }

    @Override
    public List<TourDto> getAllTours() {
        return tourRepository.findAll().stream()
                .map(tourMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TourDto> findTourById(long id) {
        return tourRepository.findById(id)
                .map(tourMapper::toDto);
    }

    @Override
    public TourDto updateTour(Long id, TourDto tourDto) {
        TourEntity tourEntity = tourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found with given id"));

        tourMapper.updateEntityFromDto(tourDto, tourEntity);

        TourEntity savedEntity = tourRepository.save(tourEntity);
        return tourMapper.toDto(savedEntity);
    }

    @Override
    public void deleteTour(Long id) {
        tourRepository.deleteById(id);
    }
}
