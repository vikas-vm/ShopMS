package sample.controllers;
import javafx.application.Platform;
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
import javafx.stage.StageStyle;
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
    placeOrderBtn, reloadBtn;
    public TextField searchField;
    public MenuItem themeBtn, logoutBtn, exitMenu;
    public Label totalAmt;

    @FXML
    private TableView<CategoryModel> categoriesTableView, shopCategoriesTableView, StockCategoriesTableView;
    @FXML
    private TableColumn<CategoryModel, Integer> cat_id;
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
    @FXML
    private TableView<OrderModel> OrderTableView;
    @FXML
    private TableColumn<OrderModel, Integer> o_id;
    @FXML
    private TableColumn<OrderModel, String> order_c_name, order_total_amt, order_paid_amt;
    @FXML
    private TableColumn<CategoryModel, String> stock_cat_title;
    public GridPane shopItemsGrid;
    DbConnection dbConnection = new DbConnection();
    Connection connection = dbConnection.getConnection();

    int x;
    int y;
    float totalAmount;

    public MainController() {
    }

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
            HashMap<String, Object> result;
            if(rs.next()){
                result = showUpdateCartPopup(id, orderId);
            }
            else {
                result = showAddToCartPopup(id, orderId);
            }
            if(result.get("updateState")!=null){
                showLoginPopup();
                showUpdatePricePopupWindow(id);
                loadShop();
                AddToCart(id);
            }
            showCartItems();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            logout();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addVendorOrderBtn.setDisable(true);
        addOrderItemBtn.setDisable(true);
        moveToStockBtn.setDisable(true);
        showVendors();
        loadShop();
        PaymentsInitialize();
        StocksInitialize();
        reloadBtn.setOnAction(event->{
            loadShop();
        });
        placeOrderBtn.setOnAction((event)->{
            showProceedToOrderPopup();
            loadShop();
            loadPayments();
        });
        exitMenu.setOnAction(event->{
            exit();
        });
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
            showLoginPopup();
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
                loadShop();
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
                HashMap<String, Object> result = showUpdateCartPopup(p.getItem_id(), GetOrCreateOrder());

                if(result.get("updateState")!=null){
                    showLoginPopup();
                    showUpdatePricePopupWindow(p.getItem_id());
                    loadShop();
                    AddToCart(p.getItem_id());
                }
                showCartItems();
            }
        });
        shopCategoriesTableView.setOnMouseClicked(mouseEvent -> {
            CategoryModel p = shopCategoriesTableView.getSelectionModel().getSelectedItem();
            if(p != null) setItemsToGrid(p.getId(), "");

        });
        vendorsTableView.setOnMouseClicked(mouseEvent -> {
            VendorModel p = vendorsTableView.getSelectionModel().getSelectedItem();
            if(p!=null){
                showVendorOrders(p.getId());
            }
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
    private void loadShop(){
        setItemsToGrid(0,"");
        showCategories();
        showCartItems();

    }
    public void logout() throws IOException {
        showLoginPopup();
    }

    public void setItemsToGrid(int id, String search){
        x=0;
        y=0;
        shopItemsGrid.getChildren().clear();
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
                    stockRem = rs.getString("stock")+" kgs rem.";
                }else {
                    itemType="/unit";
                    stockRem = Math.round(rs.getFloat("stock"))+" unts\n rem.";
                }
                addButton(rs.getInt("id"), rs.getString("title")+"\n("+
                        rs.getString("title")+")"+"\n₹ "+
                        rs.getString("mrp")+itemType+"\n\n"+stockRem);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int GetOrCreateOrder(){
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

    private HashMap<String, Object> showUpdatePricePopupWindow(int id) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../xml/UpdateItemPrice.fxml"));
        UpdateItemPrice updateItemPrice = new UpdateItemPrice();
        updateItemPrice.itemId = id;
        loader.setController(updateItemPrice);
        Parent layout;
        try {
            layout = loader.load();
            Scene scene = new Scene(layout, 650, 450);
            scene.getRoot().getStylesheets().add(dbConnection.getTheme());
            Stage popupStage = new Stage();
            updateItemPrice.setStage(popupStage);
            if(this.main!=null) {
                popupStage.initOwner(main.getPrimaryStage());
            }
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setTitle("Update Item Price | InventoryMS");
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return updateItemPrice.getResult();
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
    public void showLoginPopup(){

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/xml/login.fxml"));
        LoginController popupController = new LoginController();
        loader.setController(popupController);
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
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.setTitle("Set Quantity | InventoryMS");
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private HashMap<String, Object> showProceedToOrderPopup() {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../xml/ProceedToOrder.fxml"));
        ProceedToOrder popupController = new ProceedToOrder();
        loader.setController(popupController);
        popupController.orderId=GetOrCreateOrder();
        popupController.total = totalAmount;
        Parent layout;
        try {
            layout = loader.load();
            Scene scene = new Scene(layout, 1050, 750);
            scene.getRoot().getStylesheets().add(dbConnection.getTheme());
            Stage popupStage = new Stage();
            popupController.setStage(popupStage);
            if(this.main!=null) {
                popupStage.initOwner(main.getPrimaryStage());
            }
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setTitle("Proceed to order | InventoryMS");
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

    private void exit(){
        Platform.exit();
        System.exit(0);
    }
    public ObservableList<VendorModel> getVendorsList(){
        ObservableList<VendorModel> vendorsList = FXCollections.observableArrayList();
        String query = "SELECT * FROM vendors";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            VendorModel vendors;
            int index=1;
            while(rs.next()) {
                vendors = new VendorModel(index,rs.getInt("Id"),rs.getString("Title"),rs.getString("Contact"),
                        rs.getString("Email"),rs.getString("City"), rs.getString("Address"));
                vendorsList.add(vendors);
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vendorsList;
    }

    public ObservableList<CategoryModel> getCategoriesList(boolean state){
        ObservableList<CategoryModel> categoryModels = FXCollections.observableArrayList();
        String query = "SELECT * FROM categories";
        Statement st;
        ResultSet rs;
        CategoryModel categoryModel;
        if(state){
            categoryModel = new CategoryModel(0,0,"All");
            categoryModels.add(categoryModel);
        }
        try{
            st = connection.createStatement();
            rs = st.executeQuery(query);
            int index=1;
            while(rs.next()) {
                categoryModel = new CategoryModel(index,rs.getInt("id"),rs.getString("title"));
                categoryModels.add(categoryModel);
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryModels;
    }

    public ObservableList<VendorOrderModel> getVendorOrdersList(int id){
        addVendorOrderBtn.setDisable(false);
        ObservableList<VendorOrderModel> vendorOrdersList = FXCollections.observableArrayList();
        String query = "SELECT * FROM vendor_orders where v_id = '"+id+"' order by id desc";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            VendorOrderModel vendorOrders;
            int index=1;
            while(rs.next()) {
                vendorOrders = new VendorOrderModel(
                        index,
                        rs.getInt("id"),
                        rs.getString("v_order_title"),
                        rs.getInt("inStock")
                );
                index++;
                vendorOrdersList.add(vendorOrders);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vendorOrdersList;
    }
    public ObservableList<OrderModel> getOrdersList(int id){
        ObservableList<OrderModel> orderModels = FXCollections.observableArrayList();
        String query;
        if(id>0){
            query = "SELECT * FROM shop_orders so join customers c on c.id = so.cust_id join payments p on p.order_id=so.id where c.id = '"+id+"' order by so.id desc";
        }
        else {
            query = "SELECT * FROM shop_orders so join customers c on c.id = so.cust_id join payments p on p.order_id=so.id order by so.id desc";
        }
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            OrderModel orderModel;
            int index=1;
            while(rs.next()) {
                orderModel = new OrderModel(
                        index,
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getFloat("total_amt"),
                        rs.getFloat("amount")
                );
                index++;
                orderModels.add(orderModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderModels;
    }

    public ObservableList<ItemModel> getVendorOrdersItemList(int id){
        ObservableList<ItemModel> vendorOrdersItemList = FXCollections.observableArrayList();
        String query = "SELECT * FROM items i JOIN categories c on c.id = i.cat_id where i.vo_id = '"+id+"'";
        Statement st;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            ItemModel itemModel;
            int index=1;
            while(rs.next()) {
                itemModel = new ItemModel(
                        index,
                        rs.getInt("i.id"),
                        rs.getString("i.title"),
                        rs.getString("stock"),
                        rs.getString("price"),
                        rs.getString("mrp"),
                        rs.getInt("itemType"),
                        rs.getString("c.title"),
                        rs.getString("initial")
                        );
                index++;
                vendorOrdersItemList.add(itemModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vendorOrdersItemList;
    }
    public ObservableList<CartsModel> getCartItemsList(int id){
        placeOrderBtn.setDisable(true);
        ObservableList<CartsModel> cartsModels = FXCollections.observableArrayList();
        String query = "SELECT * FROM order_items oi JOIN items i on oi.item_id=i.id JOIN categories c on c.id = i.cat_id where oi.order_id = '"+id+"'";
        Statement st;
        ResultSet rs;
        float total = 0, haveVal=0;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            CartsModel cartsModel;
            int index=1;
            while(rs.next()) {
                cartsModel = new CartsModel(
                        index,
                        rs.getInt("oi.id"),
                        rs.getInt("i.id"),
                        rs.getString("i.title"),
                        rs.getFloat("oi.qty"),
                        rs.getFloat("i.mrp"),
                        rs.getInt("i.itemType"),
                        rs.getString("c.title")
                        );
                index++;
                cartsModels.add(cartsModel);
                total+=cartsModel.getPayable();
                haveVal=1;
            }
            if(haveVal==1){
                placeOrderBtn.setDisable(false);
            }
            totalAmount=total;
            totalAmt.setText("₹ "+ total);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartsModels;
    }

    public void showCategories() {
        ObservableList<CategoryModel> list = getCategoriesList(false);
        cat_id.setCellValueFactory(new PropertyValueFactory<>("index"));
        cat_title.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoriesTableView.setItems(list);


        ObservableList<CategoryModel> list1 = getCategoriesList(true);
        shop_cat_title.setCellValueFactory(new PropertyValueFactory<>("category"));
        shopCategoriesTableView.setItems(list1);

        ObservableList<CategoryModel> list2 = getCategoriesList(true);
        stock_cat_title.setCellValueFactory(new PropertyValueFactory<>("category"));
        StockCategoriesTableView.setItems(list2);
    }

    public void showVendors() {
        ObservableList<VendorModel> list = getVendorsList();
        v_id.setCellValueFactory(new PropertyValueFactory<>("index"));
        v_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        vendorsTableView.setItems(list);
    }
    public void showOrders(int id) {
        ObservableList<OrderModel> list = getOrdersList(id);
        o_id.setCellValueFactory(new PropertyValueFactory<>("index"));
        order_c_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        order_total_amt.setCellValueFactory(new PropertyValueFactory<>("total"));
        order_paid_amt.setCellValueFactory(new PropertyValueFactory<>("paid"));
        OrderTableView.setItems(list);
    }

    public void showVendorOrders(int id) {
        ObservableList<VendorOrderModel> list = getVendorOrdersList(id);
        vo_id.setCellValueFactory(new PropertyValueFactory<>("index"));
        vo_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        vo_status.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        vendorOrdersTableView.setItems(list);
    }

    public void showCartItems() {
        ObservableList<CartsModel> list = getCartItemsList(GetOrCreateOrder());
        order_item_id.setCellValueFactory(new PropertyValueFactory<>("index"));
        order_item_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        order_item_price.setCellValueFactory(new PropertyValueFactory<>("mrp"));
        order_item_qty.setCellValueFactory(new PropertyValueFactory<>("stock"));
        order_item_amount.setCellValueFactory(new PropertyValueFactory<>("payable"));
        ordersItemTableView.setItems(list);
    }

    public void showVendorOrderItems(int id, String stockStatus) {
        ObservableList<ItemModel> list = getVendorOrdersItemList(id);
        item_id.setCellValueFactory(new PropertyValueFactory<>("index"));
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

    //Manage Payments

    @FXML
    private TableView<CustomerModel> customersTableView;
    @FXML
    private TableView<CartsModel> OrderItemsTableView;
    @FXML
    private TableColumn<CustomerModel, Integer> id;
    @FXML
    private TableColumn<CartsModel, Integer> oi_id;
    @FXML
    private TableColumn<CustomerModel, String> customer_name, customer_contact, customer_address, customer_due;
    @FXML
    private TableColumn<CartsModel, String> oi_title, oi_qty, oi_price, oi_payable;
    public Button clearCustomerSearchBtn;
    public TextField searchCustomerField;
    public Button clearOrderSelectionBtn, newPaymentBtn;

    public HashMap<String, Object> NewPaymentRecord(int id, float due){
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../xml/NewPaymentRecord.fxml"));
        NewPaymentRecord popupController = new NewPaymentRecord();
        loader.setController(popupController);
        popupController.customer_id = id;
        popupController.total_due = due;
        Parent layout;
        try {
            layout = loader.load();
            Scene scene = new Scene(layout, 600, 350);
            scene.getRoot().getStylesheets().add(dbConnection.getTheme());
            Stage popupStage = new Stage();
            popupController.setStage(popupStage);
            if(this.main!=null) {
                popupStage.initOwner(main.getPrimaryStage());
            }
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setTitle("New Payments Record | InventoryMS");
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return popupController.getResult();
    }
    public void PaymentsInitialize(){
        loadPayments();
        newPaymentBtn.setDisable(true);
        searchCustomerField.textProperty().addListener((event)->{
            showCustomers(searchCustomerField.getText());
        });
        clearCustomerSearchBtn.setOnAction(event->{
            searchCustomerField.setText("");
        });
        newPaymentBtn.setOnAction(event->{
            showLoginPopup();
            CustomerModel p = customersTableView.getSelectionModel().getSelectedItem();
            if(p!=null){
                try {
                    NewPaymentRecord(p.getId(), p.getDue());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                showCustomers("");
                showOrders(p.getId());
                newPaymentBtn.setDisable(true);
                OrderItemsTableView.setItems(null);
            }
        });
        clearOrderSelectionBtn.setOnAction(event->{
            showOrders(0);
            newPaymentBtn.setDisable(true);
            OrderItemsTableView.setItems(null);
        });

        customersTableView.setOnMouseClicked(mouseEvent -> {
            CustomerModel p = customersTableView.getSelectionModel().getSelectedItem();
            if(p!=null){
                showOrders(p.getId());
                OrderItemsTableView.setItems(null);
                newPaymentBtn.setDisable(false);
            }
        });
        OrderTableView.setOnMouseClicked(mouseEvent -> {
            OrderModel p = OrderTableView.getSelectionModel().getSelectedItem();
            if(p!=null){
                showOrderItems(p.getId());
            }
        });
    }
    public void showCustomers(String search) {
        ObservableList<CustomerModel> list = getCustomersList(search);
        id.setCellValueFactory(new PropertyValueFactory<>("index"));
        customer_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        customer_contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        customer_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        customer_due.setCellValueFactory(new PropertyValueFactory<>("due"));
        customersTableView.setItems(list);
    }
    private void loadPayments(){
        showOrders(0);
        showCustomers("");
    }
    public void showOrderItems(int id) {
        ObservableList<CartsModel> list = getOrderItemsList(id);
        oi_id.setCellValueFactory(new PropertyValueFactory<>("index"));
        oi_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        oi_qty.setCellValueFactory(new PropertyValueFactory<>("stock"));
        oi_payable.setCellValueFactory(new PropertyValueFactory<>("payable"));
        oi_price.setCellValueFactory(new PropertyValueFactory<>("mrp"));
        OrderItemsTableView.setItems(list);
    }
    public ObservableList<CartsModel> getOrderItemsList(int id){
        placeOrderBtn.setDisable(true);
        ObservableList<CartsModel> cartsModels = FXCollections.observableArrayList();
        String query = "SELECT * FROM order_items oi JOIN items i on oi.item_id=i.id JOIN categories c on c.id = i.cat_id where oi.order_id = '"+id+"'";
        Statement st;
        ResultSet rs;
        float total = 0, haveVal=0;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            CartsModel cartsModel;
            int index=1;
            while(rs.next()) {
                cartsModel = new CartsModel(
                        index,
                        rs.getInt("oi.id"),
                        rs.getInt("i.id"),
                        rs.getString("i.title"),
                        rs.getFloat("oi.qty"),
                        rs.getFloat("oi.price"),
                        rs.getInt("i.itemType"),
                        rs.getString("c.title")
                );
                index++;
                cartsModels.add(cartsModel);
                total+=cartsModel.getPayable();
                haveVal=1;
            }
            if(haveVal==1){
                placeOrderBtn.setDisable(false);
            }
            totalAmount=total;
            totalAmt.setText("₹ "+ total);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartsModels;
    }
    public ObservableList<CustomerModel> getCustomersList(String search){
        ObservableList<CustomerModel> customerModels = FXCollections.observableArrayList();
        String query;
        if(search.equals("")){
            query = "SELECT * FROM customers order by regularity_count desc";
        }
        else {
            query = "SELECT * FROM customers where name like '"+search+"%' or contact like '"+search+"%' order by regularity_count desc ";
        }
        Statement st;
        ResultSet rs;
        CustomerModel customerModel;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            int index=1;
            while(rs.next()) {
                customerModel = new CustomerModel(
                        index,
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("contact"),
                        rs.getString("email"),
                        rs.getString("address")
                );
                index++;
                customerModels.add(customerModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerModels;
    }

    //kamana/
    public GridPane stockItemsGrid;
    public Button inStockBtn, outOfStockBtn, StockClearSearchBtn;
    public TextField StockSearchField;

    private void StocksInitialize(){
        loadStock();
        inStockBtn.setOnAction(event->{
            loadStock();
        });
        outOfStockBtn.setOnAction(event->{
            loadOutStock();
        });
        StockClearSearchBtn.setOnAction((event)->{
            StockSearchField.setText("");
        });
        StockSearchField.textProperty().addListener((event)->{
            setItemsToGrid2(0,StockSearchField.getText());
        });
        StockCategoriesTableView.setOnMouseClicked(mouseEvent -> {
            CategoryModel p = StockCategoriesTableView.getSelectionModel().getSelectedItem();
            if(p != null) setItemsToGrid2(p.getId(), "");

        });
    }
    private void addButtonStock(int id, String btnText) {
        final Button temp = new Button(btnText);
        temp.setId("" + id);
        temp.getStyleClass().add("item-special-button");
        temp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showLoginPopup();
                showUpdatePricePopupWindow(Integer.parseInt(temp.getId()));
                loadStock();
            }
        });
        stockItemsGrid.add(temp, x, y);
        if(x==7){
            x=0;
            y++;
        }
        else {
            x++;
        }

    }
    private void loadStock(){
        setItemsToGrid2(0, "");
        showCategories();
    }
    private void loadOutStock(){
        setItemsToGridout(0, "");
        showCategories();
    }
    public void setItemsToGrid2(int id, String search){
        x=0;
        y=0;
        stockItemsGrid.getChildren().clear();
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
                addButtonStock(rs.getInt("i.id"), rs.getString("i.title")+"\n("+
                        rs.getString("c.title")+")"+"\n₹ "+
                        rs.getString("i.mrp")+itemType+"\n\n"+stockRem);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void setItemsToGridout(int id, String search){
        x=0;
        y=0;
        stockItemsGrid.getChildren().clear();
        Connection connection = dbConnection.getConnection();
        String query;
        if(search.equals("")){
            if(id>0){
                query = "SELECT * FROM items i JOIN categories c on c.id = i.cat_id JOIN vendor_orders vo on (i.vo_id=vo.id  and i.stock=0) where i.cat_id = '"+id+"'";
            }
            else {
                query = "SELECT * FROM items i JOIN categories c on c.id = i.cat_id JOIN vendor_orders vo on (i.vo_id=vo.id  and i.stock=0)";
            }
        }
        else {
            query="SELECT * FROM items i JOIN categories c on c.id = i.cat_id JOIN vendor_orders vo on (i.vo_id=vo.id and i.stock=0) where" +
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
                addButtonStock(rs.getInt("i.id"), rs.getString("i.title")+"\n("+
                        rs.getString("c.title")+")"+"\n₹ "+
                        rs.getString("i.mrp")+itemType+"\n\n"+stockRem);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
