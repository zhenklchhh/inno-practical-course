package com.innowise;

import com.innowise.factory.FactorySaleManager;
import com.innowise.simulation.Simulation;
import com.innowise.util.RandomUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Evgeniy Zaleshchenok
 */
public class Faction extends Thread {
    private static final Map<RobotParts, Integer> ROBOT_BLUEPRINT;
    private static final int TOTAL_PARTS_PER_ROBOT;

    private int quantityOfRobots = 0;
    private String factionName;
    private FactorySaleManager factorySaleManager;
    private Map<RobotParts, Integer> robotPartsWarehouse = new HashMap<>();
    private final AtomicInteger amountOfParts = new AtomicInteger(0);
    private final CyclicBarrier cyclicBarrier;
    private final AtomicInteger daysCounter;
    private final RandomUtil randomUtil = new RandomUtil();

    static {
        Map<RobotParts, Integer> blueprint = new HashMap<>();
        blueprint.put(RobotParts.HEAD, 1);
        blueprint.put(RobotParts.TORSO, 1);
        blueprint.put(RobotParts.HAND, 2);
        blueprint.put(RobotParts.FEET, 2);
        ROBOT_BLUEPRINT = Collections.unmodifiableMap(blueprint);
        TOTAL_PARTS_PER_ROBOT = ROBOT_BLUEPRINT.values().stream()
                .mapToInt(Integer::intValue).sum();
    }

    public Faction(String factionName, FactorySaleManager factorySaleManager, AtomicInteger daysCounter, CyclicBarrier cyclicBarrier) {
        this.factionName = factionName;
        this.factorySaleManager = factorySaleManager;
        this.daysCounter = daysCounter;
        this.cyclicBarrier = cyclicBarrier;
    }


    @Override
    public void run() {
        while (daysCounter.get() <= Simulation.DAYS) {
            List<RobotParts> robotPartsList = factorySaleManager.buyRobotParts(
                    randomUtil.getRandomAmountOfRobotParts( 5));
            for (RobotParts robotParts : robotPartsList) {
                robotPartsWarehouse.put(robotParts, robotPartsWarehouse.getOrDefault(robotParts, 0) + 1);
                amountOfParts.incrementAndGet();
            }
            buildRobots();
            synchronized (System.out){
                System.out.println("Faction " + factionName + ":");
                System.out.println("Parts in warehouse: " + amountOfParts.get());
                System.out.println("Robots built: " + quantityOfRobots);
                System.out.println("******************************************************");
            }
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void buildRobots() {
        while (canBuildRobot()) {
            consumePartsForRobot();
            quantityOfRobots++;
            amountOfParts.addAndGet(-TOTAL_PARTS_PER_ROBOT);
        }
    }

    private boolean canBuildRobot() {
        for (Map.Entry<RobotParts, Integer> requiredPart : ROBOT_BLUEPRINT.entrySet()) {
            RobotParts partType = requiredPart.getKey();
            int requiredAmount = requiredPart.getValue();
            int availableAmount = robotPartsWarehouse.getOrDefault(partType, 0);
            if (availableAmount < requiredAmount) {
                return false;
            }
        }
        return true;
    }

    private void consumePartsForRobot() {
        for (Map.Entry<RobotParts, Integer> requiredPart : ROBOT_BLUEPRINT.entrySet()) {
            RobotParts partType = requiredPart.getKey();
            int requiredAmount = requiredPart.getValue();
            robotPartsWarehouse.compute(partType, (key, currentAmount) -> currentAmount - requiredAmount);
        }
    }

    public int getQuantityOfRobots() {
        return quantityOfRobots;
    }

    public String getFactionName() {
        return factionName;
    }
}
