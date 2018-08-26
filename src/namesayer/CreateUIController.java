package namesayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * CreateUIController - Controller for initiating Create feature (including asking user to create video and audio process)
 * AUTHOR: Eric Leung
 */
public class CreateUIController implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private JFXButton createButton;
    @FXML
    private MediaView createMedia;
    @FXML
    private JFXTextField createTextField;

    private NamesayerApp namesayerApp = new NamesayerApp();

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

    //Quit creation event to allow for QUIT Application
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

    /**
     * Create creation button deals with allowing user to create a Creation:
     * (1) User types in VALID name in text box and clicks createCreation button
     * (2) Video is created from given name (createVideo method)
     * (3) Audio is created, by 5 second recording of user (createAudio method)
     * (4) Pop up options (Save, Listen, Redo) audio options for user (createCreation method)
     * (5) Success!
     */
    @FXML
    void createCreation(ActionEvent event) {
        String creationName = createTextField.getText().replaceAll("\\s+","");

        //Checking if valid creationName: Warning dialog
        boolean validName = true;
        if (creationName.contains("$") || creationName.contains("/") || creationName.contains("\"") || creationName.isEmpty() || creationName.contains("&quot;") || creationName.contains("'")) {
            Alert confirmCreateAudio = new Alert(Alert.AlertType.WARNING, "Sorry! Please enter a valid name!", ButtonType.OK);
            validName = false;
            confirmCreateAudio.showAndWait();
        }

        //Creating video - If it already exists then suggest override
        int override = createVideo(creationName);
        if ((override == 0 || override == 2) && validName) {

            //Creating audio
            createAudio(creationName);

            //Creating creation (video + audio)
            boolean finalFlag = false;
            while (!finalFlag) {
                finalFlag = createCreation(creationName);
            }
        }
    }

    public int createVideo(String creationName) {
        int override = 0; //0 = creation doesn't exist, 1 = creation exists, override no, 2 = creation exists override yes.

        boolean checkVideo = new File("creationData", creationName + ".mp4").exists();
        String createCreationVideo = "cd creationData/videoOnly\n" +
                "ffmpeg -loglevel quiet -y -f lavfi -i color=c=black:s=1000x800:d=5 -vf \\\n" +
                "                   \"drawtext=fontsize=100: \\\n" +
                "                    fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text='" + creationName + "'\" \\\n" +
                "                    " + creationName + ".mp4";

        if (checkVideo) {
            Alert confirmCreateVideo = new Alert(Alert.AlertType.CONFIRMATION, creationName + " already exists. Would you like to Overwrite?", ButtonType.NO, ButtonType.YES);
            confirmCreateVideo.showAndWait();
            if (confirmCreateVideo.getResult() == ButtonType.YES) {
                namesayerApp.executeCommand(createCreationVideo);
                return override = 2;
            } else {
                return override = 1;
            }
        } else {
            namesayerApp.executeCommand(createCreationVideo);
            return override;
        }
    }

    public void createAudio(String creationName) {
        String createCreationAudio = "cd creationData/audioOnly\n" +
                "ffmpeg -loglevel quiet -y -f pulse -i default -t 5 " + creationName + ".wav\n";

        Alert confirmCreateAudio = new Alert(Alert.AlertType.WARNING, "Click OK to start recording: {" + creationName + "} (5 seconds):", ButtonType.OK);
        confirmCreateAudio.showAndWait();

        if (confirmCreateAudio.getResult() == ButtonType.OK) {
            namesayerApp.executeCommand(createCreationAudio);
            Alert creatingAudio = new Alert(Alert.AlertType.WARNING, "[RECORDING]: {" + creationName + "} (5 seconds):", ButtonType.OK);
            creatingAudio.showAndWait();
        }
    }

    public boolean createCreation(String creationName) {
        boolean finalFlag = false;
        String createCreation = "cd creationData/ \n" +
                "ffmpeg -loglevel quiet -i videoOnly/" + creationName + ".mp4 \\\n" +
                "          -i audioOnly/" + creationName + ".wav -vcodec libx264 -c:a aac -strict -2  " + creationName + ".mp4;";
        String createThumbnail = "cd creationData/ \n" +
                "ffmpeg -loglevel quiet -i videoOnly/" + creationName + ".mp4 -vf scale=90:-2 -ss 00:00:01 " + creationName + ".png";

        ButtonType SAVE = new ButtonType("Save");
        ButtonType LISTEN = new ButtonType("Listen");
        ButtonType REDO = new ButtonType("Redo");

        Alert confirmCreateCreation = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to: ", SAVE, LISTEN, REDO);
        confirmCreateCreation.showAndWait();

        //Saving and completing mp4 creation video and audio
        if (confirmCreateCreation.getResult() == SAVE) {
            namesayerApp.executeCommand(createCreation); //Create Creation video+audio
            namesayerApp.executeCommand(createThumbnail); //Create thumbnail
            return finalFlag = true;

            //Listening to audio through media player
        } else if (confirmCreateCreation.getResult() == LISTEN) {
            File file = new File("creationData/audioOnly/" + creationName + ".wav");
            String source = file.toURI().toString();
            Media media = new Media(source);
            MediaPlayer player = new MediaPlayer(media);

            createMedia.setMediaPlayer(player);
            player.play();

            Alert listeningConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Now listening...", ButtonType.OK);
            listeningConfirmation.showAndWait();
            if (listeningConfirmation.getResult() == ButtonType.OK) {
                return finalFlag = false;
            }

            //Redo the audio recording
        } else if (confirmCreateCreation.getResult() == REDO) {
            createAudio(creationName);
        }
        return false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createButton.setDisable(true); //Disable create button

    }
}