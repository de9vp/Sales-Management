package sample.Controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
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
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;


public class LoginController implements Initializable {

    public TextField usernameTextField;
    public Button loginButton;
    public Button closeButton;
    public Button minimizeButton;
    public PasswordField enterPasswordField;
    public FontAwesomeIcon coffeeIcon;
    public Label salesLabel;
    public FontAwesomeIcon userIcon;
    public FontAwesomeIcon passIcon;
    public FontAwesomeIcon lockIcon;
    public FontAwesomeIcon closeIcon;
    public FontAwesomeIcon minimizeIcon;
    public BorderPane frmLogin;
    public Label signinLabel;

    Connection con = null;
    String userLogged = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = DBConnection.DBConn(); //ket noi database
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
            showAlert(Alert.AlertType.ERROR, owner, "Cảnh báo!", "Không được để trống!");
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
        String Select_Query_Login = "SELECT * FROM tblAccount WHERE username = ? and password = ? ";
        try {

            pst = con.prepareStatement(Select_Query_Login);
            pst.setString(1, user);
            pst.setString(2, pass);
            rs = pst.executeQuery();
            if(!rs.next()) {
                showAlert(Alert.AlertType.ERROR, owner, "Cảnh báo!", "Sai tên đăng nhập/mật khẩu! Mời nhập lại!");
                usernameTextField.setText("");
                enterPasswordField.setText("");
            } else {
                userLogged = rs.getString("grant");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Đăng nhập thành công!");
                alert.setTitle("Thông báo!");
                Optional<ButtonType> optButton = alert.showAndWait();
                if (optButton.isPresent() && optButton.get() == ButtonType.OK) {
                    openDashboard();
                    System.out.println("Username: " + user + " đã đăng nhập!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void openDashboard()  {
        try {
            //hien thi quyen dang nhap
            Stage window = (Stage) loginButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../FXML/frmDashboard.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            DashboardController dashboardController = loader.getController();
            System.out.println(" " + userLogged + " ");
            dashboardController.userLabel.setText(userLogged);
            window.setScene(scene);
//            Parent root = FXMLLoader.load(getClass().getResource("../FXML/frmDashboard.fxml"));
//            Stage window = (Stage) loginButton.getScene().getWindow();
//            Scene scene = new Scene(root);
//            DashboardController dashboardController = FXMLLoader.load(getClass().getResource("../FXML/frmDashboard.fxml"));
//            dashboardController.userLabel.setText(userLogged);
//            window.setScene(scene);
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
