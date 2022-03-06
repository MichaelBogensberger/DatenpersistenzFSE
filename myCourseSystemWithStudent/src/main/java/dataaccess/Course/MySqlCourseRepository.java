package dataaccess.Course;

import dataaccess.DatabaseExeption;
import dataaccess.MySqlDatabaseConnection;
import domain.Course.Course;
import domain.Course.CourseType;
import util.Assert;
import util.MySqlDbConfig;
import util.MySqlDbFunctions;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlCourseRepository implements MyCourseRepository {

    private Connection con;

    public MySqlCourseRepository() throws SQLException, ClassNotFoundException {
        //this.con = MySqlDatabaseConnection.getConnection("jdbc:mysql://localhost:3306/kurssystem", "root", "");
        this.con = MySqlDatabaseConnection.getConnection(MySqlDbConfig.getUrl(), MySqlDbConfig.getUser(), MySqlDbConfig.getPwd());
    }

    @Override
    public Optional<Course> insert(Course entity) {
        Assert.notNull(entity);

        try {
            String sql = "INSERT INTO `courses` ( `name`, `description`, `hours`, `begindate`, `enddate`, `coursetype`) VALUES ( ?,?,?,?,?,?)";

            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setInt(3, entity.getHours());
            preparedStatement.setDate(4, entity.getBeginDate());
            preparedStatement.setDate(5, entity.getEndDate());
            preparedStatement.setString(6, entity.getCourseType().toString());

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
    public Optional<Course> getById(Long id) {
        Assert.notNull(id);
        if(MySqlDbFunctions.countCoursesInDbWithId(id, con) == 0) {
            return Optional.empty();
        } else {
            try {
                String sql = "SELECT * FROM courses WHERE id = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();

                Course course = new Course(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("enddate"),
                        CourseType.valueOf(resultSet.getString("coursetype"))
                );

                return Optional.of(course);

            } catch (SQLException sqlException) {
                throw new DatabaseExeption(sqlException.getMessage());
            }

        }
    }

    @Override
    public List<Course> getAll() {
        String sql = "SELECT * FROM courses";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Course> courseList = new ArrayList<>();
            while(resultSet.next()) {
                courseList.add(
                        new Course(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getInt("hours"),
                            resultSet.getDate("begindate"),
                            resultSet.getDate("enddate"),
                            CourseType.valueOf(resultSet.getString("coursetype"))
                        )
                );
            }
            return courseList;
        } catch (SQLException e) {
            throw new DatabaseExeption("Database error occured!");
        }
    }

    @Override
    public Optional<Course> update(Course entity) {
        Assert.notNull(entity);
        String sql = "UPDATE `courses` SET `name` = ?, `description` = ?, `hours` = ?, `begindate` = ?, `enddate` = ?, `coursetype` = ? WHERE `courses`.`id` = ?";

        if(MySqlDbFunctions.countCoursesInDbWithId(entity.getId(), con) == 0) {
            return Optional.empty();
        }

        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setInt(3, entity.getHours());
            preparedStatement.setDate(4, entity.getBeginDate());
            preparedStatement.setDate(5, entity.getEndDate());
            preparedStatement.setString(6, entity.getCourseType().toString());
            // for WHERE id = ?
            preparedStatement.setLong(7, entity.getId());

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
        String sql = "DELETE FROM courses WHERE id = ?";

        try {

            if(MySqlDbFunctions.countCoursesInDbWithId(id, con) == 1) {
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException sqlException) {
            throw new DatabaseExeption(sqlException.getMessage());
        }

    }

    @Override
    public List<Course> findAllCoursesByName(String name) {
        return null;
    }

    @Override
    public List<Course> findAllCoursesByDescription(String description) {
        return null;
    }

    @Override
    public List<Course> findAllCoursesByNameOrDescription(String searchText) {
        try {
            String sql = "SELECT * FROM courses WHERE LOWER(description) LIKE LOWER(?) OR LOWER(name) LIKE ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%" + searchText + "%");
            preparedStatement.setString(2, "%" + searchText + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Course> courseList = new ArrayList<>();

            while(resultSet.next()) {
                courseList.add(
                        new Course(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                resultSet.getInt("hours"),
                                resultSet.getDate("begindate"),
                                resultSet.getDate("enddate"),
                                CourseType.valueOf(resultSet.getString("coursetype"))
                ));

            }

            return courseList;

        } catch (SQLException sqlException) {
            throw new DatabaseExeption(sqlException.getMessage());
        }
    }

    @Override
    public List<Course> findAllCoursesByStartDate(Date startDate) {
        return null;
    }

    @Override
    public List<Course> findAllCoursesByCourseType(CourseType courseType) {
        return null;
    }

    @Override
    public List<Course> findAllRunningCourses() {
        String sql = "SELECT * FROM courses WHERE NOW() < enddate";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Course> courseList = new ArrayList<>();

            while (resultSet.next()) {
                courseList.add(
                        new Course(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                resultSet.getInt("hours"),
                                resultSet.getDate("begindate"),
                                resultSet.getDate("enddate"),
                                CourseType.valueOf(resultSet.getString("coursetype"))
                        ));
            }
            return courseList;
        } catch (SQLException sqlException) {
            throw new DatabaseExeption(sqlException.getMessage());
        }
    }



}