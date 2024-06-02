package com.example.jpademo.main.backend;

import com.example.jpademo.persistence.entities.TourEntity;
import com.example.jpademo.persistence.repositories.TourRepository;
import com.example.jpademo.service.dtos.TourDto;
import com.example.jpademo.service.impl.TourServiceImpl;
import com.example.jpademo.service.mapper.TourMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TourServiceTest {

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
    void saveTour() {
        // Arrange: Set up mock behavior for saving a tour
        when(tourMapper.toEntity(any())).thenReturn(tourEntity);
        when(tourRepository.save(any())).thenReturn(tourEntity);
        when(tourMapper.toDto(any())).thenReturn(tourDto);

        // Act: Call the saveTour method
        TourDto savedTour = tourService.saveTour(tourDto);

        // Assert: Verify the tour was saved correctly
        assertNotNull(savedTour);
        assertEquals("Title", savedTour.getTitle());
        verify(tourRepository, times(1)).save(tourEntity);
    }

    @Test
    void getAllTours() {
        // Arrange: Set up mock behavior for retrieving all tours
        when(tourRepository.findAll()).thenReturn(Arrays.asList(tourEntity));
        when(tourMapper.toDto(any())).thenReturn(tourDto);

        // Act: Call the getAllTours method
        List<TourDto> tours = tourService.getAllTours();

        // Assert: Verify the tours were retrieved correctly
        assertEquals(1, tours.size());
        verify(tourRepository, times(1)).findAll();
    }

    @Test
    void findTourById() {
        // Arrange: Set up mock behavior for finding a tour by ID
        when(tourRepository.findById(anyLong())).thenReturn(Optional.of(tourEntity));
        when(tourMapper.toDto(any())).thenReturn(tourDto);

        // Act: Call the findTourById method
        Optional<TourDto> foundTour = tourService.findTourById(1L);

        // Assert: Verify the tour was found correctly
        assertTrue(foundTour.isPresent());
        assertEquals("Title", foundTour.get().getTitle());
        verify(tourRepository, times(1)).findById(1L);
    }

    @Test
    void updateTour() {
        // Arrange: Set up mock behavior for updating a tour
        when(tourRepository.findById(anyLong())).thenReturn(Optional.of(tourEntity)); // Mock finding the tour
        when(tourRepository.save(any())).thenReturn(tourEntity);
        when(tourMapper.toDto(any())).thenReturn(tourDto);
        doNothing().when(tourMapper).updateEntityFromDto(any(TourDto.class), any(TourEntity.class)); // Mock the updateEntityFromDto method

        // Act: Call the updateTour method
        TourDto updatedTour = tourService.updateTour(1L, tourDto);

        // Assert: Verify the tour was updated correctly
        assertNotNull(updatedTour);
        assertEquals("Title", updatedTour.getTitle());
        verify(tourRepository, times(1)).save(tourEntity);
        verify(tourMapper, times(1)).updateEntityFromDto(any(TourDto.class), any(TourEntity.class)); // Verify the updateEntityFromDto method was called
    }


    @Test
    void deleteTour() {
        // Act: Call the deleteTour method
        tourService.deleteTour(1L);

        // Assert: Verify the tour was deleted correctly
        verify(tourRepository, times(1)).deleteById(1L);
    }
}
