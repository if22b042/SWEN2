package com.example.jpademo.second.notworkingtests;

import com.example.jpademo.javafx.Controllers.NewTourLogController;
import com.example.jpademo.service.dtos.TourLogDto;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class NewTourLogControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NewTourLogController newTourLogController;

    @Mock
    private TextField commentField;

    @Mock
    private DatePicker datePicker;

    @Mock
    private TextField difficultyField;

    @Mock
    private TextField totalDistanceField;

    @Mock
    private TextField totalTimeField;

    @Mock
    private TextField ratingField;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleSave_EmptyData() {
        // Set up mock text fields with empty data
        when(commentField.getText()).thenReturn("");
        when(datePicker.getValue()).thenReturn(null);
        when(difficultyField.getText()).thenReturn("");
        when(totalDistanceField.getText()).thenReturn("");
        when(totalTimeField.getText()).thenReturn("");
        when(ratingField.getText()).thenReturn("");

        newTourLogController.handleSave();

        // Verify that no REST call is made
        verify(restTemplate, never()).postForObject(anyString(), any(), any());

        // Note: In actual unit tests, use a library like TestFX to verify UI elements
    }

    @Test
    void handleSave_ValidData() {
        // Set up mock text fields with valid data
        when(commentField.getText()).thenReturn("Valid Comment");
        when(datePicker.getValue()).thenReturn(LocalDate.now());
        when(difficultyField.getText()).thenReturn("Easy");
        when(totalDistanceField.getText()).thenReturn("10.0");
        when(totalTimeField.getText()).thenReturn("1.0");
        when(ratingField.getText()).thenReturn("5");

        newTourLogController.handleSave();

        // Verify that a REST call is made
        verify(restTemplate, times(1)).postForObject(anyString(), any(TourLogDto.class), any());
    }
}
