package com.wildox.calculator.operators;

import com.wildox.calculator.Operator;
import com.wildox.calculator.ValidFirstChar;

/**
 * Created by Sujay on 21/02/18.
 * Plus is the
 */

public class MinusOperator extends Operator implements ValidFirstChar {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String getValue() {
        return "-";
    }

    @Override
    public double operateOn(double operand1, double operand2) {
        return operand1 - operand2;
    }

}
