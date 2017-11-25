package ru.pfq.stargame.engine.math;


public class Utils {

    public static boolean isFloatingEqual(float v1, float v2) {
        if (v1 == v2)
            return true;
        float absoluteDifference = Math.abs(v1 - v2);
        float maxUlp = Math.max(Math.ulp(v1), Math.ulp(v2));
        return absoluteDifference < 5 * maxUlp;
    }
}
