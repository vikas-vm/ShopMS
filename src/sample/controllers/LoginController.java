package sample.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DbConnection;
import sample.Main;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public Button loginButton;
    public TextField username;
    public TextField password;
    DbConnection dbConnection = new DbConnection();
    private void checkUser(){

        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM admins where username='"+username.getText()+"' and password='"+password.getText()+"'";
        Statement st;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){
                Stage primaryStage = new Stage();
                primaryStage.setMaximized(true);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/sample/xml/MainWindow.fxml"));
                MainController mainController = new MainController();
                loader.setController(mainController);
                Parent layout = loader.load();
                Scene scene = new Scene(layout , 1800, 950);
                primaryStage.setMinWidth(1800);
                primaryStage.setMinHeight(950);
                scene.getRoot().getStylesheets().add(dbConnection.getTheme());
                primaryStage.setScene(scene);
                primaryStage.show();
                primaryStage.setTitle("InventoryMS");
                Stage stage2 = (Stage) loginButton.getScene().getWindow();
                stage2.close();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("failed to login");
                alert.setHeaderText("wrong username or password");
                alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMainApp() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loginButton.setOnAction((event)->{
            checkUser();
        });
    }
}
