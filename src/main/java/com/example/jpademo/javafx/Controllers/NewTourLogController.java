package com.example.jpademo.javafx.Controllers;

import com.example.jpademo.service.dtos.TourLogDto;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

public class NewTourLogController {

    @FXML
    private TextField commentField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField difficultyField;

    @FXML
    private TextField totalDistanceField;

    @FXML
    private TextField totalTimeField;

    @FXML
    private TextField ratingField;

    private Long tourId; // This should be set before showing the form

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8080";

    @FXML
    private void handleSave() {
        String comment = commentField.getText();
        LocalDateTime dateTime = datePicker.getValue().atStartOfDay();
        String difficulty = difficultyField.getText();
        int totalDistance = Integer.parseInt(totalDistanceField.getText());
        int totalTime = Integer.parseInt(totalTimeField.getText());
        int rating = Integer.parseInt(ratingField.getText());

        TourLogDto newTourLog = new TourLogDto();
        newTourLog.setComment(comment);
        newTourLog.setDateTime(dateTime);
        newTourLog.setDifficulty(difficulty);
        newTourLog.setTotalDistance(totalDistance);
        newTourLog.setTotalTime(totalTime);
        newTourLog.setRating(rating);
        newTourLog.setTourId(tourId);

        restTemplate.postForObject(baseUrl + "/tourlogs", newTourLog, TourLogDto.class);

        // Close the window after saving
        Stage stage = (Stage) commentField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) commentField.getScene().getWindow();
        stage.close();
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }
}
