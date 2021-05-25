package sample.entity;

public class Item {
    private String productname;
    private int price;
    private int quantity;
    private int total;

    public Item(String productname, int price, int quantity, int total) {
        this.productname = productname;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
    }

    public String getProductName() {
        return productname;
    }

    public void setProductName(String productName) {
        this.productname = productName;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
