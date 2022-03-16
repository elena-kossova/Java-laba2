import java.util.*;
import java.lang.*;
import java.util.Queue;

import static java.lang.Integer.*;

/** The class is designed to calculate the value of the expression
 * */
public class calculator {
    /** Operation type enumerator
     * */
    public enum OperatorType{
        left_br, right_br, sum, diff, mult, div, num, endOfStr;
    }

    /** Class operator. Stores the type type of operation and its symbol as a string
     * */
    public static class Operator{
        /** Тип оперратора */
        OperatorType operator;
        /** Символ операции */
        String var;
        /** Конструктор
         * @param opr тип операции
         * @param v символ операции типа String
         * */
        public Operator(OperatorType opr, String v){
            this.operator = opr;
            this.var = v;
        }
        /** Конструктор
         * @param opr тип операции
         * @param v символ операции типа Character
         * */
        public Operator(OperatorType opr, Character v){
            this.operator = opr;
            this.var = v.toString();
        }

        /** Конвертация значений класса Operator в строку
         * @return строка с данными об одном операторе
         * */
        public String toString(){
            String str = "Operator: operator = " + operator + ", var = " + var + "\n" ;
            return str;
        }
    }

    /** Reading a string and converting it to a list of tokens
     * @param exp the string in which the mathematical expression is written
     * @return a list containing each element of the expression separately
     * */
    public static List<String> readExpression(String exp) {
        ArrayList<String> spisok = new ArrayList<>();
        int platz = 0;
        while ( platz < exp.length()){
            char s = exp.charAt(platz);
            switch(s){
                case '(':
                    spisok.add("(");
                    platz++;
                    continue;
                case')':
                    spisok.add(")");
                    platz++;
                    continue;
                case'+':
                    spisok.add("+");
                    platz++;
                    continue;
                case'-':
                    spisok.add("-");
                    platz++;
                    continue;
                case'*':
                    spisok.add("*");
                    platz++;
                    continue;
                case'/':
                    spisok.add("/");
                    platz++;
                    continue;
                default:
                    if (s >= '0' && s <= '9'){
                        StringBuilder number = new StringBuilder();
                        do {
                            number.append(s);
                            platz++;
                            if (platz >= exp.length()) break;
                            s = exp.charAt(platz);
                        } while (s >= '0' && s <= '9');
                        spisok.add(number.toString());
                    }
                    else{
                        if (s != ' ') {
                            throw new RuntimeException("Неизвестный символ: " + s + "\n");
                        }
                        platz++;
                    }
            }
        }
        spisok.add(" ");
        return spisok;
    }

