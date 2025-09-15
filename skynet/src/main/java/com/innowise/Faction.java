package com.innowise;

import com.innowise.factory.FactorySaleManager;
import com.innowise.simulation.Simulation;
import com.innowise.util.RandomUtil;

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
    private int quantityOfRobots = 0;
    private String factionName;
    private FactorySaleManager factorySaleManager;
    private Map<RobotParts, Integer> robotPartsWarehouse = new HashMap<>();
    private final AtomicInteger amountOfParts = new AtomicInteger(0);
    private final CyclicBarrier cyclicBarrier;
    private final AtomicInteger daysCounter;
    private final RandomUtil randomUtil = new RandomUtil();

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
        boolean hasRequiredDetails = true;
        while (hasRequiredDetails) {
            if (robotPartsWarehouse.getOrDefault(RobotParts.FEET, 0) > 1 && robotPartsWarehouse.getOrDefault(RobotParts.HAND, 0) > 1 &&
                    robotPartsWarehouse.getOrDefault(RobotParts.TORSO, 0) > 0 && robotPartsWarehouse.getOrDefault(RobotParts.HEAD, 0) > 0) {
                robotPartsWarehouse.put(RobotParts.FEET, robotPartsWarehouse.get(RobotParts.FEET) - 2);
                robotPartsWarehouse.put(RobotParts.HAND, robotPartsWarehouse.get(RobotParts.HAND) - 2);
                robotPartsWarehouse.put(RobotParts.TORSO, robotPartsWarehouse.get(RobotParts.TORSO) - 1);
                robotPartsWarehouse.put(RobotParts.HEAD, robotPartsWarehouse.get(RobotParts.HEAD) - 1);
                quantityOfRobots++;
                amountOfParts.addAndGet(-6);
            } else {
                hasRequiredDetails = false;
            }
        }
    }

    public int getQuantityOfRobots() {
        return quantityOfRobots;
    }

    public String getFactionName() {
        return factionName;
    }
}
