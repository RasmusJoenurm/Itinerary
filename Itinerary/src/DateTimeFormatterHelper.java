import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeFormatterHelper {

    // Format for Date (DD MMM YYYY)
    public static String formatDate(String isoDateString) {
        try {
            LocalDateTime date = LocalDateTime.parse(isoDateString, DateTimeFormatter.ISO_DATE_TIME);
            return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        } catch (DateTimeParseException e) {
            return isoDateString;
        }
    }

    // Format for 12 Hour Time (hh:mmAM/PM (±HH:mm))
    public static String format12HourTime(String isoDateString) {
        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(isoDateString, DateTimeFormatter.ISO_DATE_TIME);
            String formattedTime = zonedDateTime.format(DateTimeFormatter.ofPattern("hh:mma"));
            String offset = formatOffset(zonedDateTime);
            return formattedTime + " " + offset;
        } catch (DateTimeParseException e) {
            return isoDateString;
        }
    }

    // Format for 24 Hour Time (HH:mm (±HH:mm))
    public static String format24HourTime(String isoDateString) {
        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(isoDateString, DateTimeFormatter.ISO_DATE_TIME);
            String formattedTime = zonedDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            String offset = formatOffset(zonedDateTime);
            return formattedTime + " " + offset;
        } catch (DateTimeParseException e) {
            return isoDateString;
        }
    }

    // Helper method to format timezone offset (±HH:mm)
    private static String formatOffset(ZonedDateTime zonedDateTime) {
        String offset = zonedDateTime.getOffset().getId();
        if (offset.equals("Z")) {
            return "(+00:00)";
        }

        // Correctly format the offset string
        return String.format("(%s:%s)", 
                             offset.substring(0, 3), 
                             offset.substring(3).replaceAll(":", "")); // Remove any existing colons in the minutes
    }
}
