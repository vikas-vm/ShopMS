package sample;
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
import sample.models.VendorOrders;
import sample.models.Vendors;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;


public class MainController extends sample.AbstractController implements Initializable {
    public Button addVendorBtn;
    public TextField sv_title;
    public TextField sv_contact;
    public TextField sv_email;
    public TextArea sv_address;
    public TextField sv_city;
    @FXML
    private TableView<Vendors> vendorsTableView;
    @FXML
    private TableColumn<Vendors, Integer> v_id;
    @FXML
    private TableColumn<Vendors, String> v_title;
    @FXML
    private TableView<VendorOrders> vendorOrdersTableView;
    @FXML
    private TableColumn<Vendors, Integer> vo_id;
    @FXML
    private TableColumn<Vendors, String> vo_title;
    DbConnection dbConnection = new DbConnection();

    public MainController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showVendors();
        addVendorBtn.setOnAction((event)->{
            HashMap<String, Object> resultMap = showVendorPopupWindow();
            showVendors();
        });
    }

    private HashMap<String, Object> showVendorPopupWindow() {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("xml/VendorPopup.fxml"));
        sample.VendorController popupController = new sample.VendorController();
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

    public ObservableList<Vendors> getVendorsList(){
        ObservableList<Vendors> vendorsList = FXCollections.observableArrayList();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM vendors";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            Vendors vendors;
            while(rs.next()) {
                vendors = new Vendors(rs.getInt("Id"),rs.getString("Title"),rs.getString("Contact"),rs.getString("Email"),rs.getString("City"), rs.getString("Address"));
                vendorsList.add(vendors);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vendorsList;
    }

    public ObservableList<VendorOrders> getVendorOrdersList(int id){
        ObservableList<VendorOrders> vendorOrdersList = FXCollections.observableArrayList();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM vendor_orders where v_id = '"+id+"'";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            VendorOrders vendorOrders;
            while(rs.next()) {
                vendorOrders = new VendorOrders(rs.getInt("id"),rs.getString("v_order_title"));
                vendorOrdersList.add(vendorOrders);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vendorOrdersList;
    }

    public void showVendors() {
        ObservableList<Vendors> list = getVendorsList();
        v_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        v_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        vendorsTableView.setItems(list);
    }

    public void showVendorOrders(int id) {
        ObservableList<VendorOrders> list = getVendorOrdersList(id);
        vo_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        vo_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        vendorOrdersTableView.setItems(list);
    }
    public void getClicked(MouseEvent mouseEvent) throws SQLException {
        Vendors p = vendorsTableView.getSelectionModel().getSelectedItem();
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
    }
}
