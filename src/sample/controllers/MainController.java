package sample.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.DbConnection;
import sample.models.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;


public class MainController extends AbstractController implements Initializable {
    public Button addCategoryBtn, addVendorBtn, addVendorOrderBtn, addOrderItemBtn, moveToStockBtn, clearSearchBtn,
    placeOrderBtn;
    public TextField searchField;
    public MenuItem themeBtn;
    public MenuItem logoutBtn;
    public Label totalAmt;

    @FXML
    private TableView<CategoryModel> categoriesTableView, shopCategoriesTableView;
    @FXML
    private TableColumn<CategoryModel, Integer> cat_id, shop_cat_id;
    @FXML
    private TableColumn<CategoryModel, String> cat_title, shop_cat_title;

    @FXML
    private TableView<VendorModel> vendorsTableView;
    @FXML
    private TableColumn<VendorModel, Integer> v_id;
    @FXML
    private TableColumn<VendorModel, String> v_title;

    @FXML
    private TableView<VendorOrderModel> vendorOrdersTableView;
    @FXML
    private TableColumn<VendorOrderModel, Integer> vo_id;
    @FXML
    private TableColumn<VendorOrderModel, String> vo_title, vo_status;

    @FXML
    private TableView<ItemModel> vendorOrdersItemTableView;
    @FXML
    private TableColumn<ItemModel, Integer> item_id;
    @FXML
    private TableColumn<ItemModel, String> item_title, item_stock, item_price, item_mrp, item_cat, itemType, item_initial;
    @FXML
    private TableView<CartsModel> ordersItemTableView;
    @FXML
    private TableColumn<CartsModel, Integer> order_item_id;
    @FXML
    private TableColumn<CartsModel, String> order_item_title, order_item_price, order_item_qty, order_item_amount;
    public GridPane shopItemsGrid;
    DbConnection dbConnection = new DbConnection();

