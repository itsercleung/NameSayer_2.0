package namesayer;

import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * PlayUIController - Controller for initiating Play feature (supplying view of creations, thumbnails and label for
 * such creations and asking if user wants to play this certain creation).
 * AUTHOR: Eric Leung
 */
public class PlayUIController implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private JFXButton playButton;
    @FXML
    private ListView <ImageList> playListView;
    @FXML
    private MediaView playMedia;

    private NamesayerApp namesayerApp = new NamesayerApp();
    private ObservableList <ImageList> data;

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

    //When user clicks onto playListView which is found in initialized, we can select which one they've picked and plays
    //video in MediaView automatically
    @FXML
    void playSelectedCreation(MouseEvent event) {
        String fileName = playListView.getSelectionModel().selectedItemProperty().toString();
        fileName = (fileName.substring(fileName.lastIndexOf(" "), fileName.length() - 5).replaceAll("\\s+", ""));

        //If no creations then set ListView disabled
        if (data == null) {
            playListView.setMouseTransparent(true);
            playListView.setFocusTraversable(false);
            playListView.setPlaceholder(new Label("Sorry, Empty!"));
        }

        //Play selected mp4 file
        File file = new File("creationData/" + fileName + ".mp4");
        String source = file.toURI().toString();
        Media media = new Media(source);
        MediaPlayer player = new MediaPlayer(media);

        playMedia.setMediaPlayer(player);
        player.play();
    }

    //Initializing playlistView (which finds all valid creations in directory, initialise thumbnails of the videos, then
    //display thumbnails and its labels within the deleteListView.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playButton.setDisable(true); //Disable play button

        //PLAY VIEW
        data = namesayerApp.formCreationList();

        //If no creations then set ListView disabled
        if (data == null) {
            playListView.setMouseTransparent(true);
            playListView.setFocusTraversable(false);
            playListView.setPlaceholder(new Label("Sorry, Empty!"));
        }

        //Setting thumbnail of creation and name into ListView
        playListView.setCellFactory(new Callback < ListView < ImageList > , ListCell < ImageList >> () {

            @Override
            public ListCell < ImageList > call(ListView < ImageList > param) {
                ListCell < ImageList > cell = new ListCell < ImageList > () {

                    @Override
                    protected void updateItem(ImageList image, boolean flag) {
                        super.updateItem(image, flag);
                        if (image != null) {
                            setText(image.getImage());
                            File file = new File("creationData/" + image.getFlag());
                            Image img = new Image(file.toURI().toString());
                            ImageView imgView = new ImageView(img);
                            setGraphic(imgView);
                        }
                    }
                };
                return cell;
            }
        });
        playListView.setItems(data);
    }
}