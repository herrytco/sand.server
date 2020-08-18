package systems.nope.worldseed.util.exceptions;

public class AlreadyExistingException extends RuntimeException {
    public AlreadyExistingException() {
        super("Entity already exists!");
    }
}
