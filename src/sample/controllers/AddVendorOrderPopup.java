package sample.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.DbConnection;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AddVendorOrderPopup extends AbstractController implements Initializable {
    public TextField order_title;
    public Button insertNewOrderBtn;
    public Text warning;
    private HashMap<String, Object> result = new HashMap<String, Object>();
    private Stage stage = null;
    DbConnection dbConnection = new DbConnection();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public HashMap<String, Object> getResult() {
        return this.result;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        insertNewOrderBtn.setOnAction((event)->{
            if(!order_title.getText().equals("")){
                result.clear();
                result.put("orderTitle", order_title.getText());
                warning.setText("");
                closeStage();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Insertion failed!");
                alert.setHeaderText("Please, fills all mandatory fields");
                alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
                alert.getDialogPane().getStyleClass().add("myDialog");
                alert.show();
            }
        });

    }

    private void closeStage() {
        if(stage!=null) {
            stage.close();
        }
    }
}
