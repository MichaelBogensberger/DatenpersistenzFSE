import dataaccess.MySqlDatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class App {

    public static void main(String[] args) {

        try {
            Connection myConnection = MySqlDatabaseConnection.getConnection("jdbc:mysql://localhost:3307/kurssystem", "root", "");
            System.out.println("Verbindung aufgebaut");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
