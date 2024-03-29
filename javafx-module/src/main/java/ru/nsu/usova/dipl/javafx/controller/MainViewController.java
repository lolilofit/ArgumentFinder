package ru.nsu.usova.dipl.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import ru.nsu.usova.dipl.javafx.element.FxElementsUtils;


public class MainViewController {
    private static final FxElementsUtils ELEMENT_UTILS = new FxElementsUtils();

    @FXML
    private TabPane tabs;

    public void initialize() {
        openSearchTab();
    }

    public void openInsertTextTab() {
        ELEMENT_UTILS.openTab(tabs, "Новый текст", "/load_single_text.fxml");
    }

    public void openFiletTab() {
        ELEMENT_UTILS.openTab(tabs, "Открыть файл", "/open_file.fxml");
    }

    public void openViewAllTab() {
        ELEMENT_UTILS.openTab(tabs, "Вся аргументация", "/view_all.fxml");
    }

    public void openSearchTab() {
        ELEMENT_UTILS.openTab(tabs, "Поиск", "/view_args.fxml");
    }

    public void openCompareSituation() {ELEMENT_UTILS.openTab(tabs, "Сравнение", "/compare_situations.fxml");}

    public void openSituation() {ELEMENT_UTILS.openTab(tabs, "Ситуация", "/open_situation.fxml");}

    public void openInfo() {ELEMENT_UTILS.openTab(tabs, "Информация", "/info.fxml");}

    public void openDownloadText() {DownloadTextController.downloadTexts();}
}
