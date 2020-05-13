package ru.zoom4ikdan4ik.mrk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Start extends Application {
    private static Stage stage = new Stage();

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fx/GUI.fxml"));

        stage = primaryStage;

        primaryStage.setTitle("Minecraft Reverse Kit");
        primaryStage.getIcons().add(new Image("images/icon.png"));
        primaryStage.setScene(this.getScene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public Scene getScene(Parent parent) {
        Scene scene = new Scene(parent, 290.0D, 125.0D);

        scene.getStylesheets().add(this.getClass().getResource("fx/css/style.css").toExternalForm());

        return scene;
    }
}
