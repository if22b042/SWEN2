package com.example.jpademo.secondary.notworkingtests;

import com.example.jpademo.javafx.Controllers.NewTourController;
import com.example.jpademo.service.dtos.TourDto;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class NewTourControllerTest extends ApplicationTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NewTourController newTourController;

    private TextField titleField;
    private TextField descriptionField;
    private TextField startLocationField;
    private TextField endLocationField;
    private TextField transportationField;
    private TextField distanceField;
    private TextField timeField;
    private TextArea informationField;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize JavaFX controls
        titleField = new TextField();
        descriptionField = new TextField();
        startLocationField = new TextField();
        endLocationField = new TextField();
        transportationField = new TextField();
        distanceField = new TextField();
        timeField = new TextField();
        informationField = new TextArea();

        // Set the fields in the controller
        newTourController.titleField = titleField;
        newTourController.descriptionField = descriptionField;
        newTourController.startLocationField = startLocationField;
        newTourController.endLocationField = endLocationField;
        newTourController.transportationField = transportationField;
        newTourController.distanceField = distanceField;
        newTourController.timeField = timeField;
        newTourController.informationField = informationField;
    }

    @Test
    void handleSave_EmptyData() throws InterruptedException {
        // Set up text fields with empty data
        titleField.setText("");
        descriptionField.setText("");
        startLocationField.setText("");
        endLocationField.setText("");
        transportationField.setText("");
        distanceField.setText("");
        timeField.setText("");
        informationField.setText("");

        // Run the test on the JavaFX Application Thread
        Platform.runLater(() -> {
            newTourController.handleSave();

            // Verify that no REST call is made
            verify(restTemplate, never()).postForObject(anyString(), any(), any());
            // Note: In actual unit tests, use a library like TestFX to verify UI elements
        });

        // Wait for the JavaFX application thread to process events
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void handleSave_ValidData() throws InterruptedException {
        // Set up text fields with valid data
        titleField.setText("Valid Title");
        descriptionField.setText("Valid Description");
        startLocationField.setText("Start Location");
        endLocationField.setText("End Location");
        transportationField.setText("Car");
        distanceField.setText("100.0");
        timeField.setText("10.0");
        informationField.setText("Valid Information");

        // Run the test on the JavaFX Application Thread
        Platform.runLater(() -> {
            newTourController.handleSave();

            // Verify that a REST call is made
            verify(restTemplate, times(1)).postForObject(anyString(), any(TourDto.class), any());
        });

        // Wait for the JavaFX application thread to process events
        WaitForAsyncUtils.waitForFxEvents();
    }
    @Test
    void handleSave_EditExistingTour() throws InterruptedException {
        // Mock existing tour data
        TourDto existingTour = new TourDto();
        existingTour.setId(1L);
        existingTour.setTitle("Existing Title");
        existingTour.setDescription("Existing Description");

        // Set fields with updated data
        titleField.setText("Updated Title");
        descriptionField.setText("Updated Description");
        startLocationField.setText("Updated Start Location");
        endLocationField.setText("Updated End Location");
        transportationField.setText("Updated Transportation");
        distanceField.setText("200.0");
        timeField.setText("20.0");
        informationField.setText("Updated Information");

        newTourController.setTourDto(existingTour);

        // Run the test on the JavaFX Application Thread
        Platform.runLater(() -> {
            newTourController.handleSave();

            // Verify that a REST call is made with PUT method
            verify(restTemplate, times(1)).put(anyString(), any(TourDto.class));
        });

        // Wait for the JavaFX application thread to process events
        WaitForAsyncUtils.waitForFxEvents();
    }
    @Test
    void handleSave_InvalidData() throws InterruptedException {
        // Set fields with invalid data
        titleField.setText("Title");
        descriptionField.setText("Description");
        startLocationField.setText("Start Location");
        endLocationField.setText("End Location");
        transportationField.setText("Car");
        distanceField.setText("Invalid Distance");
        timeField.setText("Invalid Time");
        informationField.setText("Information");

        // Run the test on the JavaFX Application Thread
        Platform.runLater(() -> {
            newTourController.handleSave();

            // Verify that no REST call is made due to invalid data
            verify(restTemplate, never()).postForObject(anyString(), any(), any());
            //verify(restTemplate, never()).put(anyString(), any(), any());
        });

        // Wait for the JavaFX application thread to process events
        WaitForAsyncUtils.waitForFxEvents();
    }


}
