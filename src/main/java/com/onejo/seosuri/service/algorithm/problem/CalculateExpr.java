package com.onejo.seosuri.service.algorithm.problem;

import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.exception.common.ErrorCode;
import java.util.Stack;

public class CalculateExpr {

    public static int calculate(String expression) throws BusinessException{
        Stack<Integer> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!operatorStack.isEmpty() && hasPrecedence(c, operatorStack.peek())) {
                    char operator = operatorStack.pop();
                    int operand2 = operandStack.pop();
                    int operand1 = operandStack.pop();
                    int result = performOperation(operator, operand1, operand2);
                    operandStack.push(result);
                }
                operatorStack.push(c);
            } else if (c == '(') {
                operatorStack.push(c);
            } else if (c == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    try {
                        char operator = operatorStack.pop();
                        int operand2 = operandStack.pop();
                        int operand1 = operandStack.pop();
                        int result = performOperation(operator, operand1, operand2);
                        operandStack.push(result);
                    } catch(Exception e){
                        throw new BusinessException(ErrorCode.POP_ON_EMPTY_STACK);
                    }
                }
                operatorStack.pop(); // Pop '(' from the stack
            } else if (Character.isDigit(c)) {
                int operand = 0;
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    operand = operand * 10 + (expression.charAt(i) - '0');
                    i++;
                }
                i--;
                operandStack.push(operand);
            } else {
                throw new BusinessException(ErrorCode.EXPRESSION_CALCULATE_ERROR);
            }
        }

        while (!operatorStack.isEmpty()) {
            char operator = operatorStack.pop();
            int operand2 = operandStack.pop();
            int operand1 = operandStack.pop();
            int result = performOperation(operator, operand1, operand2);
            operandStack.push(result);
        }

        if (operandStack.isEmpty()) {
            throw new BusinessException(ErrorCode.EXPRESSION_CALCULATE_ERROR);
        }

        return operandStack.pop();
    }

    private static boolean hasPrecedence(char operator1, char operator2) {
        if (operator2 == '(' || operator2 == ')') {
            return false;
        }
        if ((operator1 == '*' || operator1 == '/') && (operator2 == '+' || operator2 == '-')) {
            return false;
        }
        return true;
    }

    private static int performOperation(char operator, int operand1, int operand2) {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                return operand1 / operand2;
            default:
                throw new BusinessException(ErrorCode.NO_EXPRESSION_OPERATOR_TYPE);
        }
    }

}
