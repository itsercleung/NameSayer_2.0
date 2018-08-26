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
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * DeleteUIController - Controller for initiating Delete feature (supplying view of creations, thumbnails and label for
 * such creations and asking if user wants to delete this certain creation).
 * AUTHOR: Eric Leung
 */
public class DeleteUIController implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private ListView <ImageList> deleteListView;

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

    //When user clicks onto deleteListView which is found in initialized, we can select which one they've picked and gives a
    //pop up to ask if they want to delete or not.
    @FXML
    void deleteSelectedCreation(MouseEvent event) {
        String fileName = deleteListView.getSelectionModel().selectedItemProperty().toString();
        fileName = (fileName.substring(fileName.lastIndexOf(" "), fileName.length() - 5).replaceAll("\\s+", ""));

        //Display pop up confirmation menu when user selects cell
        Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION, "Delete creation {" + fileName + "} ?", ButtonType.NO, ButtonType.YES);
        confirmDelete.showAndWait();

        //If Confirmed, delete cell
        if (confirmDelete.getResult() == ButtonType.YES) {
            String removeCreation = "cd creationData/\n" +
                    "rm " + fileName + ".mp4\n" +
                    "rm " + fileName + ".png";
            namesayerApp.executeCommand(removeCreation);

            //Remove graphics of selectedCell from ListView GUI Component
            int selectedCell = deleteListView.getSelectionModel().getSelectedIndex();
            deleteListView.getItems().remove(selectedCell);

            //If no creations then set ListView disabled
            if (data.size() == 0) {
                deleteListView.setMouseTransparent(true);
                deleteListView.setFocusTraversable(false);
                deleteListView.setPlaceholder(new Label("Sorry! There are no creations!"));
            }
        }
    }

    //Initializing deletelistView (which finds all valid creations in directory, initialise thumbnails of the videos, then
    //display thumbnails and its labels within the deleteListView.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deleteButton.setDisable(true); //Disable delete button

        //Form thumbnail data list
        data = namesayerApp.formCreationList();

        //If no creations then set ListView disabled
        if (data == null) {
            deleteListView.setMouseTransparent(true);
            deleteListView.setFocusTraversable(false);
            deleteListView.setPlaceholder(new Label("Sorry! There are no creations!"));
        }
        deleteListView.setCellFactory(new Callback < ListView < ImageList > , ListCell < ImageList >> () {

            @Override
            public ListCell < ImageList > call(ListView < ImageList > param) {
                ListCell < ImageList > cell = new ListCell < ImageList > () {

                    @Override
                    protected void updateItem(ImageList image, boolean flag) {
                        super.updateItem(image, flag);
                        if (image != null) {
                            File file = new File("creationData/" + image.getFlag());
                            Image img = new Image(file.toURI().toString());
                            ImageView imgView = new ImageView(img);
                            setGraphic(imgView);
                            setText(image.getImage());
                        } else {
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
        deleteListView.setItems(data);
    }
}