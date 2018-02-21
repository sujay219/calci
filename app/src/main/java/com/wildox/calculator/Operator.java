package com.wildox.calculator;

/**
 * Created by Sujay on 21/02/18.
 * operates +-/*%^
 */

public abstract class Operator implements Entities {

    public abstract int getPriority();

    public abstract double operateOn(double operand1, double operand2);

    @Override
    public int occupiedLength() {
        return 1;
    }
}
