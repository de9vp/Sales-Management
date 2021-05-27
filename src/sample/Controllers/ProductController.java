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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Database.DBConnection;
import sample.entity.Member;
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
    public ComboBox<String> categoryComboBox;

    public Button addcategoryButton;
    public TextField productTextField;
    public TextField priceTextField;
    public Button saveButton;
    public Button updateButton;
    public Button deleteButton;
    public TextField searchTextField;

    Connection con = null;

    Parent root;
    Scene fxmlFile;
    Stage window;
    Window owner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = DBConnection.DBConn();
        addListenerForProduct();
        getCategoriesForCombobox();
        showProduct();
    }

    public int getIdByNamecategory(String name) throws SQLException { //lay id the loai theo ten
        int idCategory = 0;
        PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM tblCategory WHERE name_category = ?");
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            idCategory = resultSet.getInt("id_category");
        }
        return idCategory;
    }

    public void SaveOnAction(ActionEvent actionEvent) {
        try {
            String name = productTextField.getText();
            String price = priceTextField.getText();
            String namecategory = categoryComboBox.getSelectionModel().getSelectedItem();

            if (name.isEmpty() || price.isEmpty() || namecategory.isEmpty()) {
                //canh bao
            } else {
                int id = getIdByNamecategory(namecategory);
                executeQuery("INSERT INTO tblProduct (name_product, price, id_category) VALUES ('"+ name +"','"+ Integer.valueOf(price) +"','"+ id +"')");
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

    public void UpdateOnAction(ActionEvent actionEvent) throws SQLException {
        String namecategory = categoryComboBox.getSelectionModel().getSelectedItem();
        Products products = productsTableView.getSelectionModel().getSelectedItem();
        String query = " UPDATE tblProduct SET name_product = '"+ productTextField.getText() +"', price = '"+ priceTextField.getText() +"', " +
                "id_category = '"+ getIdByNamecategory(namecategory) +"' WHERE id_product = '" + products.getId() + "' ";
        executeQuery(query);
        showProduct();
    }

    public void DeleteOnAction(ActionEvent actionEvent) {
        Products products = productsTableView.getSelectionModel().getSelectedItem();
        String query = " DELETE FROM tblProduct WHERE id_product = '" + products.getId() + "' ";
        executeQuery(query);
        showProduct();
    }

    public void getCategoriesForCombobox() { //Do data vao combobox the loai
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

        FilteredList<Products> filteredList = new FilteredList<>(list, b -> true);
        searchTextField.textProperty().addListener((observableValue, s, t1) -> {
            filteredList.setPredicate(products -> {
                if (t1 == null || t1.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = t1.toLowerCase();
                if (products.getNameproduct().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(products.getPrice()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Products> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(productsTableView.comparatorProperty());
        productsTableView.setItems(sortedList);
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

    public void addListenerForProduct() {
        productsTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, products, t1) -> {
            if (t1 != null) {
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                productTextField.setText(t1.getNameproduct());
                priceTextField.setText(String.valueOf(t1.getPrice()));
                categoryComboBox.getSelectionModel().select(t1.getNamecategory());
            } else {
                productTextField.setText("");
                priceTextField.setText("");
                categoryComboBox.getSelectionModel().select("Chọn Thể Loại");
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });
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


