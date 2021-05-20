package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sample.Database.DBConnection;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class AccountController implements Initializable {

    Connection con;

    public TableView accountTableView;
    public TableColumn usernameColumn;
    public TableColumn passwordColumn;
    public TableColumn grantColumn;
    public Button deleteButton;
    public Button updateButton;
    public TextField passwordTextField;
    public Button saveButton;
    public TextField usernameTextField;
    public ComboBox grantComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = DBConnection.DBConn();
    }

    public void SaveOnAction(ActionEvent actionEvent) {
    }

    public void DeleteOnAction(ActionEvent actionEvent) {
    }

    public void UpdateOnAction(ActionEvent actionEvent) {
    }


}
