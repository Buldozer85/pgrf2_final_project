package functions;
import interfaces.FunctionInterface;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import services.FunctionService;

public class AbsoluteLinearFunction implements FunctionInterface {

    private Expression expression;

    public AbsoluteLinearFunction(String expression) {
        String replacedExpression = FunctionService.replaceAbsExpression(expression);

        // Odstranění vnějších svislých čar, pokud jsou přítomny
        if (replacedExpression.startsWith("|") && replacedExpression.endsWith("|")) {
            replacedExpression = "abs(" + replacedExpression.substring(1, replacedExpression.length() - 1) + ")";
        }

        this.expression = new ExpressionBuilder(replacedExpression)
                .variables("x")
                .build();
    }

    @Override
    public double value(double x) {
       return this.expression.setVariable("x", x).evaluate();
    }
}
