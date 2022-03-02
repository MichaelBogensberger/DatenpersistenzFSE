package util;

import dataaccess.DatabaseExeption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbFunctions {

    public static int countCoursesInDbWithId(Long id, Connection con) {
        try {
            String countSql = "SELECT COUNT(*) FROM courses WHERE id = ?";
            PreparedStatement preparedStatementCount = con.prepareStatement(countSql);
            preparedStatementCount.setLong(1, id);
            ResultSet resultSetCount = preparedStatementCount.executeQuery();
            resultSetCount.next();
            int courseCount = resultSetCount.getInt(1);
            return courseCount;
        } catch (SQLException sqlException) {
            throw new DatabaseExeption(sqlException.getMessage());
        }

    }


}
