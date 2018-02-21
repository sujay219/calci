package com.wildox.calculator;

/**
 * Created by Sujay on 21/02/18.
 * Plus is the
 */

public abstract class PlusOperator extends Operator implements Entity {

    @Override
    public double operateOn(double operand1, double operand2) {
        return operand1 + operand2;
    }

    @Override
    public String getOperator() {
        return "+";
    }
}
