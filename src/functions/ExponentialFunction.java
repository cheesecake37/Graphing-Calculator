package functions;

import java.awt.*;

public class ExponentialFunction implements Function {
    private final String expression;
    private Color color;

    public ExponentialFunction(String expression) {
        this.expression = expression;
    }

    @Override
    public double evaluate(double x) {
        try {
            if (expression.contains("e^")) {
                return Math.exp(x);
            } else if (expression.matches(".*\\d+\\^x.*")) {
                double base = Double.parseDouble(expression.split("\\^")[0]);
                return Math.pow(base, x);
            }
        } catch (Exception ignored) {}
        return Double.NaN;
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
