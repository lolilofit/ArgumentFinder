package ru.nsu.usova.dipl;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import ru.nsu.usova.dipl.model.ReasoningTable;

public class FxElementsUtils {
    public static void initTable(StackPane stackPane, TableView argumentTable) {
        TableColumn premiseCol = new TableColumn<>("premise");
        premiseCol.setMinWidth(400);
        premiseCol.setCellValueFactory(new PropertyValueFactory<ReasoningTable, String>("premise"));

        TableColumn resultCol = new TableColumn<>("result");
        resultCol.setMinWidth(400);
        resultCol.setCellValueFactory(new PropertyValueFactory<ReasoningTable, String>("result"));

        TableColumn idCol = new TableColumn<>("similarity");
        idCol.setMinWidth(100);
        idCol.setCellValueFactory(new PropertyValueFactory<ReasoningTable, String>("id"));

        argumentTable.getColumns().addAll(premiseCol, resultCol, idCol);

        stackPane.getChildren().addAll(FXCollections.observableArrayList(argumentTable));
    }

}
