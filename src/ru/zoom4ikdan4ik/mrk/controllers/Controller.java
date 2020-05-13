package ru.zoom4ikdan4ik.mrk.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import ru.zoom4ikdan4ik.mrk.Start;
import ru.zoom4ikdan4ik.mrk.threads.ReverseThread;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private final String WORKING_DIRECTORY = System.getProperty("user.dir");

    @FXML
    public TextField modFile;
    @FXML
    public Button decompileButton;
    @FXML
    public Button browseButton;
    @FXML
    public ChoiceBox mcVersion;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> mcps = new ArrayList<>();

        for (File file : Objects.requireNonNull(Paths.get(this.WORKING_DIRECTORY, "mcp").toFile().listFiles(pathname -> pathname.isDirectory())))
            mcps.add(file.getName());

        this.mcVersion.setItems(FXCollections.observableArrayList(mcps));
        this.mcVersion.setValue(this.mcVersion.getItems().get(0));
    }

    public void chooserButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Minecraft Mod Files (*.jar, *.zip, *.litemod)", "*.jar", "*.zip", "*.litemod");

        fileChooser.setTitle("Выберите модификацию");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(Start.getStage().getOwner());

        if (file != null)
            this.modFile.setText(file.getPath());
    }

    public void startReverse() {
        if (!this.modFile.getText().isEmpty()) {
            ReverseThread reverseThread = new ReverseThread(this.getMCPPath(), this.modFile.getText());

            reverseThread.start();
        }
    }

    private String getMCPPath() {
        return Paths.get(this.WORKING_DIRECTORY, "mcp", (String) this.mcVersion.getValue()).toString();
    }
}
