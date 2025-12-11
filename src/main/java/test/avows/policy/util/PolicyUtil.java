package test.avows.policy.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PolicyUtil {
    public static String generatePolicyNumber() {
        LocalDateTime localDate = LocalDateTime.now();
        String date = localDate.format(DateTimeFormatter.BASIC_ISO_DATE);
        String time = localDate.format(DateTimeFormatter.ofPattern("HHmmss"));

        return "POL-" + date + "-" + time;
    }
}
