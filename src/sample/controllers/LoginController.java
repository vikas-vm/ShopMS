package sample.controllers;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.DbConnection;
import sample.Main;


import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginController extends AbstractController implements Initializable {
    public Button loginButton, exitBtn;
    public TextField username;
    public TextField password;
    DbConnection dbConnection = new DbConnection();
    private Stage stage = null;
    private void checkUser(){

        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM admins where username='"+username.getText()+"' and password='"+password.getText()+"'";
        Statement st;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){
                closeStage();
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
        exitBtn.setOnAction((event)->{
            exit();
        });
    }
    private void exit(){

        Platform.exit();
        System.exit(0);
    }
    private void closeStage() {
        if(stage!=null) {
            stage.close();
        }
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
