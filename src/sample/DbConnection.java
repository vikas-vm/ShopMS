package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DbConnection {

    public int executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public Connection getConnection() {
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop_sample","root","");
            return conn;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
