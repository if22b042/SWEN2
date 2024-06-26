package com.example.jpademo.javafx.Controllers;

import com.example.jpademo.service.dtos.TourDto;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;

public class NewTourController {

    @FXML
    public TextField titleField;

    @FXML
    public TextField descriptionField;

    @FXML
    public TextField startLocationField;

    @FXML
    public TextField endLocationField;

    @FXML
    public TextField transportationField;

    @FXML
    public TextField distanceField;

    @FXML
    public TextField timeField;

    @FXML
    public TextArea informationField;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8080";
    public TourDto tourDto; // Add a field to hold the tour DTO

    @FXML
    public void handleSave() {
        if (isInputValid()) {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String startLocation = startLocationField.getText();
            String endLocation = endLocationField.getText();
            String transportation = transportationField.getText();
            double distance = Double.parseDouble(distanceField.getText());
            double time = Double.parseDouble(timeField.getText());
            String information = informationField.getText();

            if (tourDto == null) {
                // Create new tour
                TourDto newTour = new TourDto();
                newTour.setTitle(title);
                newTour.setDescription(description);
                newTour.setStartLocation(startLocation);
                newTour.setEndLocation(endLocation);
                newTour.setTransportation(transportation);
                newTour.setDistance((int) distance);
                newTour.setTime((int) time);
                newTour.setInformation(information);

                restTemplate.postForObject(baseUrl + "/tours", newTour, TourDto.class);
            } else {
                // Update existing tour
                tourDto.setTitle(title);
                tourDto.setDescription(description);
                tourDto.setStartLocation(startLocation);
                tourDto.setEndLocation(endLocation);
                tourDto.setTransportation(transportation);
                tourDto.setDistance((int) distance);
                tourDto.setTime((int) time);
                tourDto.setInformation(information);

                restTemplate.put(baseUrl + "/tours/" + tourDto.getId(), tourDto, TourDto.class);
            }

            // Close the window after saving
            Stage stage = (Stage) titleField.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }

    public void setTourDto(TourDto tourDto) {
        this.tourDto = tourDto;
        if (tourDto != null) {
            titleField.setText(tourDto.getTitle());
            descriptionField.setText(tourDto.getDescription());
            startLocationField.setText(tourDto.getStartLocation());
            endLocationField.setText(tourDto.getEndLocation());
            transportationField.setText(tourDto.getTransportation());
            distanceField.setText(String.valueOf(tourDto.getDistance()));
            timeField.setText(String.valueOf(tourDto.getTime()));
            informationField.setText(tourDto.getInformation());
        }
    }

    private boolean isInputValid() {
        StringBuilder errorMessage = new StringBuilder();

        if (titleField.getText() == null || titleField.getText().isEmpty()) {
            errorMessage.append("No valid title!\n");
        }
        if (descriptionField.getText() == null || descriptionField.getText().isEmpty()) {
            errorMessage.append("No valid description!\n");
        }
        if (startLocationField.getText() == null || startLocationField.getText().isEmpty()) {
            errorMessage.append("No valid start location!\n");
        }
        if (endLocationField.getText() == null || endLocationField.getText().isEmpty()) {
            errorMessage.append("No valid end location!\n");
        }
        if (transportationField.getText() == null || transportationField.getText().isEmpty()) {
            errorMessage.append("No valid transportation!\n");
        }
        if (distanceField.getText() == null || distanceField.getText().isEmpty()) {
            errorMessage.append("No valid distance!\n");
        } else {
            try {
                Double.parseDouble(distanceField.getText());
            } catch (NumberFormatException e) {
                errorMessage.append("No valid distance (must be a number)!\n");
            }
        }
        if (timeField.getText() == null || timeField.getText().isEmpty()) {
            errorMessage.append("No valid time!\n");
        } else {
            try {
                Double.parseDouble(timeField.getText());
            } catch (NumberFormatException e) {
                errorMessage.append("No valid time (must be a number)!\n");
            }
        }
        if (informationField.getText() == null || informationField.getText().isEmpty()) {
            errorMessage.append("No valid information!\n");
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
