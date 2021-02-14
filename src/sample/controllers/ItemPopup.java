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

public class ItemPopup extends AbstractController implements Initializable {
    public Button updateItemBtn;
    public Button deleteBtn;
    public int itemId;
    public TextField cat;
    public TextField itemType;
    public Label title;
    public TextField mrp;
    public TextField price;
    public TextField stock;
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
        updateItemBtn.setOnAction((event)->{
//            insertItem();
        });
        mrp.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,10}([\\.]\\d{0,2})?")) {
                    mrp.setText(oldValue);
                }
            }
        });
        price.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,10}([\\.]\\d{0,2})?")) {
                    price.setText(oldValue);
                }
            }
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
                System.out.println();
                if(rs.getInt("vo.inStock")==0){
                    mrp.setEditable(true);
                    price.setEditable(true);
                    stock.setEditable(true);
                    updateItemBtn.setDisable(false);
                    deleteBtn.setDisable(false);

                    mrp.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            if (!newValue.matches("\\d{0,10}([\\.]\\d{0,2})?")) {
                                mrp.setText(oldValue);
                            }
                        }
                    });
                    price.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            if (!newValue.matches("\\d{0,10}([\\.]\\d{0,2})?")) {
                                price.setText(oldValue);
                            }
                        }
                    });
                    StockTextFieldProperty(rs.getInt("itemType") != 0);
                }
                else {
                    mrp.setEditable(false);
                    price.setEditable(false);
                    stock.setEditable(false);
                    updateItemBtn.setDisable(true);
                    deleteBtn.setDisable(true);
                }

                ItemModel itemModel = new ItemModel(rs.getInt("i.id"), rs.getString("i.title"),
                        rs.getString("stock"), rs.getString("price"), rs.getString("mrp"),
                        rs.getInt("itemType"), rs.getString("c.title"));
                title.setText(itemModel.getTitle());
                cat.setText(itemModel.getCategory());
                mrp.setText(String.valueOf(itemModel.getMrp()));
                price.setText(String.valueOf(itemModel.getPrice()));
                stock.setText(String.valueOf(itemModel.getStock()));
                itemType.setText(itemModel.getItemType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void StockTextFieldProperty(boolean toInt){
        stock.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!toInt){
                    if (!newValue.matches("\\d{0,10}([\\.]\\d{0,3})?")) {
                        stock.setText(oldValue);
                    }
                }else {
                    if (!newValue.matches("\\d*")) {
                        stock.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }

            }
        });
    }
    public void insertItem(){

//        if(cat.getValue()!=null && !title.getText().equals("") && !stock.getText().equals("")){
//            int varItemType = 0;
//            String varStock;
//            if(itemType.getValue().equals("By Unit")){
//                varItemType = 1;
//                varStock = String.valueOf(Integer.parseInt(stock.getText()));
//            }else {
//                varStock = String.valueOf(Float.parseFloat(stock.getText()));
//            }
//            int cat_id = 0;
//            String query = "SELECT * FROM categories where title = '"+cat.getValue()+"'";
//            Statement st;
//            ResultSet resultSet;
//            try {
//                st = connection.createStatement();
//                resultSet = st.executeQuery(query);
//                while(resultSet.next()) {
//                    cat_id = resultSet.getInt("id");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Alert alert = new Alert(Alert.AlertType.WARNING);
//                alert.setTitle("Internal Error");
//                alert.setHeaderText("Internal Error");
//                alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
//                alert.show();
//            }
//            try {
//                String query1 = "insert into items(cat_id, vo_id, title, mrp, price, stock, itemType)" +
//                        " values('"+cat_id+"','"+orderId+"','"+title.getText()+"'," +
//                        "'"+mrp.getText()+"','"+price.getText()+"', '"+varStock+"','"+varItemType+"')";
//                int return_result = dbConnection.executeQuery(query1);
//                if (return_result>0){
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                    alert.setTitle("Item Added Successfully");
//                    alert.setHeaderText("Item Added Successfully");
//                    alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
//                    alert.showAndWait().ifPresent(rs -> {
//                        if (rs == ButtonType.OK) {
//                            cat.getSelectionModel().clearSelection();
//                            title.setText("");
//                            mrp.setText("0");
//                            price.setText("0");
//                            stock.setText("");
//                            itemType.getSelectionModel().selectFirst();
//                        }
//                    });
//                }
//                else {
//                    Alert alert = new Alert(Alert.AlertType.WARNING);
//                    alert.setTitle("Internal Error");
//                    alert.setHeaderText("Internal Error");
//                    alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
//                    alert.show();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Alert alert = new Alert(Alert.AlertType.WARNING);
//                alert.setTitle("Internal Error");
//                alert.setHeaderText("Internal Error");
//                alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
//                alert.show();
//            }
//
//        }else {
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("item failed to save");
//            alert.setHeaderText("Please fills all mandatory fields");
//            alert.setContentText("Recheck again category, title and amt/qty fields");
//            alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
//            alert.show();
//        }
    }
    private void closeStage() {
        if(stage!=null) {
            stage.close();
        }
    }
}
