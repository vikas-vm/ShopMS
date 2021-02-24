package sample.models;

import sample.DbConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerModel {
    DbConnection dbConnection = new DbConnection();
    Connection connection = dbConnection.getConnection();

    private final int id;
    private final int index;
    private final String name;
    private final String contact;
    private final String email;
    private final String address;

    public CustomerModel(int index, int Id, String Name, String Contact, String Email,  String Address){
        this.index = index;
        this.id = Id;
        this.name=Name;
        this.contact=Contact;
        this.email=Email;
        this.address=Address;
    }

    public int getIndex() {
        return index;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getContact() {
        return contact;
    }
    public String getEmail() {
        return email;
    }
    public String getAddress() {
        return address;
    }
    public float getDue() throws SQLException {
        float total_amt=0, paid_amt=0;
        String query = "Select sum(total_amt) as total_amt from shop_orders where cust_id='"+id+"'";
        Statement st;
        ResultSet rs;
        st = connection.createStatement();
        rs = st.executeQuery(query);
        if(rs.next()){
            total_amt = rs.getFloat("total_amt");
        }
        query = "Select sum(amount) as total_paid from payments p join shop_orders so on p.order_id=so.id where so.cust_id='"+id+"'";
        st = connection.createStatement();
        rs = st.executeQuery(query);
        if(rs.next()){
            paid_amt = rs.getFloat("total_paid");
        }
        return (total_amt-paid_amt);
    }

}

