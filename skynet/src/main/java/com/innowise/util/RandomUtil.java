package com.innowise.util;

import com.innowise.RobotParts;

import java.awt.*;
import java.util.Random;

/**
 * @author Evgeniy Zaleshchenok
 */
public class RandomUtil {
    private static final Random RANDOM = new Random();

    public int getRandomAmountOfRobotParts(int upperBound) {
        return RANDOM.nextInt(upperBound) + 1;
    }

    public RobotParts getRandomRobotPart() {
        RobotParts[] robotParts = RobotParts.values();
        return robotParts[RANDOM.nextInt(robotParts.length)];
    }
}
