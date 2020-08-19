package systems.nope.worldseed.exception;

public class AlreadyExistingException extends RuntimeException {
    public AlreadyExistingException() {
        super("Entity already exists!");
    }

    public AlreadyExistingException(String id) {
        super(String.format("Entity with id '%s' already exists!", id));
    }
}
