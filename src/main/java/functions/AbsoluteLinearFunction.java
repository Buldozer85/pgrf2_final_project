package functions;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.commons.math3.analysis.UnivariateFunction;
import services.FunctionService;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class AbsoluteLinearFunction implements UnivariateFunction {

    private Expression expression;

    public AbsoluteLinearFunction(String expression) {
     //   System.out.println(FunctionService.replaceAbsExpression(expression));
/*        this.expression = new ExpressionBuilder(FunctionService.replaceAbsExpression(expression))
                .variables("x")
                .build();*/

    }

    @Override
    public double value(double x) {
        return x;
      // return this.expression.setVariable("x", x).evaluate();
    }
}
