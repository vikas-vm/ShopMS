package sample.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.DbConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AddCategoryPopup extends AbstractController implements Initializable {
    public Button insertCategoryBtn;
    public TextField category;
    public TextArea description;
    private HashMap<String, Object> result = new HashMap<String, Object>();
    private Stage stage = null;
    DbConnection dbConnection = new DbConnection();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public HashMap<String, Object> getResult() {
        return this.result;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        insertCategoryBtn.setOnAction((event)->{
           insertCategory();
        });

    }

    private void closeStage() {
        if(stage!=null) {
            stage.close();
        }
    }
    public void insertCategory(){
        if( !category.getText().equals("")){
            Connection connection = dbConnection.getConnection();
            String query = "SELECT * FROM categories where LOWER(title) = LOWER( '"+category.getText()+"')";
            Statement st;
            ResultSet resultSet;
            try {
                st = connection.createStatement();
                resultSet = st.executeQuery(query);
                if(!resultSet.next()) {
                    String query1 = "insert into categories(title, description)" +
                            " values('"+category.getText()+"','"+description.getText()+"')";
                    int return_result = dbConnection.executeQuery(query1);
                    if (return_result>0){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Item Added Successfully");
                        alert.setHeaderText("Item Added Successfully");
                        alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
                        alert.showAndWait().ifPresent(rs -> {
                            if (rs == ButtonType.OK) {
                                closeStage();
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
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Category Already exists");
                    alert.setHeaderText("Category Already exists");
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
            alert.setTitle("item failed to save");
            alert.setHeaderText("Please fills all mandatory fields");
            alert.setContentText("Recheck again category fields");
            alert.getDialogPane().getStylesheets().add(getClass().getResource(dbConnection.getTheme()).toExternalForm());
            alert.show();
        }
    }
}
