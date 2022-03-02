package ui;

import dataaccess.DatabaseExeption;
import dataaccess.MyCourseRepository;
import domain.Course;
import domain.CourseType;
import domain.InvalidValueExeption;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Cli {

    Scanner scan;
    MyCourseRepository repo;

    public Cli(MyCourseRepository repo) {
        this.repo = repo;
        this.scan = new Scanner(System.in);
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

    private void runningCourses() {
        System.out.println("Aktuelle laufende Kurse:");
        List<Course> courseList;

        try {
            courseList = repo.findAllRunningCourses();

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
            courseList = repo.findAllCoursesByNameOrDescription(searchString);

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
        System.out.println("Welchen Kurs möchten Sie löschen? Bitte ID eingeben:");
        Long courseIdToDelete = Long.parseLong(scan.nextLine());

        try {
            repo.deleteByID(courseIdToDelete);
            System.out.println("Kurs mit ID " + courseIdToDelete + " gelöscht");

        } catch (DatabaseExeption databaseExeption) {
            System.out.println("Datenbankfehler beim löschen: " + databaseExeption.getMessage());
        }
        catch (Exception e) {
            System.out.println("Unbekannter Fehler beim löschen: " + e.getMessage());
        }

    }

    private void updateCourseDetails() {
        System.out.println("Für welche Kurs-ID möchten Sie die Kursdetails ändern");
        Long courseId = Long.parseLong(scan.nextLine());

        try {
            Optional<Course> courseOptional = repo.getById(courseId);
            if(courseOptional.isEmpty()) {
                System.out.println("Kurs mit der gegebenen ID nicht in der Datenbank");
            } else {
                System.out.println("Änderungen für folgenden Kurs: ");
                Course course = courseOptional.get();
                System.out.println(course);

                String name, description, hours, dateFrom, dateTo, courseType;
                System.out.println("Bitte neue Kursdaten angeben (Enter falls keine Änderung gewünscht ist):");

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


                Optional<Course> optionalCourseUpdated = repo.update(
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
            System.out.println("Datenbankfehler beim einfügen: " + databaseExeption.getMessage());
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

            Optional<Course> optionalCourse = repo.insert(
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
            System.out.println("Datenbankfehler beim einfügen: " + databaseExeption.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler: " + e.getMessage());
        }

    }


    private void showCourseDetails() {
        System.out.println("Für welchen Kurs möchten Sie die Kursdetails anzeigen?");
        Long courseId = Long.parseLong(scan.nextLine());
        try {
            Optional<Course> courseOptional = repo.getById(courseId);
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
            list = repo.getAll();
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
        System.out.println("Bitte nur die Zahlen der Menüauswahl eingeben!");
    }

    private void showMenue() {
        System.out.println("--------------------- KURSMANAGEMENT ---------------------");
        System.out.println("(1) Kurs eingeben \t (2) Alle Kurse anzeigen \t (3) Kursdetails anzeigen \t (4) Kursdetails ändern \t (5) Kurs löschen \t (6) Kurs suchen \t (7) Laufende Kurse anzeigen \t (x) Ende");
    }


}
