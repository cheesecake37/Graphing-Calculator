package functions;

import java.awt.*;

public class TrigFunction implements Function {
    private Color color;
    private final String expression;

    public TrigFunction(String expression) {
        this.expression = expression;
    }

    @Override
    public double evaluate(double x) {
        try {
            String e = expression.toLowerCase().replace("x", "(" + x + ")");
            if (e.contains("sin")) return evalTrig(e, "sin", Math::sin);
            if (e.contains("cos")) return evalTrig(e, "cos", Math::cos);
            if (e.contains("tan")) return evalTrig(e, "tan", Math::tan);
        } catch (Exception ignored) {}
        return Double.NaN;
    }

    private double evalTrig(String expr, String op, java.util.function.DoubleUnaryOperator trigFunc) {
        String inner = expr.substring(expr.indexOf(op) + op.length());
        inner = inner.replaceAll("[()]", "");
        double val = Double.parseDouble(inner);
        return trigFunc.applyAsDouble(val);
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public String getExpression() {
        return expression;
    }
}
