package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Database.DBConnection;
import sample.entity.InvoiceDetails;
import sample.entity.Member;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class PurchasedController implements Initializable {
    public TextField searchTextField;

    public TableView<InvoiceDetails> invoiceDetailTableView;
    public TableColumn<InvoiceDetails, Integer> idColumn;
    public TableColumn<InvoiceDetails, String> idinvoiceColumn;
    public TableColumn<InvoiceDetails, Integer> idproductColumn;
    public TableColumn<InvoiceDetails, String> nameColumn;
    public TableColumn<InvoiceDetails, Integer> priceColumn;
    public TableColumn<InvoiceDetails, Integer> quantityColumn;
    public TableColumn<InvoiceDetails, Integer> totalColumn;
    public Button exitButton;

    Connection con;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = DBConnection.DBConn();
        showPurchased();
    }

    public String getNameByIdproduct(int id) throws SQLException {
        String name = null;
        PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM tblProduct WHERE id_product = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            name= resultSet.getString("name_product");
        }
        return name;
    }

    public void showPurchased() {
        ObservableList<InvoiceDetails> list = getPurchased();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idinvoiceColumn.setCellValueFactory(new PropertyValueFactory<>("idInvoice"));
        idproductColumn.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameProduct"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalprice"));

        FilteredList<InvoiceDetails> filteredList = new FilteredList<>(list, b -> true);
        searchTextField.textProperty().addListener((observableValue, s, t1) -> {
            filteredList.setPredicate(invoiceDetails -> {
                if (t1 == null || t1.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = t1.toLowerCase();
                if (invoiceDetails.getIdInvoice().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (invoiceDetails.getNameProduct().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<InvoiceDetails> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(invoiceDetailTableView.comparatorProperty());
        invoiceDetailTableView.setItems(sortedList);
    }

    private ObservableList<InvoiceDetails> getPurchased()  {
        ObservableList<InvoiceDetails> list = FXCollections.observableArrayList();
        try {
            ResultSet rs = con.createStatement().executeQuery(" SELECT * FROM tblInvoiceDetail ");
            InvoiceDetails invoiceDetails;
            String name;
            while (rs.next()) {
                name = getNameByIdproduct(rs.getInt("id_product"));
                invoiceDetails = new InvoiceDetails(rs.getInt("id"), rs.getString("id_invoice"),
                        rs.getInt("id_product"), name, rs.getInt("price"),
                        rs.getInt("quantity"), rs.getInt("totalprice"));
                list.add(invoiceDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void ExitOnAction(ActionEvent actionEvent) {
        try {
            openSales();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSales() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../FXML/frmSales.fxml"));
        Scene fxmlFile = new Scene(root);
        Stage window = (Stage) exitButton.getScene().getWindow();
        window.setScene(fxmlFile);
        window.setTitle("DEMO 5");
        window.show();
    }
}
