import java.util.List;
import java.util.Queue;
/** @author Elena Chernuhina
 *  */
public class main_calculator {
    public static void main(String[] args){
        String formel = "3 * 9";
        List<String> expression = calculator.readExpression(formel);
        System.out.println("Строка: " + expression);
        Queue<String> expQ = calculator.postfixRecord(expression);
        System.out.println("Постфиксная запись: " + expQ);
        float answer = calculator.calculatorOfExpression(expQ);
        System.out.println("Ответ = " + answer);
    }
}