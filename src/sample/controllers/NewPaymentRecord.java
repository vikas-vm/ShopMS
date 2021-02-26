
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
//    public Button resetFormBtn, closeBtn, clearSearchBtn, placeOrderBtn, editTotalBtn;
//    public TextField searchField, totalAmt, paidAmt, name, contact, email;
//    public TextArea address;
    int customer_id=0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        showCustomers("");
//        totalAmt.setText(String.valueOf(total));
//        paidAmt.setText(String.valueOf(total));
//        closeBtn.setOnAction((event)->{
//            closeStage();
//        });
//        contact.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.matches("\\d{0,10}?")) {
//                contact.setText(oldValue);
//            }
//        });
//        paidAmt.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.matches("\\d{0,10}([\\.]\\d{0,2})?")) {
//                paidAmt.setText(oldValue);
//            }
//        });
//        placeOrderBtn.setOnAction((event)->{
//            AddCustomer();
//        });
//
//
    }

//
//    public void PlaceOrder(){
//        String query = "UPDATE order_items oi JOIN items i on oi.item_id=i.id set oi.price=i.mrp, i.stock=i.stock-oi.qty where oi.order_id='"+orderId+"'";
//        dbConnection.executeQuery(query);
//        query = "UPDATE shop_orders so  set so.status='1', so.cust_id='"+customer_id+"', total_amt='0' where id='"+orderId+"'";
//        dbConnection.executeQuery(query);
//        query = "Insert into payments(order_id, amount) values('"+orderId+"','"+paidAmt.getText()+"')";
//        dbConnection.executeQuery(query);
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Information | InventoryMS");
//        alert.setHeaderText("Order Completed");
//        alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
//        alert.showAndWait().ifPresent(rs -> {
//            if (rs == ButtonType.OK) {
//                closeStage();
//            }
//        });
//    }
    private void closeStage() {
        if(stage!=null) {
            stage.close();
        }
    }
}



