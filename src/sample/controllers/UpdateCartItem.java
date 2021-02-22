
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

public class UpdateCartItem extends AbstractController implements Initializable {

    public int itemId;
    public int cartItemId;
    public int orderId;
    public float availStock;

    public Button updateCartItemBtn, deleteBtn, closeBtn, updatePriceBtn;
    public TextField qty;
    public Label title, mrp,stockLabel,stock,qtyLabel;
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
        updateCartItemBtn.setOnAction((event)->{
            updateCartItem();
        });
        deleteBtn.setOnAction((event)->{
            deleteItem();
        });
        closeBtn.setOnAction((event)->{
            closeStage();
        });
        updatePriceBtn.setOnAction((event)->{
            UpdatePrice();
        });
    }
    public void setForm(){
        String query = "SELECT * FROM order_items oi JOIN items i on i.id = oi.item_id JOIN categories c on c.id = i.cat_id JOIN vendor_orders vo on vo.id = i.vo_id" +
                " where oi.item_id = '"+itemId+"' and oi.order_id='"+orderId+"'";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()){
                cartItemId=rs.getInt("oi.id");
                qty.setText(rs.getString("oi.qty"));
                availStock = rs.getFloat("i.stock");
                title.setText(rs.getString("i.title")+" ("+rs.getString("c.title")+")");
                if(rs.getInt("i.itemType")==0){
                    System.out.println(rs.getString("oi.qty"));
                    qty.setText(rs.getString("oi.qty"));
                    mrp.setText("₹ "+rs.getString("i.mrp")+"/kg");
                    qtyLabel.setText("Quantity (in kg):");
                    stockLabel.setText("Available Quantity:");
                    stock.setText(rs.getString("i.stock")+" kg");
                    qtyTextFieldProperty(false);
                }
                else {
                    qty.setText(String.valueOf(Math.round(rs.getFloat("oi.qty"))));
                    mrp.setText("₹ "+rs.getString("i.mrp")+"/unt");
                    qtyLabel.setText("Unts:");
                    stockLabel.setText("Available in stock:");
                    stock.setText(rs.getString("i.stock")+" unt");
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
    public void updateCartItem(){

        if(!qty.getText().equals("") && Float.parseFloat(qty.getText())<=availStock && 0<Float.parseFloat(qty.getText())){
            System.out.println("ok");
            System.out.println(Float.parseFloat(qty.getText()));

            String query = "update order_items set qty='"+qty.getText()+"' where id='"+cartItemId+"'";
            try {
                int return_result = dbConnection.executeQuery(query);
                if (return_result>0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information | InventoryMS");
                    alert.setHeaderText("Item updated Successfully");
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
            alert.setHeaderText("Re-enter quantity");
            alert.setContentText("Quantity fields must not have 0 or null value and must be lesser than available quantity in stock");
            alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
            alert.show();
        }
    }
        public void deleteItem(){
        String query = "DELETE FROM order_items where id='"+cartItemId+"'";
        try {
            int return_result = dbConnection.executeQuery(query);
            if (return_result>0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information | InventoryMS");
                alert.setHeaderText("Item removed from Successfully");
                alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        closeStage();
                    }
                });
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning | InventoryMS");
                alert.setHeaderText("Internal Error");
                alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning | InventoryMS");
            alert.setHeaderText("Internal Error");
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

