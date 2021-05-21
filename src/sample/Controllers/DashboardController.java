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
        try {
            openModalWindow("../FXML/frmProduct.fxml", "The loại !!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AccountButtonOnAction(ActionEvent actionEvent) {
//        String grant = userLabel.getText();
//        if (grant != "admin") {
//            showAlert(Alert.AlertType.ERROR, owner, "Cảnh báo!", "Admin mới được quyền này!");
//        } else {
//            openAccount();
//        }
        try {
            openModalWindow("../FXML/frmAccount.fxml", "The loại !!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void InvoiceButtonOnAction(ActionEvent actionEvent) {
        try {
            openModalWindow("../FXML/frmInvoice.fxml", "The loại !!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SellButtonOnAction(ActionEvent actionEvent) {
        try {
            openModalWindow("../FXML/frmSales.fxml", "The loại !!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void minimizeButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
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

    //handle

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public void openModalWindow(String resource, String tittle) throws IOException {
        root = FXMLLoader.load(getClass().getResource(resource));
        fxmlFile = new Scene(root);
        window = new Stage();
        window.setScene(fxmlFile);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setAlwaysOnTop(true);
        window.setIconified(false);
        window.setTitle(tittle);
        window.showAndWait();
    }
}
