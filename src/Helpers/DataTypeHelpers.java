package Helpers;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DataTypeHelpers {
    public static String convertDateToISO(Date date) {
        Instant instant = date.toInstant();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC);
        return formatter.format(instant);
    }

    public static String convertToDateOnlyISO(Date date) {
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(ZoneId.of("UTC")).toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return localDate.format(formatter);
    }

    public static Date parseDateString(String dateString) {
        String pattern = "yyyy-MM-dd";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }
}