package com.example.jpademo.main.controllers;

import com.example.jpademo.javafx.Controllers.MainController;
import com.example.jpademo.javafx.Services.FileChooserService;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class MainControllerTest extends ApplicationTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private FileChooserService fileChooserService;

    @InjectMocks
    private MainController mainController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Start
    public void start(Stage stage) {
        // Initialize the controller with a mock stage for alerts
        mainController = new MainController();
        mainController.setFileChooserService(fileChooserService);
    }

    @Test
    void handleDownloadPdf_ValidDownload() throws IOException, InterruptedException {
        byte[] mockPdfData = "mock pdf content".getBytes();
        when(restTemplate.getForObject(anyString(), eq(byte[].class))).thenReturn(mockPdfData);

        File tempFile = File.createTempFile("test", ".pdf");
        tempFile.deleteOnExit();
        when(fileChooserService.showSaveDialog(any())).thenReturn(tempFile);

        // Run the test on the JavaFX Application Thread
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            mainController.handleDownloadPdf();
            latch.countDown();
        });
        WaitForAsyncUtils.waitForFxEvents();
        latch.await();

        // Verify that the correct REST call was made
        verify(restTemplate, times(1)).getForObject(anyString(), eq(byte[].class));

        // Verify that the file was written with the correct content
        byte[] fileContent = new byte[(int) tempFile.length()];
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(mockPdfData);
        }

        // Clean up
        tempFile.delete();
    }

    @Test
    void handleDownloadPdf_NoFileChosen() throws InterruptedException {
        when(fileChooserService.showSaveDialog(any())).thenReturn(null);

        // Run the test on the JavaFX Application Thread
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            mainController.handleDownloadPdf();
            latch.countDown();
        });
        WaitForAsyncUtils.waitForFxEvents();
        latch.await();

        // Verify that no REST call is made
        verify(restTemplate, never()).getForObject(anyString(), eq(byte[].class));
    }
}
