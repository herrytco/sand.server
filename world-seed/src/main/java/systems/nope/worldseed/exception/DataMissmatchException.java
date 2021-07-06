package systems.nope.worldseed.exception;

public class DataMissmatchException extends Exception {
    public final String message;

    public DataMissmatchException(String message) {
        this.message = message;
    }
}
