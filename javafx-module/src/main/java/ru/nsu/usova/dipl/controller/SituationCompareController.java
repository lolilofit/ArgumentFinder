package ru.nsu.usova.dipl.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.nsu.usova.dipl.element.FxElementsUtils;
import ru.nsu.usova.dipl.model.CompareRequest;
import ru.nsu.usova.dipl.model.SituationMetric;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static ru.nsu.usova.dipl.controller.SearchController.CLIENT;

public class SituationCompareController {
    private static final Gson GSON = new Gson();

    @FXML
    private TextField fStr;

    @FXML
    private TextField sStr;

    @FXML
    private Label metric;

    @FXML
    private Label samePartType;

    @FXML
    private Label structuralType;

    public void compare() {
        try {
            CompareRequest request = new CompareRequest(fStr.getText(), sStr.getText());
            String requestStr = GSON.toJson(request);

            HttpRequest getVersionBuilder = HttpRequest.newBuilder(
                    new URI("http://localhost:8080/situation/compare"))
                    .header("Accept", "application/json")
                    .header("Content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestStr))
                    .build();

            HttpResponse<String> response = CLIENT.send(getVersionBuilder, HttpResponse.BodyHandlers.ofString());

            SituationMetric m = GSON.fromJson(response.body(), SituationMetric.class);

            metric.setText(Float.toString(m.getDistance()));
            samePartType.setText(m.getSamePartRelationType());
            structuralType.setText(m.getStructuralRelationType());

        } catch (URISyntaxException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
