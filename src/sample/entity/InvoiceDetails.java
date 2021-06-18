package sample.entity;

public class InvoiceDetails {
    private int number;
    private int id;
    private String idInvoice;
    private int idProduct;
    private String nameProduct;
    private int price;
    private int quantity;
    private int totalprice;
    private String datepurchase;

    public InvoiceDetails(int id, String idInvoice, int idProduct, String nameProduct, int price, int quantity, int totalprice, String datepurchase) {
        this.id = id;
        this.idInvoice = idInvoice;
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.price = price;
        this.quantity = quantity;
        this.totalprice = totalprice;
        this.datepurchase = datepurchase;
    }

    public InvoiceDetails(String idInvoice, int idProduct, int price, int quantity, int totalprice) {
        this.idInvoice = idInvoice;
        this.idProduct = idProduct;
        this.price = price;
        this.quantity = quantity;
        this.totalprice = totalprice;
    }

    public InvoiceDetails(int number, String nameProduct, int price, int quantity, int totalprice) {
        this.number = number;
        this.nameProduct = nameProduct;
        this.price = price;
        this.quantity = quantity;
        this.totalprice = totalprice;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(String idInvoice) {
        this.idInvoice = idInvoice;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public String getDatepurchase() {
        return datepurchase;
    }

    public void setDatepurchase(String datepurchase) {
        this.datepurchase = datepurchase;
    }
}
