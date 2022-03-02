package dataaccess.Student;

import dataaccess.DatabaseExeption;
import dataaccess.MySqlDatabaseConnection;
import domain.Course.Course;
import domain.Course.CourseType;
import domain.Student.Student;
import util.Assert;
import util.DbConfig;
import util.DbFunctions;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class MySqlStudentRepository implements MyStudentRepository{

    private Connection con;

    public MySqlStudentRepository() throws SQLException, ClassNotFoundException {
        this.con = MySqlDatabaseConnection.getConnection(DbConfig.getUrl(), DbConfig.getUser(), DbConfig.getPwd());
    }


    @Override
    public Optional<Student> insert(Student entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Student> getById(Long id) {
        Assert.notNull(id);
        if(DbFunctions.countCoursesInDbWithId(id, con) == 0) {
            return Optional.empty();
        } else {
            try {
                String sql = "SELECT * FROM students WHERE id = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();

                Student student = new Student(
                        resultSet.getLong("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getDate("birthDate")

                );

                return Optional.of(student);

            } catch (SQLException sqlException) {
                throw new DatabaseExeption(sqlException.getMessage());
            }

        }
    }



    @Override
    public List<Student> getAll() {
        return null;
    }

    @Override
    public Optional<Student> update(Student entity) {
        return Optional.empty();
    }

    @Override
    public void deleteByID(Long id) {

    }

    @Override
    public List<Student> findAllByFirstnameOrLastname(String searchText) {
        return null;
    }

    @Override
    public List<Student> findAllByBirthDate(Date birthDate) {
        return null;
    }

    @Override
    public List<Student> findAllWithBirthDatesBetween(Date beginDate, Date endDate) {
        return null;
    }
}
