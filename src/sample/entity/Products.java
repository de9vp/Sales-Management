package sample.entity;

public class Products {
    private int id;
    private String nameproduct;
    private int price;
    private int idcategory;
    private String namecategory;

    public Products(int id, String nameproduct, int price, String namecategory) {
        this.id = id;
        this.nameproduct = nameproduct;
        this.price = price;
        this.namecategory = namecategory;
    }

    public Products(int id, String nameproduct, int price, int idcategory, String namecategory) {
        this.id = id;
        this.nameproduct = nameproduct;
        this.price = price;
        this.idcategory = idcategory;
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

    public int getIdcategory() {
        return idcategory;
    }

    public String getNamecategory() {
        return namecategory;
    }
}
