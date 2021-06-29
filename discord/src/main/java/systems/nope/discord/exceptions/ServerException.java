package systems.nope.discord.exceptions;

public class ServerException extends Exception {
    private final String message;

    public ServerException(String message) {
        this.message = message;
    }
}
