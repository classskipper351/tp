package seedu.duke;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static java.lang.Character.isDigit;

/**
 * This class is responsible for calculating the result of the user input.
 * It also generates the explanation of the calculation.
 */
public class Calculator {

    private static List<String> explanations;

    /**
     * This method calculates the result of the user input.
     *
     * @param sb The user input.
     * @return The result of the calculation.
     */
    public double calculate(StringBuilder sb) {
        Stack<Double> numStack = new Stack<>();
        Stack<String> opStack = new Stack<>();
        explanations = new ArrayList<>();
        ArrayList<Object> formula = toFormula(sb);
        ArrayList<Object> suffix = toSuffix(formula);
        for (Object object : suffix) {
            if (object instanceof Integer) {
                numStack.push(Double.valueOf((Integer) object));
            } else {

                double num2 = numStack.pop();
                double num1 = numStack.pop();
                double result = calculateTwo(num1, num2, (String) object);
                explanations.add(getExplanation(num1, num2, (String) object, result));
                numStack.push(result);
            }
        }
        assert numStack.size() == 1 : "wrong formula";
        // round to 3 decimal places
        return Math.round(numStack.peek() * 1000.0) / 1000.0;
    }

    /**
     * This method generates the explanation of the calculation.
     *
     * @param num1   The first number in the calculation.
     * @param num2   The second number in the calculation.
     * @param op     The operator in the calculation.
     * @param answer The result of the calculation.
     * @return The explanation of the calculation.
     */
    private static String getExplanation(double num1, double num2, String op, double answer) {
        String start = "The computation of the problem: " +
                num1 + " " + op + " " +
                num2 + " = " +
                answer + "\n\n";
        List<String> explanation = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        String alignedProblem;
        if (op.equals("/")) {
            alignedProblem = "The division of " + num1 + " and " + num2 + " is " + answer + "\n";
        } else {
            String firstString = String.valueOf(num1);
            String secondString = String.valueOf(num2);
            String firstIntegerPart = firstString.split("\\.")[0];
            String secondIntegerPart = secondString.split("\\.")[0];
            // Put the longer number in the first place
            if (firstIntegerPart.length() < secondIntegerPart.length()) {
                String temp = firstString;
                firstString = secondString;
                secondString = temp;
            }
            if (op.equals("*")) {
                op = "x";
            }
            // align the problem to the . place
            String firstDecimalPart = firstString.split("\\.")[1];
            String secondDecimalPart = secondString.split("\\.")[1];

            if (firstDecimalPart.length() < secondDecimalPart.length()) {
                firstString = firstString +
                        new String(new char[secondDecimalPart.length() -
                                firstDecimalPart.length()]).replace("\0", "0");
            } else {
                secondString = secondString +
                        new String(new char[firstDecimalPart.length() -
                                secondDecimalPart.length()]).replace("\0", "0");
            }

            explanation.add(firstString.trim());
            explanation.add(op + "          " + secondString.trim());
            explanation.add("---------------------------------------");
            explanation.add(String.valueOf(answer));
            for (String element : explanation) {
                builder.append(String.format("%30s%n", element));
            }
            alignedProblem = builder.toString();


        }
        return start + alignedProblem + "\n";
    }

    /**
     * This method calculates the result of the two numbers.
     *
     * @param num1 The first number in the calculation.
     * @param num2 The second number in the calculation.
     * @param op   The operator in the calculation.
     * @return The result of the calculation.
     */
    private static double calculateTwo(double num1, double num2, String op) {

        double answer;
        switch (op) {
        case ("+"):
            answer = num1 + num2;
            break;
        case ("-"):
            answer = num1 - num2;
            break;
        case ("*"):
            answer = num1 * num2;
            break;
        case ("/"):
            answer = num1 / num2;
            break;
        default:
            return 0;
        }
        return answer;
    }

    /**
     * This method converts the formula to suffix notation.
     *
     * @param formula The formula to be converted.
     * @return The formula in suffix notation.
     */
    private static ArrayList<Object> toSuffix(ArrayList<Object> formula) {
        ArrayList<Object> suffix = new ArrayList<>();
        Stack<String> opStack = new Stack<>();

        for (Object object : formula) {
            if (object instanceof Integer) {
                suffix.add(object);
            } else if (object instanceof String) {
                String op = (String) object;

                if (opStack.empty()) {
                    opStack.push(op);
                    continue;
                }

                while (!opStack.empty()) {
                    if (!prior(op, opStack.peek())) {
                        suffix.add(opStack.pop());
                    } else {
                        break;
                    }
                }
                opStack.push(op);
            }
        }

        while (!opStack.empty()) {
            suffix.add(opStack.pop());
        }
        return suffix;
    }

    /**
     * This method checks if the first operator has higher priority than the second operator.
     *
     * @param op1 The first operator.
     * @param op2 The second operator.
     * @return True if the first operator has higher priority than the second operator, false otherwise.
     */
    private static boolean prior(String op1, String op2) {
        int priority1;
        int priority2;
        priority1 = getPriority(op1);
        priority2 = getPriority(op2);
        return priority1 > priority2;
    }

    /**
     * This method returns the priority of the operator.
     *
     * @param op The operator.
     * @return The priority of the operator.
     */
    static int getPriority(String op) {
        int priority = 0;
        switch (op) {
        case "+":
        case "-":
            priority = 1;
            break;
        case "*":
        case "/":
            priority = 2;
            break;
        default:
            break;
        }
        return priority;
    }

    /**
     * This method converts the formula to separate objects.
     *
     * @param sb The formula to be converted.
     * @return The formula in separate objects.
     */
    private static ArrayList<Object> toFormula(StringBuilder sb) {
        ArrayList<Object> formula = new ArrayList<>();
        for (int i = 0; i < sb.length(); ) { // change to separate objects

            if (isDigit(sb.charAt(i))) {

                int numStart = i;
                int numEnd = numStart;
                while (numEnd < sb.length() && isDigit(sb.charAt(numEnd))) {
                    numEnd++;
                }
                Integer num = Integer.parseInt(sb.substring(numStart, numEnd));
                formula.add(num);
                i = numEnd;
            } else {
                formula.add(Character.toString(sb.charAt(i)));
                i++;
            }
        }
        return formula;
    }

    /**
     * This method returns the explanation of the calculation.
     *
     * @return The explanation of the calculation.
     */
    public String getExplanationsString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < explanations.size(); i++) {
            builder.append(i + 1).append(". ").append(explanations.get(i));
        }
        return builder.toString();
    }
}
