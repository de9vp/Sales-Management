package sample.entity;

public class InvoiceDetails {
    private int id;
    private int idInvoice;
    private int idProduct;
    private int price;
    private int quantity;
    private int totalprice;

    public InvoiceDetails(int id, int idInvoice, int idProduct, int price, int quantity, int totalprice) {
        this.id = id;
        this.idInvoice = idInvoice;
        this.idProduct = idProduct;
        this.price = price;
        this.quantity = quantity;
        this.totalprice = totalprice;
    }

    public InvoiceDetails(int idInvoice, int idProduct, int price, int quantity, int totalprice) {
        this.idInvoice = idInvoice;
        this.idProduct = idProduct;
        this.price = price;
        this.quantity = quantity;
        this.totalprice = totalprice;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdInvoice(int idInvoice) {
        this.idInvoice = idInvoice;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public int getId() {
        return id;
    }

    public int getIdInvoice() {
        return idInvoice;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalprice() {
        return totalprice;
    }
}
