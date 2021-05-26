package sample.entity;

public class Item {
    private String nameproduct;
    private int price;
    private int quantity;
    private int total;

    public Item(String nameproduct, int price, int quantity, int total) {
        this.nameproduct = nameproduct;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
    }

    public String getNameProduct() {
        return nameproduct;
    }

    public void setNameproduct(String nameProduct) {
        this.nameproduct = nameProduct;
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
