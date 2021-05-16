package sample.other;

public class Products {
    private final int id;
    private final String nameproduct;
    private final int price;
    private final String namecategory;

    public Products(int id, String nameproduct, int price, String namecategory) {
        this.id = id;
        this.nameproduct = nameproduct;
        this.price = price;
        this.namecategory = namecategory;
    }

    public int getId() {
        return id;
    }

    public String getNameproduct() {
        return nameproduct;
    }

    public int getPrice() {
        return price;
    }

    public String getNamecategory() {
        return namecategory;
    }
}
