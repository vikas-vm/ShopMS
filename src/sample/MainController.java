package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.models.Vendors;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;


public class MainController extends sample.AbstractController implements Initializable {
    public Button addVendorBtn;
    public TextField vn_title;
    public TextField vn_contact;
    public TextField vn_email;
    public TextField vn_city;
    public TextArea vn_address;
    public Text vn_warning;
    @FXML
    private TableView<Vendors> tableView;
    @FXML
    private TableColumn<Vendors, Integer> v_id;
    @FXML
    private TableColumn<Vendors, String> v_title;
    DbConnection dbConnection = new DbConnection();

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
        // initializing the controller
        sample.VendorController popupController = new sample.VendorController();
        loader.setController(popupController);
        Parent layout;
        try {
            layout = loader.load();
            Scene scene = new Scene(layout, 600, 500);
            // this is the popup stage
            Stage popupStage = new Stage();
            // Giving the popup controller access to the popup stage (to allow the controller to close the stage)
            popupController.setStage(popupStage);
            if(this.main!=null) {
                popupStage.initOwner(main.getPrimaryStage());
            }
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setTitle("Add Vendor | InventoryMS");
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
        String query = "SELECT * FROM vendor";
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



    public void showVendors() {
        ObservableList<Vendors> list = getVendorsList();
        v_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        v_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableView.setItems(list);
    }
}
