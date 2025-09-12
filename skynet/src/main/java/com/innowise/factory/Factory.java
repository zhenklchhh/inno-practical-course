package com.innowise.factory;

import com.innowise.RobotParts;
import com.innowise.simulation.Simulation;
import com.innowise.util.RandomUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Evgeniy Zaleshchenok
 */
public class Factory extends Thread{
    private final RandomUtil randomUtil = new RandomUtil();
    private ConcurrentHashMap<RobotParts, Integer> robotPartsOnSale = new ConcurrentHashMap<>();
    private final AtomicInteger daysCounter;
    private final CyclicBarrier cyclicBarrier;
    private AtomicInteger amountOfParts = new AtomicInteger(0);

    public Factory(CyclicBarrier barrier, AtomicInteger dayCounter) {
        this.cyclicBarrier = barrier;
        this.daysCounter = dayCounter;
    }

    @Override
    public void run() {
        while(daysCounter.get() <= Simulation.DAYS) {
            int randomNumberOfParts = randomUtil.getRandomAmountOfRobotParts(0,10);
            for (int i = 0; i < randomNumberOfParts; i++) {
                RobotParts randomRobotPart = randomUtil.getRandomRobotPart();
                robotPartsOnSale.put(randomRobotPart, robotPartsOnSale.getOrDefault(randomRobotPart, 0) + 1);
                amountOfParts.incrementAndGet();
            }
            synchronized (System.out){
                System.out.println("Factory produced " + randomNumberOfParts + " parts");
                System.out.println("Total parts at the factory: " + amountOfParts.get());
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

    public synchronized List<RobotParts> getRobotPartsOnSale(int quantity) {
        List<RobotParts> robotPartsToGive = new ArrayList<>();
        List<RobotParts> availableRobotParts = getListOfAvailableParts();
        int partsToTake = Math.min(quantity, availableRobotParts.size());
        for (int i = 0; i < Math.min(quantity, partsToTake); i++) {
            RobotParts robotPart = availableRobotParts.get(i);
            robotPartsToGive.add(robotPart);
            robotPartsOnSale.compute(robotPart, (k,v) -> v - 1);
            amountOfParts.decrementAndGet();
        }
        synchronized (System.out){
            if (partsToTake == 0){
                System.out.println("(No parts were available at the factory, the faction bought nothing)");
            }else{
                System.out.println("(bought " + partsToTake + " parts, remaining at factory: " + amountOfParts + ")");
            }
        }
        return robotPartsToGive;
    }

    private List<RobotParts> getListOfAvailableParts(){
        List<RobotParts> robotParts = new ArrayList<>();
        for(Map.Entry<RobotParts, Integer> entry : robotPartsOnSale.entrySet()) {
            for(int i = 0; i < entry.getValue(); i++){
                robotParts.add(entry.getKey());
            }
        }
        Collections.shuffle(robotParts);
        return robotParts;
    }
}
