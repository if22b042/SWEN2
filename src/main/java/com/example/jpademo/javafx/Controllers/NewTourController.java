package com.example.jpademo.javafx.Controllers;

import com.example.jpademo.service.dtos.TourDto;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;

public class NewTourController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField descriptionField;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8080";

    @FXML
    private void handleSave() {
        String title = titleField.getText();
        String description = descriptionField.getText();

        TourDto newTour = new TourDto();
        newTour.setTitle(title);
        newTour.setDescription(description);

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
