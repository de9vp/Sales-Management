package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import sample.Database.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.*;

public class DashboardController implements Initializable {

    public Button logountButton;
    public Button closeButton;
    public Button minimizeButton;

    Connection con = null;

    public Label userLabel;
    public Button productButton;
    public Button accountButton;
    public Button invoiceButton;
    public Button sellButton;

    Parent root;
    Scene fxmlFile;
    Stage window;
    Window owner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = DBConnection.DBConn();
    }

    public void ProductButtonOnAction(ActionEvent actionEvent) {
        openProduct();
    }

    public void AccountButtonOnAction(ActionEvent actionEvent) {
        openAccount();
    }

    public void InvoiceButtonOnAction(ActionEvent actionEvent) {
        openInvoice();
    }

    public void SellButtonOnAction(ActionEvent actionEvent) {
        openSell();
    }

    public void closeButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void minimizeButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    //handle

    public void setUserLabel(String username) {
        userLabel.setText(username);
    }

    public void openProduct()  {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../FXML/frmProduct.fxml"));
            Stage window = (Stage) productButton.getScene().getWindow();
            window.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void openAccount()  {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../FXML/frmAccount.fxml"));
            Stage window = (Stage) accountButton.getScene().getWindow();
            window.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void openSell()  {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../FXML/frmSales.fxml"));
            Stage window = (Stage) accountButton.getScene().getWindow();
            window.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void openInvoice() {

    }

    public void LogoutOnAction(ActionEvent actionEvent) throws IOException {
        Alert alertUser = new Alert(Alert.AlertType.CONFIRMATION, "Bạn chắc chắn muốn đăng xuất?");
        alertUser.setTitle("Cảnh báo!");
        Optional<ButtonType> optButton = alertUser.showAndWait();
        if (optButton.isPresent() && optButton.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../FXML/frmLogin.fxml")));
            Stage stage = (Stage) logountButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}
