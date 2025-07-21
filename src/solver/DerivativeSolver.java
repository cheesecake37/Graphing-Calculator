package solver;

import functions.Function;

public class DerivativeSolver {
    public static double derivative(Function f, double x) {
        double h = 1e-5;
        double fxh1 = f.evaluate(x + h);
        double fxh2 = f.evaluate(x - h);
        if (Double.isNaN(fxh1) || Double.isNaN(fxh2)) return Double.NaN;
        return (fxh1 - fxh2) / (2 * h);
    }
}
