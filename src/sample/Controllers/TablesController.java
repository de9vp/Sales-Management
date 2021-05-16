package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Database.DBConnection;
import sample.other.Tables;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

public class TablesController implements Initializable {

    public TableView<Tables> tablesTableView;
    public TableColumn<Tables, Integer> idColumn;
    public TableColumn<Tables, String> nameColumn;
    public TextField tablenameTextField;
    public Button saveButton;
    public Button updateButton;
    public Button deleteButton;

    Connection con = null ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListenerForTable();
        con = DBConnection.DBConn();
        showTable();
    }

    public void showTable() {
        ObservableList<Tables> list = getTableList();
        idColumn.setCellValueFactory(new PropertyValueFactory<Tables, Integer>("Id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Tables, String>("Name"));
        tablesTableView.setItems(list);
    }

    private void insertRecord() {
        String name = tablenameTextField.getText();
        if (!name.isEmpty()) {
            String query = "INSERT INTO tblTables (id,name) VALUES('" + name + "' ,'" + name + "')";
            executeQuery(query);
            showTable();
            tablenameTextField.setText("");
        }
    }

    private void executeQuery(String query) {
        Statement st;
        try {
            st = con.createStatement();
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<Tables> getTableList() {
        ObservableList<Tables> tableList = FXCollections.observableArrayList();
        String query = "SELECT * FROM tblTables";
        Statement st;
        ResultSet rs;

        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
            Tables tables;
            while(rs.next()) {
                tables = new Tables(rs.getInt("id"), rs.getString("name"));
                tableList.add(tables);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableList;
    }

    public void SaveOnAction(ActionEvent event) {
        insertRecord();
    }

    public void addListenerForTable() {
        tablesTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                tablenameTextField.setText(newSelection.getName());
            } else {
                tablenameTextField.setText("");
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });
    }

    public void UpdateOnAction(ActionEvent event) {
        Tables table = tablesTableView.getSelectionModel().getSelectedItem();
        String query = "UPDATE tblTables SET name = '" + tablenameTextField.getText() + "' WHERE id = '" + table.getId() + "' ";
        executeQuery(query);
        showTable();
    }

    public void DeleteOnAction(ActionEvent event) {
        Tables table = tablesTableView.getSelectionModel().getSelectedItem();
        String query = " DELETE FROM tblTables WHERE id = '" + table.getId() + "' ";
        executeQuery(query);
        showTable();
    }
}
