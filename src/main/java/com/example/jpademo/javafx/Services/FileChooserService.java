package com.example.jpademo.javafx.Services;

import javafx.stage.Window;

import java.io.File;

public interface FileChooserService {
    File showSaveDialog(Window window);
}

