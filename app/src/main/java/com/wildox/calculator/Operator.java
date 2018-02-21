package com.wildox.calculator;

/**
 * Created by Sujay on 21/02/18.
 * operates +-/*%^
 */

public abstract class Operator {

    abstract String getOperator();
    abstract double operateOn(double operand1, double operand2);
}
