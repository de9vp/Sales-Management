package sample.Controllers;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import sample.entity.Member;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MemberController implements Initializable {
    public TableView<Member> memberTableView;
    public TableColumn<Member, Integer> idColumn;
    public TableColumn<Member, Integer> codeColumn;
    public TableColumn<Member, String> memberColumn;
    public TextField memberTextField;
    public Button saveButton;
    public Button deleteButton;
    public Button updateButton;
    public TextField codeTextField;
    public TextField searchTextField;
    public Button searchButton;
    public Button exitButton;

    Connection con;
    ObservableList<Member> listMemberSearched = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = DBConnection.DBConn();
        addListenerForMember();
        showMemberSearched();
    }

    public void SaveOnAction(ActionEvent actionEvent) {
            insertRecord();
    }

    public void DeleteOnAction(ActionEvent actionEvent) {
        Member member = memberTableView.getSelectionModel().getSelectedItem();
        String query = " DELETE FROM tblMember WHERE id = '" + member.getId() + "' ";
        executeQuery(query);
        showMemberSearched();
    }

    public void UpdateOnAction(ActionEvent actionEvent) {
        Member member = memberTableView.getSelectionModel().getSelectedItem();
        String query = " UPDATE tblMember SET code_member = '" + codeTextField.getText() + "', name_member = '" + memberTextField.getText() + "' WHERE id = '" + member.getId()+ "' ";
        executeQuery(query);
        showMemberSearched();
    }

    //handle

    public void showMemberSearched() {

        ObservableList<Member> listMemberSearched = getMemberList();

        idColumn.setCellValueFactory(new PropertyValueFactory<Member, Integer>("id"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<Member, Integer>("code"));
        memberColumn.setCellValueFactory(new PropertyValueFactory<Member, String>("name"));

        FilteredList<Member> filteredList = new FilteredList<>(listMemberSearched, b -> true);
        searchTextField.textProperty().addListener((observableValue, s, t1) -> {
            filteredList.setPredicate(member -> {
                if (t1 == null || t1.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = t1.toLowerCase();
                if (member.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (member.getCode().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Member> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(memberTableView.comparatorProperty());
        memberTableView.setItems(sortedList);
    }

    private ObservableList<Member> getMemberList() {
        ObservableList<Member> memberList = FXCollections.observableArrayList();
        String query = "SELECT * FROM tblMember";
        Statement st;
        ResultSet rs;

        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
            Member member;
            while(rs.next()) {
                member = new Member(rs.getInt("id"), rs.getString("code_member"), rs.getString("name_member"));
                memberList.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memberList;
    }

    public void addListenerForMember() {
        memberTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, products, t1) -> {
            if (t1 != null) {
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                codeTextField.setText(String.valueOf(t1.getCode()));
                memberTextField.setText(t1.getName());
            } else {
                codeTextField.setText("");
                memberTextField.setText("");
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });
    }

    public void insertRecord() {
        String codemember = codeTextField.getText();
        String membername = memberTextField.getText();
        if (!codemember.isEmpty() && !membername.isEmpty()) {
            String query = "INSERT INTO tblMember (code_member, name_member) VALUES ('" + codemember + "', '" + membername + "')";
            executeQuery(query);
            showMemberSearched();
            codeTextField.setText("");
            memberTextField.setText("");
        }
    }

    public void executeQuery(String query) {
        Statement st;
        System.out.println(query);
        try {
            st = con.createStatement();
            st.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Đã tồn tại. Vui lòng nhập lại tên!");
        }
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
