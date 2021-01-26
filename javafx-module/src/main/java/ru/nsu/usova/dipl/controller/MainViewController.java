package ru.nsu.usova.dipl.controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import ru.nsu.usova.dipl.FxElementsUtils;
import ru.nsu.usova.dipl.model.ReasoningTable;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


public class MainViewController {
    private static final HttpClient client = HttpClient.newHttpClient();

    private final Gson gson = new Gson();

    @FXML
    private TextField statement;

    @FXML
    private StackPane stackPane;

    private static final AtomicBoolean isTableInitialized = new AtomicBoolean(false);

    private final TableView<ReasoningTable> argumentTable = new TableView<>();

    private ObservableList<ReasoningTable> extractData(HttpResponse<String> response) {
        List<Object> m = gson.fromJson(response.body(), List.class);
        return FXCollections.observableArrayList(
                m.stream().map(e -> {
                            LinkedTreeMap<String, Object> elements = (LinkedTreeMap<String, Object>) e;
                            LinkedTreeMap situationLink = (LinkedTreeMap) elements.get("link");
                            return new ReasoningTable(situationLink.get("premise").toString(), situationLink.get("result").toString(), elements.get("metric").toString());
                        }
                ).collect(Collectors.toList()));
    }

    public void searchForArguments() {
        if(!isTableInitialized.get()) {
            FxElementsUtils.initTable(stackPane, argumentTable);
            isTableInitialized.set(true);
        }
        try {
            argumentTable.getItems().clear();

            HttpRequest getVersionBuilder = HttpRequest.newBuilder(
                    new URI("http://localhost:8080/argument/statement"))
                    .header("Accept", "application/json")
                    .header("Content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(String.format("{ \"statement\" : \"%s\"}", statement.getText())))
                    .build();
            HttpResponse<String> response = client.send(getVersionBuilder, HttpResponse.BodyHandlers.ofString());

            argumentTable.setItems(extractData(response));
        } catch (InterruptedException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
