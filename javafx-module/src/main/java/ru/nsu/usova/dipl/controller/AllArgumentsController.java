package ru.nsu.usova.dipl.controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import ru.nsu.usova.dipl.element.FxElementsUtils;
import ru.nsu.usova.dipl.model.ReasoningTable;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static ru.nsu.usova.dipl.controller.SearchController.CLIENT;

public class AllArgumentsController {
    private static final Gson GSON = new Gson();

    private static final FxElementsUtils ELEMENT_UTILS = new FxElementsUtils();

    private static final AtomicBoolean isTableInitialized = new AtomicBoolean(false);

    @FXML
    private StackPane stackPane;

    private final TableView<ReasoningTable> argumentTable = new TableView<>();

    private ObservableList<ReasoningTable> extractData(HttpResponse<String> response) {
        List<Object> m = GSON.fromJson(response.body(), List.class);
        return FXCollections.observableArrayList(
                m.stream().map(e -> {
                            LinkedTreeMap<String, Object> elements = (LinkedTreeMap<String, Object>) e;
                            return new ReasoningTable(
                                    elements.get("premise").toString(),
                                    elements.get("result").toString());
                        }
                ).collect(Collectors.toList()));
    }

    public void loadAllArguments() {
        if (!isTableInitialized.get()) {
            ELEMENT_UTILS.initBaseTable(stackPane, argumentTable);
            isTableInitialized.set(true);
        }
        try {
            argumentTable.getItems().clear();
            HttpRequest getVersionBuilder = HttpRequest.newBuilder(
                    new URI("http://localhost:8080/argument/all"))
                    .header("Accept", "application/json")
                    .header("Content-type", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = CLIENT.send(getVersionBuilder, HttpResponse.BodyHandlers.ofString());

            argumentTable.setItems(extractData(response));
        } catch (InterruptedException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
