import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {

    public static void main(String[] args) {

        selectAllDemo();

    }

    public static void selectAllDemo() {
        String sqlSelectAllPersons = "SELECT * FROM `student`";
        String connectionUrl = "jdbc:mysql://localhost:3306/jdbcdemo";
        String username = "root";
        String pw = "";

        try(Connection conn = DriverManager.getConnection(connectionUrl, username, pw)) {
            System.out.println("DB Verbindung ok");
        } catch (SQLException e) {
            System.out.println("DB Verbindung fehlgeschlagen " + e.getMessage());
        }


    }

}
