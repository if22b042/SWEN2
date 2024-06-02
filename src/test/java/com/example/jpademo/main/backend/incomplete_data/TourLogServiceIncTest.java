package com.example.jpademo.main.backend.incomplete_data;

import com.example.jpademo.persistence.entities.TourEntity;
import com.example.jpademo.persistence.entities.TourLogEntity;
import com.example.jpademo.persistence.repositories.TourLogRepository;
import com.example.jpademo.service.impl.TourLogServiceImpl;
import com.example.jpademo.service.dtos.TourLogDto;
import com.example.jpademo.service.mapper.TourLogMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TourLogServiceIncTest {

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
    void updateTourLog_WithIncompleteData() {
        when(tourLogRepository.existsById(anyLong())).thenReturn(true);
        when(tourLogRepository.findById(anyLong())).thenReturn(Optional.of(tourLogEntity));
        when(tourLogRepository.save(any(TourLogEntity.class))).thenReturn(tourLogEntity);
        when(tourLogMapper.toDto(any())).thenReturn(tourLogDto);

        TourLogDto incompleteTourLogDto = new TourLogDto();
        incompleteTourLogDto.setId(1L);
        incompleteTourLogDto.setComment("Updated Comment");

        TourLogDto updatedTourLog = tourLogService.updateTourLog(1L, incompleteTourLogDto);

        assertNotNull(updatedTourLog);
        assertEquals("Updated Comment", updatedTourLog.getComment());
        verify(tourLogRepository, times(1)).save(tourLogEntity);
    }

    @Test
    void deleteTourLog_WithInvalidId() {
        doThrow(new IllegalArgumentException("Invalid ID")).when(tourLogRepository).deleteById(anyLong());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            tourLogService.deleteTourLog(999L);
        });

        assertEquals("Invalid ID", thrown.getMessage());
        verify(tourLogRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void getTourLogsByTourId_WithInvalidId() {
        when(tourLogRepository.findByTourId(anyLong())).thenReturn(List.of());

        List<TourLogDto> result = tourLogService.getTourLogsByTourId(999L);

        assertTrue(result.isEmpty());
        verify(tourLogRepository, times(1)).findByTourId(anyLong());
    }

    @Test
    void updateTourLog_WithNonExistentId() {
        when(tourLogRepository.existsById(anyLong())).thenReturn(false);

        TourLogDto updatedTourLog = tourLogService.updateTourLog(999L, tourLogDto);

        assertNull(updatedTourLog);
        verify(tourLogRepository, never()).save(any());
    }

    @Test
    void saveTourLog_WithNullData() {
        TourLogDto nullTourLogDto = null;

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            tourLogService.saveTourLog(nullTourLogDto);
        });

        assertEquals("tourLogDto is marked non-null but is null", thrown.getMessage());
        verify(tourLogRepository, never()).save(any());
    }
}
