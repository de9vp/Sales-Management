package sample.Controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Database.DBConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;


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
    String grantLogged = null;

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

        PreparedStatement pst ;
        ResultSet rs;

        Window owner = loginButton.getScene().getWindow();
        String user = usernameTextField.getText();
        String pass = enterPasswordField.getText();
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
                grantLogged = rs.getString("grantname");
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
            Stage window = (Stage) loginButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../FXML/frmDashboard.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);

            //hien thi quyen dang nhap
            DashboardController dashboardController = loader.getController();
            System.out.println("" + grantLogged + "");
            dashboardController.userLabel.setText(grantLogged);
            window.setTitle("DEMO 2");
            window.setScene(scene);
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
