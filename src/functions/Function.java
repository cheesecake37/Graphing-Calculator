package functions;

import java.awt.Color;

public interface Function {
    double evaluate(double x);

    Color getColor();
    void setColor(Color color);

    // 🔥 This is the missing method causing all @Override errors
    String getExpression();
}
