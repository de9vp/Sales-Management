package sample.Database;

import javafx.fxml.FXML;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DBConnection {
    //Connection connect = null;
    //public static DBConnection handler = null;





    public static Connection DBConn() {
        try {
            String url = "jdbc:sqlserver://DESKTOP-QEN4LJI\\SQLEXPRESS;database=DBJavaFx";
            String user = "sa";
            String password = "sa";

            Connection connect = DriverManager.getConnection(url, user, password);
            return connect;

        } catch (SQLException e) {
            System.out.println("Opps, there's an error:");
            e.printStackTrace();
            return null;
        }
    }

    //get Instance
    /*public static DBConnection getInstance() {
        if(handler == null) {
            handler = new DBConnection();
        }
        return handler;
    }*/

}
