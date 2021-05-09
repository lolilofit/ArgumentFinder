package ru.nsu.usova.dipl.element;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import ru.nsu.usova.dipl.model.ReasoningTable;
import ru.nsu.usova.dipl.model.Situation;
import ru.nsu.usova.dipl.model.SituationQuestions;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        TableColumn idCol = new TableColumn<>("similarity metric");
        idCol.setMinWidth(100);
        idCol.setCellValueFactory(new PropertyValueFactory<ReasoningTable, String>("id"));

        TableColumn samePartRelationType = new TableColumn<>("Same part similarity type");
        samePartRelationType.setMinWidth(100);
        samePartRelationType.setCellValueFactory(new PropertyValueFactory<ReasoningTable, String>("samePartRelationType"));

        TableColumn structuralRelationType = new TableColumn<>("Structural similarity type");
        structuralRelationType.setMinWidth(100);
        structuralRelationType.setCellValueFactory(new PropertyValueFactory<ReasoningTable, String>("structuralRelationType"));

        argumentTable.getColumns().addAll(baseColumns);
        argumentTable.getColumns().addAll(samePartRelationType, structuralRelationType, idCol);

        //stackPane.getChildren().addAll(FXCollections.observableArrayList(argumentTable));
    }

    public void initBaseTable(StackPane stackPane, TableView argumentTable) {
        List<TableColumn> baseColumns = getBaseColumns();
        argumentTable.getColumns().addAll(baseColumns);
        //stackPane.getChildren().addAll(FXCollections.observableArrayList(argumentTable));
    }

    public void openTab(TabPane tabs, String tabName, String fileName) {
        if (tabs.getTabs().stream().anyMatch(t -> t.getText().contains(tabName)))
            return;
        try {
            Tab tab = new Tab();
            tab.setText(tabName);
            Parent p = getViewParent(fileName);
            tab.setContent(p);

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

    public TreeView<String> drawSituations(Situation[] s) {
        TreeItem<String> nodes = new TreeItem<>("Полученные ситуации");

        Integer count = 1;

        for(Situation situation : s) {
            nodes.getChildren().add(drawTreeItemsSituations(situation, count.toString()));
            count++;
        }
        return new TreeView<String>(nodes);
    }

    public TreeItem<String> drawTreeItemsSituations(Situation s, String name) {
        Integer count = 1;
        TreeItem<String> treeItem = new TreeItem<>(name);

        if(s.getQuestionsList() == null || s.getQuestionsList().size() == 0) {
            for(Situation situation : s.getChildSituations()) {
                treeItem.getChildren().add(drawTreeItemsSituations(situation, name + "_" + count));
                count++;
            }
        } else {
            return drawTreeItemsQuestions(s.getQuestionsList(), name);
        }
        return treeItem;
    }

    public TreeItem<String> drawTreeItemsQuestions(List<SituationQuestions> questions, String name) {
        TreeItem<String> treeItem = new TreeItem<>(name);

        for(SituationQuestions s : questions) {
            treeItem.getChildren().add(new TreeItem<>(s.getQuestionKey() + " : " + s.getQuestionValue()));
        }
        return treeItem;
    }
}
