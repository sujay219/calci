package com.wildox.calculator;

/**
 * Created by Sujay on 20/02/18.
 * expression variable
 */

public class Expression {
    boolean isSolved = false;
    String expression = "";
    double value;

    public Expression(String expression) {
        this.expression = expression;
    }

    public Expression(double value, boolean isSolved) {
        this.value = value;
        this.isSolved = isSolved;
    }

    public String getExpression() {
        return expression;
    }

    public double getValue() {
        return value;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    public boolean isSolved() {
        return isSolved;
    }
}