    int x;
    int y;
    private void addButton(int id, String btnText) {
        final Button temp = new Button(btnText);
        temp.setId("" + id);
        temp.getStyleClass().add("item-special-button");
        temp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                AddToCart(Integer.parseInt(temp.getId()));
            }
        });
        shopItemsGrid.add(temp, x, y);
        if(x==7){
            x=0;
            y++;
        }
        else {
            x++;
        }

    }

    public void AddToCart(int id){
        int orderId=GetOrCreateOrder();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM order_items where item_id='"+id+"'and order_id='"+orderId+"'";
        Statement st;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){
                showUpdateCartPopup(id, orderId);
            }
            else {
                showAddToCartPopup(id, orderId);
            }
            showCartItems();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setItemsToGrid(0,"");
        showCategories();
        showCartItems();

        addVendorOrderBtn.setDisable(true);
        addOrderItemBtn.setDisable(true);
        moveToStockBtn.setDisable(true);
        showVendors();
        placeOrderBtn.setOnAction((event)->{PlaceOrder();});
        themeBtn.setOnAction((event)->{
            showThemesListPopup();
            Stage stage2 = (Stage) moveToStockBtn.getScene().getWindow();
            Parent root = stage2.getScene().getRoot();
            root.getStylesheets().clear();
            root.getStylesheets().add(dbConnection.getTheme());
        });
        logoutBtn.setOnAction((event)->{
            try {
                logout();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        moveToStockBtn.setOnAction((event)->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Are you sure to move this order in stock?");
            alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(yesButton, cancelButton);

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == yesButton)
            {
                VendorOrderModel p = vendorOrdersTableView.getSelectionModel().getSelectedItem();
                moveToStock(p.getId());
                showVendors();
            }
        });

        addCategoryBtn.setOnAction((event)->{
            HashMap<String, Object> resultMap = showCategoryPopupWindow();
            showCategories();
        });
        clearSearchBtn.setOnAction((event)->{
            searchField.setText("");
        });
        searchField.textProperty().addListener((event)->{
            setItemsToGrid(0,searchField.getText());
        });
        addVendorBtn.setOnAction((event)->{
            HashMap<String, Object> resultMap = showVendorPopupWindow();
            showVendors();
        });
        ordersItemTableView.setOnMouseClicked(mouseEvent -> {
            CartsModel p = ordersItemTableView.getSelectionModel().getSelectedItem();
            if (p!=null) {
                showUpdateCartPopup(p.getItem_id(), GetOrCreateOrder());
                showCartItems();
            }
        });
        shopCategoriesTableView.setOnMouseClicked(mouseEvent -> {
            CategoryModel p = shopCategoriesTableView.getSelectionModel().getSelectedItem();
            if(p != null) setItemsToGrid(p.getId(), "");

        });
        vendorsTableView.setOnMouseClicked(mouseEvent -> {
            VendorModel p = vendorsTableView.getSelectionModel().getSelectedItem();
            showVendorOrders(p.getId());
            addOrderItemBtn.setDisable(true);
            moveToStockBtn.setDisable(true);
            vendorOrdersItemTableView.setItems(null);
        });
        vendorOrdersTableView.setOnMouseClicked(mouseEvent -> {
            VendorOrderModel p = vendorOrdersTableView.getSelectionModel().getSelectedItem();
            if(p!=null){
                showVendorOrderItems(p.getId(), p.getInStock());
                addOrderItemBtn.setDisable(!p.getInStock().equals("In Order"));
            }
        });
        vendorOrdersItemTableView.setOnMouseClicked(mouseEvent -> {
            ItemModel p = vendorOrdersItemTableView.getSelectionModel().getSelectedItem();
            if(p!=null) showUpdateItemPopup(p.getId());
            VendorOrderModel pi = vendorOrdersTableView.getSelectionModel().getSelectedItem();
            if(pi!=null) showVendorOrderItems(pi.getId(), pi.getInStock());
        });
        addVendorOrderBtn.setOnAction((event)->{
            HashMap<String, Object> resultMap = showAddVendorOrderPopupWindow();
            VendorModel p = vendorsTableView.getSelectionModel().getSelectedItem();
            if(p != null && resultMap.get("orderTitle") != null){
                String query = "insert into vendor_orders(v_order_title, v_id) values('"+resultMap.get("orderTitle")+"','"+p.getId()+"')";
                dbConnection.executeQuery(query);
                showVendorOrders(p.getId());
            }
        });

        addOrderItemBtn.setOnAction((event)->{
            HashMap<String, Object> resultMap = showAddItemPopup();
            VendorOrderModel p = vendorOrdersTableView.getSelectionModel().getSelectedItem();
            if(p!=null) showVendorOrderItems(p.getId(), p.getInStock());
        });
    }

    public void logout() throws IOException {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/xml/login.fxml"));
        LoginController loginController = new LoginController();
        loginController.setMainApp();
        loader.setController(loginController);
        Parent layout = loader.load();
        Scene scene = new Scene(layout , 450, 320);
        scene.getRoot().getStylesheets().add(dbConnection.getTheme());
        primaryStage.setScene(scene);
        primaryStage.setTitle("InventoryMS | login panel");
        primaryStage.show();
        Stage stage2 = (Stage) moveToStockBtn.getScene().getWindow();
        stage2.close();
    }

    public void setItemsToGrid(int id, String search){
        x=0;
        y=0;
        shopItemsGrid.getChildren().clear();
        Connection connection = dbConnection.getConnection();
        String query;
        if(search.equals("")){
            if(id>0){
                query = "SELECT * FROM items i JOIN categories c on c.id = i.cat_id JOIN vendor_orders vo on (i.vo_id=vo.id and vo.inStock='1' and i.stock>0) where i.cat_id = '"+id+"'";
            }
            else {
                query = "SELECT * FROM items i JOIN categories c on c.id = i.cat_id JOIN vendor_orders vo on (i.vo_id=vo.id and vo.inStock='1' and i.stock>0)";
            }
        }
        else {
            query="SELECT * FROM items i JOIN categories c on c.id = i.cat_id JOIN vendor_orders vo on (i.vo_id=vo.id and vo.inStock='1' and i.stock>0) where" +
                    " i.title LIKE '%"+search+"%' or c.title LIKE '%"+search+"%' ";
        }
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

    public boolean hasItems(int id) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM items i JOIN categories c on c.id = i.cat_id where i.vo_id = '"+id+"'";
        Statement st;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            return rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    private HashMap<String, Object> showAddVendorOrderPopupWindow() {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../xml/AddNewOrderPopup.fxml"));
        AddVendorOrderPopup addVendorOrderController = new AddVendorOrderPopup();
        loader.setController(addVendorOrderController);
        Parent layout;
        try {
            layout = loader.load();
            Scene scene = new Scene(layout, 400, 280);
            scene.getRoot().getStylesheets().add(dbConnection.getTheme());
            Stage popupStage = new Stage();
            addVendorOrderController.setStage(popupStage);
            if(this.main!=null) {
                popupStage.initOwner(main.getPrimaryStage());
            }
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setTitle("Add Vendor | InventoryMS");
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addVendorOrderController.getResult();
    }

    private HashMap<String, Object> showAddItemPopup() {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../xml/AddItemPopup.fxml"));
        AddItemPopup addItemPopup = new AddItemPopup();
        loader.setController(addItemPopup);
        Parent layout;
        try {
            layout = loader.load();
            Scene scene = new Scene(layout, 550, 650);
            scene.getRoot().getStylesheets().add(dbConnection.getTheme());
            Stage popupStage = new Stage();
            addItemPopup.setStage(popupStage);
            if(this.main!=null) {
                popupStage.initOwner(main.getPrimaryStage());
            }
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setTitle("Add Items | InventoryMS");
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.setScene(scene);
            VendorOrderModel p = vendorOrdersTableView.getSelectionModel().getSelectedItem();
            addItemPopup.orderId = p.getId();
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return addItemPopup.getResult();
    }

    private HashMap<String, Object> showUpdateItemPopup(int id) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../xml/ItemPopup.fxml"));
        ItemPopup itemPopup = new ItemPopup();
        itemPopup.itemId = id;
        loader.setController(itemPopup);
        Parent layout;
        try {
            layout = loader.load();
            Scene scene = new Scene(layout, 650, 450);
            scene.getRoot().getStylesheets().add(dbConnection.getTheme());
            Stage popupStage = new Stage();
            itemPopup.setStage(popupStage);
            if(this.main!=null) {
                popupStage.initOwner(main.getPrimaryStage());
            }
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setTitle("Update Items | InventoryMS");
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.setScene(scene);
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemPopup.getResult();
    }

    private HashMap<String, Object> showThemesListPopup() {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../xml/ThemeList.fxml"));
        ThemesList themesList = new ThemesList();
        loader.setController(themesList);
        Parent layout;
        try {
            layout = loader.load();
            Scene scene = new Scene(layout, 1800, 850);
            scene.getRoot().getStylesheets().add(dbConnection.getTheme());
            Stage popupStage = new Stage();
            themesList.setStage(popupStage);
            if(this.main!=null) {
                popupStage.initOwner(main.getPrimaryStage());
            }
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setTitle("Appearance | InventoryMS");
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.setScene(scene);
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return themesList.getResult();
    }

    private HashMap<String, Object> showCategoryPopupWindow() {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../xml/AddCategoryPopup.fxml"));
        AddCategoryPopup addCategoryPopup = new AddCategoryPopup();
        loader.setController(addCategoryPopup);
        Parent layout;
        try {
            layout = loader.load();
            Scene scene = new Scene(layout, 500, 350);
            scene.getRoot().getStylesheets().add(dbConnection.getTheme());
            Stage popupStage = new Stage();
            addCategoryPopup.setStage(popupStage);
            if(this.main!=null) {
                popupStage.initOwner(main.getPrimaryStage());
            }
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setTitle("Add Category | InventoryMS");
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addCategoryPopup.getResult();
    }

    private HashMap<String, Object> showVendorPopupWindow() {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../xml/VendorPopup.fxml"));
        AddVendorPopup popupController = new AddVendorPopup();
        loader.setController(popupController);
        Parent layout;
        try {
            layout = loader.load();
            Scene scene = new Scene(layout, 600, 500);
            scene.getRoot().getStylesheets().add(dbConnection.getTheme());
            Stage popupStage = new Stage();
            popupController.setStage(popupStage);
            if(this.main!=null) {
                popupStage.initOwner(main.getPrimaryStage());
            }
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setTitle("Add Vendor | InventoryMS");
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return popupController.getResult();
    }
    private HashMap<String, Object> showAddToCartPopup(int itemId, int orderId) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../xml/AddToCartPopup.fxml"));
        AddToCartPopup popupController = new AddToCartPopup();
        loader.setController(popupController);
        popupController.itemId = itemId;
        popupController.orderId = orderId;
        Parent layout;
        try {
            layout = loader.load();
            Scene scene = new Scene(layout, 500, 350);
            scene.getRoot().getStylesheets().add(dbConnection.getTheme());
            Stage popupStage = new Stage();
            popupController.setStage(popupStage);
            if(this.main!=null) {
                popupStage.initOwner(main.getPrimaryStage());
            }
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setTitle("Set Quantity | InventoryMS");
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return popupController.getResult();
    }
    private HashMap<String, Object> showUpdateCartPopup(int itemId, int orderId) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../xml/UpdateCartItemPopup.fxml"));
        UpdateCartItem popupController = new UpdateCartItem();
        loader.setController(popupController);
        popupController.itemId = itemId;
        popupController.orderId = orderId;
        Parent layout;
        try {
            layout = loader.load();
            Scene scene = new Scene(layout, 500, 350);
            scene.getRoot().getStylesheets().add(dbConnection.getTheme());
            Stage popupStage = new Stage();
            popupController.setStage(popupStage);
            if(this.main!=null) {
                popupStage.initOwner(main.getPrimaryStage());
            }
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setTitle("Update Quantity | InventoryMS");
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return popupController.getResult();
    }

    public ObservableList<VendorModel> getVendorsList(){
        ObservableList<VendorModel> vendorsList = FXCollections.observableArrayList();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM vendors";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            VendorModel vendors;
            while(rs.next()) {
                vendors = new VendorModel(rs.getInt("Id"),rs.getString("Title"),rs.getString("Contact"),
                        rs.getString("Email"),rs.getString("City"), rs.getString("Address"));
                vendorsList.add(vendors);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vendorsList;
    }

    public ObservableList<CategoryModel> getCategoriesList(boolean state){
        ObservableList<CategoryModel> categoryModels = FXCollections.observableArrayList();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM categories";
        Statement st;
        ResultSet rs;
        CategoryModel categoryModel;
        if(state){
            categoryModel = new CategoryModel(0,"All");
            categoryModels.add(categoryModel);
        }
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

    public ObservableList<VendorOrderModel> getVendorOrdersList(int id){
        addVendorOrderBtn.setDisable(false);
        ObservableList<VendorOrderModel> vendorOrdersList = FXCollections.observableArrayList();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM vendor_orders where v_id = '"+id+"'";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            VendorOrderModel vendorOrders;
            while(rs.next()) {
                vendorOrders = new VendorOrderModel(
                        rs.getInt("id"),
                        rs.getString("v_order_title"),
                        rs.getInt("inStock")
                );
                vendorOrdersList.add(vendorOrders);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vendorOrdersList;
    }

    public ObservableList<ItemModel> getVendorOrdersItemList(int id){
        ObservableList<ItemModel> vendorOrdersItemList = FXCollections.observableArrayList();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM items i JOIN categories c on c.id = i.cat_id where i.vo_id = '"+id+"'";
        Statement st;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            ItemModel itemModel;
            while(rs.next()) {
                itemModel = new ItemModel(
                        rs.getInt("i.id"),
                        rs.getString("i.title"),
                        rs.getString("stock"),
                        rs.getString("price"),
                        rs.getString("mrp"),
                        rs.getInt("itemType"),
                        rs.getString("c.title"),
                        rs.getString("initial")
                        );
                vendorOrdersItemList.add(itemModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vendorOrdersItemList;
    }
    public ObservableList<CartsModel> getCartItemsList(int id){
        ObservableList<CartsModel> cartsModels = FXCollections.observableArrayList();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM order_items oi JOIN items i on oi.item_id=i.id JOIN categories c on c.id = i.cat_id where oi.order_id = '"+id+"'";
        Statement st;
        ResultSet rs;
        double total = 0;
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
                total+=cartsModel.getPayable();
            }
            totalAmt.setText("₹ "+ total);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartsModels;
    }

    public void showCategories() {
        ObservableList<CategoryModel> list = getCategoriesList(false);
        cat_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        cat_title.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoriesTableView.setItems(list);


        ObservableList<CategoryModel> list1 = getCategoriesList(true);
        shop_cat_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        shop_cat_title.setCellValueFactory(new PropertyValueFactory<>("category"));
        shopCategoriesTableView.setItems(list1);
    }

    public void showVendors() {
        ObservableList<VendorModel> list = getVendorsList();
        v_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        v_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        vendorsTableView.setItems(list);
    }

    public void showVendorOrders(int id) {
        ObservableList<VendorOrderModel> list = getVendorOrdersList(id);
        vo_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        vo_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        vo_status.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        vendorOrdersTableView.setItems(list);
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

    public void showVendorOrderItems(int id, String stockStatus) {
        ObservableList<ItemModel> list = getVendorOrdersItemList(id);
        item_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        item_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        item_cat.setCellValueFactory(new PropertyValueFactory<>("category"));
        itemType.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        item_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        item_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        item_mrp.setCellValueFactory(new PropertyValueFactory<>("mrp"));
        item_initial.setCellValueFactory(new PropertyValueFactory<>("initial"));
        vendorOrdersItemTableView.setItems(list);

        moveToStockBtn.setDisable(true);
        try {
            if(hasItems(id) && stockStatus.equals("In Order")){
                moveToStockBtn.setDisable(false);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void moveToStock(int id){
        String query = "UPDATE items set items.initial=items.stock where vo_id='"+id+"'";
        String query1 = "UPDATE vendor_orders set inStock='1' where id='"+id+"'";
        try {
            int return_result1 = dbConnection.executeQuery(query1);
            int return_result = dbConnection.executeQuery(query);
            if (return_result>0 && return_result1>0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("information");
                alert.setHeaderText("Order Successfully Moved to stock");
                alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        addOrderItemBtn.setDisable(true);
                        moveToStockBtn.setDisable(true);
                        VendorModel p = vendorsTableView.getSelectionModel().getSelectedItem();
                        showVendorOrders(p.getId());
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
    }
    public void PlaceOrder(){
        int order_id = GetOrCreateOrder();
        String query = "UPDATE order_items oi JOIN items i on oi.item_id=i.id set i.stock=i.stock-oi.qty where oi.order_id='"+order_id+"'";
        dbConnection.executeQuery(query);
        query = "UPDATE shop_orders so  set so.status='1' where id='"+order_id+"'";
        dbConnection.executeQuery(query);
        setItemsToGrid(0,"");
        ordersItemTableView.setItems(null);
        totalAmt.setText("0");
    }

}
