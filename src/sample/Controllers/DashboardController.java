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
        try {
            openModalWindow("../FXML/frmProduct.fxml", "Quan Ly San Pham!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AccountButtonOnAction(ActionEvent actionEvent) {

    }

    public void InvoiceButtonOnAction(ActionEvent actionEvent) {
    }

    public void SellButtonOnAction(ActionEvent actionEvent) {
    }

    public void setUserLabel(String username) {
        userLabel.setText(username);
    }

    public void TableOnAction(ActionEvent event) {
        try {
            openModalWindow("../FXML/frmTables.fxml", "Quan Ly Ban!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openModalWindow(String resource, String tittle) throws IOException {
        root = FXMLLoader.load(getClass().getResource(resource));
        fxmlFile = new Scene(root);
        window = new Stage();
        window.setScene(fxmlFile);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setAlwaysOnTop(true);
        window.setIconified(false);
        //window.initStyle(StageStyle.UNDECORATED);
        window.setTitle(tittle);
        window.showAndWait();
    }

    public void SetMemberOnAction(ActionEvent actionEvent) {
        try {
            openModalWindow("../FXML/frmMember.fxml", "Quan Ly Khach Hang!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
