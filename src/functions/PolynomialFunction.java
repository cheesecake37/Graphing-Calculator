package functions;

import java.awt.*;

public class PolynomialFunction implements Function {
    private final double[] coefficients; // from constant to highest degree
    private Color color;
    private final String expression;

    public PolynomialFunction(String expression) {
        this.expression = expression;
        this.coefficients = parse(expression);
    }

    private double[] parse(String expr) {
        expr = expr.replaceAll("\\s+", "").replaceAll("-", "+-");
        String[] terms = expr.split("\\+");
        double[] coef = new double[10]; // Supports degree 9 max

        for (String term : terms) {
            if (term.isEmpty()) continue;

            int degree = 0;
            double value = 0;

            if (term.contains("x")) {
                if (term.contains("^")) {
                    String[] parts = term.split("x\\^");
                    value = parts[0].isEmpty() || parts[0].equals("+") ? 1 : parts[0].equals("-") ? -1 : Double.parseDouble(parts[0]);
                    degree = Integer.parseInt(parts[1]);
                } else {
                    value = term.equals("x") ? 1 : term.equals("-x") ? -1 : Double.parseDouble(term.replace("x", ""));
                    degree = 1;
                }
            } else {
                value = Double.parseDouble(term);
                degree = 0;
            }

            coef[degree] += value;
        }

        return coef;
    }

    @Override
    public double evaluate(double x) {
        double result = 0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, i);
        }
        return result;
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
