package com.example.jpademo.javafx.Controllers;

import com.example.jpademo.javafx.Services.FileChooserService;
import com.example.jpademo.javafx.Services.FileChooserServiceImpl;
import com.example.jpademo.service.dtos.TourDto;
import com.example.jpademo.service.dtos.TourLogDto;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class MainController {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<TourDto> tourListView;

    @FXML
    private TextArea tourDetailsArea;

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

    @FXML
    private TableColumn<TourLogDto, String> commentColumn;

    @FXML
    private TableColumn<TourLogDto, String> difficultyColumn;

    @FXML
    private TableColumn<TourLogDto, String> totalDistanceColumn;

    @FXML
    private TableColumn<TourLogDto, String> totalTimeColumn;

    @FXML
    private ImageView tourLogImageView;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8080";
    private WebSocketClient webSocketClient;

    private FileChooserService fileChooserService = new FileChooserServiceImpl();

    @FXML
    private void initialize() {
        // Initialize table columns
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateTimeProperty().asString());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().totalTimeProperty().asString());
        distanceColumn.setCellValueFactory(cellData -> cellData.getValue().totalDistanceProperty().asString());
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().ratingProperty().asString());
        commentColumn.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
        difficultyColumn.setCellValueFactory(cellData -> cellData.getValue().difficultyProperty());
        totalDistanceColumn.setCellValueFactory(cellData -> cellData.getValue().totalDistanceProperty().asString());
        totalTimeColumn.setCellValueFactory(cellData -> cellData.getValue().totalTimeProperty().asString());

        // Load initial data
        loadTours();

        // Connect to WebSocket
        connectToWebSocket();
    }

    public void setFileChooserService(FileChooserService fileChooserService) {
        this.fileChooserService = fileChooserService;
    }

    private void loadTours() {
        try {
            List<TourDto> tourList = Arrays.asList(restTemplate.getForObject(baseUrl + "/tours", TourDto[].class));
            tourListView.setItems(FXCollections.observableArrayList(tourList));
            tourListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(TourDto item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getTitle());
                    }
                }
            });
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
            displayTourDetails(selectedTour);
            loadTourLogs(selectedTour.getId());
        }
    }

    private void displayTourDetails(TourDto tour) {
        StringBuilder details = new StringBuilder();
        details.append("Tour ID: ").append(tour.getId()).append("\n");
        details.append("Title: ").append(tour.getTitle()).append("\n");
        details.append("Description: ").append(tour.getDescription()).append("\n");
        details.append("Start Location: ").append(tour.getStartLocation()).append("\n");
        details.append("End Location: ").append(tour.getEndLocation()).append("\n");
        details.append("Transportation: ").append(tour.getTransportation()).append("\n");
        details.append("Distance: ").append(tour.getDistance()).append("\n");
        details.append("Time: ").append(tour.getTime()).append("\n");
        details.append("Information: ").append(tour.getInformation()).append("\n");

        tourDetailsArea.setText(details.toString());
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

    @FXML
    private void handleTourLogSelection(MouseEvent event) {
        TourLogDto selectedLog = tourLogTableView.getSelectionModel().getSelectedItem();
        if (selectedLog != null) {
            loadImageForTourLog(selectedLog.getId());
        }
    }

    private void loadImageForTourLog(Long logId) {
        //Image not working
        try {
            // Fetch the tour log to check if it has an image path
            TourLogDto tourLog = restTemplate.getForObject(baseUrl + "/tourlogs/" + logId, TourLogDto.class);

            // Check if the tour log has an image path
            if (tourLog != null && tourLog.getImagePath() != null && !tourLog.getImagePath().isEmpty()) {
                // Fetch the image bytes
                byte[] imageBytes = restTemplate.getForObject(baseUrl + "/tourlogs/" + logId + "/image", byte[].class);

                if (imageBytes != null && imageBytes.length > 0) {
                    Image image = new Image(new ByteArrayInputStream(imageBytes));
                    tourLogImageView.setImage(image);
                } else {
                    tourLogImageView.setImage(null);
                }
            } else {
                tourLogImageView.setImage(null);
            }
        } catch (ResourceAccessException e) {
            System.err.println("Failed to load image: " + e.getMessage());
            showAlert("Error", "Failed to load image for the selected tour log.");
            tourLogImageView.setImage(null);
        } catch (HttpClientErrorException e) {
            System.err.println("Failed to load image: " + e.getMessage());
            showAlert("Error", "Failed to load image for the selected tour log.");
            tourLogImageView.setImage(null);
        }
    }

    private void showAlert(String title, String content) {
        showAlert(title, content, Alert.AlertType.ERROR);
    }
//Handles amount of variables showAlert can recieve
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleNewTour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.jpademo/javafx/views/NewTourView.fxml"));
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
    private void handleEditTour() {
        TourDto selectedTour = tourListView.getSelectionModel().getSelectedItem();
        if (selectedTour != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.jpademo/javafx/views/NewTourView.fxml"));
                Parent root = loader.load();
                NewTourController controller = loader.getController();
                controller.setTourDto(selectedTour); // Set the selected tour to the controller
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Edit Tour");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                // Reload tours after editing
                loadTours();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to open the edit tour form.");
            }
        } else {
            showAlert("Warning", "Please select a tour to edit.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleNewTourLog() {
        TourDto selectedTour = tourListView.getSelectionModel().getSelectedItem();
        if (selectedTour != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.jpademo/javafx/views/NewTourLogView.fxml"));
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
    private void handleEditTourLog() {
        TourLogDto selectedLog = tourLogTableView.getSelectionModel().getSelectedItem();
        if (selectedLog != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com.example.jpademo/javafx/views/NewTourLogView.fxml"));
                Parent root = loader.load();
                NewTourLogController controller = loader.getController();
                controller.setTourLogDto(selectedLog); // Set the selected tour log to the controller
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Edit Tour Log");
                stage.setScene(new Scene(root));
                stage.showAndWait();
                // Reload tour logs after editing
                loadTourLogs(selectedLog.getTourId());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to open the edit tour log form.");
            }
        } else {
            showAlert("Warning", "Please select a tour log to edit.", Alert.AlertType.WARNING);
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
        TourDto selectedTour = tourListView.getSelectionModel().getSelectedItem();
        if (selectedTour != null) {
            restTemplate.delete(baseUrl + "/tours/" + selectedTour.getId());
            loadTours();
            tourDetailsArea.clear();
            tourLogTableView.getItems().clear();
            showAlert("Success", "Tour deleted successfully.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Warning", "Please select a tour to delete.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleDeleteLog() {
        TourLogDto selectedLog = tourLogTableView.getSelectionModel().getSelectedItem();
        if (selectedLog != null) {
            restTemplate.delete(baseUrl + "/tourlogs/" + selectedLog.getId());
            loadTourLogs(selectedLog.getTourId());
            showAlert("Success", "Tour log deleted successfully.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Warning", "Please select a tour log to delete.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void handleDownloadPdf() {
        File file = fileChooserService.showSaveDialog(null);

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
