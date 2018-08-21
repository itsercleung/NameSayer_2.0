package namesayer;

import com.jfoenix.controls.JFXTextArea;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NamesayerController implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private JFXTextArea namesayerTextArea;

    private NamesayerApp namesayerApp = new NamesayerApp();

    @FXML
    void playCreation(ActionEvent event) {
        //Load Play pane
        AnchorPane playPane = null;
        try {
            playPane = FXMLLoader.load(getClass().getResource("/namesayer/resources/PlayUI.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPane.getChildren().setAll(playPane);
    }

    @FXML
    void createCreation(ActionEvent event) {
        //Load Play pane
        AnchorPane createPane = null;
        try {
            createPane = FXMLLoader.load(getClass().getResource("/namesayer/resources/CreateUI.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPane.getChildren().setAll(createPane);
    }

    @FXML
    void deleteCreation(ActionEvent event) {
        //Load Delete pane
        AnchorPane deletePane = null;
        try {
            deletePane = FXMLLoader.load(getClass().getResource("/namesayer/resources/DeleteUI.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPane.getChildren().setAll(deletePane);
    }

    @FXML
    void quitNamesayer(ActionEvent event) {
        Task taskQuit = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                System.exit(0);
                return null;
            }
        };
        new Thread (taskQuit).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Create appropriate files to store video/audio/creations
        String creationDataFolder = "if [ ! -d ./creationData ]; then\n" +
                "        mkdir -p ./creationData\n" +
                "    fi";
        String videoAudioFolder = "if [[ ! -d creationData/videoOnly || ! -d creationData/audioOnly ]]; then\n" +
                "        cd creationData/\n" +
                "        if [[ ! -d creationData/videoOnly ]]; then\n" +
                "            mkdir -p ./videoOnly\n" +
                "        fi\n" +
                "        if [[ ! -d creationData/audioOnly ]]; then\n" +
                "            mkdir -p ./audioOnly\n" +
                "        fi\n" +
                "        cd ..\n" +
                "    fi";
        namesayerApp.executeCommand(creationDataFolder);
        namesayerApp.executeCommand(videoAudioFolder);
    }
}