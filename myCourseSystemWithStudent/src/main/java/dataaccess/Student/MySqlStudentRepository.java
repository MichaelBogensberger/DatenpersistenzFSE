package dataaccess.Student;

import dataaccess.DatabaseExeption;
import dataaccess.MySqlDatabaseConnection;
import domain.Course.Course;
import domain.Course.CourseType;
import domain.Student.Student;
import util.Assert;
import util.MySqlDbConfig;
import util.MySqlDbFunctions;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlStudentRepository implements MyStudentRepository{

    private Connection con;

    public MySqlStudentRepository() throws SQLException, ClassNotFoundException {
        this.con = MySqlDatabaseConnection.getConnection(MySqlDbConfig.getUrl(), MySqlDbConfig.getUser(), MySqlDbConfig.getPwd());
    }


    @Override
    public Optional<Student> insert(Student entity) {
        Assert.notNull(entity);

        try {
            String sql = "INSERT INTO `students` ( `firstname`, `lastname`, `birthDate`) VALUES ( ?,?,?)";

            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getFirstname());
            preparedStatement.setString(2, entity.getLastname());
            preparedStatement.setDate(3, entity.getBirthDate());

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows == 0) {
                return Optional.empty();
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()) {
                return this.getById(generatedKeys.getLong(1));
            } else {
                return Optional.empty();
            }

        } catch (SQLException sqlException) {
            throw new DatabaseExeption(sqlException.getMessage());
        }
    }



    @Override
    public Optional<Student> getById(Long id) {
        Assert.notNull(id);
        if(MySqlDbFunctions.countStudentsInDbWithId(id, con) == 0) {
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
        String sql = "SELECT * FROM students";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Student> studentList = new ArrayList<>();
            while(resultSet.next()) {
                studentList.add(
                        new Student(
                                resultSet.getLong("id"),
                                resultSet.getString("firstname"),
                                resultSet.getString("lastname"),
                                resultSet.getDate("birthDate")
                        )
                );
            }
            return studentList;
        } catch (SQLException e) {
            throw new DatabaseExeption("Database error occured!");
        }
    }


    @Override
    public Optional<Student> update(Student entity) {
        Assert.notNull(entity);
        String sql = "UPDATE `students` SET `firstname` = ?, `lastname` = ?, `birtDate` = ? WHERE `students`.`id` = ?";

        if(MySqlDbFunctions.countStudentsInDbWithId(entity.getId(), con) == 0) {
            return Optional.empty();
        }

        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, entity.getFirstname());
            preparedStatement.setString(2, entity.getLastname());
            preparedStatement.setDate(3, entity.getBirthDate());

            // for WHERE id = ?
            preparedStatement.setLong(4, entity.getId());

            int affectedRows = preparedStatement.executeUpdate();

            if(affectedRows == 0) {
                return Optional.empty();
            } else {
                return this.getById(entity.getId());
            }

        } catch (SQLException sqlException) {
            throw new DatabaseExeption(sqlException.getMessage());
        }
    }

    @Override
    public void deleteByID(Long id) {
        Assert.notNull(id);
        String sql = "DELETE FROM students WHERE id = ?";
        try {
            if(MySqlDbFunctions.countStudentsInDbWithId(id, con) == 1) {
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException sqlException) {
            throw new DatabaseExeption(sqlException.getMessage());
        }
    }

    @Override
    public List<Student> findAllByFirstnameOrLastname(String searchText) {
        try {
            String sql = "SELECT * FROM students WHERE LOWER(firstname) LIKE LOWER(?) OR LOWER(lastname) LIKE ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%" + searchText + "%");
            preparedStatement.setString(2, "%" + searchText + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Student> studentList = new ArrayList<>();

            while(resultSet.next()) {
                studentList.add(
                        new Student(
                                resultSet.getLong("id"),
                                resultSet.getString("firstname"),
                                resultSet.getString("lastname"),
                                resultSet.getDate("birthDate")
                        ));
            }

            return studentList;

        } catch (SQLException sqlException) {
            throw new DatabaseExeption(sqlException.getMessage());
        }
    }

    @Override
    public List<Student> findAllByBirthDate(Date birthDate) {
        String sql = "SELECT * FROM students WHERE birthDate = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setDate(1, birthDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Student> studentList = new ArrayList<>();

            while (resultSet.next()) {
                studentList.add(
                        new Student(
                                resultSet.getLong("id"),
                                resultSet.getString("firstname"),
                                resultSet.getString("lastname"),
                                resultSet.getDate("birthDate")
                        ));
            }
            return studentList;
        } catch (SQLException sqlException) {
            throw new DatabaseExeption(sqlException.getMessage());
        }
    }

    @Override
    public List<Student> findAllWithBirthDatesBetween(Date beginDate, Date endDate) {
        String sql = "SELECT * FROM students WHERE birthDate BETWEEN ? AND ?";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setDate(1, beginDate);
            preparedStatement.setDate(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Student> studentList = new ArrayList<>();

            while (resultSet.next()) {
                studentList.add(
                        new Student(
                                resultSet.getLong("id"),
                                resultSet.getString("firstname"),
                                resultSet.getString("lastname"),
                                resultSet.getDate("birthDate")
                        ));
            }
            return studentList;

        } catch (SQLException sqlException) {
            throw new DatabaseExeption(sqlException.getMessage());
        }
    }
}
