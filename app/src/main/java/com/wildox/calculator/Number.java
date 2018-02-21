package com.wildox.calculator;

/**
 * Created by Sujay on 21/02/18.
 *
 */

public class Number implements Entity {

    private double digit = 0;
    private String digitString;

    public Number(double digit) {
        this.digit = digit;
        this.digitString = String.valueOf(digit);
    }

    @Override
    public int occupiedLength() {
        return digitString.length();
    }

    @Override
    public String getValue() {
        return digitString;
    }

    public double calculateWith(double anotherDigit, Operator operator) {
        return operator.operateOn(this.digit, anotherDigit);
    }
}
