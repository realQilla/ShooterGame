package net.qilla.zombieshooter.Utils;

import java.util.Random;

public class Randomizer {

    public float standard(float min, float max) {
        Random random = new Random();
        float num = random.nextFloat(max - min) + max;
        return num;
    }
}