    /** Compilation of a postfix entry
     * @param spisok a list containing each element of the expression separately
     * @return a queue containing a postfix entry of a mathematical expression
     * */
    public static Queue<String> postfixRecord(List<String> spisok){
        Queue<String> result = new LinkedList<>();
        Stack<String> temporary = new Stack<>();
        Stack<String> checkingBrackets = new Stack<>();
        int countOfNumber = 0, countOfOperation = 0;
        int platz = 0;
        while(platz < spisok.size()){
            String str = spisok.get(platz);
            //System.out.println(str);
            if (str.equals(" ")){
                System.out.println("Конец строки");
                break;
            }
            else {
                switch (str) {
                    case "(" -> {
                        temporary.push(String.valueOf('('));
                        checkingBrackets.push(String.valueOf('('));
                        platz++;
                        continue;
                    }
                    case ")" -> {
                        if (checkingBrackets.isEmpty()){
                            throw new RuntimeException("Неверно написаны скобки. Лишняя закрывающая скобка.");
                        }
                        else {
                            if (checkingBrackets.peek().equals("("))
                                checkingBrackets.pop();
                            else {
                                throw new RuntimeException("Неверно написаны скобки.");
                            }
                        }
                        while (!temporary.peek().equals(String.valueOf('('))) {
                            result.add(temporary.pop());
                        }
                        temporary.pop();
                        platz++;
                        continue;
                    }
                    case "+" -> {
                        if (temporary.isEmpty() || temporary.peek().equals(String.valueOf('('))) {
                            temporary.push(String.valueOf('+'));
                        } else {
                            if (temporary.peek().equals(String.valueOf('*')) || temporary.peek().equals(String.valueOf('/')) || temporary.peek().equals(String.valueOf('-'))) {
                                while (!temporary.isEmpty() && !temporary.peek().equals(String.valueOf('('))) {
                                    result.add(temporary.pop());
                                }
                                temporary.push(String.valueOf('+'));
                            }
                        }
                        platz++;
                        countOfOperation++;
                        continue;
                    }
                    case "-" -> {
                        if (temporary.isEmpty() || temporary.peek().equals(String.valueOf('('))) {
                            temporary.push(String.valueOf('-'));
                        } else {
                            if (temporary.peek().equals(String.valueOf('*')) || temporary.peek().equals(String.valueOf('/')) || temporary.peek().equals(String.valueOf('+'))) {
                                while (!temporary.isEmpty() && !temporary.peek().equals(String.valueOf('('))) {
                                    result.add(temporary.pop());
                                }
                                temporary.push(String.valueOf('-'));
                            }
                        }
                        platz++;
                        countOfOperation++;
                        continue;
                    }
                    case "*" -> {
                        if (temporary.isEmpty() || temporary.peek().equals(String.valueOf('(')) || temporary.peek().equals(String.valueOf('+')) || temporary.peek().equals(String.valueOf('-'))) {
                            temporary.push(String.valueOf('*'));
                        } else {
                            if (temporary.peek().equals(String.valueOf('/'))) {
                                while (!temporary.isEmpty() && !temporary.peek().equals(String.valueOf('('))) {
                                    result.add(temporary.pop());
                                }
                                temporary.push(String.valueOf('*'));
                            }
                        }
                        platz++;
                        countOfOperation++;
                        continue;
                    }
                    case "/" -> {
                        if (temporary.isEmpty() || temporary.peek().equals(String.valueOf('(')) || temporary.peek().equals(String.valueOf('+')) || temporary.peek().equals(String.valueOf('-'))) {
                            temporary.push(String.valueOf('/'));
                        } else {
                            if (temporary.peek().equals(String.valueOf('*'))) {
                                while (!temporary.isEmpty() && !temporary.peek().equals(String.valueOf('('))) {
                                    result.add(temporary.pop());
                                }
                                temporary.push(String.valueOf('/'));
                            }
                        }
                        platz++;
                        countOfOperation++;
                        continue;
                    }
                    default -> {
                        int zahl = parseInt(str);
                        if (zahl >= 0 && zahl < 2147483647) {
                            result.add(str);
                        }
                        platz++;
                        countOfNumber++;
                    }
                }
            }
            //System.out.println("Стэк" + temporary);
            //System.out.println("Очередь" + result);
        }
        if(!checkingBrackets.isEmpty()){
            throw new RuntimeException("Неверно написаны скобки. Лишняя открывающая скобочка.");
        }
        while(!temporary.isEmpty()){
            result.add(temporary.pop());
        }
        if (result.size() == 1){
            if (result.peek().equals(String.valueOf('+')) || result.peek().equals(String.valueOf('-')) ||
                    result.peek().equals(String.valueOf('*')) || result.peek().equals(String.valueOf('/'))){
                throw new RuntimeException("Невозможно решить! Введена одна операция!");
            }
        }
        if (countOfNumber-1 > countOfOperation){
            throw new RuntimeException("Не хватает операции.");
        }
        else{
            if (countOfNumber-1 < countOfOperation){
                throw new RuntimeException("Не хватает числа.");
            }
        }

        return result;
    }

    /** Calculating the value of an expression
     * @param result a queue containing a postfix entry of a mathematical expression
     * @return the calculated value of the mathematical expression
     * */
    public static float calculatorOfExpression(Queue<String> result){
        Stack<Float> calculator = new Stack<>();
        float number1, number2, decision = 0, tempoElem; //решение
        String elem;
        if (result.size() == 1){
            decision = parseInt(result.remove());
            return decision;
        }
        while(!result.isEmpty()){
            elem = result.remove();
            switch (elem) {
                case "+" -> {
                    number2 = calculator.pop();
                    number1 = calculator.pop();
                    decision = number1 + number2;
                    calculator.push(decision);
                    continue;
                }
                case "-" -> {
                    number2 = calculator.pop();
                    number1 = calculator.pop();
                    decision = number1 - number2;
                    calculator.push(decision);
                    continue;
                }
                case "*" -> {
                    number2 = calculator.pop();
                    number1 = calculator.pop();
                    decision = number1 * number2;
                    calculator.push(decision);
                    continue;
                }
                case "/" -> {
                    number2 = calculator.pop();
                    number1 = calculator.pop();
                    if (number2 == 0) {
                        throw new RuntimeException("Деление на ноль недопустимо!");
                    } else {
                        decision = number1 / number2;
                        calculator.push(decision);
                    }
                    continue;
                }
                default -> {
                    calculator.push((float) parseInt(elem));
                }
            }
        }
        return decision;
    }
}