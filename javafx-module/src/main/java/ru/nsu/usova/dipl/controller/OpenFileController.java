package ru.nsu.usova.dipl.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import ru.nsu.usova.dipl.element.ButtonEventHandler;

import java.io.File;

public class OpenFileController {
    @FXML
    private ListView files;

    @FXML
    private TextField fileName;

    @FXML
    private Label ArgumentsCount;

    private File parentFile;

    private File[] dirFiles;

    public void initialize() {
        loadCurrentDir(new File(System.getProperty("user.dir")));
    }

    public void loadCurrentDir(File dir) {
        if (dir == null)
            return;
        files.getItems().clear();

        String parentName = dir.getParent();
        parentFile = parentName != null ? new File(parentName) : null;
        addNewButton("...", mouseEvent -> loadCurrentDir(parentFile));

        dirFiles = dir.listFiles();
        for (int i = 0; i < (dirFiles != null ? dirFiles.length : 0); i++)
            addNewButton(dirFiles[i].getName(), new ButtonEventHandler(dirFiles[i], this));
    }

    private void addNewButton(String text, EventHandler<MouseEvent> eventHandler) {
        Button parentButton = new Button(text);
        parentButton.setMinWidth(900);
        parentButton.setStyle("-fx-border-color: transparent; -fx-background-color: transparent; -fx-border-width: 0; -fx-alignment: center-left;");
        parentButton.setOnMouseClicked(eventHandler);
        files.getItems().add(parentButton);
    }

    public void setLoadingFileName(String fileName) {
        this.fileName.setText(fileName);
    }

    public void openFileSendText() {
        
    }
}
