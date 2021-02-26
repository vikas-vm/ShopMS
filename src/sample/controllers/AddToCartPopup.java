package sample.controllers;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.DbConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AddToCartPopup extends AbstractController implements Initializable {

    public int itemId;
    public int orderId;
    public float availStock;

    public Button addToCartBtn, updatePriceBtn, closeBtn;
    public TextField qty;
    public Label title,mrp,stockLabel,stock,qtyLabel;
    private final HashMap<String, Object> result = new HashMap<String, Object>();
    private Stage stage = null;
    DbConnection dbConnection = new DbConnection();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public HashMap<String, Object> getResult() {
        return this.result;
    }

    Connection connection = dbConnection.getConnection();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setForm();
        addToCartBtn.setOnAction((event)->{
            MoveToCart();
        });
        closeBtn.setOnAction((event)->{
            closeStage();
        });
        updatePriceBtn.setOnAction((event)->{
            UpdatePrice();
        });
    }
    public void setForm(){
        String query = "SELECT * FROM items i JOIN categories c on c.id = i.cat_id JOIN vendor_orders vo on vo.id = i.vo_id where i.id = '"+itemId+"'";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()){
                availStock = rs.getFloat("stock");
                title.setText(rs.getString("title")+" ("+rs.getString("title")+")");
                if(rs.getInt("itemType")==0){
                    mrp.setText("₹ "+rs.getString("mrp")+"/kg");
                    qtyLabel.setText("Quantity (in kg):");
                    stockLabel.setText("Available Quantity:");
                    stock.setText(rs.getString("stock")+" kg");
                    qtyTextFieldProperty(false);
                }
                else {
                    mrp.setText("₹ "+rs.getString("mrp")+"/unt");
                    qtyLabel.setText("Unts:");
                    stockLabel.setText("Available in stock:");
                    stock.setText(rs.getString("stock")+" unt");
                    qtyTextFieldProperty(true);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void UpdatePrice(){
        result.clear();
        result.put("updateState", 1);
        closeStage();
    }


    public void qtyTextFieldProperty(boolean toInt){
        qty.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!toInt){
                if (!newValue.matches("\\d{0,10}([\\.]\\d{0,3})?")) {
                    qty.setText(oldValue);
                }
            }else {
                if (!newValue.matches("\\d*")) {
                    qty.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }

        });
    }
    public void MoveToCart(){

        if(!qty.getText().equals("") && Float.parseFloat(qty.getText())<=availStock && 0<Float.parseFloat(qty.getText())){
            String query = "insert into order_items(item_id, order_id, qty) values('"+itemId+"','"+orderId+"'," +
                    "'"+qty.getText()+"')";
            try {
                int return_result = dbConnection.executeQuery(query);
                if (return_result>0){
                    closeStage();
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
            alert.setHeaderText("Re-enter quantity");
            alert.setContentText("Quantity fields must not have 0 or null value and must be lesser than available quantity in stock");
            alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
            alert.show();
        }
    }
    private void closeStage() {
        if(stage!=null) {
            stage.close();
        }
    }
}
