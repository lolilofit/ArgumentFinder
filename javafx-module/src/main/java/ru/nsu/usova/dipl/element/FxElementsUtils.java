package ru.nsu.usova.dipl.element;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import ru.nsu.usova.dipl.model.ReasoningTable;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class FxElementsUtils {
    private List<TableColumn> getBaseColumns() {
        TableColumn premiseCol = new TableColumn<>("premise");
        premiseCol.setMinWidth(400);
        premiseCol.setCellValueFactory(new PropertyValueFactory<ReasoningTable, String>("premise"));

        TableColumn resultCol = new TableColumn<>("result");
        resultCol.setMinWidth(400);
        resultCol.setCellValueFactory(new PropertyValueFactory<ReasoningTable, String>("result"));

        return List.of(premiseCol, resultCol);
    }

    public void initMetricTable(StackPane stackPane, TableView argumentTable) {
        List<TableColumn> baseColumns = getBaseColumns();

        TableColumn idCol = new TableColumn<>("similarity");
        idCol.setMinWidth(100);
        idCol.setCellValueFactory(new PropertyValueFactory<ReasoningTable, String>("id"));

        argumentTable.getColumns().addAll(baseColumns);
        argumentTable.getColumns().add(idCol);

        stackPane.getChildren().addAll(FXCollections.observableArrayList(argumentTable));
    }

    public void initBaseTable(StackPane stackPane, TableView argumentTable) {
        List<TableColumn> baseColumns = getBaseColumns();
        argumentTable.getColumns().addAll(baseColumns);
        stackPane.getChildren().addAll(FXCollections.observableArrayList(argumentTable));
    }

    public void openTab(TabPane tabs, String tabName, String fileName) {
        if (tabs.getTabs().stream().anyMatch(t -> t.getText().contains(tabName)))
            return;
        try {
            Tab tab = new Tab();
            tab.setText(tabName);
            tab.setContent(getViewParent(fileName));

            tabs.getTabs().add(tab);
            tabs.getSelectionModel().selectLast();
        } catch (IOException ignored) {
        }
    }

    public Parent getViewParent(String filename) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource(filename);
        loader.setLocation(xmlUrl);
        return loader.load();
    }
}
