package solver;

import functions.Function;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class IntersectionFinder {
    public static List<Point2D.Double> findIntersections(Function f1, Function f2, double start, double end, double step) {
        List<Point2D.Double> points = new ArrayList<>();

        for (double x = start; x < end; x += step) {
            double y1 = f1.evaluate(x);
            double y2 = f2.evaluate(x);
            double diff1 = y1 - y2;

            double y1Next = f1.evaluate(x + step);
            double y2Next = f2.evaluate(x + step);
            double diff2 = y1Next - y2Next;

            if (Double.isNaN(diff1) || Double.isNaN(diff2)) continue;

            if (diff1 * diff2 <= 0) {
                double rootX = refine(f1, f2, x, x + step);
                double rootY = f1.evaluate(rootX);
                if (!Double.isNaN(rootX) && !Double.isNaN(rootY))
                    points.add(new Point2D.Double(rootX, rootY));
            }
        }

        return points;
    }

    private static double refine(Function f1, Function f2, double a, double b) {
        for (int i = 0; i < 20; i++) {
            double mid = (a + b) / 2.0;
            double fa = f1.evaluate(a) - f2.evaluate(a);
            double fm = f1.evaluate(mid) - f2.evaluate(mid);

            if (fa * fm <= 0)
                b = mid;
            else
                a = mid;
        }
        return (a + b) / 2.0;
    }
}
