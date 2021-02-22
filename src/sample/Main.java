package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controllers.LoginController;
import sample.controllers.MainController;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    DbConnection dbConnection = new DbConnection();

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;

        primaryStage.setMaximized(true);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/xml/MainWindow.fxml"));
        MainController mainController = new MainController();
        loader.setController(mainController);
        Parent layout = loader.load();
        mainController.setMainApp(this);
//        mainController.logout();
        Scene scene = new Scene(layout , 1800, 950);
        primaryStage.setMinWidth(1800);
        primaryStage.setMinHeight(950);
        layout.getStylesheets().add(dbConnection.getTheme());
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
