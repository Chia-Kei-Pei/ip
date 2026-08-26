package exceptions;

/**
 * Signals that an invalid or out-of-bounds task index was specified.
 */
public class InvalidIndexException extends BertException {
    /**
     * Constructs an {@code InvalidIndexException} with a default error message.
     */
    public InvalidIndexException() {
        super("The specified task index is invalid.");
    }

    /**
     * Constructs an {@code InvalidIndexException} with the specified detail message.
     *
     * @param message The detail message describing the error.
     */
    public InvalidIndexException(String message) {
        super(message);
    }
}
