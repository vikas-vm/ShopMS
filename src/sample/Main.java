package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controllers.MainController;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        primaryStage.setMaximized(true);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("xml/MainWindow.fxml"));
        MainController mainController = new MainController();
        mainController.setMainApp(this);
        loader.setController(mainController);
        Parent layout = loader.load();
        Scene scene = new Scene(layout , 700, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("InventoryMS");

    }

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
