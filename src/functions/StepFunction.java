package functions;

import java.awt.*;

public class StepFunction implements Function {
    private final String expression;
    private Color color;

    public StepFunction(String expression) {
        this.expression = expression;
    }

    @Override
    public double evaluate(double x) {
        return Math.floor(x);  // ✅ Step returns floor value for each x
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