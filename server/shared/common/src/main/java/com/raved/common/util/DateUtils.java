package com.raved.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private static final DateTimeFormatter ISO_OFFSET = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    private DateUtils() {}

    public static String formatIsoOffset(OffsetDateTime odt) {
        return odt == null ? null : ISO_OFFSET.format(odt);
    }

    public static String formatIsoDate(LocalDate date) {
        return date == null ? null : ISO_DATE.format(date);
    }

    public static OffsetDateTime nowUtc() {
        return OffsetDateTime.now(ZoneId.of("UTC"));
    }

    public static Instant toInstant(OffsetDateTime odt) {
        return odt == null ? null : odt.toInstant();
    }
}
