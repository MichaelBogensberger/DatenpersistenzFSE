package ui;

import dataaccess.Course.MySqlCourseRepository;
import dataaccess.DatabaseExeption;
import dataaccess.Course.MyCourseRepository;
import dataaccess.Student.MyStudentRepository;
import domain.Course.Course;
import domain.Course.CourseType;
import domain.InvalidValueExeption;
import domain.Student.Student;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Cli {

    Scanner scan;
    MyCourseRepository courseReporepo;
    MyStudentRepository studentRepo;

    public Cli(MyCourseRepository courseReporepo, MyStudentRepository studentRepo) {
        this.courseReporepo = courseReporepo;
        this.scan = new Scanner(System.in);
        this.studentRepo = studentRepo;
    }

    public void start() {
        String input = "-";

        while(!input.equals("x")) {
            showMenue();
            input = scan.nextLine();
            switch(input) {
                case "1":
                    addCourse();
                    break;
                case "2":
                    showAllCourses();
                    break;
                case "3":
                    showCourseDetails();
                    break;
                case "4":
                    updateCourseDetails();
                    break;
                case "5":
                    deleteCourse();
                    break;
                case "6":
                    courseSearch();
                    break;
                case "7":
                    runningCourses();
                    break;
                case "8":
                    addStudent();
                    break;
                case "9":
                    showAllStudents();
                    break;
                case "10":
                    showStudentDetails();
                    break;
                case "11":
                    deleteStudent();
                    break;
                case "12":
                    studentSearch();
                    break;
                case "13":
                    findStudentByBirthDate();
                    break;
                case "14":
                    findStudentBetweenDates();
                    break;
                case "x":
                    System.out.println("beenden");
                    break;
                default:
                    inputError();
                    break;
            }

        }
        scan.close();


    }

    private void findStudentBetweenDates() {
        System.out.println("Geben Sie bitte ein Start-Datum ein (YYYY-MM-DD):");
        Date beginDate = Date.valueOf(scan.nextLine());

        System.out.println("Geben Sie bitte ein End-Datum ein (YYYY-MM-DD)");
        Date endDate = Date.valueOf(scan.nextLine());

        List<Student> studentList;

        try {
            studentList = studentRepo.findAllWithBirthDatesBetween(beginDate,endDate);

            for (Student student: studentList) {
                System.out.println(student);
            }


        } catch (DatabaseExeption databaseExeption) {
            System.out.println("DatenbankFehler bei Studentensuche: " +databaseExeption.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler: " + e.getMessage());
        }

    }

    private void findStudentByBirthDate() {
        System.out.println("Bitte geben Sie ein Datum ein (YYYY-MM-DD):");
        Date searchDate = Date.valueOf(scan.nextLine());
        List<Student> studentList;

        try {
            studentList = studentRepo.findAllByBirthDate(searchDate);

            for (Student student: studentList) {
                System.out.println(student);
            }


        } catch (DatabaseExeption databaseExeption) {
            System.out.println("DatenbankFehler bei Studentensuche: " +databaseExeption.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler: " + e.getMessage());
        }

    }

    private void studentSearch() {
        System.out.println("Geben Sie einen Suchbegriff ein:");
        String searchString = scan.nextLine();
        List<Student> studentList;

        try {
            studentList = studentRepo.findAllByFirstnameOrLastname(searchString);

            for(Student student : studentList) {
                System.out.println(student);
            }

        } catch (DatabaseExeption databaseExeption) {
            System.out.println("DatenbankFehler bei Studentensuche: " +databaseExeption.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler: " + e.getMessage());
        }
    }

    private void addStudent() {
        String firstname, lastname;
        Date birthDate;


        try {
            System.out.println("Geben Sie nun bitte folgende Daten ein:");

            System.out.println("Vorname:");
            firstname = scan.nextLine();
            if(firstname.equals("")) {throw new IllegalArgumentException("Argument darf nicht leer sein!");}

            System.out.println("Nachname:");
            lastname = scan.nextLine();
            if(lastname.equals("")) {throw new IllegalArgumentException("Argument darf nicht leer sein!");}

            System.out.println("Geburtsdatum (YYYY-MM-DD):");
            birthDate = Date.valueOf(scan.nextLine());


            Optional<Student> optionalStudent = studentRepo.insert(
                    new Student(firstname,lastname,birthDate)
            );

            if(optionalStudent.isPresent()) {
                System.out.println("Student angelegt: " +optionalStudent.get());
            } else {
                System.out.println("Student konnte nicht angelegt werden!");
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefahler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueExeption invalidValueExeption) {
            System.out.println("Studentendaten nicht korrekt angegeben: " + invalidValueExeption.getMessage());
        } catch (DatabaseExeption databaseExeption) {
            System.out.println("Datenbankfehler beim einf??gen: " + databaseExeption.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler: " + e.getMessage());
        }
    }

    private void deleteStudent() {
        System.out.println("Welchen Studenten m??chten Sie l??schen? Bitte ID eingeben:");
        Long studentIdToDelete = Long.parseLong(scan.nextLine());

        try {
            studentRepo.deleteByID(studentIdToDelete);
            System.out.println("Student mit ID " + studentIdToDelete + " gel??scht");

        } catch (DatabaseExeption databaseExeption) {
            System.out.println("Datenbankfehler beim l??schen: " + databaseExeption.getMessage());
        }
        catch (Exception e) {
            System.out.println("Unbekannter Fehler beim l??schen: " + e.getMessage());
        }
    }

    private void showAllStudents() {
        List<Student> list = null;
        try {
            list = studentRepo.getAll();
            if (list.size() > 0) {
                for (Student student : list) {
                    System.out.println(student);
                }
            } else {
                System.out.println("Studententabelle leer!");
            }
        } catch (DatabaseExeption databaseExeption) {
            System.out.println("Datenbankfehler bei Anzeige aller Studenten: " + databaseExeption.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei anzeige aller Studenten: " +exception.getMessage());
        }
    }

    private void showStudentDetails() {
        System.out.println("Welchen Studenten m??chten Sie anzeigen (id)?");
        Long studentId = Long.parseLong(scan.nextLine());
        try {
            Optional<Student> studentOptional = studentRepo.getById(studentId);
            if(studentOptional.isPresent()) {
                System.out.println(studentOptional.get());
            } else {
                System.out.println("Student mit der Id: " + studentId + " nicht gefunden!");
            }

        } catch (DatabaseExeption databaseExeption) {
            System.out.println("Datenbankfehler bei Student Anzeige: " +databaseExeption.getMessage());
        } catch (Exception exeption) {
            System.out.println("Unbekannter Fehler bei Student Anzeige: " +exeption.getMessage());
        }
    }

    private void runningCourses() {
        System.out.println("Aktuelle laufende Kurse:");
        List<Course> courseList;

        try {
            courseList = courseReporepo.findAllRunningCourses();

            for(Course course : courseList) {
                System.out.println(course);
            }

        } catch (DatabaseExeption databaseExeption) {
            System.out.println("Datenbankfehler: " +databaseExeption.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler: " + e.getMessage());
        }

    }

    private void courseSearch() {
        System.out.println("Geben Sie einen Suchbegriff ein:");
        String searchString = scan.nextLine();
        List<Course> courseList;

        try {
            courseList = courseReporepo.findAllCoursesByNameOrDescription(searchString);

            for(Course course : courseList) {
                System.out.println(course);
            }

        } catch (DatabaseExeption databaseExeption) {
            System.out.println("DatenbankFehler bei Kurssuche: " +databaseExeption.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler: " + e.getMessage());
        }

    }

    private void deleteCourse() {
        System.out.println("Welchen Kurs m??chten Sie l??schen? Bitte ID eingeben:");
        Long courseIdToDelete = Long.parseLong(scan.nextLine());

        try {
            courseReporepo.deleteByID(courseIdToDelete);
            System.out.println("Kurs mit ID " + courseIdToDelete + " gel??scht");

        } catch (DatabaseExeption databaseExeption) {
            System.out.println("Datenbankfehler beim l??schen: " + databaseExeption.getMessage());
        }
        catch (Exception e) {
            System.out.println("Unbekannter Fehler beim l??schen: " + e.getMessage());
        }

    }

    private void updateCourseDetails() {
        System.out.println("F??r welche Kurs-ID m??chten Sie die Kursdetails ??ndern");
        Long courseId = Long.parseLong(scan.nextLine());

        try {
            Optional<Course> courseOptional = courseReporepo.getById(courseId);
            if(courseOptional.isEmpty()) {
                System.out.println("Kurs mit der gegebenen ID nicht in der Datenbank");
            } else {
                System.out.println("??nderungen f??r folgenden Kurs: ");
                Course course = courseOptional.get();
                System.out.println(course);

                String name, description, hours, dateFrom, dateTo, courseType;
                System.out.println("Bitte neue Kursdaten angeben (Enter falls keine ??nderung gew??nscht ist):");

                System.out.println("Name:");
                name = scan.nextLine();
                System.out.println("Beschreibung:");
                description = scan.nextLine();
                System.out.println("Stundenanzahl:");
                hours = scan.nextLine();
                System.out.println("Startdatum (YYYY-MM-DD):");
                dateFrom = scan.nextLine();
                System.out.println("Enddatum (YYYY-MM-DD):");
                dateTo = scan.nextLine();
                System.out.println("Kurstyp (ZA/BF/FF/OE):");
                courseType = scan.nextLine();


                Optional<Course> optionalCourseUpdated = courseReporepo.update(
                  new Course(
                          course.getId(),
                          name.equals("") ? course.getName() : name,
                          description.equals("") ? course.getDescription() : description,
                          hours.equals("") ? course.getHours() : Integer.parseInt(hours),
                          dateFrom.equals("") ? course.getBeginDate() : Date.valueOf(dateFrom),
                          dateTo.equals("") ? course.getEndDate() : Date.valueOf(dateTo),
                          courseType.equals("") ? course.getCourseType() : CourseType.valueOf(courseType)
                  )
                );

                optionalCourseUpdated.ifPresentOrElse(
                        (c) -> System.out.println("Kurs aktualisiert: " + c),
                        () -> System.out.println("Kurs konnte nicht aktualisiert werden!")
                );


            }


        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefahler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueExeption invalidValueExeption) {
            System.out.println("Kursdaten nicht korrekt angegeben: " + invalidValueExeption.getMessage());
        } catch (DatabaseExeption databaseExeption) {
            System.out.println("Datenbankfehler beim einf??gen: " + databaseExeption.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler: " + e.getMessage());
        }

    }


    private void addCourse() {
        String name, description;
        int hours;
        Date dateFrom, dateTo;
        CourseType courseType;

        try {
            System.out.println("Geben Sie nun bitte folgende Daten ein:");

            System.out.println("Name:");
            name = scan.nextLine();
            if(name.equals("")) {throw new IllegalArgumentException("Argument darf nicht leer sein!");}

            System.out.println("Beschreibung:");
            description = scan.nextLine();
            if(description.equals("")) {throw new IllegalArgumentException("Argument darf nicht leer sein!");}

            System.out.println("Stundenanzahl:");
            hours = Integer.parseInt(scan.nextLine());
            System.out.println("Startdatum (YYYY-MM-DD):");
            dateFrom = Date.valueOf(scan.nextLine());

            System.out.println("Enddatum (YYYY-MM-DD):");
            dateTo = Date.valueOf(scan.nextLine());

            System.out.println("Kurstyp (ZA/BF/FF/OE) :");
            courseType = CourseType.valueOf(scan.nextLine());

            Optional<Course> optionalCourse = courseReporepo.insert(
                    new Course(name,description,hours,dateFrom,dateTo,courseType)
            );

            if(optionalCourse.isPresent()) {
                System.out.println("Kurs angelegt: " +optionalCourse.get());
            } else {
                System.out.println("Kurs konnte nicht angelegt werden!");
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefahler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueExeption invalidValueExeption) {
            System.out.println("Kursdaten nicht korrekt angegeben: " + invalidValueExeption.getMessage());
        } catch (DatabaseExeption databaseExeption) {
            System.out.println("Datenbankfehler beim einf??gen: " + databaseExeption.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler: " + e.getMessage());
        }

    }


    private void showCourseDetails() {
        System.out.println("F??r welchen Kurs m??chten Sie die Kursdetails anzeigen?");
        Long courseId = Long.parseLong(scan.nextLine());
        try {
            Optional<Course> courseOptional = courseReporepo.getById(courseId);
            if(courseOptional.isPresent()) {
                System.out.println(courseOptional.get());
            } else {
                System.out.println("Kurs mit der Id: " + courseId + " nicht gefunden!");
            }

        } catch (DatabaseExeption databaseExeption) {
            System.out.println("Datenbankfehler bei Kursdetail Anzeige: " +databaseExeption.getMessage());
        } catch (Exception exeption) {
            System.out.println("Unbekannter Fehler bei Kursdetail ANzeige: " +exeption.getMessage());
        }

    }

    private void showAllCourses() {
        List<Course> list = null;
        try {
            list = courseReporepo.getAll();
            if (list.size() > 0) {
                for (Course course : list) {
                    System.out.println(course);
                }
            } else {
                System.out.println("Kursliste leer!");
            }
        } catch (DatabaseExeption databaseExeption) {
            System.out.println("Datenbankfehler bei Anzeige aller Kurse: " + databaseExeption.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei anzeige aller Kurse: " +exception.getMessage());
        }
    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen der Men??auswahl eingeben!");
    }

    private void showMenue() {
        System.out.println("--------------------- KURSMANAGEMENT ---------------------");
        System.out.println("(1) Kurs eingeben \t (2) Alle Kurse anzeigen \t (3) Kursdetails anzeigen \t (4) Kursdetails ??ndern \t (5) Kurs l??schen \t (6) Kurs suchen \t (7) Laufende Kurse anzeigen");
        System.out.println("(8) Student eingeben \t (9) Alle Studenten anzeigen \t (10) Studentendetails anzeigen \t (11) Student l??schen \t (12) Studenten suchen (VN, NN)");
        System.out.println("(13) Studenten mit Geburtsdatum finden \t (14) Studenten mit Geburtsdatum zwischen finden \t (x) Ende");
    }


}
