package functions;

import interfaces.FunctionInterface;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class ExponentialFunction implements FunctionInterface {

    private final Expression expression;

    public ExponentialFunction(String expression) {
        String expression1 = "";

        if (expression.contains("=")) {
            String[] parts = expression.split("=", 2);
            expression1 = parts[1];
        }

        this.expression = new ExpressionBuilder(expression1)
                .variables("x")
                .build();
    }
    public static final String regex = "^y\\s*=\\s*(e|\\d)\\^\\((-?\\d*(\\.\\d+)?)x\\s*([+-]?\\s*\\d*(\\.\\d+)?)\\)\\s*([+-]?\\s*\\d*(\\.\\d+)?)*$";
    @Override
    public double value(double x) {
        return this.expression.setVariable("x", x).evaluate();
    }
}
