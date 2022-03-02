package domain;

public class InvalidValueExeption extends RuntimeException {
    public InvalidValueExeption(String message) {
        super(message);
    }
}
