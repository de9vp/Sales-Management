package sample.entity;

public class Invoice {
    private String id;
    private String code_member;
    private String name_member;
    private String datecreated;
    private int discount;
    private int total;

    public Invoice(String id, String code_member, String name_member, String datecreated, int discount, int total) {
        this.id = id;
        this.code_member = code_member;
        this.name_member = name_member;
        this.datecreated = datecreated;
        this.discount = discount;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode_member() {
        return code_member;
    }

    public void setCode_member(String code_member) {
        this.code_member = code_member;
    }

    public String getName_member() {
        return name_member;
    }

    public void setName_member(String name_member) {
        this.name_member = name_member;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
