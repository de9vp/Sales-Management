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
import sample.entity.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class SalesController implements Initializable {

    public TableView<Products> productTableView;
    public TableColumn<Products, String> productColumn;
    public TableColumn<Products, Integer> priceColumn;
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
    public Button deleteButton;
    public Button checkButton;
    public Button paymentButton;
    public Button cancelinvoiceButton;
    public TextField deductionTextField;
    public TextField discountTextField;
    public TextField provisionalTextField;
    public TextField codeTextField;
    public TextField paidAmountTextField;
    public Button purchasedButton;
    public Button dashboardButton;

    Connection con;
    Window owner;
    ObservableList<Item> itemlist;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = DBConnection.DBConn();
        getCategoriesForCombobox();
        //getSpinner();
        addListenerForProduct();
        addListenerForItem();
        showProduct();
        showOrderProduct();
        handlePayment();

    }

    public void MemberButtonOnAction(ActionEvent actionEvent) {
        try {
            openMember();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PurchasedButtonOnAction(ActionEvent actionEvent) {
        try {
            openPurchase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CancelProductOnAction(ActionEvent actionEvent) {
        productTableView.getSelectionModel().clearSelection();
    }

    public void AddProductOnAcTion(ActionEvent actionEvent) {
        String Productname = productTextField.getText();
        int Price = Integer.parseInt(priceTextField.getText());
        int Quantity = quantitySpinner.getValue();
        int Total = Integer.parseInt(totalpriceTextField.getText());

        itemlist.add(new Item(Productname, Price, Quantity, Total));
        productTableView.getSelectionModel().clearSelection();
        handlePayment();
        handleButtonPayandCancel();
    }

    public void DeleteOnAction(ActionEvent actionEvent) {
        Item item = orderTableView.getSelectionModel().getSelectedItem();
        orderTableView.getItems().remove(item);
        orderTableView.getSelectionModel().clearSelection();
        handlePayment();
        handleButtonPayandCancel();
    }

    public void DashboardOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) dashboardButton.getScene().getWindow();
        stage.close();
    }

    public void CheckCodeOnAction(ActionEvent actionEvent) throws SQLException { // kiem tra ma thanh vien neu dung giam 40%
        String code = codeTextField.getText();
        String query = "SELECT * FROM tblMember WHERE code_member = ? ";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setString(1, code);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            codeTextField.setText("");
            discountTextField.setText("0");
        } else {
            codeTextField.setText(""+ code +"");
            discountTextField.setText("40");
            codeTextField.setDisable(true);
        }
        handlePayment();
    }

    public void PaymentOnAction(ActionEvent actionEvent) throws SQLException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String idinvoice = String.valueOf(timestamp.getTime()); //dat id hoa don theo thoi gian ()
        String Date = timestamp.toString(); // hien thi thoi gian theo dang 2021-12-31 19:30:46.123

        String codeM; // neu bo trong o nhap ma thanh vien thì se luu gia tri String 'null'
        if (codeTextField.getText().isEmpty()) {
            codeM = "null";
        } else {
            codeM = codeTextField.getText();
        }

        int discount = Integer.parseInt(discountTextField.getText());
        int total = Integer.parseInt(paidAmountTextField.getText());

//        System.out.println("" + idinvoice +"");
//        System.out.println("" + date +"");

        //Tao hoa don add vao tblInvoice
        executeQuery(" INSERT INTO tblInvoice (id, code_member, datecreated, discount, total) " +
                "VALUES ( '"+ idinvoice +"' , '"+ codeM +"' , '"+ Date +"' , '"+ discount +"' , '"+ total +"' ) ");

        //xet tung hang trong (item) trong table view add vao tblInvoiceDetail
        for (Item i : itemlist) {
            int id = getIdByNameproduct(i.getNameProduct());
            executeQuery("INSERT INTO tblInvoiceDetail" +
                    " ( id_invoice, id_product, quantity, price, totalprice, datepurchase ) " +
                    "VALUES ( '"+ idinvoice +"', '"+ id +"', '"+ i.getQuantity() +"', '"+ i.getPrice() +"', '"+ i.getTotal() +"', '"+ Date +"') ");
        }
        orderTableView.getItems().clear();
        handlePayment();
        handleButtonPayandCancel();
        codeTextField.setDisable(false);
        codeTextField.setText("");
        discountTextField.setText("0");

        //showAlert(Alert.AlertType.CONFIRMATION, owner, "Thông báo!", "Thanh toán thành công!");
        System.out.println("Thanh toán thành công!");
    }

    public void CancelInvoiceOnAction(ActionEvent actionEvent) {
        if (itemlist.isEmpty()) {
            //showAlert(Alert.AlertType.ERROR, owner, "Cảnh báo!", "Hóa đơn đang trống. Mời thêm món!");
            System.out.println("Hóa đơn đang trống. Mời thêm món!!");
        } else {
            orderTableView.getItems().clear();
            handlePayment();
            System.out.println("Hủy hóa đơn thành công!");
            handleButtonPayandCancel();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getIdByNameproduct(String nameproduct) throws SQLException {
        int id = 0;
        PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM tblProduct WHERE name_product = ?");
        preparedStatement.setString(1, nameproduct);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            id = resultSet.getInt("id_product");
        }
        return id;
    }

    public void showOrderProduct() {
        itemlist = FXCollections.observableArrayList();

        productOrderColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("nameProduct")); //nameProduct lay ten bien theo method setNameProduct của class Item
        priceOrderColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantity"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("total"));

        orderTableView.setItems(itemlist);
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

        productColumn.setCellValueFactory(new PropertyValueFactory<Products, String>("nameproduct"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Products, Integer>("price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Products, String>("namecategory"));

        FilteredList<Products> filteredList = new FilteredList<>(list, b -> true); //

        // tim kiem theo textfield (theo ten va gia tien)
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

        //tim kiem theo combobox( Theo the loai)
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

                products = new Products(rs.getString("name_product"),
                        rs.getInt("price"), nameCategory);
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
                productTextField.setText("");
                productTextField.setPromptText("Tên Sản Phẩm");

                priceTextField.setText("");
                priceTextField.setPromptText("Giá Tiền");

                totalpriceTextField.setText("");
                totalpriceTextField.setPromptText("Tổng tiền");

                cancelProductButton.setDisable(true);
                addProductButton.setDisable(true);
                quantitySpinner.setDisable(true);
            }
        });
    }

    public void openMember() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../FXML/frmMember.fxml"));
        Scene fxmlFile = new Scene(root);
        Stage window = (Stage) memberButton.getScene().getWindow();
        window.setScene(fxmlFile);
        window.setTitle("DEMO 8");
        window.show();
    }

    public void openPurchase() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../FXML/frmPurchased.fxml"));
        Scene fxmlFile = new Scene(root);
        Stage window = (Stage) purchasedButton.getScene().getWindow();
        window.setScene(fxmlFile);
        window.setTitle("DEMO 9");
        window.show();
    }

    public void addListenerForItem() {
        orderTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, item, t1) -> {
            if (t1 != null) {
                deleteButton.setDisable(false);
            } else {
                deleteButton.setDisable(true);
            }
        });
    }

    public void handlePayment() {  // ham tinh tong tien tam thoi chua giam gia
        int provisional = 0;
        //tinh tong tat ca gia tri theo 1 cot trong table  view
        provisional = orderTableView.getItems().stream().map(
                (item) -> item.getTotal()).reduce(provisional, (accumulator, _item) -> accumulator + _item);

        provisionalTextField.setText("" + provisional + "");
        deductionTextField.setText("" + ( Integer.parseInt(discountTextField.getText()) * Integer.parseInt(provisionalTextField.getText()) ) / 100 + "");
        paidAmountTextField.setText("" + ( Integer.parseInt(provisionalTextField.getText()) - Integer.parseInt(deductionTextField.getText()) ) + "");
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

    public void handleButtonPayandCancel() {
        if (!orderTableView.getItems().isEmpty()) {
            paymentButton.setDisable(false);
            cancelinvoiceButton.setDisable(false);
        } else {
            paymentButton.setDisable(true);
            cancelinvoiceButton.setDisable(true);
        }
    }
}
