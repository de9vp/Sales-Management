package sample.entity;

public class InvoiceDetails {
    private int id;
    private String idInvoice;
    private int idProduct;
    private int price;
    private int quantity;
    private int totalprice;

    public InvoiceDetails(int id, String idInvoice, int idProduct, int price, int quantity, int totalprice) {
        this.id = id;
        this.idInvoice = idInvoice;
        this.idProduct = idProduct;
        this.price = price;
        this.quantity = quantity;
        this.totalprice = totalprice;
    }

    public InvoiceDetails(String idInvoice, int idProduct, int price, int quantity, int totalprice) {
        this.idInvoice = idInvoice;
        this.idProduct = idProduct;
        this.price = price;
        this.quantity = quantity;
        this.totalprice = totalprice;
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
}
