package ru.pfq.stargame.engine.math;

import java.util.Random;



public class Rnd {
    private static final Random random = new Random();

    public static float nextFloat(float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }

    public static int nextint(int min, int max) {
        int randomNum = random.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
