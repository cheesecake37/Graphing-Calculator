package util;

import java.io.Serializable;
import java.util.List;

public class WorkspaceState implements Serializable {
    private static final long serialVersionUID = 1L;

    public List<String> expressions;
    public boolean showExtrema;
    public boolean showInflection;
    public boolean showDerivative;
    public String areaX1;
    public String areaX2;

    public WorkspaceState(List<String> expressions, boolean showExtrema, boolean showInflection,
                          boolean showDerivative, String areaX1, String areaX2) {
        this.expressions = expressions;
        this.showExtrema = showExtrema;
        this.showInflection = showInflection;
        this.showDerivative = showDerivative;
        this.areaX1 = areaX1;
        this.areaX2 = areaX2;
    }
}
