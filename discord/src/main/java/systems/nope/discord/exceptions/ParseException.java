package systems.nope.discord.exceptions;

public class ParseException extends Exception {
    private final String errorMessage;

    public ParseException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
