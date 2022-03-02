package dataaccess.Student;

import dataaccess.BaseRepository;
import domain.Student.Student;

import java.sql.Date;
import java.util.List;

public interface MyStudentRepository extends BaseRepository<Student, Long> {

    List<Student> findAllByFirstnameOrLastname(String searchText);
    List<Student> findAllByBirthDate(Date birthDate);
    List<Student> findAllWithBirthDatesBetween(Date beginDate, Date endDate);

}

