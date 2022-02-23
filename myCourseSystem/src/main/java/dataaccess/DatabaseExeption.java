package dataaccess;

public class DatabaseExeption extends RuntimeException {
    public DatabaseExeption(String message) {
        super(message);
    }
}
