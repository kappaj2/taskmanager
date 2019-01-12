package com.ajk.taskman.utilities;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Utility class to handle common date functions.
 */
public class DateUtils {

    private DateUtils() {
    }

    /**
     * Create a zoned LocalDate object using now as start and deducting the offSet to get days-ago.
     *
     * @param offSet
     * @return LocalDate
     */
    public static LocalDate getZonedDate(int offSet) {

        LocalDate startDate = LocalDate.now().minus(offSet, ChronoUnit.DAYS);
        return (ZonedDateTime.of(startDate.atStartOfDay(), ZoneId.of("UTC"))).toLocalDate();
    }

    /**
     * Create a zoned LocalDateTime object using now as start and deducting the offSet to get days-ago.
     *
     * @param offSet
     * @return
     */
    public static LocalDateTime getZonedDateTime(int offSet) {

        LocalDate startDate = LocalDate.now().minus(offSet, ChronoUnit.DAYS);
        return (ZonedDateTime.of(startDate.atStartOfDay(), ZoneId.of("UTC"))).toLocalDateTime();
    }

    /**
     * Generate a localDateTime object.
     *
     * @return
     */
    public static LocalDateTime getCurrentDateTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime;
    }

    /**
     * Generate a timestamp object.
     *
     * @return
     */
    public static Timestamp getCurrentTimestamp() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        return currentTime;
    }
}
