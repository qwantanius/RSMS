package RSMS.common.helpers;

import RSMS.Locomotive.SpeedSeeder;

public class MathHelper {

    public static double calculatePercentage(double lhsn, double rhsn) {
        return (lhsn / rhsn) * 100;
    }

    public static int millisToSeconds(double millis) {
        return (int)(millis / SpeedSeeder.ONE_SECOND_IN_MILLIS);
    }

    public static long secondsToMillis(int seconds) {
        return seconds * SpeedSeeder.ONE_SECOND_IN_MILLIS;
    }

}
