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
import sample.entity.InvoiceDetails;
import sample.entity.Item;
import sample.entity.Products;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class SalesController implements Initializable {

    public TableView<Products> productTableView;
    public TableColumn<Products, Integer> idproductColumn;
    public TableColumn<Products, String> productColumn;
    public TableColumn<Products, Integer> priceColumn;
    public TableColumn<Products, Integer> idcateggoryColumn;
    public TableColumn<Products, String> categoryColumn;
    public ComboBox<String> categoryComboBox;
    public TextField searchTextField;
    public TextField productTextField;
    public Button cancelProductButton;
    public TextField priceTextField;
    public Button addProductButton;
    public TextField totalpriceTextField;
    public Button memberButton;
    public Spinner<Integer> quantitySpinner;
    public TableView<Item> orderTableView;
    public TableColumn<Item, String> productOrderColumn;
    public TableColumn<Item, Integer> priceOrderColumn;
    public TableColumn<Item, Integer> quantityColumn;
    public TableColumn<Item, Integer> totalColumn;

    Connection con;
    Parent root;
    Scene fxmlFile;
    Stage window;
    Window owner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = DBConnection.DBConn();
        getCategoriesForCombobox();
        //getSpinner();
        addListenerForProduct();
        showProduct();
    }

    public void MemberButtonOnAction(ActionEvent actionEvent) {
        try {
            openModalWindow("../FXML/frmMember.fxml", "KHÁCH HÀNG!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CancelProductOnAction(ActionEvent actionEvent) {
    }

    public void AddProductOnAcTion(ActionEvent actionEvent) {
        ObservableList<Item> list = FXCollections.observableArrayList();

        String Productname = productTextField.getText();
        int Price = Integer.parseInt(priceTextField.getText());
        int Quantity = quantitySpinner.getValue();
        int Total = Integer.parseInt(totalpriceTextField.getText());

        orderTableView.setItems(list);
        resetAdd();
    }

    public void resetAdd() {
        productTextField.setText("");
        priceTextField.setText("");
        quantitySpinner.setDisable(true);
        totalpriceTextField.setText("");
        cancelProductButton.setDisable(true);
        addProductButton.setDisable(true);
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

        idproductColumn.setCellValueFactory(new PropertyValueFactory<Products, Integer>("id"));
        productColumn.setCellValueFactory(new PropertyValueFactory<Products, String>("nameproduct"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Products, Integer>("price"));
        idcateggoryColumn.setCellValueFactory(new PropertyValueFactory<Products, Integer>("idcategory"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Products, String>("namecategory"));

        FilteredList<Products> filteredList = new FilteredList<>(list, b -> true); //

        // tim kiem theo textfield
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

        //tim kiem theo combobox
        categoryComboBox.valueProperty().addListener((observableValue, s, t1) -> {
            filteredList.setPredicate(products -> {
                if (t1 == null || t1.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = t1.toLowerCase();
                if (products.getNamecategory().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Products> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(productTableView.comparatorProperty());
        productTableView.setItems(sortedList);
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

    public void getDataForSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,100,1);
        quantitySpinner.setValueFactory(valueFactory);
    }

    public void addListenerForProduct() {
        productTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, products, t1) -> {
            if (t1 != null) {
                cancelProductButton.setDisable(false);
                addProductButton.setDisable(false);
                quantitySpinner.setDisable(false);

                productTextField.setText(t1.getNameproduct());
                priceTextField.setText(String.valueOf(t1.getPrice()));
                getDataForSpinner();
                totalpriceTextField.setText(String.valueOf(t1.getPrice()));

                quantitySpinner.valueProperty().addListener((observableValue1, integer, t11) -> {
                    if (t11 != null) {
                        int Total = t1.getPrice() * t11.intValue();
                        totalpriceTextField.setText(String.valueOf(Total));
                    }
                });
            } else {
                productTextField.setPromptText("Tên Sản Phẩm");
                priceTextField.setPromptText("Giá Tiền");
                totalpriceTextField.setPromptText("Tổng tiền");
                cancelProductButton.setDisable(true);
                addProductButton.setDisable(true);
                quantitySpinner.setDisable(true);
            }
        });
    }

    public void openModalWindow(String resource, String tittle) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(resource));
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
