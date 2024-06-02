package com.example.jpademo.javafx.Services;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class FileChooserServiceImpl implements com.example.jpademo.javafx.Services.FileChooserService {

    private final FileChooser fileChooser;

    public FileChooserServiceImpl() {
        this.fileChooser = new FileChooser();
        this.fileChooser.setTitle("Save PDF");
        this.fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
    }

    @Override
    public File showSaveDialog(Window window) {
        return fileChooser.showSaveDialog(window);
    }
}
