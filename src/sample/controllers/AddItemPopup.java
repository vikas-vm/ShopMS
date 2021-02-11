package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.DbConnection;
import sample.models.CategoryModel;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AddItemPopup extends AbstractController implements Initializable {
    public Button insertVendorBtn;
    public Text warning;
    public int orderId;
    public ComboBox<String> cat;
    public ComboBox<String> itemType;
    public TextField title;
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

        setCategory();
        insertVendorBtn.setOnAction((event)->{
            if(cat.getValue()!=null && !title.getText().equals("") && !stock.getText().equals("")){
                int varItemType = 0;
                String varStock;
                if(itemType.getValue().equals("By Unit")){
                    varItemType = 1;
                    varStock = String.valueOf(Integer.parseInt(stock.getText()));
                }else {
                    varStock = String.valueOf(Float.parseFloat(stock.getText()));
                }
                int cat_id = 0;
                String query = "SELECT * FROM categories where title = '"+cat.getValue()+"'";
                Statement st;
                ResultSet resultSet;
                try {
                    st = connection.createStatement();
                    resultSet = st.executeQuery(query);
                    while(resultSet.next()) {
                        cat_id = resultSet.getInt("id");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                String query1 = "insert into items(cat_id, vo_id, title, mrp, price, stock, itemType)" +
                        " values('"+cat_id+"','"+orderId+"','"+title.getText()+"'," +
                        "'"+mrp.getText()+"','"+price.getText()+"', '"+varStock+"','"+varItemType+"')";
                int return_result = dbConnection.executeQuery(query1);
                if (return_result>0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Item Added Successfully");
                    alert.setHeaderText("Item Added Successfully");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            cat.getSelectionModel().clearSelection();
                            title.setText("");
                            mrp.setText("");
                            price.setText("");
                            stock.setText("");
                            itemType.getSelectionModel().selectFirst();
                        }
                    });
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Internal Error");
                    alert.setHeaderText("Internal Error");
                    alert.show();
                }

            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("item failed to save");
                alert.setHeaderText("Please fills all mandatory fields");
                alert.setContentText("Recheck again category, title and amt/qty fields");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });
            }
        });

    }

    public ObservableList<CategoryModel> getCategoryList(){
        ObservableList<CategoryModel> categoryList = FXCollections.observableArrayList();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM categories";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            CategoryModel categoryModel;
            while(rs.next()) {
                categoryModel = new CategoryModel(
                        rs.getInt("id"),
                        rs.getString("title")
                );
                categoryList.add(categoryModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryList;
    }
    public void setCategory(){

        String query = "SELECT * FROM categories";
        Statement st;
        ResultSet resultSet;
        itemType.getSelectionModel().selectFirst();

        try {
            st = connection.createStatement();
            resultSet = st.executeQuery(query);
            while(resultSet.next()) {
                cat.getItems().add(resultSet.getString("title"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeStage() {
        if(stage!=null) {
            stage.close();
        }
    }
}
