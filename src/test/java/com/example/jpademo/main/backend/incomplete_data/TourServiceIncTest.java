package com.example.jpademo.main.backend.incomplete_data;

import com.example.jpademo.persistence.entities.TourEntity;
import com.example.jpademo.persistence.repositories.TourRepository;
import com.example.jpademo.service.dtos.TourDto;
import com.example.jpademo.service.impl.TourServiceImpl;
import com.example.jpademo.service.mapper.TourMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TourServiceIncTest {

    @Mock
    private TourRepository tourRepository;

    @Mock
    private TourMapper tourMapper;

    @InjectMocks
    private TourServiceImpl tourService;

    private TourDto tourDto;
    private TourEntity tourEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tourDto = new TourDto(1L, "Title", "Description", "Start", "End", "Car", 100, 10, "Info");
        tourEntity = new TourEntity(1L, "Title", "Description", "Start", "End", "Car", 100, 10, "Info");
    }

    @Test
    void updateTour_WithIncompleteData() {
        when(tourRepository.findById(anyLong())).thenReturn(Optional.of(tourEntity));
        doAnswer(invocation -> {
            TourDto dto = invocation.getArgument(0);
            TourEntity entity = invocation.getArgument(1);
            if (dto.getTitle() != null) entity.setTitle(dto.getTitle());
            if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
            // Do the same for other fields if necessary
            return null;
        }).when(tourMapper).updateEntityFromDto(any(TourDto.class), any(TourEntity.class));
        when(tourMapper.toDto(any())).thenReturn(tourDto);

        TourDto incompleteTourDto = new TourDto();
        incompleteTourDto.setId(1L);
        incompleteTourDto.setTitle("Updated Title");

        TourDto updatedTour = tourService.updateTour(1L, incompleteTourDto);

        assertNotNull(updatedTour);
        assertEquals("Title", updatedTour.getTitle());
        assertEquals("Description", updatedTour.getDescription());
        verify(tourRepository, times(1)).save(tourEntity);
    }

    @Test
    void deleteTour_WithInvalidId() {
        doThrow(new IllegalArgumentException("Invalid ID")).when(tourRepository).deleteById(anyLong());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            tourService.deleteTour(999L);
        });

        assertEquals("Invalid ID", thrown.getMessage());
        verify(tourRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void findTourById_WithInvalidId() {
        when(tourRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<TourDto> result = tourService.findTourById(999L);

        assertFalse(result.isPresent());
        verify(tourRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateTour_WithNonExistentId() {
        when(tourRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            tourService.updateTour(999L, tourDto);
        });

        assertEquals("Tour not found with given id", thrown.getMessage());
        verify(tourRepository, never()).save(any());
    }

    @Test
    void saveTour_WithNullData() {
        TourDto nullTourDto = null;

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            tourService.saveTour(nullTourDto);
        });

        assertEquals("tourDto is marked non-null but is null", thrown.getMessage());
        verify(tourRepository, never()).save(any());
    }
}
