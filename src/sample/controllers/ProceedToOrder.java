package sample.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.DbConnection;
import sample.models.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProceedToOrder extends AbstractController implements Initializable {
    private final HashMap<String, Object> result = new HashMap<String, Object>();
    private Stage stage = null;
    DbConnection dbConnection = new DbConnection();
    Connection connection = dbConnection.getConnection();
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public HashMap<String, Object> getResult() {
        return this.result;
    }
    public Button resetFormBtn, closeBtn, clearSearchBtn, placeOrderBtn, editTotalBtn;
    public TextField searchField, totalAmt, paidAmt, name, contact, email;
    public TextArea address;
    @FXML
    private TableView<CustomerModel> customersTableView;
    @FXML
    private TableColumn<CustomerModel, Integer> id;
    @FXML
    private TableColumn<CustomerModel, String> customer_name, customer_contact, customer_address, customer_due;
    int orderId;
    int customer_id=0;
    double total;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showCustomers("");
        totalAmt.setText(String.valueOf(total));
        closeBtn.setOnAction((event)->{
            closeStage();
        });
        resetFormBtn.setOnAction((event)->{
            ResetForm();
        });
        searchField.textProperty().addListener((event)->{
            showCustomers(searchField.getText());
        });
        name.textProperty().addListener((event)->{
            showCustomers(name.getText());
        });
        contact.textProperty().addListener((event)->{
            showCustomers(contact.getText());
        });
        clearSearchBtn.setOnAction(event->{
            searchField.setText("");
        });
        totalAmt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,10}([\\.]\\d{0,2})?")) {
                totalAmt.setText(oldValue);
            }
        });
        contact.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,10}?")) {
                contact.setText(oldValue);
            }
        });
        paidAmt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,10}([\\.]\\d{0,2})?")) {
                paidAmt.setText(oldValue);
            }
        });
        placeOrderBtn.setOnAction((event)->{
            AddCustomer();
        });
        editTotalBtn.setOnAction((event)->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Are you sure want to edit total amount field?");
            alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(yesButton, cancelButton);

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == yesButton)
            {
                totalAmt.setEditable(true);
            }
        });

        customersTableView.setOnMouseClicked(mouseEvent -> {
            CustomerModel p = customersTableView.getSelectionModel().getSelectedItem();
            if(p!=null){
                customer_id=p.getId();
                name.setText(p.getName());
                contact.setText(p.getContact());
                email.setText(p.getEmail());
                address.setText(p.getAddress());
            }
        });
    }
    private void ResetForm(){
        customer_id=0;
        name.setText("");
        contact.setText("");
        email.setText("");
        address.setText("");
    }

    public void showCustomers(String search) {
        ObservableList<CustomerModel> list = getCustomersList(search);
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        customer_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        customer_contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        customer_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        customer_due.setCellValueFactory(new PropertyValueFactory<>("due"));
        customersTableView.setItems(list);
    }
    public ObservableList<CustomerModel> getCustomersList(String search){
        ObservableList<CustomerModel> customerModels = FXCollections.observableArrayList();
        String query;
        if(search.equals("")){
           query = "SELECT * FROM customers order by regularity_count desc";
        }
        else {
            query = "SELECT * FROM customers where name like '%"+search+"%' or contact like '"+search+"%' order by regularity_count desc ";
        }
        Statement st;
        ResultSet rs;
        CustomerModel customerModel;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);lo
            while(rs.next()) {
                customerModel = new CustomerModel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("contact"),
                        rs.getString("email"),
                        rs.getString("address")
                );
                customerModels.add(customerModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerModels;
    }
    public void AddCustomer(){
        if(!name.getText().equals("")  && 0<Float.parseFloat(paidAmt.getText()) && 0<Float.parseFloat(totalAmt.getText())){
            String query;
            if(customer_id==0){
                query = "Insert into customers(name, contact, email, address, regularity_count)" +
                        " values('"+name.getText()+"', '"+contact.getText()+"', '"+email.getText()+"'," +
                        " '"+address.getText()+"', '1' )";
            }
            else {
                query = "Update customers set name='"+name.getText()+"', contact='"+contact.getText()+"'," +
                        "email='"+email.getText()+"', address='"+address.getText()+"', regularity_count=regularity_count+1 where id='"+customer_id+"'";
            }
            try {
                int return_result = dbConnection.executeQuery(query);
                if (return_result>0){
                    if(customer_id==0){
                        query = "Select * from customers order by id desc";
                        Statement st;
                        ResultSet rs;

                        try {
                            st = connection.createStatement();
                            rs = st.executeQuery(query);
                            if (rs.next()){
                                customer_id = rs.getInt("id");
                                System.out.println(customer_id);

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
                    PlaceOrder();
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
    public void PlaceOrder(){
        String query = "UPDATE order_items oi JOIN items i on oi.item_id=i.id set i.stock=i.stock-oi.qty where oi.order_id='"+orderId+"'";
        dbConnection.executeQuery(query);
        query = "UPDATE shop_orders so  set so.status='1', so.cust_id='"+customer_id+"', total_amt='"+totalAmt.getText()+"' where id='"+orderId+"'";
        dbConnection.executeQuery(query);
        query = "Insert into payments(order_id, amount) values('"+orderId+"','"+paidAmt.getText()+"')";
        dbConnection.executeQuery(query);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information | InventoryMS");
        alert.setHeaderText("Order Completed");
        alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                closeStage();
            }
        });
    }
    private void closeStage() {
        if(stage!=null) {
            stage.close();
        }
    }
}


