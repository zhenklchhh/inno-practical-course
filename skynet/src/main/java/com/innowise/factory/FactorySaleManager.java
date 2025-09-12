package com.innowise.factory;

import com.innowise.RobotParts;

import java.util.List;

/**
 * @author Evgeniy Zaleshchenok
 */
public class FactorySaleManager {
    private Factory factory;

    public FactorySaleManager(Factory factory) {
        this.factory = factory;
    }

    public List<RobotParts> buyRobotParts(int quantity){
        return factory.getRobotPartsOnSale(quantity);
    }
}
