package ru.nsu.usova.dipl.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.usova.dipl.javafx.model.LoadTextInfo;
import ru.nsu.usova.dipl.javafx.model.ReasononingRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static ru.nsu.usova.dipl.javafx.controller.SearchController.CLIENT;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoadSingleTextController {
    private static final Gson gson = new Gson();

    @FXML
    private TextArea loadingText;

    @FXML
    private Label message;

    public void loadText() throws URISyntaxException, IOException, InterruptedException {
        ReasononingRequest request = new ReasononingRequest(loadingText.getText());

        HttpRequest getVersionBuilder = HttpRequest.newBuilder(
                new URI("http://localhost:8080/text/load"))
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                .build();
        HttpResponse<String> response = CLIENT.send(getVersionBuilder, HttpResponse.BodyHandlers.ofString());

        System.out.println(response);

        LoadTextInfo loadTextInfo = gson.fromJson(response.body(), LoadTextInfo.class);
        message.setText(String.format("Извлечено аргументов: %s", loadTextInfo.getExtractedArguments()));
    }
}
