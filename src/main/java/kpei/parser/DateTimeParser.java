package kpei.parser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import kpei.exceptions.BertException;

/**
 * Parses ISO-8601 date and time strings used by the application.
 */
public class DateTimeParser {

    /**
     * Parses a date in {@code yyyy-MM-dd} format.
     *
     * @param input The raw date string.
     * @return The parsed date.
     * @throws BertException If the input is blank or is not a valid date.
     */
    public static LocalDate parseDate(String input) throws BertException {
        validateInput(input, "Date");

        try {
            return LocalDate.parse(input.trim());
        } catch (DateTimeParseException e) {
            throw new BertException("Invalid date: " + input, e);
        }
    }

    /**
     * Parses a time in {@code HH:mm} format.
     *
     * @param input The raw time string.
     * @return The parsed time.
     * @throws BertException If the input is blank or is not a valid time.
     */
    public static LocalTime parseTime(String input) throws BertException {
        validateInput(input, "Time");

        try {
            return LocalTime.parse(input.trim());
        } catch (DateTimeParseException e) {
            throw new BertException("Invalid time: " + input, e);
        }
    }

    /**
     * Validates that an input string contains a value.
     *
     * @param input The input string to validate.
     * @param valueName The human-readable name of the value.
     * @throws BertException If the input is blank.
     */
    private static void validateInput(String input, String valueName) throws BertException {
        if (input == null || input.isBlank()) {
            throw new BertException(valueName + " string cannot be empty.");
        }
    }
}
