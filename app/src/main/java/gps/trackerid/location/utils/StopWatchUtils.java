package gps.trackerid.location.utils;

public class StopWatchUtils {
    public static String getStopwatch(long j) {
        StringBuilder sb = new StringBuilder();
        long j2 = j / 1000;
        long j3 = j2 / 3600;
        if (j3 > 0) {
            sb.append(number((int) j3));
            sb.append(":");
        }
        long j4 = j2 % 3600;
        sb.append(number((int) (j4 / 60)));
        sb.append(":");
        sb.append(number((int) (j4 % 60)));
        sb.append(".");
        sb.append(number((int) ((j % 1000) / 10)));
        return sb.toString();
    }

    public static String number(int i) {
        if (i < 10) {
            return "0" + i;
        }
        return i + "";
    }

    public static String getTimer(long j) {
        StringBuilder sb = new StringBuilder();
        long j2 = j / 3600;
        if (j2 > 0) {
            sb.append(number((int) j2));
            sb.append(":");
        }
        long j3 = j % 3600;
        sb.append(number((int) (j3 / 60)));
        sb.append(":");
        sb.append(number((int) (j3 % 60)));
        return sb.toString();
    }

}
