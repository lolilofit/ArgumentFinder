package ru.nsu.usova.dipl.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import ru.nsu.usova.dipl.javafx.element.FxElementsUtils;
import ru.nsu.usova.dipl.javafx.model.Situation;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SituationsController {
    private static final FxElementsUtils ELEMENT_UTILS = new FxElementsUtils();

    private static final Gson GSON = new Gson();

    @FXML
    private TextField text;

    @FXML
    private StackPane stackPane;

    public void loadSituation() {
        try {
            HttpRequest getVersionBuilder = HttpRequest.newBuilder(
                    new URI("http://localhost:8080/situation"))
                    .header("Content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(text.getText()))
                    .build();

            HttpResponse<String> response = SearchController.CLIENT.send(getVersionBuilder, HttpResponse.BodyHandlers.ofString());

            Situation[] s = GSON.fromJson(response.body(), Situation[].class);

            stackPane.getChildren().add(ELEMENT_UTILS.drawSituations(s));
        } catch (URISyntaxException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
