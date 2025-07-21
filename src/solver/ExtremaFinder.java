package solver;

import functions.Function;

import java.util.ArrayList;
import java.util.List;

public class ExtremaFinder {
    public static List<Double> findExtrema(Function f, double start, double end, double step) {
        List<Double> extrema = new ArrayList<>();

        for (double x = start + step; x <= end - step; x += step) {
            double y1 = f.evaluate(x - step);
            double y2 = f.evaluate(x);
            double y3 = f.evaluate(x + step);

            if ((y2 > y1 && y2 > y3) || (y2 < y1 && y2 < y3)) {
                extrema.add(x);
            }
        }

        return extrema;
    }
}
