package sample.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sample.DbConnection;
import sample.models.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;


public class ThemesList extends AbstractController implements Initializable {
    @FXML
    private TableView<CartsModel> ordersItemTableView;
    @FXML
    private TableColumn<CartsModel, Integer> order_item_id;
    @FXML
    private TableColumn<CartsModel, String> order_item_title, order_item_price, order_item_qty, order_item_amount;

    @FXML
    private TableView<CategoryModel> shopCategoriesTableView;
    @FXML
    private TableColumn<CategoryModel, Integer> shop_cat_id;
    @FXML
    private TableColumn<CategoryModel, String> shop_cat_title;

    @FXML
    private TableView<ThemesModel> themeTable;
    @FXML
    private TableColumn<ThemesModel, String> theme;

    private final HashMap<String, Object> result = new HashMap<String, Object>();
    private Stage stage = null;
    DbConnection dbConnection = new DbConnection();
    public Button applyBtn;
    public GridPane shopItemsGrid;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public HashMap<String, Object> getResult() {
        return this.result;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showThemes();
        setItemsToGrid(0,"");
        showCategories();
        showCartItems();
        applyBtn.setOnAction((event)->{
            ThemesModel p = themeTable.getSelectionModel().getSelectedItem();
            String query = "UPDATE appearance set themeFile='"+p.getTitle()+".css"+"' where id='1'";
            dbConnection.executeQuery(query);
            closeStage();
        });
        themeTable.setOnMouseClicked(mouseEvent -> {
            ThemesModel p = themeTable.getSelectionModel().getSelectedItem();
            if(p!=null){
                Stage stage2 = (Stage) themeTable.getScene().getWindow();
                Parent root = stage2.getScene().getRoot();
                root.getStylesheets().clear();
                root.getStylesheets().add("/sample/themes/"+p.getTitle()+".css");
            }
        });
    }

    int x;
    int y;
    private void addButton(int id, String btnText) {
        final Button temp = new Button(btnText);
        temp.setId("" + id);
        temp.getStyleClass().add("item-special-button");
        shopItemsGrid.add(temp, x, y);
        if(x==5){
            x=0;
            y++;
        }
        else {
            x++;
        }

    }
    public void setItemsToGrid(int id, String search){
        x=0;
        y=0;
        shopItemsGrid.getChildren().clear();
        Connection connection = dbConnection.getConnection();
        String query;
        query = "SELECT * FROM items i JOIN categories c on c.id = i.cat_id JOIN vendor_orders vo on (i.vo_id=vo.id and vo.inStock='1' and i.stock>0)";
        Statement st;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            String itemType;
            String stockRem;
            while (rs.next()){
                if(rs.getInt("itemType")==0){
                    itemType="/kg";
                    stockRem = rs.getString("i.stock")+" kgs rem.";
                }else {
                    itemType="/unit";
                    stockRem = Math.round(rs.getFloat("i.stock"))+" unts\n rem.";
                }
                addButton(rs.getInt("i.id"), rs.getString("i.title")+"\n("+
                        rs.getString("c.title")+")"+"\n₹ "+
                        rs.getString("i.mrp")+itemType+"\n\n"+stockRem);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ObservableList<ThemesModel> getList(){
        ObservableList<ThemesModel> themesModels = FXCollections.observableArrayList();
        ThemesModel themesModel;
        String[] ar = { "light-theme", "dark-theme", "dark-theme-stripped", "dark-standard", "dark-fabulous-orange",
                "dark-chocolate", "material-red", "material-yellow", "material-blue-dark"};
        int i;
        for (i = 0; i < ar.length; i++) {
            themesModel = new ThemesModel(1,ar[i]);
            themesModels.add(themesModel);
        }
        return themesModels;
    }

    public void showThemes() {
        ObservableList<ThemesModel> list = getList();
        theme.setCellValueFactory(new PropertyValueFactory<>("title"));
        themeTable.setItems(list);
    }
    private void closeStage() {
        if(stage!=null) {
            stage.close();
        }
    }
    public void showCategories() {
        ObservableList<CategoryModel> list1 = getCategoriesList();
        shop_cat_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        shop_cat_title.setCellValueFactory(new PropertyValueFactory<>("category"));
        shopCategoriesTableView.setItems(list1);
    }
    public ObservableList<CategoryModel> getCategoriesList(){
        ObservableList<CategoryModel> categoryModels = FXCollections.observableArrayList();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM categories";
        Statement st;
        ResultSet rs;
        CategoryModel categoryModel;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            while(rs.next()) {
                categoryModel = new CategoryModel(rs.getInt("id"),rs.getString("title"));
                categoryModels.add(categoryModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryModels;
    }

    public void showCartItems() {
        ObservableList<CartsModel> list = getCartItemsList(GetOrCreateOrder());
        order_item_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        order_item_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        order_item_price.setCellValueFactory(new PropertyValueFactory<>("mrp"));
        order_item_qty.setCellValueFactory(new PropertyValueFactory<>("stock"));
        order_item_amount.setCellValueFactory(new PropertyValueFactory<>("payable"));
        ordersItemTableView.setItems(list);
    }
    public ObservableList<CartsModel> getCartItemsList(int id){
        ObservableList<CartsModel> cartsModels = FXCollections.observableArrayList();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM order_items oi JOIN items i on oi.item_id=i.id JOIN categories c on c.id = i.cat_id where oi.order_id = '"+id+"'";
        Statement st;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            CartsModel cartsModel;
            while(rs.next()) {
                cartsModel = new CartsModel(
                        rs.getInt("oi.id"),
                        rs.getInt("i.id"),
                        rs.getString("i.title"),
                        rs.getFloat("oi.qty"),
                        rs.getFloat("i.mrp"),
                        rs.getInt("i.itemType"),
                        rs.getString("c.title")
                );
                cartsModels.add(cartsModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartsModels;
    }
    public int GetOrCreateOrder(){
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM shop_orders where status='0'";
        Statement st;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){
                return rs.getInt("id");
            }
            else {
                query = "insert into shop_orders(status) values('0')";
                int return_result = dbConnection.executeQuery(query);
                if(return_result>0) {
                    query = "SELECT * FROM shop_orders where status='0'";
                    rs = st.executeQuery(query);
                    if(rs.next()){
                        return rs.getInt("id");
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
        return 0;
    }

}

