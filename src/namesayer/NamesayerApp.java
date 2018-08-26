package namesayer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

/**
 * NamesayerApp: Main class to run such application, controllers and starting NamesayerUI fxml file. Also stores important
 * methods such as execute command (which runs ProcessBuilder bash commands and deals with multithreading of running commands in diff processes
 * AUTHOR: Eric Leung
 */
public class NamesayerApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("resources/NamesayerUI.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        root.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }

    //Method to execute any command without duplicate code - also using Task class to run such commands in a different process
    //to reduce freezing on GUI.
    public void executeCommand(String command) {
        //Running process builder task
        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
                try {
                    Process process = processBuilder.start();
                    process.waitFor();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread (task).start();
    }

    //Put thumbnail and name of Creation into ListView UI Controllers including Play and Delete classes.
    public ObservableList <ImageList> formCreationList() {
        //Populate list of available creations
        final ObservableList <ImageList> data = FXCollections.observableArrayList();
        data.clear();

        File file = new File("creationData/");
        File[] files = file.listFiles();
        assert files != null;
        Boolean emptyFlag = false;

        //Scan throughout files and if .png is found then add onto Observable List.
        for (File f: files) {
            if (f.getName().endsWith(".png")) {
                String fileName = f.getName();
                data.add(new ImageList(fileName.substring(0, fileName.lastIndexOf('.')), f.getName()));
                emptyFlag = true;
            }
        }
        //Checking if there are no png files
        if (!emptyFlag) {
            return null;
        }
        return data;
    }


}