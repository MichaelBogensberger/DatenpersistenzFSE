package domain.Student;

import domain.BaseEntity;
import domain.Course.CourseType;
import domain.InvalidValueExeption;

import java.sql.Date;

public class Student extends BaseEntity {
    private String firstname;
    private String lastname;
    private Date birthDate;


    public Student(Long id, String firstname, String lastname, Date birthDate)
            throws InvalidValueExeption {
        super(id);
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.setBirthDate(birthDate);
    }

    public Student(String name, String firstname, String lastname, Date birthDate)
            throws InvalidValueExeption {
        super(null);
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.setBirthDate(birthDate);
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + this.getId() +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }

}
