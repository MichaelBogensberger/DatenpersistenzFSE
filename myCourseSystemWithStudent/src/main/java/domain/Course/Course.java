package domain.Course;

import domain.BaseEntity;
import domain.InvalidValueExeption;

import java.sql.Date;

public class Course extends BaseEntity {

    private String name;
    private String description;
    private int hours;
    private Date beginDate;
    private Date endDate;
    private CourseType  courseType;

    public Course(Long id, String name, String description, int hours, Date beginDate, Date endDate, CourseType courseType)
            throws InvalidValueExeption {
        super(id);
        this.setName(name);
        this.setDescription(description);
        this.setHours(hours);
        this.setBeginDate(beginDate);
        this.setEndDate(endDate);
        this.setCourseType(courseType);
    }

    public Course(String name, String description, int hours, Date beginDate, Date endDate, CourseType courseType)
            throws InvalidValueExeption {
        super(null);
        this.setName(name);
        this.setDescription(description);
        this.setHours(hours);
        this.setBeginDate(beginDate);
        this.setEndDate(endDate);
        this.setCourseType(courseType);
    }


    // GETTER und SETTER
    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidValueExeption {
        if(name!=null && name.length()>1) {
            this.name = name;
        } else {
            throw new InvalidValueExeption("Kursname muss mindestens zwei Zeichen lang sein");
        }

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws InvalidValueExeption {
        if(description != null && description.length()>10) {
            this.description = description;
        } else {
            throw new InvalidValueExeption("Description muss mindestens 10 Zeichen lang sein");
        }
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) throws InvalidValueExeption {
        if(hours > 0 && hours < 10) {
            this.hours = hours;
        } else {
            throw new InvalidValueExeption("Stunden müssen zwischen 0 und 10 liegen");
        }

    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) throws InvalidValueExeption {
        if(beginDate != null) {
            if(this.endDate != null) {
                if(beginDate.before(this.endDate)) {
                    this.beginDate = beginDate;
                } else {
                    throw new InvalidValueExeption("Kursbeginn muss vor Kursende sein");
                }
            } else {
                this.beginDate = beginDate;
            }
        } else {
            throw new InvalidValueExeption("Kursbeginn darf nicht leer sein");
        }


    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) throws InvalidValueExeption {
        if(endDate != null) {
            if(this.beginDate != null) {
                if(endDate.after(this.beginDate)) {
                    this.endDate = endDate;
                } else {
                    throw new InvalidValueExeption("Kursende muss nach Kursbeginn sein");
                }
            } else {
                this.endDate = endDate;
            }
        } else {
            throw new InvalidValueExeption("Kursende darf nicht leer sein");
        }
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) throws InvalidValueExeption {
        if(courseType!= null) {
            this.courseType = courseType;
        } else {
            throw new InvalidValueExeption("Kurstyp darf nicht leer sein");
        }

    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + this.getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", hours=" + hours +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", courseType=" + courseType +
                '}';
    }


}
