package parser;

import functions.*;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class FunctionParser {
    public static Function parse(String input) {
        input = input.trim();

        // Allow "y = ..." or "f(x) = ..." formats
        if (input.toLowerCase().startsWith("y=") || input.toLowerCase().startsWith("y ="))
            input = input.substring(input.indexOf('=') + 1).trim();
        if (input.toLowerCase().matches("f\\s*\\(\\s*x\\s*\\)\\s*=.*"))
            input = input.substring(input.indexOf('=') + 1).trim();

        return new ParsedFunction(input);
    }

    public static class ParsedFunction implements Function {
        private final String exprString;
        private Expression expression;
        private java.awt.Color color = java.awt.Color.BLUE;

        public ParsedFunction(String expr) {
            this.exprString = expr;
            try {
                this.expression = new ExpressionBuilder(expr)
                        .variables("x")
                        .function(new net.objecthunter.exp4j.function.Function("step", 1) {
                            @Override
                            public double apply(double... args) {
                                // Heaviside step function: returns 1 if x >= 0, else 0
                                return args[0] >= 0 ? 1.0 : 0.0;
                            }
                        })
                        .build();
            } catch (Exception e) {
                System.err.println("Error parsing expression: " + expr);
            }
        }

        @Override
        public double evaluate(double x) {
            try {
                expression.setVariable("x", x);
                return expression.evaluate();
            } catch (Exception e) {
                return Double.NaN;
            }
        }

        @Override
        public void setColor(java.awt.Color color) {
            this.color = color;
        }

        @Override
        public java.awt.Color getColor() {
            return color;
        }

        @Override
        public String getExpression() {
            return exprString;
        }
    }
}