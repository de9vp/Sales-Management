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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Database.DBConnection;
import sample.entity.Products;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    public TableView<Products> productsTableView;
    public TableColumn<Products, Integer> idColumn;
    public TableColumn<Products, String> productColumn;
    public TableColumn<Products, Integer> priceColumn;
    public TableColumn<Products, Integer> idcategoryColumn;
    public TableColumn<Products, String> categoryColumn;

    public Button addcategoryButton;
    public TextField productTextField;
    public TextField priceTextField;
    public ComboBox<String> categoryComboBox;
    public Button saveButton;
    public Button updateButton;
    public Button deleteButton;
    public TextField searchTextField;
    public Button searchButton;

    Connection con = null;

    Parent root;
    Scene fxmlFile;
    Stage window;
    Window owner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = DBConnection.DBConn();
        getCategories();
        showProduct();
    }

    public void SaveOnAction(ActionEvent actionEvent) {
        try {
            String name = productTextField.getText();
            String price = priceTextField.getText();
            String namecategory = categoryComboBox.getSelectionModel().getSelectedItem();

            if (name.isEmpty() || price.isEmpty() || namecategory.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, owner, "Alert!", "Enter your infor");
            } else {

                PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO tblProduct (name_product, price, id_category) VALUES (?,?,?)");
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, Integer.parseInt(price));
                //preparedStatement.setInt(3, idcategory);


                int resultSet1 = preparedStatement.executeUpdate();
                showAlert(Alert.AlertType.CONFIRMATION, owner, "Alert!", "Product saved successfully");
                productTextField.setText("");
                priceTextField.setText("");
                categoryComboBox.valueProperty().set(null);
                showProduct();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AddCategoryOnAction(ActionEvent actionEvent) {
        try {
            openModalWindow("../FXML/frmCategory.fxml", "The loại !!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void UpdateOnAction(ActionEvent actionEvent) {
    }

    public void DeleteOnAction(ActionEvent actionEvent) {
    }

    public void SearchOnAction(ActionEvent actionEvent) {
    }

    public void getCategories() { //Do data vao combobox the loai
        ObservableList<String> list = FXCollections.observableArrayList();
        Statement statement;
        try {
            ResultSet resultSet = con.createStatement().executeQuery("SELECT name_category FROM tblCategory");
            while (resultSet.next()) {
                list.add(resultSet.getString("name_category"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        categoryComboBox.setItems(null);
        categoryComboBox.setItems(list);
    }

    public void showProduct() {
        ObservableList<Products> list = getProductList();
        idColumn.setCellValueFactory(new PropertyValueFactory<Products, Integer>("id"));
        productColumn.setCellValueFactory(new PropertyValueFactory<Products, String>("nameproduct"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Products, Integer>("price"));
        idcategoryColumn.setCellValueFactory(new PropertyValueFactory<Products, Integer>("idcategory"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Products, String>("namecategory"));
        productsTableView.setItems(list);
    }

    public ObservableList<Products> getProductList() {
        ObservableList<Products> productList = FXCollections.observableArrayList();

        String query = " SELECT * FROM tblProduct ";
        Statement st;
        ResultSet rs;

        Statement st1;
        ResultSet rs1;
        String query1 = "SELECT * FROM tblCategory ";

        try {

            st = con.createStatement();
            rs = st.executeQuery(query);


            Products products;

            while(rs.next()) {
                int test = rs.getInt("id_category");
                String nameCategory = null;
                //truy van ten category theo idcategory
                try {
                    st1 = con.createStatement();
                    rs1 = st1.executeQuery(query1);
                    while (rs1.next()) {
                        if (test == rs1.getInt("id_category")) {
                            nameCategory = rs1.getString("name_category");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                products = new Products(rs.getInt("id_product"), rs.getString("name_product"),
                        rs.getInt("price"),rs.getInt("id_category"), nameCategory);
                productList.add(products);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
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

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}


