package bert.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

import bert.exceptions.BertException;

/**
 * Utility class for parsing and formatting date and time strings.
 * <p>
 * The default date-time pattern is {@code dd/MM/yyyy HH:mm} (e.g. {@code 02/12/2019 18:00}).
 * Multiple common formats are supported for user convenience, falling back to a start-of-day
 * time (00:00) when only a date is provided.
 * </p>
 */
public class DateTimeParser {

    /** Default date-time format for input and persistence: dd/MM/yyyy HH:mm */
    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /** Human-readable display format: MMM dd yyyy at HH:mm (e.g. Dec 02 2019 at 18:00) */
    public static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy 'at' HH:mm");

    /** Display format for date-only outputs: MMM dd yyyy (e.g. Dec 02 2019) */
    public static final DateTimeFormatter DISPLAY_DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy");

    /**
     * List of supported date-time formatters attempted during parsing.
     * Flexible with single-digit day/month, multiple separators, 12/24-hour time formats,
     * textual months, and optional colons/seconds.
     */
    private static final List<DateTimeFormatter> DATE_TIME_FORMATTERS = List.of(
            // Slash-separated (d/M/yyyy)
            createFormatter("d/M/yyyy HH:mm"),
            createFormatter("d/M/yyyy HHmm"),
            createFormatter("d/M/yyyy HH:mm:ss"),
            createFormatter("d/M/yyyy h:mm a"),
            createFormatter("d/M/yyyy hh:mm a"),
            createFormatter("d/M/yyyy h:mma"),
            createFormatter("d/M/yyyy hh:mma"),

            // Dash-separated (d-M-yyyy)
            createFormatter("d-M-yyyy HH:mm"),
            createFormatter("d-M-yyyy HHmm"),
            createFormatter("d-M-yyyy HH:mm:ss"),
            createFormatter("d-M-yyyy h:mm a"),
            createFormatter("d-M-yyyy hh:mm a"),
            createFormatter("d-M-yyyy h:mma"),
            createFormatter("d-M-yyyy hh:mma"),

            // ISO-like date with time (yyyy-M-d)
            createFormatter("yyyy-M-d HH:mm"),
            createFormatter("yyyy-M-d HHmm"),
            createFormatter("yyyy-M-d HH:mm:ss"),
            createFormatter("yyyy-M-d h:mm a"),
            createFormatter("yyyy-M-d hh:mm a"),
            createFormatter("yyyy-M-d h:mma"),
            createFormatter("yyyy-M-d hh:mma"),

            // Slash year-first (yyyy/M/d)
            createFormatter("yyyy/M/d HH:mm"),
            createFormatter("yyyy/M/d HHmm"),
            createFormatter("yyyy/M/d HH:mm:ss"),
            createFormatter("yyyy/M/d h:mm a"),
            createFormatter("yyyy/M/d hh:mm a"),
            createFormatter("yyyy/M/d h:mma"),
            createFormatter("yyyy/M/d hh:mma"),

            // Dot-separated (d.M.yyyy)
            createFormatter("d.M.yyyy HH:mm"),
            createFormatter("d.M.yyyy HHmm"),
            createFormatter("d.M.yyyy HH:mm:ss"),
            createFormatter("d.M.yyyy h:mm a"),
            createFormatter("d.M.yyyy hh:mm a"),
            createFormatter("d.M.yyyy h:mma"),
            createFormatter("d.M.yyyy hh:mma"),

            // Dot-separated year-first (yyyy.M.d)
            createFormatter("yyyy.M.d HH:mm"),
            createFormatter("yyyy.M.d HHmm"),
            createFormatter("yyyy.M.d HH:mm:ss"),
            createFormatter("yyyy.M.d h:mm a"),
            createFormatter("yyyy.M.d hh:mm a"),

            // Textual month names (d MMM yyyy / d MMMM yyyy)
            createFormatter("d MMM yyyy HH:mm"),
            createFormatter("d MMM yyyy HHmm"),
            createFormatter("d MMM yyyy HH:mm:ss"),
            createFormatter("d MMM yyyy h:mm a"),
            createFormatter("d MMM yyyy hh:mm a"),
            createFormatter("d MMMM yyyy HH:mm"),
            createFormatter("d MMMM yyyy HHmm"),
            createFormatter("d MMMM yyyy HH:mm:ss"),
            createFormatter("d MMMM yyyy h:mm a"),
            createFormatter("d MMMM yyyy hh:mm a"),

            // ISO Local Date Time with 'T'
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
    );

