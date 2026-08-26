package exceptions;

/**
 * Signals that a required field or argument for a command is missing or empty.
 */
public class MissingFieldException extends BertException {
    /**
     * Constructs a {@code MissingFieldException} with the specified detail message.
     *
     * @param message The detail message describing the missing field.
     */
    public MissingFieldException(String message) {
        super(message);
    }
}
