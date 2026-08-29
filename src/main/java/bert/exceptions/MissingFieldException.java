package bert.exceptions;

/**
 * Signals that a required field or argument for a command is missing or empty.
 */
public class MissingFieldException extends BertException {
    /**
     * Constructs a {@code MissingFieldException} with the specified detail message.
     *
     * @param missingField The name of the field that is required when creating a item.
     * @param item The type of item (such as "todo", "event", "deadline").
     */
    public MissingFieldException(String missingField, String item) {
        super(missingField);
    }
}
