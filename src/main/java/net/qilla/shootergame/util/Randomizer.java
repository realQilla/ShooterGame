package net.qilla.shootergame.util;

import java.util.Random;

public class Randomizer {

    public float between(float min, float max) {
        Random random = new Random();
        return random.nextFloat() * (max - min) + min;
    }

    public double between(double min, double max) {
        Random random = new Random();
        return random.nextDouble() * (max - min) + min;
    }
}
