package util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ColorUtil {
    private static final List<Color> COLORS = new ArrayList<>();
    private static int index = 0;

    static {
        COLORS.add(Color.BLUE);
        COLORS.add(Color.RED);
        COLORS.add(Color.GREEN.darker());
        COLORS.add(Color.MAGENTA);
        COLORS.add(Color.ORANGE);
        COLORS.add(Color.PINK);
        COLORS.add(Color.CYAN.darker());
        COLORS.add(new Color(128, 0, 128)); // Purple
        COLORS.add(new Color(0, 128, 128)); // Teal
    }

    public static Color getNextColor() {
        Color color = COLORS.get(index % COLORS.size());
        index++;
        return color;
    }

    public static void reset() {
        index = 0;
    }
}
