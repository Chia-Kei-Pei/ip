package kpei.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import kpei.exceptions.BertException;

/**
 * Tests the date and time parsing capabilities of {@link DateTimeParser}.
 */
class DateTimeParserTest {

    @Test
    void parseDate_validIsoDate_success() throws BertException {
        assertEquals(LocalDate.of(2026, 8, 29), DateTimeParser.parseDate("2026-08-29"));
        assertEquals(LocalDate.of(2026, 8, 29), DateTimeParser.parseDate("29/8/2026"));
        assertEquals(LocalDate.of(2026, 8, 29), DateTimeParser.parseDate("29 August 2026"));
    }

    @Test
    void parseTime_validIsoTime_success() throws BertException {
        assertEquals(LocalTime.of(16, 0), DateTimeParser.parseTime("16:00"));
        assertEquals(LocalTime.of(16, 0), DateTimeParser.parseTime("1600"));
        assertEquals(LocalTime.of(16, 0), DateTimeParser.parseTime("04:00 PM"));
    }

    @Test
    void parseDate_nullOrBlankInput_exceptionThrown() {
        assertThrows(BertException.class, () -> DateTimeParser.parseDate(null));
        assertThrows(BertException.class, () -> DateTimeParser.parseDate(""));
        assertThrows(BertException.class, () -> DateTimeParser.parseDate("   "));
    }

    @Test
    void parseTime_nullOrBlankInput_exceptionThrown() {
        assertThrows(BertException.class, () -> DateTimeParser.parseTime(null));
        assertThrows(BertException.class, () -> DateTimeParser.parseTime(""));
        assertThrows(BertException.class, () -> DateTimeParser.parseTime("   "));
    }

    @Test
    void parseDate_invalidInput_exceptionThrown() {
        assertThrows(BertException.class, () -> DateTimeParser.parseDate("29/08/26"));
        assertThrows(BertException.class, () -> DateTimeParser.parseDate("2026-99-99"));
        assertThrows(BertException.class, () -> DateTimeParser.parseDate("2026-02-29"));
    }

    @Test
    void parseTime_invalidInput_exceptionThrown() {
        assertThrows(BertException.class, () -> DateTimeParser.parseTime("25:00"));
    }

    @Test
    void formatDateAndTime_validValues_success() {
        assertEquals("Aug 29 2026",
                DateTimeParser.formatDate(LocalDate.of(2026, 8, 29)));
        assertEquals("16:00", DateTimeParser.formatTime(LocalTime.of(16, 0)));
    }
}
