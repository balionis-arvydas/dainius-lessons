package com.balionis.dainius.lesson3.service;

import java.util.List;

public class SumMovingServiceFunction implements MovingServiceFunction {

    @Override
    public Double apply(List<Double> values) {
        double sum = 0;
        for (int i = 0; i < values.size(); i++) {
            sum += values.get(i);
        }
        return sum;
    }
}
