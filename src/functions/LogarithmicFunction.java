package functions;

import java.awt.*;

public class LogarithmicFunction implements Function {
    private final String expression;
    private Color color;

    public LogarithmicFunction(String expression) {
        this.expression = expression;
    }

    @Override
    public double evaluate(double x) {
        try {
            if (x <= 0) return Double.NaN;
            if (expression.contains("ln"))
                return Math.log(x);
            else
                return Math.log10(x);
        } catch (Exception ignored) {
            return Double.NaN;
        }
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
