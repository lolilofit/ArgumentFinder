package ru.nsu.usova.dipl.controller;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import ru.nsu.usova.dipl.model.ReasoningTable;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;


public class MainViewController {
    private static final HttpClient client = HttpClient.newHttpClient();

    private final Gson gson = new Gson();

    @FXML
    private TextField statement;

    @FXML
    private AnchorPane tablePane;

    private final TableView<ReasoningTable> argumentTable = new TableView<>();

    private void initTable() {
        TableColumn premiseCol = new TableColumn<>("premise");
        premiseCol.setMaxWidth(200);
        premiseCol.setCellValueFactory(new PropertyValueFactory<ReasoningTable, String>("premise"));

        TableColumn resultCol = new TableColumn<>("result");
        resultCol.setMinWidth(500);
        resultCol.setCellValueFactory(new PropertyValueFactory<ReasoningTable, String>("result"));

        TableColumn idCol = new TableColumn<>("id");
        idCol.setMinWidth(70);
        idCol.setCellValueFactory(new PropertyValueFactory<ReasoningTable, String>("id"));

        argumentTable.getColumns().addAll(premiseCol, resultCol, idCol);

        tablePane.getChildren().addAll(FXCollections.observableArrayList(argumentTable));
    }
    public void searchForArguments() {
        initTable();

        try {
            HttpRequest getVersionBuilder = HttpRequest.newBuilder(
                    new URI("http://localhost:8080/argument/statement"))
                    .header("Accept", "application/json")
                    .header("Content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(String.format("{ \"statement\" : \"%s\"}", statement.getText())))
                    .build();
            HttpResponse<String> response = client.send(getVersionBuilder, HttpResponse.BodyHandlers.ofString());

            Map map = gson.fromJson(response.body(), Map.class);

            System.out.println("");
            //HashMap<SituationLink, Float> arguments = mapper.readValue(response.body(), new TypeReference<>() {});
            //clear argumentList
            //ObservableList<ReasoningTable> data = FXCollections.observableArrayList(
            //        arguments.entrySet().stream().map(link -> new ReasoningTable("", "", link.getValue().toString())).collect(Collectors.toList())
            //);
            //argumentTable.setItems(data);
        }
        catch (InterruptedException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
