package sample;
import java.sql.*;

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
    public String getTheme(){
        Connection connection = getConnection();
        String query = "SELECT * FROM appearance where id=1";
        Statement st;
        ResultSet rs;
        String themeVar=null;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){
                themeVar="/sample/themes/"+rs.getString("themeFile");
            }
            else {
                themeVar="/sample/themes/light-theme.css";
            }
            return themeVar;
        } catch (Exception e) {
            e.printStackTrace();
            return "/sample/themes/light-theme.css";
        }
    }
}
