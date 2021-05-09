package ru.nsu.usova.dipl.controller;

import com.google.gson.Gson;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import ru.nsu.usova.dipl.element.ButtonEventHandler;
import ru.nsu.usova.dipl.model.LoadTextInfo;
import ru.nsu.usova.dipl.model.ReasononingRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static ru.nsu.usova.dipl.controller.SearchController.CLIENT;

public class OpenFileController {
    private static final Gson gson = new Gson();

    @FXML
    private ListView files;

    @FXML
    private TextField fileName;

    @FXML
    private Label argumentsCount;

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
        try {
            File file = new File(fileName.getText());
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fileInputStream.read(data);
            fileInputStream.close();

            ReasononingRequest request = new ReasononingRequest(new String(data));

            HttpRequest getVersionBuilder = HttpRequest.newBuilder(
                    new URI("http://localhost:8080/text/load"))
                    .header("Accept", "application/json")
                    .header("Content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                    .build();
            HttpResponse<String> response = CLIENT.send(getVersionBuilder, HttpResponse.BodyHandlers.ofString());

            System.out.println(response);

            LoadTextInfo loadTextInfo = gson.fromJson(response.body(), LoadTextInfo.class);
            argumentsCount.setText(loadTextInfo.getExtractedArguments().toString());
        } catch (IOException | URISyntaxException | InterruptedException e) {
        }
    }
}
