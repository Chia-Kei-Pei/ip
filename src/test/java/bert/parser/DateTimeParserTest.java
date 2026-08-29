package bert.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import bert.exceptions.BertException;

/**
 * Tests the date and time parsing capabilities of {@link DateTimeParser}.
 */
class DateTimeParserTest {

    @Test
    void parse_standardFormats_success() throws BertException {
        LocalDateTime expected = LocalDateTime.of(2026, 8, 29, 16, 0);

        // Slash-separated with colon time and compact time
        assertEquals(expected, DateTimeParser.parse("29/8/2026 16:00"));
        assertEquals(expected, DateTimeParser.parse("29/08/2026 1600"));

        // Dash-separated (d-M-yyyy)
        assertEquals(expected, DateTimeParser.parse("29-8-2026 16:00"));
        assertEquals(expected, DateTimeParser.parse("29-08-2026 1600"));

        // ISO-like date with time (yyyy-M-d)
        assertEquals(expected, DateTimeParser.parse("2026-8-29 16:00"));
        assertEquals(expected, DateTimeParser.parse("2026-08-29 1600"));

        // ISO Local Date Time with 'T'
        assertEquals(expected, DateTimeParser.parse("2026-08-29T16:00:00"));
    }

    @Test
    void parse_dateOnlyFormats_defaultsToMidnight() throws BertException {
        LocalDateTime expectedMidnight = LocalDateTime.of(2026, 8, 29, 0, 0);

        assertEquals(expectedMidnight, DateTimeParser.parse("29/8/2026"));
        assertEquals(expectedMidnight, DateTimeParser.parse("29/08/2026"));
        assertEquals(expectedMidnight, DateTimeParser.parse("29-8-2026"));
        assertEquals(expectedMidnight, DateTimeParser.parse("29-08-2026"));
        assertEquals(expectedMidnight, DateTimeParser.parse("2026-8-29"));
        assertEquals(expectedMidnight, DateTimeParser.parse("2026-08-29"));
    }

    /*
     * Tests various common user datetime formats.
     * Note: Some formats (e.g. slash with yyyy first, dot separators, month names, AM/PM)
     * are not currently supported by DateTimeParser and may cause this test to fail.
     */
    @Test
    void parse_alternativeFormats_success() throws BertException {
        LocalDateTime expected = LocalDateTime.of(2026, 8, 29, 16, 0);

        // Slash with year first (yyyy/MM/dd)
        assertEquals(expected, DateTimeParser.parse("2026/08/29 16:00"));
        assertEquals(expected, DateTimeParser.parse("2026/8/29 1600"));

        // Dot-separated (dd.MM.yyyy)
        assertEquals(expected, DateTimeParser.parse("29.08.2026 16:00"));

        // Datetime with seconds (without 'T')
        assertEquals(expected, DateTimeParser.parse("2026-08-29 16:00:00"));

        // Textual month names
        assertEquals(expected, DateTimeParser.parse("29 Aug 2026 16:00"));
        assertEquals(expected, DateTimeParser.parse("29 August 2026 16:00"));

        // 12-hour AM/PM format
        assertEquals(expected, DateTimeParser.parse("29/08/2026 04:00 PM"));
    }

    @Test
    void parse_nullOrBlankInput_exceptionThrown() {
        assertThrows(BertException.class, () -> DateTimeParser.parse(null));
        assertThrows(BertException.class, () -> DateTimeParser.parse(""));
        assertThrows(BertException.class, () -> DateTimeParser.parse("   "));
    }

    @Test
    void parse_invalidFormat_exceptionThrown() {
        assertThrows(BertException.class, () -> DateTimeParser.parse("not-a-date"));
        assertThrows(BertException.class, () -> DateTimeParser.parse("2026-99-99"));
        assertThrows(BertException.class, () -> DateTimeParser.parse("29/02/2026")); // Not a leap year
    }
}
