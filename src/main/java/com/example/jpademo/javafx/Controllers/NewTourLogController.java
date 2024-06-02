package com.example.jpademo.javafx.Controllers;

import com.example.jpademo.service.dtos.TourLogDto;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

    @FXML
    private Label imageLabel;

    private File selectedImageFile;

    private Long tourId;
    private TourLogDto tourLogDto;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8080";

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }

    @FXML
    public void handleSave() {
        if (isInputValid()) {
            String comment = commentField.getText();
            LocalDate date = datePicker.getValue();
            String difficulty = difficultyField.getText();
            double totalDistance = Double.parseDouble(totalDistanceField.getText());
            double totalTime = Double.parseDouble(totalTimeField.getText());
            int rating = Integer.parseInt(ratingField.getText());

            if (tourLogDto == null) {
                // Create new tour log
                TourLogDto newTourLog = new TourLogDto();
                newTourLog.setTourId(tourId);
                newTourLog.setComment(comment);
                newTourLog.setDateTime(date.atStartOfDay());
                newTourLog.setDifficulty(difficulty);
                newTourLog.setTotalDistance((int) totalDistance);
                newTourLog.setTotalTime((int) totalTime);
                newTourLog.setRating(rating);

                TourLogDto savedTourLog = restTemplate.postForObject(baseUrl + "/tourlogs", newTourLog, TourLogDto.class);
                if (selectedImageFile != null) {
                    uploadImage(savedTourLog.getId(), selectedImageFile);
                }
            } else {
                // Update existing tour log
                tourLogDto.setComment(comment);
                tourLogDto.setDateTime(date.atStartOfDay());
                tourLogDto.setDifficulty(difficulty);
                tourLogDto.setTotalDistance((int) totalDistance);
                tourLogDto.setTotalTime((int) totalTime);
                tourLogDto.setRating(rating);

                restTemplate.put(baseUrl + "/tourlogs/" + tourLogDto.getId(), tourLogDto, TourLogDto.class);
                if (selectedImageFile != null) {
                    System.out.println("asldkfjlkajlsk");
                    uploadImage(tourLogDto.getId(), selectedImageFile);
                }
            }

            // Close the window after saving
            Stage stage = (Stage) commentField.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        selectedImageFile = fileChooser.showOpenDialog(commentField.getScene().getWindow());
        if (selectedImageFile != null&&imageLabel!=null) {
            imageLabel.setText(selectedImageFile.getName());
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) commentField.getScene().getWindow();
        stage.close();
    }

    public void setTourLogDto(TourLogDto tourLogDto) {
        this.tourLogDto = tourLogDto;
        if (tourLogDto != null) {
            commentField.setText(tourLogDto.getComment());
            datePicker.setValue(tourLogDto.getDateTime().toLocalDate());
            difficultyField.setText(tourLogDto.getDifficulty());
            totalDistanceField.setText(String.valueOf(tourLogDto.getTotalDistance()));
            totalTimeField.setText(String.valueOf(tourLogDto.getTotalTime()));
            ratingField.setText(String.valueOf(tourLogDto.getRating()));
        }
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

    private void uploadImage(Long tourLogId, File imageFile) {
        try {
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            restTemplate.postForObject(baseUrl + "/tour/" + tourLogId + "/image", imageBytes, Void.class);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to upload image.", "");
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
