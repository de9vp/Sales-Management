package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Database.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Stack;

public class DashboardController implements Initializable {

    Connection con = null;

    public Label userLabel;
    public Button productButton;
    public Button accountButton;
    public Button invoiceButton;
    public Button sellButton;

    Parent root;
    Scene fxmlFile;
    Stage window;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = DBConnection.DBConn();
    }

    public void ProductButtonOnAction(ActionEvent actionEvent) {
        openProduct();
    }

    public void AccountButtonOnAction(ActionEvent actionEvent) {

    }

    public void InvoiceButtonOnAction(ActionEvent actionEvent) {
    }

    public void SellButtonOnAction(ActionEvent actionEvent) {
        openSell();
    }

    public void setUserLabel(String username) {
        userLabel.setText(username);
    }

    public void TableOnAction(ActionEvent event) {
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

}
