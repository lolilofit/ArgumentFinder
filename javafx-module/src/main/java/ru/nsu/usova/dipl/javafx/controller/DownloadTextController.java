package ru.nsu.usova.dipl.javafx.controller;

import com.google.gson.Gson;
import ru.nsu.usova.dipl.javafx.element.FxElementsUtils;
import ru.nsu.usova.dipl.javafx.model.LoadTextInfo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DownloadTextController {
    public static void downloadTexts() {
        Gson gson = new Gson();

        try {
            HttpRequest getVersionBuilder = HttpRequest.newBuilder(
                    new URI("http://localhost:8080/text/download"))
                    .header("Accept", "application/json")
                    .header("Content-type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = SearchController.CLIENT.send(getVersionBuilder, HttpResponse.BodyHandlers.ofString());

            LoadTextInfo loadTextInfo = gson.fromJson(response.body(), LoadTextInfo.class);

            FxElementsUtils.showNotification(
                    "Загрузка текстов",
                    String.format("Извлечено %s аргументов из скачанных текстов", loadTextInfo.getExtractedArguments()),
                    "Аргументы доступны в списке всех сохраненных аргументов"
            );
        } catch (URISyntaxException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }


}
