package systems.nope.worldseed.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(int id) {
        super(String.format("Could not find entity with id %d", id));
    }

    public NotFoundException(String seed) {
        super(String.format("Could not find entity with id '%s'", seed));
    }
}
