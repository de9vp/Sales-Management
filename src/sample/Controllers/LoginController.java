package sample.Controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.BorderPane;

import javafx.stage.Stage;
import javafx.event.ActionEvent;

import javafx.stage.Window;

import sample.Database.DBConnection;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class LoginController implements Initializable {
    @FXML
    public TextField usernameTextField;
    @FXML
    public Button loginButton;
    @FXML
    public Button closeButton;
    @FXML
    public Button minimizeButton;
    @FXML
    public PasswordField enterPasswordField;
    @FXML
    public FontAwesomeIcon coffeeIcon;
    @FXML
    public Label salesLabel;
    @FXML
    public FontAwesomeIcon userIcon;
    @FXML
    public FontAwesomeIcon passIcon;
    @FXML
    public FontAwesomeIcon lockIcon;
    @FXML
    public FontAwesomeIcon closeIcon;
    @FXML
    public FontAwesomeIcon minimizeIcon;
    @FXML
    public BorderPane frmLogin;

    public LoginController() {

    }

    Connection con = null;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        con = DBConnection.DBConn(); //ket noi co so du lieu voi con
    }

    public void closeButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void loginButtonOnAction(ActionEvent event) {
        Window owner = loginButton.getScene().getWindow();

        if(!usernameTextField.getText().isBlank() && !enterPasswordField.getText().isBlank()) {
            validateLogin();
        } else {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter your Username");
        }
    }

    public void minimizeButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    public void validateLogin() {

        PreparedStatement pst = null;
        ResultSet rs = null;

        Window owner = loginButton.getScene().getWindow();
        String user = usernameTextField.getText().toString();
        String pass = enterPasswordField.getText().toString();
        //query
        String Select_Query_Login = "SELECT * FROM tblAccount WHERE username = '"+ user +"' and password = '"+ pass +"'";
        try {

            pst = con.prepareStatement(Select_Query_Login);
//            pst.setString(1, user);
//            pst.setString(2, pass);
            rs = pst.executeQuery();
            if(!rs.next()) {
                showAlert(Alert.AlertType.ERROR, owner, "Alert!", "Enter your User/Pass");
            } else {
                showAlert(Alert.AlertType.CONFIRMATION, owner, "Successfully!", "Đăng nhập thành công !!");
                openDashboard();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void openDashboard()  {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../FXML/frmDashboard.fxml"));
            Stage window = (Stage) loginButton.getScene().getWindow();
            window.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}
