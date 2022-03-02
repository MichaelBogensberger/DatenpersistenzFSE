import dataaccess.Course.MySqlCourseRepository;
import dataaccess.Student.MySqlStudentRepository;
import ui.Cli;

import java.sql.SQLException;

public class App {

    public static void main(String[] args) {

        Cli myCli = null;

        try {
            myCli = new Cli(new MySqlCourseRepository(), new MySqlStudentRepository());
            myCli.start();
        } catch (SQLException e) {
            System.out.println("Datenbankfehler: " +e.getMessage() + " SQL State: " + e.getSQLState() );
        } catch (ClassNotFoundException e) {
            System.out.println("Datenbankfehler: " +e.getMessage() );
        }


        /*
        try {
            Connection myConnection = MySqlDatabaseConnection.getConnection("jdbc:mysql://localhost:3306/kurssystem", "root", "");
            System.out.println("Verbindung aufgebaut");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */


    }


}
