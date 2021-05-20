package sample.entity;

public class Member {
    private int id;
    private int code;
    private String name;


    public Member(int id, int code, String name) {
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

    public int getCode() {
        return code;
    }
}
