package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Database.DBConnection;
import sample.entity.Invoice;
import sample.entity.InvoiceDetails;
import sample.entity.Member;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class InvoiceController implements Initializable {
    public TableView<Invoice> invoiceTableView;
    public TableColumn<Invoice, Integer> idColumn;
    public TableColumn<Invoice, String> codememberColumn;
    public TableColumn<Invoice, String> namememberColumn;
    public TableColumn<Invoice, String> datecreateColumn;
    public TableColumn<Invoice, Integer> discountColumn;
    public TableColumn<Invoice, Integer> totalColumn;

    public Button viewButton;
    public TextField searchTextField;
    public ComboBox dayComboBox;
    public ComboBox monthComboBox;
    public ComboBox yearComboBox;
    public Button searchButton;
    public Button exitButton;
    Connection con;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = DBConnection.DBConn();
        showInvoiceBySearch();
        getDataForComboBoxDate();
    }

    public void ViewOnAction(ActionEvent actionEvent) {
    }

    public void SearchOnAction(ActionEvent actionEvent) { //tim kiem theo ngay thang nam

    }

    public void showInvoiceBySearch() {
        ObservableList<Invoice> list = getInvoice();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        codememberColumn.setCellValueFactory(new PropertyValueFactory<>("code_member"));
        namememberColumn.setCellValueFactory(new PropertyValueFactory<>("name_member"));
        datecreateColumn.setCellValueFactory(new PropertyValueFactory<>("datecreated"));
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        FilteredList<Invoice> filteredList = new FilteredList<>(list, b -> true);
        searchTextField.textProperty().addListener((observableValue, s, t1) -> {
            filteredList.setPredicate(invoice -> {
                if (t1 == null || t1.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = t1.toLowerCase();
                if (invoice.getName_member().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (invoice.getCode_member().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (invoice.getId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Invoice> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(invoiceTableView.comparatorProperty());
        invoiceTableView.setItems(sortedList);
    }

    private ObservableList<Invoice> getInvoice() {
        ObservableList<Invoice> list = FXCollections.observableArrayList();
        try {
            ResultSet rs = con.createStatement().executeQuery(" SELECT * FROM tblInvoice ");
            Invoice invoice;
            String name;
            while (rs.next()) {
                name = getNameByCodemember(rs.getString("code_member"));
                invoice = new Invoice(rs.getString("id"), rs.getString("code_member"),
                        name, rs.getString("datecreated"),
                        rs.getInt("discount"), rs.getInt("total"));
                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getNameByCodemember(String code) throws SQLException { // khi hien thi 1 hoa don ma co thong tin cua khach hang da bi xoa tu table member??????
        String name = null;
        PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM tblMember WHERE code_member = ?");
        preparedStatement.setString(1, code);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            name= resultSet.getString("name_member");
        }
        return name;
    }

    public void getDataForComboBoxDate() {
        ObservableList<Integer> listDay = FXCollections.observableArrayList();
        ObservableList<Integer> listMonth = FXCollections.observableArrayList();
        ObservableList<Integer> listYear = FXCollections.observableArrayList();
        for (int j = 1; j <= 31; j++) {
            listDay.add(j);
        }
        for (int j = 1; j <= 12; j++) {
            listMonth.add(j);
        }
        for (int j = 1970; j <= 2100; j++) {
            listYear.add(j);
        }
        dayComboBox.setItems(null);
        monthComboBox.setItems(null);
        yearComboBox.setItems(null);
        dayComboBox.setItems(listDay);
        monthComboBox.setItems(listMonth);
        yearComboBox.setItems(listYear);
    }

    public void ExitOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
