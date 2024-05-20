package com.example.jpademo.service;

import com.example.jpademo.persistence.entities.TourLogEntity;
import com.example.jpademo.persistence.repositories.TourLogRepository;
import com.example.jpademo.service.dtos.TourLogDto;
import com.example.jpademo.service.mapper.TourLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TourLogService {

    @Autowired
    private TourLogRepository tourLogRepository;

    @Autowired
    private TourLogMapper tourLogMapper;

    public TourLogDto saveTourLog(TourLogDto tourLogDto) {
        TourLogEntity tourLogEntity = tourLogMapper.toEntity(tourLogDto);
        TourLogEntity savedTourLog = tourLogRepository.save(tourLogEntity);
        return tourLogMapper.toDto(savedTourLog);
    }

    public List<TourLogDto> getAllTourLogs() {
        return tourLogRepository.findAll().stream()
                .map(tourLogMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TourLogDto> getTourLogsByTourId(Long tourId) {
        return tourLogRepository.findByTourId(tourId).stream()
                .map(tourLogMapper::toDto)
                .collect(Collectors.toList());
    }

    public TourLogDto updateTourLog(Long id, TourLogDto tourLogDto) {
        if (tourLogRepository.existsById(id)) {
            TourLogEntity tourLogEntity = tourLogMapper.toEntity(tourLogDto);
            tourLogEntity.setId(id);
            TourLogEntity updatedTourLog = tourLogRepository.save(tourLogEntity);
            return tourLogMapper.toDto(updatedTourLog);
        }
        return null;
    }

    public void deleteTourLog(Long id) {
        tourLogRepository.deleteById(id);
    }
}
