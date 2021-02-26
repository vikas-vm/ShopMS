
package sample.controllers;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;
        import javafx.stage.Stage;
        import sample.DbConnection;
        import sample.models.*;

        import java.net.URL;
        import java.sql.Connection;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.sql.Statement;
        import java.util.HashMap;
        import java.util.Optional;
        import java.util.ResourceBundle;

public class NewPaymentRecord extends AbstractController implements Initializable {
    private final HashMap<String, Object> result = new HashMap<String, Object>();
    private Stage stage = null;
    DbConnection dbConnection = new DbConnection();
    Connection connection = dbConnection.getConnection();
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public HashMap<String, Object> getResult() {
        return this.result;
    }
    public TextField amount, name, contact,email;
    public TextArea address;
    public Button addPaymentBtn, closeBtn;
    int customer_id;
    float total_due;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setForm();
        closeBtn.setOnAction((event)->{
            closeStage();
        });
        contact.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,10}?")) {
                contact.setText(oldValue);
            }
        });
        addPaymentBtn.setOnAction((event)->{
            UpdateCustomer();
        });
    }
    public void setForm(){
        String query = "SELECT * FROM customers where id = '"+customer_id+"'";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()){
                name.setText(rs.getString("name"));
                contact.setText(rs.getString("contact"));
                email.setText(rs.getString("email"));
                address.setText(rs.getString("address"));
                amount.setText(""+total_due);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void UpdateCustomer(){
        if(!name.getText().equals("")  && !amount.getText().equals("")){
            String query;
            query = "Update customers set name='"+name.getText()+"', contact='"+contact.getText()+"'," +
                    "email='"+email.getText()+"', address='"+address.getText()+"', regularity_count=regularity_count+1 where id='"+customer_id+"'";
            try {
                int return_result = dbConnection.executeQuery(query);
                if (return_result>0){
                    PlaceOrder();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Internal Error");
                    alert.setHeaderText("Internal Error");
                    alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
                    alert.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Internal Error");
                alert.setHeaderText("Internal Error");
                alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
                alert.show();
            }

        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning | InventoryMS");
            alert.setHeaderText("Re-enter Amount");
            alert.setContentText("Amount fields must not have null value");
            alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
            alert.show();
        }
    }

//
    public void PlaceOrder() throws SQLException {
        String query;
        ResultSet rs;
        Statement st;
        int orderId;

        st = connection.createStatement();
        query = "insert into shop_orders(status, cust_id, total_amt) values('1','"+customer_id+"','0')";
        int return_result = dbConnection.executeQuery(query);
        if(return_result>0) {
            query = "SELECT * FROM shop_orders where status='1' order by id desc ";
            rs = st.executeQuery(query);
            if(rs.next()){
                orderId = rs.getInt("id");
                query = "Insert into payments(order_id, amount) values('"+orderId+"','"+amount.getText()+"')";
                dbConnection.executeQuery(query);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information | InventoryMS");
                alert.setHeaderText("Payment record added successfully");
                alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
                alert.showAndWait().ifPresent(rs1 -> {
                    if (rs1 == ButtonType.OK) {
                        closeStage();
                    }
                });
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Internal Error");
                alert.setHeaderText("Internal Error");
                alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
                alert.show();
            }
        }
    }
    private void closeStage() {
        if(stage!=null) {
            stage.close();
        }
    }
}



