package com.example.jpademo.javafx.Controllers;

import com.example.jpademo.service.dtos.TourDto;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;

public class NewTourController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField startLocationField;

    @FXML
    private TextField endLocationField;

    @FXML
    private TextField transportationField;

    @FXML
    private TextField distanceField;


    @FXML
    private TextArea informationField;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8080";

    @FXML
    private void handleSave() {
        String title = titleField.getText();
        String description = descriptionField.getText();
        String startLocation = startLocationField.getText();
        String endLocation = endLocationField.getText();
        String transportation = transportationField.getText();
        double distance = Double.parseDouble(distanceField.getText());

        String information = informationField.getText();

        TourDto newTour = new TourDto();
        newTour.setTitle(title);
        newTour.setDescription(description);
        newTour.setStartLocation(startLocation);
        newTour.setEndLocation(endLocation);
        newTour.setTransportation(transportation);
        newTour.setDistance((int) distance);
        newTour.setInformation(information);

        restTemplate.postForObject(baseUrl + "/tours", newTour, TourDto.class);

        // Close the window after saving
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}
