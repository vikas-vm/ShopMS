package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controllers.LoginController;

public class Main extends Application {

    private Stage primaryStage;
    DbConnection dbConnection = new DbConnection();

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/xml/login.fxml"));
        LoginController loginController = new LoginController();
        loginController.setMainApp();
        loader.setController(loginController);
        Parent layout = loader.load();
        Scene scene = new Scene(layout , 450, 320);
        scene.getRoot().getStylesheets().add(dbConnection.getTheme());
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("InventoryMS | login panel");

    }
    public static void main(String[] args) {
        launch(args);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
