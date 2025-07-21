package solver;

import functions.Function;

public class IntegrationSolver {
    public static double integrate(Function f, double a, double b, double step) {
        double area = 0.0;
        for (double x = a; x < b; x += step) {
            double y1 = f.evaluate(x);
            double y2 = f.evaluate(x + step);
            if (Double.isNaN(y1) || Double.isNaN(y2)) continue;
            area += 0.5 * (y1 + y2) * step;
        }
        return area;
    }

    public static double integrateBetween(Function f1, Function f2, double a, double b, double step) {
        double area = 0.0;
        for (double x = a; x < b; x += step) {
            double y1a = f1.evaluate(x);
            double y2a = f2.evaluate(x);
            double y1b = f1.evaluate(x + step);
            double y2b = f2.evaluate(x + step);

            if (Double.isNaN(y1a) || Double.isNaN(y2a) || Double.isNaN(y1b) || Double.isNaN(y2b)) continue;

            double diff1 = y1a - y2a;
            double diff2 = y1b - y2b;
            area += 0.5 * (Math.abs(diff1) + Math.abs(diff2)) * step;
        }
        return area;
    }
}
