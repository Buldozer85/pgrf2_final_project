package functions;

import interfaces.FunctionInterface;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class LinearFunction implements FunctionInterface {
    Expression expression;

    public static final String regex = "^\\s*y\\s*=(\\s*[+-]?\\s*\\d*\\s*x?\\s*([+-]?\\s*\\d+\\s*)*|(\\s*[+-]?\\s*\\d+\\s*))$";

    public LinearFunction(String expression) {
        String expression1 = "";

        if (expression.contains("=")) {
            String[] parts = expression.split("=", 2);
            expression1 = parts[1];
        }

        this.expression = new ExpressionBuilder(expression1)
                .variables("x")
                .build();
    }

    @Override
    public double value(double x) {
        return this.expression.setVariable("x", x).evaluate();
    }
}
