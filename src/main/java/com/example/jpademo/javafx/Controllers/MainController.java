package com.example.jpademo.javafx.Controllers;

import com.example.jpademo.service.dtos.TourDto;
import com.example.jpademo.service.dtos.TourLogDto;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class MainController {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<TourDto> tourListView;

    @FXML
    private ImageView mapView;

    @FXML
    private TableView<TourLogDto> tourLogTableView;

    @FXML
    private TableColumn<TourLogDto, String> dateColumn;

    @FXML
    private TableColumn<TourLogDto, String> durationColumn;

    @FXML
    private TableColumn<TourLogDto, String> distanceColumn;

    @FXML
    private TableColumn<TourLogDto, String> ratingColumn;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8080";
    private WebSocketClient webSocketClient;

    @FXML
    private void initialize() {
        // Initialize table columns
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateTimeProperty().asString());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().totalTimeProperty().asString());
        distanceColumn.setCellValueFactory(cellData -> cellData.getValue().totalDistanceProperty().asString());
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().ratingProperty().asString());

        // Load initial data
        loadTours();

        // Connect to WebSocket
        connectToWebSocket();
    }

    private void loadTours() {
        try {
            List<TourDto> tourList = Arrays.asList(restTemplate.getForObject(baseUrl + "/tours", TourDto[].class));
            tourListView.setItems(FXCollections.observableArrayList(tourList));
        } catch (ResourceAccessException e) {
            System.err.println("Failed to connect to backend: " + e.getMessage());
            showAlert("Error", "Failed to load tours. Displaying default data.");
            loadDefaultTours();
        }
    }

    private void loadDefaultTours() {
        TourDto defaultTour = new TourDto();
        defaultTour.setId(1L);
        defaultTour.setTitle("Default Tour");
        defaultTour.setDescription("This is a default tour description.");
        tourListView.getItems().add(defaultTour);
    }

    @FXML
    private void handleTourSelection(MouseEvent event) {
        TourDto selectedTour = tourListView.getSelectionModel().getSelectedItem();
        if (selectedTour != null) {
            loadTourLogs(selectedTour.getId());
        }
    }

    private void loadTourLogs(Long tourId) {
        try {
            List<TourLogDto> tourLogList = Arrays.asList(restTemplate.getForObject(baseUrl + "/tourlogs/tour/" + tourId, TourLogDto[].class));
            tourLogTableView.setItems(FXCollections.observableArrayList(tourLogList));
        } catch (ResourceAccessException e) {
            System.err.println("Failed to connect to backend: " + e.getMessage());
            showAlert("Error", "Failed to load tour logs. Displaying default data.");
            loadDefaultTourLogs();
        }
    }

    private void loadDefaultTourLogs() {
        TourLogDto defaultLog = new TourLogDto();
        defaultLog.setId(1L);
        defaultLog.setDateTime(LocalDateTime.now());
        defaultLog.setComment("Default log comment");
        defaultLog.setDifficulty("Easy");
        defaultLog.setTotalDistance(5);
        defaultLog.setTotalTime(60);
        defaultLog.setRating(4);
        defaultLog.setTourId(1L);
        tourLogTableView.getItems().add(defaultLog);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleNewTour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/jpademo/javafx/views/NewTourView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("New Tour");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            // Reload tours after adding a new one
            loadTours();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the new tour form.");
        }
    }

    @FXML
    private void handleNewTourLog() {
        TourDto selectedTour = tourListView.getSelectionModel().getSelectedItem();
        if (selectedTour != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/jpademo/javafx/views/NewTourLogView.fxml"));
                Parent root = loader.load();
                NewTourLogController controller = loader.getController();
                controller.setTourId(selectedTour.getId());
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("New Tour Log");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                // Reload tour logs after adding a new one
                loadTourLogs(selectedTour.getId());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to open the new tour log form.");
            }
        } else {
            showAlert("Warning", "Please select a tour first.");
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText();
        // Implement search logic
    }

    @FXML
    private void handleAddTour() {
        handleNewTour();
    }

    @FXML
    private void handleDeleteTour() {
        // Implement logic to delete selected tour
    }

    @FXML
    private void handleEditTour() {
        // Implement logic to edit selected tour
    }

    @FXML
    private void handleAddLog() {
        handleNewTourLog();
    }

    @FXML
    private void handleDeleteLog() {
        // Implement logic to delete selected tour log
    }

    @FXML
    private void handleEditLog() {
        // Implement logic to edit selected tour log
    }

    @FXML
    private void handleDownloadPdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                byte[] pdfBytes = restTemplate.getForObject(baseUrl + "/api/download/pdf", byte[].class);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(pdfBytes);
                }
                showAlert("Success", "PDF downloaded successfully.", Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to download PDF.", Alert.AlertType.ERROR);
            }
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void connectToWebSocket() {
        try {
            webSocketClient = new WebSocketClient(new URI("ws://localhost:8080/tour-updates")) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("Connected to WebSocket");
                }

                @Override
                public void onMessage(String message) {
                    Platform.runLater(() -> {
                        // Reload tours or update UI based on the message
                        loadTours();
                    });
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Disconnected from WebSocket");
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };
            webSocketClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
