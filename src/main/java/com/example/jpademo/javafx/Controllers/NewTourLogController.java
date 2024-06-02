package com.example.jpademo.javafx.Controllers;

import com.example.jpademo.service.dtos.TourLogDto;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

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

    private Long tourId;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8080";

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            String comment = commentField.getText();
            LocalDate date = datePicker.getValue();
            String difficulty = difficultyField.getText();
            double totalDistance = Double.parseDouble(totalDistanceField.getText());
            double totalTime = Double.parseDouble(totalTimeField.getText());
            int rating = Integer.parseInt(ratingField.getText());

            TourLogDto newTourLog = new TourLogDto();
            newTourLog.setTourId(tourId);
            newTourLog.setComment(comment);
            newTourLog.setDateTime(date.atStartOfDay());
            newTourLog.setDifficulty(difficulty);
            newTourLog.setTotalDistance((int) totalDistance);
            newTourLog.setTotalTime((int) totalTime);
            newTourLog.setRating(rating);

            restTemplate.postForObject(baseUrl + "/tourlogs", newTourLog, TourLogDto.class);

            // Close the window after saving
            Stage stage = (Stage) commentField.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) commentField.getScene().getWindow();
        stage.close();
    }

    private boolean isInputValid() {
        StringBuilder errorMessage = new StringBuilder();

        if (commentField.getText() == null || commentField.getText().isEmpty()) {
            errorMessage.append("No valid comment!\n");
        }
        if (datePicker.getValue() == null) {
            errorMessage.append("No valid date!\n");
        }
        if (difficultyField.getText() == null || difficultyField.getText().isEmpty()) {
            errorMessage.append("No valid difficulty!\n");
        }
        if (totalDistanceField.getText() == null || totalDistanceField.getText().isEmpty()) {
            errorMessage.append("No valid total distance!\n");
        } else {
            try {
                Double.parseDouble(totalDistanceField.getText());
            } catch (NumberFormatException e) {
                errorMessage.append("No valid total distance (must be a number)!\n");
            }
        }
        if (totalTimeField.getText() == null || totalTimeField.getText().isEmpty()) {
            errorMessage.append("No valid total time!\n");
        } else {
            try {
                Double.parseDouble(totalTimeField.getText());
            } catch (NumberFormatException e) {
                errorMessage.append("No valid total time (must be a number)!\n");
            }
        }
        if (ratingField.getText() == null || ratingField.getText().isEmpty()) {
            errorMessage.append("No valid rating!\n");
        } else {
            try {
                Integer.parseInt(ratingField.getText());
            } catch (NumberFormatException e) {
                errorMessage.append("No valid rating (must be a number)!\n");
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            showAlert("Invalid Fields", "Please correct invalid fields", errorMessage.toString());
            return false;
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
