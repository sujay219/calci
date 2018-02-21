package com.wildox.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wildox.calculator.operators.DivideOperator;
import com.wildox.calculator.operators.MinusOperator;
import com.wildox.calculator.operators.ModulusOperator;
import com.wildox.calculator.operators.MultiplyOperator;
import com.wildox.calculator.operators.PlusOperator;
import com.wildox.calculator.operators.PowerOperator;
import com.wildox.loghere.LogHere;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

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

        // break this down to entities.
        ArrayList<Entities> singleEntities = new ArrayList<>();

        int lastIndex = 0;
        int length = 0;
        for (int i = 0; i < input.length(); i++) {

            // LogHere.e(i + " " + input.charAt(i) + " len " + length + " " + lastIndex);
            // If it belongs to number.. else increment for operator!
            if ((i == 0 && isValidFirstChar(input.charAt(i))) ||
                    (length == 0 && isValidFirstChar(input.charAt(i))) ||
                    (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.')) {

                length++;
            } else {

                final String str = input.substring(lastIndex, lastIndex + length);
                lastIndex += length;
                length = 0;

                singleEntities.add(new NumberModel(Double.valueOf(str)));

                final String operator = input.substring(lastIndex, lastIndex + 1);
                lastIndex++;

                switch (operator) {
                    case "^":
                        singleEntities.add(new PowerOperator());
                        break;
                    case "%":
                        singleEntities.add(new ModulusOperator());
                        break;
                    case "/":
                        singleEntities.add(new DivideOperator());
                        break;
                    case "*":
                        singleEntities.add(new MultiplyOperator());
                        break;
                    case "+":
                        singleEntities.add(new PlusOperator());
                        break;
                    case "-":
                        singleEntities.add(new MinusOperator());
                        break;
                }
            }
        }

        final String s = input.substring(lastIndex, lastIndex + length);
        singleEntities.add(new NumberModel(Double.valueOf(s)));

        for (int k = 0; k < singleEntities.size(); k++) {
            LogHere.e("--> " + singleEntities.get(k).getValue());
        }

        String expression = calculateAnswer(singleEntities);

        if (expression == null) {
            Toast.makeText(this, "Calculation error", Toast.LENGTH_SHORT).show();
            return;
        }
        LogHere.e("calcualted " + expression);
        String x = UtilHelper.getLongIfPossible(Double.valueOf(expression));
        userInput.setText(x);
    }

    private String calculateAnswer(ArrayList<Entities> entities) {

        LogHere.e("--> processing: " + entities.toString());

        if (entities.size() == 1) {
            if (entities.get(0) instanceof ValidFirstChar) {
                LogHere.e("returning proper value");
                return entities.get(0).getValue();
            } else {
                return null;
            }
        }

        // check for priority expressions
        for (int i = 1; i < entities.size() - 1; i++) {

            if (entities.get(i) instanceof Operator) {

                Operator operator = (Operator) entities.get(i);
                NumberModel first = (NumberModel) entities.get(i - 1);
                NumberModel second = (NumberModel) entities.get(i + 1);

                double result = 0;
                try {
                    result = operator.operateOn(Double.valueOf(first.getValue()),
                            Double.valueOf(second.getValue()));

                    entities.remove(0);
                    entities.remove(0);
                    entities.remove(0);
                    entities.add(0, new NumberModel(result));
                } catch (NumberFormatException e) {
                    return null;
                }

                for (int k = 0; k < entities.size(); k++) {
                    LogHere.e("----> " + entities.get(k).getValue());
                }
                break;
            }
        }

        return calculateAnswer(entities);
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
                if (isOperator(str.charAt(i + 1)) &&
                        !isPlusMinus(str.charAt(i + 1))) {
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

    boolean isPlusMinus(char ch) {
        return (ch == '+' || ch == '-');
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

    boolean isValidNumber(char ch) {
        return (Character.isDigit(ch) || ch == '+' || ch == '-');
    }

    boolean isValidFirstChar(char ch) {
        return (Character.isDigit(ch) || ch == '+' || ch == '-');
    }
}