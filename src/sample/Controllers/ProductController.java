package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Database.DBConnection;
import sample.other.Products;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    public TableView<Products> productsTableView;
    public TableColumn<Products, Integer> idColumn;
    public TableColumn<Products, String> productColumn;
    public TableColumn<Products, Integer> priceColumn;
    public TableColumn<Products, String> categoryColumn;


    Connection con = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = DBConnection.DBConn();
        showProduct();
    }

    public void showProduct() {
        ObservableList<Products> list = getProductList();
        idColumn.setCellValueFactory(new PropertyValueFactory<Products, Integer>("Id"));
        productColumn.setCellValueFactory(new PropertyValueFactory<Products, String>("Nameproduct"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Products, Integer>("Price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Products, String>("Namecategory"));
        productsTableView.setItems(list);
    }



    private ObservableList<Products> getProductList() {
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
                        rs.getInt("price"), nameCategory);
                productList.add(products);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }
}


