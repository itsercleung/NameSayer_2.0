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

    //Method to execute any command without duplicate code
    public void executeCommand(String command) {
        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
                try {
                    Process process = processBuilder.start();
                    process.waitFor();

                } catch (IOException | InterruptedException e1) {
                    e1.printStackTrace();
                }
                return null;
            }
        };
        new Thread (task).start();
    }

    //Put thumbnail and name of Creation into ListView
    public ObservableList < ImageList > formCreationList() {
        //Populate list of available creations
        final ObservableList < ImageList > data = FXCollections.observableArrayList();
        data.clear();

        File file = new File("creationData/");
        File[] files = file.listFiles();
        assert files != null;
        Boolean emptyFlag = false;

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