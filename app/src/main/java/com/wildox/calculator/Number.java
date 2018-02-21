package com.wildox.calculator;

/**
 * Created by Sujay on 21/02/18.
 *
 */

public abstract class Number implements Entity {

    private int digit = 0;
    private String digitString;

    public Number(int digit) {
        this.digit = digit;
        this.digitString = String.valueOf(digit);
    }

    @Override
    public int occupiedLength() {
        return digitString.length();
    }

    public int getDigit() {
        return this.digit;
    }

    public double calculateWith(double anotherDigit, Operator operator) {
        return operator.operateOn(this.digit, anotherDigit);
    }
}
