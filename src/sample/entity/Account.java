package sample.entity;

public class Account {
    private int id;
    private String username;
    private String password;
    private String grant;

    public Account(int id, String username, String password, String grant) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.grant = grant;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getGrant() {
        return grant;
    }
}
