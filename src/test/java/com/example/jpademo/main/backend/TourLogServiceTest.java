package com.example.jpademo.main.backend;

import com.example.jpademo.persistence.entities.TourEntity;
import com.example.jpademo.persistence.entities.TourLogEntity;
import com.example.jpademo.persistence.repositories.TourLogRepository;
import com.example.jpademo.service.TourLogService;
import com.example.jpademo.service.dtos.TourLogDto;
import com.example.jpademo.service.impl.TourLogServiceImpl;
import com.example.jpademo.service.mapper.TourLogMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TourLogServiceTest {

    @Mock
    private TourLogRepository tourLogRepository;

    @Mock
    private TourLogMapper tourLogMapper;

    @InjectMocks
    private TourLogServiceImpl tourLogService;

    private TourLogDto tourLogDto;
    private TourLogEntity tourLogEntity;
    private TourEntity tourEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tourLogDto = new TourLogDto();
        tourLogDto.setId(1L);
        tourLogDto.setComment("Comment");
        tourLogDto.setDateTime(LocalDateTime.now());
        tourLogDto.setDifficulty("Easy");
        tourLogDto.setTotalDistance(10);
        tourLogDto.setTotalTime(1);
        tourLogDto.setRating(5);
        tourLogDto.setTourId(1L);
        tourLogDto.setImagePath("uploads/tourlog-images/1_image.png");

        tourEntity = new TourEntity(1L, "Title", "Description", "Start", "End", "Car", 100, 10, "Info");

        tourLogEntity = new TourLogEntity();
        tourLogEntity.setId(1L);
        tourLogEntity.setDateTime(LocalDateTime.now());
        tourLogEntity.setComment("Comment");
        tourLogEntity.setDifficulty("Easy");
        tourLogEntity.setTotalDistance(10);
        tourLogEntity.setTotalTime(1);
        tourLogEntity.setRating(5);
        tourLogEntity.setTour(tourEntity);
        tourLogEntity.setImagePath("uploads/tourlog-images/1_image.png");
    }

    @Test
    void saveTourLog() {
        when(tourLogMapper.toEntity(any())).thenReturn(tourLogEntity);
        when(tourLogRepository.save(any())).thenReturn(tourLogEntity);
        when(tourLogMapper.toDto(any())).thenReturn(tourLogDto);

        TourLogDto savedTourLog = tourLogService.saveTourLog(tourLogDto);

        assertNotNull(savedTourLog);
        assertEquals("Comment", savedTourLog.getComment());
        verify(tourLogRepository, times(1)).save(tourLogEntity);
    }

    @Test
    void getAllTourLogs() {
        when(tourLogRepository.findAll()).thenReturn(Arrays.asList(tourLogEntity));
        when(tourLogMapper.toDto(any())).thenReturn(tourLogDto);

        List<TourLogDto> tourLogs = tourLogService.getAllTourLogs();

        assertEquals(1, tourLogs.size());
        verify(tourLogRepository, times(1)).findAll();
    }

    @Test
    void getTourLogsByTourId() {
        when(tourLogRepository.findByTourId(anyLong())).thenReturn(Arrays.asList(tourLogEntity));
        when(tourLogMapper.toDto(any())).thenReturn(tourLogDto);

        List<TourLogDto> tourLogs = tourLogService.getTourLogsByTourId(1L);

        assertEquals(1, tourLogs.size());
        verify(tourLogRepository, times(1)).findByTourId(1L);
    }

    @Test
    void updateTourLog() {
        when(tourLogRepository.existsById(anyLong())).thenReturn(true);
        when(tourLogMapper.toEntity(any())).thenReturn(tourLogEntity);
        when(tourLogRepository.save(any())).thenReturn(tourLogEntity);
        when(tourLogMapper.toDto(any())).thenReturn(tourLogDto);

        TourLogDto updatedTourLog = tourLogService.updateTourLog(1L, tourLogDto);

        assertNotNull(updatedTourLog);
        assertEquals("Comment", updatedTourLog.getComment());
        verify(tourLogRepository, times(1)).save(tourLogEntity);
    }

    @Test
    void deleteTourLog() {
        tourLogService.deleteTourLog(1L);
        verify(tourLogRepository, times(1)).deleteById(1L);
    }
}
