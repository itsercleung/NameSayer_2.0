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

/**
 * NamesayerController - Initial UI menu screen, which has all available features of name sayer buttons, and introductory
 * description and overall presentation.
 * AUTHOR: Eric Leung
 */
public class NamesayerController implements Initializable {

    @FXML
    private AnchorPane mainPane;

    private NamesayerApp namesayerApp = new NamesayerApp();

    //Play creation event to go into PLAY UI
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

    //Create creation event to go into CREATE UI
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

    //Delete creation event to go into DELETE UI
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

    //Quit creation event to go into QUIT UI
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

    //Initialize creation data folders including videoOnly and audioOnly folders
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Create appropriate files to store video/audio/creations
        String creationDataFolder = "if [ ! -d ./creationData ]; then\n" +
                "        mkdir -p ./creationData\n" +
                "    fi";
        String videoAudioFolder = "if [[ ! -d creationData/videoOnly || ! -d creationData/audioOnly ]]; then\n" +
                "        cd creationData/\n" +
                "        if [[ ! -d videoOnly ]]; then\n" +
                "            mkdir -p creationData/videoOnly\n" +
                "        fi\n" +
                "        if [[ ! -d audioOnly ]]; then\n" +
                "            mkdir -p creationData/audioOnly\n" +
                "        fi\n" +
                "        cd ..\n" +
                "    fi";
        namesayerApp.executeCommand(creationDataFolder);
        namesayerApp.executeCommand(videoAudioFolder);
    }
}