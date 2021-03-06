package com.wildox.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wildox.loghere.LogHere;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivityOld extends AppCompatActivity {

    @BindView(R.id.user_input)
    TextView userInput;
    String priorities[] = {"^", "%/*", "+-"};
    //String priorities[] = {"^", "%", "/", "\\*", "\\+", "-"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        LogHere.initialize(BuildConfig.DEBUG, LogHere.ERROR, "wildox");
    }

    @OnClick({R.id.cancel_all, R.id.cancel_last})
    void cancelOperations(View view) {
        String modifiedString;
        switch (view.getId()) {
            case R.id.cancel_all:
                userInput.setText("0");
                break;
            case R.id.cancel_last:
                modifiedString = userInput.getText().toString();
                if (modifiedString.length() > 1) {
                    userInput.setText(modifiedString.substring(0, modifiedString.length() - 1));
                } else {
                    userInput.setText("0");
                }
                break;
        }
    }

    @OnClick({R.id.number_0, R.id.number_1, R.id.number_2, R.id.number_3, R.id.number_4, R.id.number_5,
            R.id.number_6, R.id.number_7, R.id.number_8, R.id.number_9, R.id.decimal_point,
            R.id.math_plus, R.id.math_minus, R.id.math_multiplication, R.id.math_division,
            R.id.math_modulus, R.id.math_power})
    void numberClick(View view) {
        String modifiedString = "";
        boolean isValidFirstDigit = false;
        switch (view.getId()) {
            case R.id.number_0:
                isValidFirstDigit = true;
                modifiedString = "0";
                break;
            case R.id.number_1:
                isValidFirstDigit = true;
                modifiedString = "1";
                break;
            case R.id.number_2:
                isValidFirstDigit = true;
                modifiedString = "2";
                break;
            case R.id.number_3:
                isValidFirstDigit = true;
                modifiedString = "3";
                break;
            case R.id.number_4:
                isValidFirstDigit = true;
                modifiedString = "4";
                break;
            case R.id.number_5:
                isValidFirstDigit = true;
                modifiedString = "5";
                break;
            case R.id.number_6:
                isValidFirstDigit = true;
                modifiedString = "6";
                break;
            case R.id.number_7:
                isValidFirstDigit = true;
                modifiedString = "7";
                break;
            case R.id.number_8:
                isValidFirstDigit = true;
                modifiedString = "8";
                break;
            case R.id.number_9:
                isValidFirstDigit = true;
                modifiedString = "9";
                break;
            case R.id.decimal_point:
                modifiedString = ".";
                break;
            case R.id.math_modulus:
                modifiedString = "%";
                break;
            case R.id.math_power:
                modifiedString = "^";
                break;
            case R.id.math_division:
                modifiedString = "/";
                break;
            case R.id.math_multiplication:
                modifiedString = "*";
                break;
            case R.id.math_plus:
                isValidFirstDigit = true;
                modifiedString = "+";
                break;
            case R.id.math_minus:
                isValidFirstDigit = true;
                modifiedString = "-";
                break;
        }
        LogHere.e(modifiedString + " --");
        String existingString = userInput.getText().toString();
        if (existingString.equals("0")) {

            if (modifiedString.equals(".")) {
                userInput.setText("0.");
            } else {
                if (isValidFirstDigit) {
                    userInput.setText(modifiedString);
                } else {
                    Toast.makeText(this, "First digit cannot be the operator.", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            modifiedString = existingString + modifiedString;
            userInput.setText(modifiedString);
        }
    }

    @OnClick(R.id.math_equals)
    void equalsCalculation() {

        String input = userInput.getText().toString();
        boolean valid = isValidInput(input);
        if (!valid) {
            Toast.makeText(this, "Not a valid input", Toast.LENGTH_SHORT).show();
            return;
        }
        String expression = calculateAnswer(input, 0);
        if (expression == null) {
            Toast.makeText(this, "Calculation error", Toast.LENGTH_SHORT).show();
            return;
        }
        LogHere.e("calcualted " + expression);
        String x = UtilHelper.getLongIfPossible(Double.valueOf(expression));

        userInput.setText(x);
    }

    private String calculateAnswer(String expression, int priorityWheel) {

        expression = expression.trim();
        LogHere.e("--> processing: " + expression);

        if (priorityWheel >= priorities.length) {
            LogHere.e("priorities overloaded this will the final result: " + expression);
            return expression;
        }

        if (isValidNumeric(expression)) {
            return expression;
        }

        try {
            // have we reached the expression
            Double.valueOf(expression);
            return expression;
        } catch (NumberFormatException e) {
            // just go on
            LogHere.e("NON: " + expression);
        }

        String actualExpression = expression;

        char operatorNow = isAvailableInPriority(actualExpression, priorities[priorityWheel]);
        // error: calculate next
        if (operatorNow == 'N') {
            LogHere.e("not found in priority " + priorities[priorityWheel] + " checking others.");
            return calculateAnswer(expression, priorityWheel + 1);
        } else {
            LogHere.e("found operator " + operatorNow);
        }

        String stringExpressions[] = new String[2];
        stringExpressions[0] = expression.substring(0, actualExpression.indexOf(operatorNow));
        stringExpressions[1] = expression.substring(actualExpression.indexOf(operatorNow) + 1);

        LogHere.e("string expressions: " + stringExpressions[0] + " . " + operatorNow + " . " + stringExpressions[1]);
        if (stringExpressions.length == 1) {
            return calculateAnswer(expression, priorityWheel + 1);
        } else if (stringExpressions[0].length() == 0) {
            return calculateAnswer(stringExpressions[1], priorityWheel);
        } else if (stringExpressions[1].length() == 0) {
            return calculateAnswer(stringExpressions[0], priorityWheel);
        } else {
            // now we can calculate the expression based on priorities.
            String valueOf = "";

            double firstOperand = UtilHelper.fetchLast(stringExpressions[0]);
            stringExpressions[0] = UtilHelper.stripLast(stringExpressions[0]);

            double secondOperand = UtilHelper.fetchFirst(stringExpressions[1]);
            stringExpressions[1] = UtilHelper.stripFirst(stringExpressions[1]);

            LogHere.e("operands : " + firstOperand + " " + operatorNow + " " + secondOperand);
            LogHere.e("after Strip: " + stringExpressions[0] + " & " + stringExpressions[1]);
            double temp;
            String processedExp;
            switch (operatorNow) {
                case '^':

                    temp = (int) (Math.pow(firstOperand, secondOperand));
                    valueOf = calculateAnswer(stringExpressions[0] + temp +
                            stringExpressions[1], priorityWheel);
                    break;
                case '%':

                    if (secondOperand == 0) {
                        Toast.makeText(this, "Division by zero error", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                    temp = firstOperand % secondOperand;
                    valueOf = calculateAnswer(stringExpressions[0] + temp + stringExpressions[1],
                            priorityWheel);
                    break;
                case '/':

                    if (secondOperand == 0) {
                        Toast.makeText(this, "Division by zero error", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                    temp = firstOperand / secondOperand;
                    processedExp = UtilHelper.getLongIfPossible(temp);
                    if (stringExpressions[0].equals("-")) {
                        stringExpressions[0] = "";
                    }
                    valueOf = calculateAnswer(stringExpressions[0] + processedExp +
                            stringExpressions[1], priorityWheel);
                    break;
                case '*':

                    temp = firstOperand * secondOperand;
                    processedExp = UtilHelper.getLongIfPossible(temp);
                    // for the negative
                    if (stringExpressions[0].equals("-")) {
                        stringExpressions[0] = "";
                    }
                    valueOf = calculateAnswer(stringExpressions[0] + processedExp +
                            stringExpressions[1], priorityWheel);
                    break;
                case '+':

                    temp = firstOperand + secondOperand;
                    processedExp = UtilHelper.getLongIfPossible(temp);
                    valueOf = calculateAnswer(stringExpressions[0] + processedExp +
                            stringExpressions[1], priorityWheel);
                    break;
                case '-':

                    temp = firstOperand - secondOperand;
                    processedExp = UtilHelper.getLongIfPossible(temp);
                    valueOf = calculateAnswer(stringExpressions[0] + processedExp +
                            stringExpressions[1], priorityWheel);
                    break;
            }
            return valueOf;
        }
    }


    char isAvailableInPriority(String wholeTerm, String searchThis) {

        char ch[] = searchThis.toCharArray();
        int[] x = new int[3];
        x[0] = Integer.MAX_VALUE;
        x[1] = Integer.MAX_VALUE;
        x[2] = Integer.MAX_VALUE;

        boolean found = false;
        // could be max 3.
        for (int k = 0; k < ch.length; k++) {

            if (wholeTerm.indexOf(searchThis.charAt(k)) > -1) {

                x[k] = wholeTerm.indexOf(searchThis.charAt(k));

                // if it's + - priority,
                // no need to check first index in whole term as
                // if - is in index of 0, we wont need to mark it as operator.
                if (searchThis.equals(priorities[2]) && x[k] == 0) {
                    x[k] = Integer.MAX_VALUE;
                    continue;
                }
                found = true;
                LogHere.e(".. " + k + " --> " + x[k]);
            }
        }
        if (found) {
            LogHere.e(".. found " + searchThis.charAt(min(x)));
            return searchThis.charAt(min(x));
        }
        //int minimum = min(x);
        //if (minimum != Integer.MAX_VALUE)
        return 'N';
    }

    int min(int x[]) {
        if (x[0] < x[1])
            return x[0] < x[2] ? 0 : 2;
        else
            return x[1] < x[2] ? 1 : 2;
    }

    boolean isValidInput(String str) {

        if (str.length() == 0) {
            LogHere.e("length zero");
            return false;
        }

        if (str.length() == 1 && !Character.isDigit(str.charAt(0))) {
            LogHere.e("not valid");
            return false;
        }

        // If the 1st char is not '+', '-' or digit
        if (!Character.isDigit(str.charAt(0)) &&
                str.charAt(0) != '+' &&
                str.charAt(0) != '-') {
            LogHere.e("not valid");
            return false;
        }

        for (int i = 1; i < str.length(); i++) {
            // If any of the char does not belong to {digit, +, -, ., e}

            if (Character.isDigit(str.charAt(i))) {
                continue;
            }

            if (isOperator(str.charAt(i))) {

                // couldn't be last.
                if (i + 1 >= str.length()) {
                    return false;
                }
                // if next is also an operator.
                if (isOperator(str.charAt(i + 1))) {
                    return false;
                }
            }

            if (str.charAt(i) == '.') {

                // If '.' is the last character.
                if (i + 1 >= str.length())
                    return false;

                // if '.' is not followed by a digit.
                if (!Character.isDigit(str.charAt(i + 1)))
                    return false;

                //if there are two consecutive '.'s
                int k = i + 1;
                while (k < str.length()) {

                    // check if there's no other
                    if (Character.isDigit(str.charAt(k)))
                        k++;
                    else if (str.charAt(k) == '.')
                        return false;
                    else
                        break;
                }
            }
        }


        // If the string skips all above cases, then it is numeric
        return true;
    }

    boolean isOperator(char ch) {
        return (ch == '/' || ch == '*' || ch == '+' || ch == '-' || ch == '^' || ch == '%');
    }

    public boolean isValidNumeric(String expression) {

        if (expression.length() == 0) {
            LogHere.e("length zero");
            return false;
        }

        if (expression.length() == 1 && !isValidFirstChar(expression.charAt(0))) {
            LogHere.e("return from zero..");
            return false;
        }

        // If the 1st char is not '+', '-', '.' or digit
        if (expression.charAt(0) != '+' || expression.charAt(0) != '-' ||
                !Character.isDigit(expression.charAt(0)) ||
                expression.charAt(0) != '.')
            return false;

        for (int i = 1; i < expression.length(); i++) {
            // If any of the char does not belong to {digit, +, -, ., e}
            LogHere.e("" + expression.charAt(i));

            if (Character.isDigit(expression.charAt(i))) {
                LogHere.e("" + expression.charAt(i));
                continue;
            }

            if (expression.charAt(i) == '/' || expression.charAt(i) == '*' ||
                    expression.charAt(i) == '+' || expression.charAt(i) == '-')
                return false;

            if (expression.charAt(i) == '.') {

                // If '.' is the last character.
                if (i + 1 >= expression.length())
                    return false;

                // if '.' is not followed by a digit.
                if (!Character.isDigit(expression.charAt(i + 1)))
                    return false;
            }
        }

        // If the string skips all above cases, then it is numeric
        return true;
    }

    boolean isValidFirstChar(char ch) {

        return (Character.isDigit(ch) || ch == '+' || ch == '-');
    }
}