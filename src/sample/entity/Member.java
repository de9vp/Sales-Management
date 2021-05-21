package sample.entity;

public class Member {
    private int id;
    private String code;
    private String name;


    public Member(int id, String code, String name) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
