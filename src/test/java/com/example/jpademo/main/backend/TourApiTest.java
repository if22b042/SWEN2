package com.example.jpademo.main.backend;

import com.example.jpademo.api.TourApi;
import com.example.jpademo.service.TourService;
import com.example.jpademo.service.dtos.TourDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TourApiTest {

    @Mock
    private TourService tourService;

    @InjectMocks
    private TourApi tourApi;

    private TourDto tourDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tourDto = new TourDto(1L, "Title", "Description", "Start", "End", "Car", 100, 10, "Info");
    }

    @Test
    void createTour() {
        when(tourService.saveTour(any())).thenReturn(tourDto);

        //Call the createTour method
        ResponseEntity<TourDto> response = tourApi.createTour(tourDto);

        //Verify the tour was created correctly
        assertNotNull(response.getBody());
        assertEquals("Title", response.getBody().getTitle());
        verify(tourService, times(1)).saveTour(tourDto);
    }

    @Test
    void getAllTours() {
        when(tourService.getAllTours()).thenReturn(Arrays.asList(tourDto));

        //Call the getAllTours method
        ResponseEntity<List<TourDto>> response = tourApi.getAllTours();

        //Verify the tours were retrieved correctly
        assertEquals(1, response.getBody().size());
        verify(tourService, times(1)).getAllTours();
    }

    @Test
    void updateTour() {
        // Arrange: Set up mock behavior for updating a tour
        when(tourService.updateTour(anyLong(), any())).thenReturn(tourDto);

        // Act: Call the updateTour method
        ResponseEntity<TourDto> response = tourApi.updateTour(1L, tourDto);

        // Assert: Verify the tour was updated correctly
        assertNotNull(response.getBody());
        assertEquals("Title", response.getBody().getTitle());
        verify(tourService, times(1)).updateTour(1L, tourDto);
    }

    @Test
    void deleteTour() {
        // Arrange: Set up mock behavior for deleting a tour
        doNothing().when(tourService).deleteTour(anyLong());

        // Act: Call the deleteTour method
        ResponseEntity<Void> response = tourApi.deleteTour(1L);

        // Assert: Verify the tour was deleted correctly
        assertEquals(204, response.getStatusCodeValue());
        verify(tourService, times(1)).deleteTour(1L);
    }
}
