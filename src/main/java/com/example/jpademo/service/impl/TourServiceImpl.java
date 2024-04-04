package com.example.jpademo.service.impl;

import com.example.jpademo.persistence.entities.TourEntity;
import com.example.jpademo.persistence.repositories.TourRepository;
import com.example.jpademo.service.TourService;
import com.example.jpademo.service.dtos.TourDto;
import com.example.jpademo.service.mapper.TourMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TourServiceImpl implements TourService {

    @Autowired
    private TourRepository tourRepository;
    private final TourMapper tourMapper = TourMapper.INSTANCE;

    @Override
    public TourDto saveTour(TourDto tourDto) {
        TourEntity tourEntity = tourMapper.toEntity(tourDto);
        tourEntity = tourRepository.save(tourEntity);
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

}
