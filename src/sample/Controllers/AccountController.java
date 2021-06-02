package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Database.DBConnection;
import sample.entity.Account;
import sample.entity.Member;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AccountController implements Initializable {

    public TableView<Account> accountTableView;
    public TableColumn<Account, Integer> idColumn;
    public TableColumn<Account, String> usernameColumn;
    public TableColumn<Account, String> passwordColumn;
    public TableColumn<Account, String> grantColumn;
    public ComboBox<String> grantComboBox;

    public Button deleteButton;
    public Button updateButton;
    public TextField passwordTextField;
    public Button saveButton;
    public TextField usernameTextField;
    public Button dashboardButton;


    Connection con;
    Window owner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = DBConnection.DBConn();
        getAccountForComboBox();
        addListenerForAccount();
        showAccount();
    }

    public void SaveOnAction(ActionEvent actionEvent) {
        insertAccount();
    }

    public void DeleteOnAction(ActionEvent actionEvent) {
        Account account = accountTableView.getSelectionModel().getSelectedItem();
        String query = " DELETE FROM tblAccount WHERE id_acc = '" + account.getId() + "' ";
        executeQuery(query);
        showAccount();
    }

    public void UpdateOnAction(ActionEvent actionEvent) {
        Account account = accountTableView.getSelectionModel().getSelectedItem();
        String query = " UPDATE tblAccount SET username = '" + usernameTextField.getText() + "', " +
                "password = '" + passwordTextField.getText() + "', grantname = '"+ grantComboBox.getSelectionModel().getSelectedItem()
                +"' WHERE id_acc = '" + account.getId()+ "' ";
        executeQuery(query);
        showAccount();
    }

    public void DashboardOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) dashboardButton.getScene().getWindow();
        stage.close();
    }

    public void showAccount() {
        ObservableList<Account> list = getAccountList();
        idColumn.setCellValueFactory(new PropertyValueFactory<Account, Integer>("Id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<Account, String>("Username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<Account, String>("Password"));
        grantColumn.setCellValueFactory(new PropertyValueFactory<Account, String>("Grant"));
        accountTableView.setItems(list);
    }

    private ObservableList<Account> getAccountList() {
        ObservableList<Account> accountList = FXCollections.observableArrayList();
        Statement st;
        ResultSet rs;

        try {
            Account account;
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM tblAccount");

            while(rs.next()) {
                account = new Account(rs.getInt("id_acc"), rs.getString("username"), rs.getString("password"), rs.getString("grantname"));
                accountList.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountList;
    }

    public void addListenerForAccount() {
        accountTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                usernameTextField.setText(String.valueOf(newSelection.getUsername()));
                passwordTextField.setText(newSelection.getPassword());
                grantComboBox.getSelectionModel().select(newSelection.getGrant());
            } else {
                usernameTextField.setText("");
                passwordTextField.setText("");
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });
    }

    public void getAccountForComboBox() { //Do data vao combobox
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("ADMIN");
        list.add("EMPLOYEE");
        grantComboBox.setItems(null);
        grantComboBox.setItems(list);
    }

    public void insertAccount() {
        try {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            String grant = grantComboBox.getSelectionModel().getSelectedItem();
            if (!username.isEmpty() && !password.isEmpty() && !grant.isEmpty()) {
                executeQuery("INSERT INTO tblAccount (username, password, grantname) VALUES ('"+ username +"','"+ password +"','"+ grant +"')");
                showAccount();
                usernameTextField.setText("");
                passwordTextField.setText("");
                grantComboBox.valueProperty().set(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ShowAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public void executeQuery(String query) {
        Statement st;
        System.out.println(query);
        try {
            st = con.createStatement();
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