    /**
     * List of supported date-only formatters attempted when no time is supplied.
     */
    private static final List<DateTimeFormatter> DATE_ONLY_FORMATTERS = List.of(
            createFormatter("d/M/yyyy"),
            createFormatter("d-M-yyyy"),
            createFormatter("yyyy-M-d"),
            createFormatter("yyyy/M/d"),
            createFormatter("d.M.yyyy"),
            createFormatter("yyyy.M.d"),
            createFormatter("d MMM yyyy"),
            createFormatter("d MMMM yyyy"),
            DateTimeFormatter.ISO_LOCAL_DATE
    );

    /**
     * Creates a case-insensitive {@link DateTimeFormatter} for the given pattern in {@link Locale#ENGLISH}.
     *
     * @param pattern Date/time format pattern.
     * @return Formatter configured for case-insensitive English parsing.
     */
    private static DateTimeFormatter createFormatter(String pattern) {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern(pattern)
                .toFormatter(Locale.ENGLISH);
    }

    /**
     * Parses a date-time string into a {@link LocalDateTime} object.
     * <p>
     * Attempts to parse full date-time strings first against supported formats.
     * If no time component is present, attempts date-only patterns and defaults
     * the time to start of day (00:00).
     * </p>
     *
     * @param input The raw date-time string from the user or storage.
     * @return The parsed {@link LocalDateTime}.
     * @throws BertException If the string does not match any recognized date/time format.
     */
    public static LocalDateTime parse(String input) throws BertException {
        if (input == null || input.isBlank()) {
            throw new BertException("Date/time string cannot be empty.");
        }

        String trimmed = input.trim();

        // Try date-time formatters
        for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
            try {
                return LocalDateTime.parse(trimmed, formatter);
            } catch (DateTimeParseException ignored) {
                // Try next pattern
            }
        }

        // Try date-only formatters (default time to 00:00)
        for (DateTimeFormatter formatter : DATE_ONLY_FORMATTERS) {
            try {
                LocalDate date = LocalDate.parse(trimmed, formatter);
                return date.atStartOfDay();
            } catch (DateTimeParseException ignored) {
                // Try next pattern
            }
        }

        throw new BertException(
                "Invalid date/time format: \"" + input + "\"."
                + " Expected DD/MM/YYYY HH:MM (e.g., 02/12/2019 18:00) or DD/MM/YYYY.");
    }

    /**
     * Formats a {@link LocalDateTime} into a standard user-friendly display string.
     * If the time is 00:00, outputs the date only (e.g. {@code Dec 02 2019});
     * otherwise includes time (e.g. {@code Dec 02 2019 at 18:00}).
     *
     * @param dateTime The {@link LocalDateTime} to format.
     * @return Formatted date string for user display.
     */
    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        if (dateTime.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return dateTime.format(DISPLAY_DATE_ONLY_FORMATTER);
        }
        return dateTime.format(DISPLAY_FORMATTER);
    }

    /**
     * Formats a {@link LocalDateTime} into the default storage/file format {@code dd/MM/yyyy HH:mm}.
     *
     * @param dateTime The {@link LocalDateTime} to format.
     * @return Formatted string suitable for file persistence.
     */
    public static String formatForStorage(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DEFAULT_FORMATTER);
    }
}
