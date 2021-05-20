package sample.entity;

public class Category {

    private final int id;
    private final String namecategory;

    public Category(int id, String namecategory) {
        this.id = id;
        this.namecategory = namecategory;
    }

    public int getId() {
        return id;
    }

    public String getNamecategory() {
        return namecategory;
    }
}
