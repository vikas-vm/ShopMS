package sample.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.DbConnection;
import sample.models.ItemModel;
import sample.models.VendorOrderModel;
import sample.models.VendorModel;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;


public class MainController extends AbstractController implements Initializable {
    public Button addVendorBtn;
    public Button addVendorOrderBtn;
    public Button addOrderItemBtn;
    public TextField sv_title;
    public TextField sv_contact;
    public TextField sv_email;
    public TextArea sv_address;
    public TextField sv_city;
    @FXML
    private TableView<VendorModel> vendorsTableView;
    @FXML
    private TableColumn<VendorModel, Integer> v_id;
    @FXML
    private TableColumn<VendorModel, String> v_title;
    @FXML
    private TableView<VendorOrderModel> vendorOrdersTableView;
    @FXML
    private TableColumn<VendorModel, Integer> vo_id;
    @FXML
    private TableColumn<VendorModel, String> vo_title;
    @FXML
    private TableColumn<VendorModel, String> vo_status;
    @FXML
    private TableView<ItemModel> vendorOrdersItemTableView;
    @FXML
    private TableColumn<ItemModel, String> item_id;
    @FXML
    private TableColumn<ItemModel, String> item_title;
    @FXML
    private TableColumn<ItemModel, String> item_stock;
    @FXML
    private TableColumn<ItemModel, String> item_price;
    @FXML
    private TableColumn<ItemModel, String> item_mrp;
    DbConnection dbConnection = new DbConnection();

    public MainController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addVendorOrderBtn.setDisable(true);
        addOrderItemBtn.setDisable(true);
        showVendors();
        addVendorBtn.setOnAction((event)->{
            HashMap<String, Object> resultMap = showVendorPopupWindow();
            showVendors();
        });
        vendorOrdersTableView.setOnMouseClicked(mouseEvent -> {
            VendorOrderModel p = vendorOrdersTableView.getSelectionModel().getSelectedItem();
            showVendorOrderItems(p.getId());
            addOrderItemBtn.setDisable(false);
        });
        addVendorOrderBtn.setOnAction((event)->{
            HashMap<String, Object> resultMap = showAddVendorOrderPopupWindow();
            VendorModel p = vendorsTableView.getSelectionModel().getSelectedItem();
            if(resultMap.get("orderTitle") != null){
                String query = "insert into vendor_orders(v_order_title, v_id) values('"+resultMap.get("orderTitle")+"','"+p.getId()+"')";
                dbConnection.executeQuery(query);
                showVendorOrders(p.getId());
            }
        });

        addOrderItemBtn.setOnAction((event)->{
            HashMap<String, Object> resultMap = showAddItemPopup();
            VendorOrderModel p = vendorOrdersTableView.getSelectionModel().getSelectedItem();
            showVendorOrderItems(p.getId());
        });
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
        String query = "SELECT * FROM items where vo_id = '"+id+"'";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            ItemModel itemModel;
            while(rs.next()) {
                itemModel = new ItemModel(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getFloat("stock"),
                        rs.getFloat("price"),
                        rs.getFloat("mrp")
                        );
                vendorOrdersItemList.add(itemModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vendorOrdersItemList;
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
    public void showVendorOrderItems(int id) {
        ObservableList<ItemModel> list = getVendorOrdersItemList(id);
        item_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        item_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        item_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        item_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        item_mrp.setCellValueFactory(new PropertyValueFactory<>("mrp"));
        vendorOrdersItemTableView.setItems(list);
    }
    public void getClicked(MouseEvent mouseEvent) throws SQLException {
        VendorModel p = vendorsTableView.getSelectionModel().getSelectedItem();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM vendors where id = '"+p.getId()+"'";
        Statement st;
        ResultSet rs;
        st = connection.createStatement();
        rs = st.executeQuery(query);
        while(rs.next()) {
            sv_title.setText(rs.getString("title"));
            sv_contact.setText(rs.getString("contact"));
            sv_email.setText(rs.getString("email"));
            sv_address.setText(rs.getString("address"));
            sv_city.setText(rs.getString("city"));
        }
        showVendorOrders(p.getId());
        addOrderItemBtn.setDisable(true);
        vendorOrdersItemTableView.setItems(null);
    }
}
