package systems.nope.worldseed.exception;

public class AlreadyExistingException extends RuntimeException {
    public AlreadyExistingException() {
        super("Entity already exists!");
    }
}
