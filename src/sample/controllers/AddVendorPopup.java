package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.DbConnection;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AddVendorPopup extends AbstractController implements Initializable {
    public TextField email;
    public TextField city;
    public TextArea address;
    public Button insertVendorBtn;
    public Text vendor_warning;
    public TextField title;
    public TextField contact;
    DbConnection dbConnection = new DbConnection();
    private HashMap<String, Object> result = new HashMap<String, Object>();
    private Stage stage = null;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public HashMap<String, Object> getResult() {
        return this.result;
    }

    public void initialize(URL url, ResourceBundle rb) {
        insertVendorBtn.setOnAction((event)->{
            if(!title.getText().equals("") && !city.getText().equals("")){
                String query = "insert into vendors(title, address, city, email, contact)" +
                        " values('"+title.getText()+"','"+address.getText()+"','"+city.getText()+"'," +
                        "'"+email.getText()+"','"+contact.getText()+"')";
                int return_result = dbConnection.executeQuery(query);
                if (return_result>0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Vendor Added Successfully");
                    alert.setHeaderText("Vendor Added Successfully");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            closeStage();
                        }
                    });
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Internal Error");
                    alert.setHeaderText("Internal Error");
                    alert.show();
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Vendor failed to save");
                alert.setHeaderText("Please fills all mandatory fields");
                alert.show();
                alert.setContentText("Recheck again title and city amt/qty fields");
            }
        });
    }

    private void closeStage() {
        if(stage!=null) {
            stage.close();
        }
    }
}
