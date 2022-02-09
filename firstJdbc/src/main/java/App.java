import java.sql.*;

public class App {

    public static void main(String[] args) {

        findAllCoursesFromStudent(3);
        addCourseToStudent(3,3);
        findAllCoursesFromStudent(3);
        //findAllByNameLike("Mi");
        //deleteStudentDemo();
        //updateStudentDemo();
        //insertStudentDemo();
        //selectAllDemo();

    }

    public static Connection getConnection() throws SQLException {
        String connectionUrl = "jdbc:mysql://localhost:3307/jdbcdemo";
        String username = "root";
        String pw = "";
        return DriverManager.getConnection(connectionUrl, username, pw);
    }


    public static void addCourseToStudent(int studentID, int courseId) {

        try(Connection conn = getConnection() ) {
            System.out.println("DB Verbindung ok");

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO student_course (student_id, course_id) VALUES(?,?);");
            try {
                preparedStatement.setInt(1, studentID);
                preparedStatement.setInt(2, courseId);

                int rowAffected = preparedStatement.executeUpdate();
                System.out.println("Datensätze eingefügt: " + rowAffected);
            } catch (SQLException ex) {
                System.out.println("Fehler im SQL-INSERT Statment" + ex.getMessage());
            }


        } catch (SQLException e) {
            System.out.println("DB Verbindung fehlgeschlagen " + e.getMessage());
        }
    }

    public static void findAllCoursesFromStudent(int studentId) {
        String sqlStatement = "SELECT c.coursename, c.room, c.id FROM student s JOIN student_course sc ON s.id = sc.student_id JOIN course c ON c.id = sc.course_id WHERE s.id = ?";

        try(Connection conn = getConnection() ) {
            System.out.println("DB Verbindung ok");

            PreparedStatement preparedStatement = conn.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, studentId);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next())
            {
                int r_id = rs.getInt("id");
                String r_coursename = rs.getString("coursename");
                String r_room = rs.getString("room");
                System.out.println("Kurse für den Studenten["+studentId+"] aus DB -> [courseId]:" +r_id+
                        "\t[coursename]:" +r_coursename+
                        "\t\t[room]:" +r_room);
            }



        } catch (SQLException e) {
            System.out.println("DB Verbindung fehlgeschlagen " + e.getMessage());
        }
    }

    public static void findAllByNameLike(String name) {
        String sqlSelectAllByNameLike = "SELECT * FROM `student` WHERE `student`.`name` LIKE ? ";

        try(Connection conn = getConnection() ) {
            System.out.println("DB Verbindung ok");

            PreparedStatement preparedStatement = conn.prepareStatement(sqlSelectAllByNameLike);
            preparedStatement.setString(1, "%"+name+"%");
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next())
            {
                int r_id = rs.getInt("id");
                String r_name = rs.getString("name");
                String r_email = rs.getString("email");
                System.out.println("Student aus DB -> [ID]:" +r_id+
                        "\t[name]:" +r_name+
                        "\t\t[email]:" +r_email);
            }



        } catch (SQLException e) {
            System.out.println("DB Verbindung fehlgeschlagen " + e.getMessage());
        }

    }

    public static void deleteStudentDemo() {
        try(Connection conn = getConnection() ) {
            System.out.println("DB Verbindung ok");

            int id = 5;
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "DELETE FROM `student` WHERE `student`.`id`=" +id);
            try {

                int rowAffected = preparedStatement.executeUpdate();
                System.out.println("Datensätze entfernt: " + rowAffected);
            } catch (SQLException ex) {
                System.out.println("Fehler im SQL-UPDATE Statment" + ex.getMessage());
            }


        } catch (SQLException e) {
            System.out.println("DB Verbindung fehlgeschlagen " + e.getMessage());
        }
    }

    public static void updateStudentDemo() {
        try(Connection conn = getConnection() ) {
            System.out.println("DB Verbindung ok");

            int id = 2;
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "UPDATE `student` SET `name` = ? WHERE `student`.`id` =" + id);
            try {
                preparedStatement.setString(1, "Noggolas");
                int rowAffected = preparedStatement.executeUpdate();
                System.out.println("Datensätze geändert: " + rowAffected);
            } catch (SQLException ex) {
                System.out.println("Fehler im SQL-UPDATE Statment" + ex.getMessage());
            }


        } catch (SQLException e) {
            System.out.println("DB Verbindung fehlgeschlagen " + e.getMessage());
        }
    }

    public static void insertStudentDemo() {
        try(Connection conn = getConnection() ) {
            System.out.println("DB Verbindung ok");

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO `student` (`id`, `name`, `email`)" +
                            " VALUES (NULL, ?, ?)");
            try {
                preparedStatement.setString(1, "Michael");
                preparedStatement.setString(2, "mbogensberger@gmail.com");
                int rowAffected = preparedStatement.executeUpdate();
                System.out.println("Datensätze eingefügt: " + rowAffected);
            } catch (SQLException ex) {
                System.out.println("Fehler im SQL-INSERT Statment" + ex.getMessage());
            }


        } catch (SQLException e) {
            System.out.println("DB Verbindung fehlgeschlagen " + e.getMessage());
        }

    }

    public static void selectAllDemo() {
        String sqlSelectAllPersons = "SELECT * FROM `student`";

        try(Connection conn = getConnection() ) {
            System.out.println("DB Verbindung ok");

            PreparedStatement preparedStatement = conn.prepareStatement(sqlSelectAllPersons);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next())
            {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                System.out.println("Student aus DB -> [ID]:" +id+
                        "\t[name]:" +name+
                        "\t\t[email]:" +email);
            }



        } catch (SQLException e) {
            System.out.println("DB Verbindung fehlgeschlagen " + e.getMessage());
        }


    }

}
