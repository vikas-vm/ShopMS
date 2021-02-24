package sample.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.DbConnection;
import sample.models.CategoryModel;
import sample.models.ItemModel;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;

public class UpdateItemPrice extends AbstractController implements Initializable {

    public int itemId;
    public float availStock;

    public Button updateCartItemBtn, closeBtn, updatePriceBtn;
    public TextField newMrp;
    public Label title, mrp,stockLabel,stock,newMrpLabel;
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
        closeBtn.setOnAction((event)->{
            closeStage();
        });
        updatePriceBtn.setOnAction((event)->{
            UpdatePrice();
        });
    }
    public void setForm(){
        String query = "SELECT * FROM items i JOIN categories c on c.id = i.cat_id where i.id = '"+itemId+"'";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()){
                newMrp.setText(rs.getString("i.mrp"));
                availStock = rs.getFloat("i.stock");
                title.setText(rs.getString("i.title")+" ("+rs.getString("c.title")+")");
                if(rs.getInt("i.itemType")==0){
                    mrp.setText("₹ "+rs.getString("i.mrp")+"/kg");
                    newMrpLabel.setText("New Price (per kg):");
                    stockLabel.setText("Available Quantity:");
                    stock.setText(rs.getString("i.stock")+" kg");
                }
                else {
                    mrp.setText("₹ "+rs.getString("i.mrp")+"/unt");
                    newMrpLabel.setText("New Price (per unt):");
                    stockLabel.setText("Available in stock:");
                    stock.setText(rs.getString("i.stock")+" unt");
                }
                newPriceTextFieldProperty();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void newPriceTextFieldProperty(){
        newMrp.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,10}([\\.]\\d{0,2})?")) {
                newMrp.setText(oldValue);
            }

        });
    }
    public void UpdatePrice(){

        if(!newMrp.getText().equals("")  && 0<Float.parseFloat(newMrp.getText())){
            String query = "update items set mrp='"+newMrp.getText()+"' where id='"+itemId+"'";
            try {
                int return_result = dbConnection.executeQuery(query);
                if (return_result>0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information | InventoryMS");
                    alert.setHeaderText("Item price updated Successfully");
                    alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
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
            alert.setHeaderText("Re-enter new price");
            alert.setContentText("New Price fields must not have 0 or null value");
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

