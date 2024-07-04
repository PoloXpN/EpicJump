package fr.poloxpn.epicjump.utils;

public class Utils {

    public static String getTimeFormated(long time) {
        long seconds = time / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds %= 60;
        minutes %= 60;
        return String.format("%02dh:%02dm:%02ds", hours, minutes, seconds);
    }

    public static long timeFormatedToLong(String s) {
        if (s == null) {
            return 0;
        }
        String[] hms = s.split(":");
        long time = 0;
        if (hms.length == 3) {
            time += Long.parseLong(hms[0]) * 3600000;
            time += Long.parseLong(hms[1]) * 60000;
            time += Long.parseLong(hms[2]) * 1000;
        }
        return time;
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
