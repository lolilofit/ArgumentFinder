package ru.nsu.usova.dipl;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ApplicationContext context = new AnnotationConfigApplicationContext(JavafxConfig.class);
        JavafxConfig.View mainView = (JavafxConfig.View) context.getBean("mainView");

        Parent root = mainView.getParent();
        VBox layout = new VBox(root);
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
