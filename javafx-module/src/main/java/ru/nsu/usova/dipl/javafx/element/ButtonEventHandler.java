package ru.nsu.usova.dipl.javafx.element;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import ru.nsu.usova.dipl.javafx.controller.OpenFileController;

import java.io.File;

public class ButtonEventHandler implements EventHandler<MouseEvent> {
    private File relatedFile;

    private OpenFileController openFileController;

    public ButtonEventHandler(File relatedFile, OpenFileController openFileController) {
        this.relatedFile = relatedFile;
        this.openFileController = openFileController;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if(relatedFile == null)
            return;

        if(relatedFile.isDirectory())
            openFileController.loadCurrentDir(relatedFile);
        else
            openFileController.setLoadingFileName(relatedFile.getAbsolutePath());
    }
}
