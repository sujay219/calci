package com.wildox.calculator.operators;

import com.wildox.calculator.Operator;

/**
 * Created by Sujay on 21/02/18.
 * Plus is the
 */

public class MultiplyOperator extends Operator {

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public String getValue() {
        return "*";
    }

    @Override
    public double operateOn(double operand1, double operand2) {
        return operand1 * operand2;
    }
}
