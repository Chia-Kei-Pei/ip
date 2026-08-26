package exceptions;

/**
 * Signals that an unrecognized or unsupported command was entered.
 */
public class UnknownCommandException extends BertException {
    /**
     * Constructs an {@code UnknownCommandException} with the specified detail message.
     *
     * @param message The detail message describing the error.
     */
    public UnknownCommandException(String invalidCommand) {
        super("Unknown command: " + invalidCommand);
    }
}
