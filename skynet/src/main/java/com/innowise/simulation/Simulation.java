package com.innowise.simulation;

import com.innowise.Faction;
import com.innowise.factory.Factory;
import com.innowise.factory.FactorySaleManager;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Evgeniy Zaleshchenok
 */
public class Simulation {
    public static final int NUMBER_OF_THREADS = 3;
    public static final int DAYS = 100;

    public static void main(String[] args) {
        AtomicInteger daysCounter = new AtomicInteger(1);
        Runnable action = () -> {
            System.out.println("---------------- Day " + daysCounter.get() + " has ended ----------------");
            daysCounter.incrementAndGet();
        };
        CyclicBarrier barrier = new CyclicBarrier(NUMBER_OF_THREADS, action);
        Factory factory = new Factory(barrier, daysCounter);
        Faction world = new Faction("World", new FactorySaleManager(factory), daysCounter, barrier);
        Faction wednesday = new Faction("Wednesday", new FactorySaleManager(factory), daysCounter, barrier);
        factory.start();
        world.start();
        wednesday.start();
        try {
            factory.join();
            world.join();
            wednesday.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("\n================ FINAL RESULTS ================");
        System.out.println("Total robots for faction " + world.factionName + ": " + world.quantityOfRobots);
        System.out.println("Total robots for faction " + wednesday.factionName + ": " + wednesday.quantityOfRobots);

        if (world.quantityOfRobots > wednesday.quantityOfRobots) {
            System.out.println("The winning faction is: " + world.factionName);
        } else if (wednesday.quantityOfRobots > world.quantityOfRobots) {
            System.out.println("The winning faction is: " + wednesday.factionName);
        } else {
            System.out.println("It's a tie!");
        }
        System.out.println("==============================================");
    }
}