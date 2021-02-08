package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class VendorController extends sample.AbstractController implements Initializable {
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

    public void addVendor(ActionEvent actionEvent) {
        if(!title.getText().equals("") && !city.getText().equals("")){
            String query = "insert into vendor(title, address, city, email, contact)" +
                    " values('"+title.getText()+"','"+address.getText()+"','"+city.getText()+"'," +
                    "'"+email.getText()+"','"+contact.getText()+"')";
            int result = dbConnection.executeQuery(query);
            if (result>0){
                vendor_warning.setText("");
            }
            else {
                vendor_warning.setText("Internal Error");
            }
        }
        else {
            vendor_warning.setText("Please, fills all mandatory fields");
        }
    }

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

                    vendor_warning.setText("");
                    closeStage();

                }
                else {
                    vendor_warning.setText("Internal Error");
                }
            }
            else {
                vendor_warning.setText("Please, fills all mandatory fields");
            }
        });

    }

    private void closeStage() {
        if(stage!=null) {
            stage.close();
        }
    }
}
