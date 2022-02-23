package ui;

import dataaccess.DatabaseExeption;
import dataaccess.MyCourseRepository;
import domain.Course;

import java.util.List;
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
                    System.out.println("Kurseingabe");
                    break;
                case "2":
                    showAllCourses();
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
        System.out.println("(1) Kurs eingeben \t (2) Alle Kurse anzeigen \t (x) Ende");
    }


}
