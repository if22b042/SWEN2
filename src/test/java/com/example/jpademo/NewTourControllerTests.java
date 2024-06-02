package com.example.jpademo;

import com.example.jpademo.javafx.Controllers.NewTourController;
import com.example.jpademo.service.dtos.TourDto;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class NewTourControllerTests {

    @InjectMocks
    private NewTourController newTourController;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Stage stage;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        setPrivateField(newTourController, "titleField", new TextField());
        setPrivateField(newTourController, "descriptionField", new TextField());
        setPrivateField(newTourController, "startLocationField", new TextField());
        setPrivateField(newTourController, "endLocationField", new TextField());
        setPrivateField(newTourController, "transportationField", new TextField());
        setPrivateField(newTourController, "distanceField", new TextField());
        setPrivateField(newTourController, "timeField", new TextField());
        setPrivateField(newTourController, "informationField", new TextArea());

        setTextFieldValue(newTourController, "titleField", "Sample Tour");
        setTextFieldValue(newTourController, "descriptionField", "Sample Description");
        setTextFieldValue(newTourController, "startLocationField", "Start Location");
        setTextFieldValue(newTourController, "endLocationField", "End Location");
        setTextFieldValue(newTourController, "transportationField", "Car");
        setTextFieldValue(newTourController, "distanceField", "10");
        setTextFieldValue(newTourController, "timeField", "2");
        setTextAreaValue(newTourController, "informationField", "Additional Information");

        setPrivateField(newTourController, "restTemplate", restTemplate);
    }

    private void setPrivateField(Object object, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    private void setTextFieldValue(Object object, String fieldName, String value) throws NoSuchFieldException, IllegalAccessException {
        TextField textField = (TextField) getPrivateField(object, fieldName);
        textField.setText(value);
    }

    private void setTextAreaValue(Object object, String fieldName, String value) throws NoSuchFieldException, IllegalAccessException {
        TextArea textArea = (TextArea) getPrivateField(object, fieldName);
        textArea.setText(value);
    }

    private Object getPrivateField(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    @Test
    public void testHandleSave() {
        newTourController.handleSave();

        TourDto expectedTour = new TourDto();
        expectedTour.setTitle("Sample Tour");
        expectedTour.setDescription("Sample Description");
        expectedTour.setStartLocation("Start Location");
        expectedTour.setEndLocation("End Location");
        expectedTour.setTransportation("Car");
        expectedTour.setDistance(10);
        expectedTour.setTime(2);
        expectedTour.setInformation("Additional Information");

        verify(restTemplate).postForObject(eq("http://localhost:8080/tours"), eq(expectedTour), eq(TourDto.class));
    }

    @Test
    public void testHandleCancel() {
        newTourController.handleCancel();
        verify(stage).close();
    }

    @Test
    public void testTitleFieldIsEmpty() throws NoSuchFieldException, IllegalAccessException {
        setTextFieldValue(newTourController, "titleField", "");
        newTourController.handleSave();
        verify(restTemplate).postForObject(eq("http://localhost:8080/tours"), eq(new TourDto()), eq(TourDto.class));
    }

    @Test
    public void testDistanceFieldIsEmpty() throws NoSuchFieldException, IllegalAccessException {
        setTextFieldValue(newTourController, "distanceField", "");
        newTourController.handleSave();
        verify(restTemplate).postForObject(eq("http://localhost:8080/tours"), eq(new TourDto()), eq(TourDto.class));
    }

    @Test
    public void testTimeFieldIsEmpty() throws NoSuchFieldException, IllegalAccessException {
        setTextFieldValue(newTourController, "timeField", "");
        newTourController.handleSave();
        verify(restTemplate).postForObject(eq("http://localhost:8080/tours"), eq(new TourDto()), eq(TourDto.class));
    }

    // Additional tests for other fields being empty or invalid
}
