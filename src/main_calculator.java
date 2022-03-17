import java.util.List;
import java.util.Queue;
/** @author Elena Chernuhina
 *  */
public class main_calculator {
    public static void main(String[] args){
        String formel = "(3 / 3) * (7 + 19) - 8";
        List<String> expression = calculator.readExpression(formel);
        System.out.println("Строка: " + expression);
        Queue<String> expQ = calculator.postfixRecord(expression);
        System.out.println("Постфиксная запись: " + expQ);
        float answer = calculator.calculatorOfExpression(expQ);
        System.out.println("Ответ = " + answer);
    }
}