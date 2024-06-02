package com.example.jpademo.service.impl;

import com.example.jpademo.persistence.entities.TourLogEntity;
import com.example.jpademo.persistence.repositories.TourLogRepository;
import com.example.jpademo.service.TourLogService;
import com.example.jpademo.service.dtos.TourLogDto;
import com.example.jpademo.service.mapper.TourLogMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TourLogServiceImpl implements TourLogService {

    @Autowired
    private TourLogRepository tourLogRepository;

    @Autowired
    private TourLogMapper tourLogMapper;

    @Override
    public TourLogDto saveTourLog(TourLogDto tourLogDto) {
        if (tourLogDto == null) {
            throw new NullPointerException("tourLogDto is marked non-null but is null");
        }
        TourLogEntity tourLogEntity = tourLogMapper.toEntity(tourLogDto);
        TourLogEntity savedTourLog = tourLogRepository.save(tourLogEntity);
        return tourLogMapper.toDto(savedTourLog);
    }

    @Override
    public List<TourLogDto> getAllTourLogs() {
        return tourLogRepository.findAll().stream()
                .map(tourLogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TourLogDto> getTourLogsByTourId(Long tourId) {
        return tourLogRepository.findByTourId(tourId).stream()
                .map(tourLogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TourLogDto updateTourLog(Long id, TourLogDto tourLogDto) {
        if (tourLogRepository.existsById(id)) {
            TourLogEntity tourLogEntity = tourLogMapper.toEntity(tourLogDto);
            tourLogEntity.setId(id);
            TourLogEntity updatedTourLog = tourLogRepository.save(tourLogEntity);
            return tourLogMapper.toDto(updatedTourLog);
        }
        return null;
    }

    @Override
    public void deleteTourLog(Long id) {
        tourLogRepository.deleteById(id);
    }

    @Override
    public void updateImagePath(Long id, String imagePath) {
        TourLogEntity tourLogEntity = tourLogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour log not found"));
        tourLogEntity.setImagePath(imagePath);
        tourLogRepository.save(tourLogEntity);
    }

    @Override
    public Optional<TourLogDto> findTourLogById(Long id) {
        return tourLogRepository.findById(id).map(tourLogMapper::toDto);
    }
}
