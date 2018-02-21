package com.wildox.calculator.operators;

import com.wildox.calculator.Operator;

/**
 * Created by Sujay on 21/02/18.
 * divide is the
 */

public class ModulusOperator extends Operator {

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public String getValue() {
        return "%";
    }

    @Override
    public double operateOn(double operand1, double operand2) throws NumberFormatException {
        if (operand2 == 0)
            throw new NumberFormatException();
        return operand1 % operand2;
    }
}
