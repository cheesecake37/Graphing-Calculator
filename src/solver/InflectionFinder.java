package solver;

import functions.Function;

import java.util.ArrayList;
import java.util.List;

public class InflectionFinder {
    public static List<Double> findInflectionPoints(Function f, double start, double end, double step) {
        List<Double> inflections = new ArrayList<>();

        for (double x = start + step; x <= end - step; x += step) {
            double secondDerivativeLeft = secondDerivative(f, x - step);
            double secondDerivativeRight = secondDerivative(f, x + step);

            if (secondDerivativeLeft * secondDerivativeRight < 0) {
                inflections.add(x);
            }
        }

        return inflections;
    }

    private static double secondDerivative(Function f, double x) {
        double h = 0.001;
        double f1 = f.evaluate(x - h);
        double f2 = f.evaluate(x);
        double f3 = f.evaluate(x + h);
        return (f1 - 2 * f2 + f3) / (h * h);
    }
}
