package ui;

import functions.Function;
import parser.FunctionParser;
import solver.IntersectionFinder;
import util.ColorUtil;
import util.SVGExporter;
import util.WorkspaceState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FunctionPlotterGUI extends JFrame {
    private ZoomablePlotPanel plotPanel;
    private JTextField functionInputField;
    private JCheckBox derivativeBox;
    private JTextField areaStartField, areaEndField;
    private JLabel clickedPointLabel;
    private JCheckBox extremaBox, inflectionBox;

    public FunctionPlotterGUI() {
        super("Function Plotter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout());

        // MAIN VERTICAL PANEL
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        // FIRST ROW
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        functionInputField = new JTextField(30);
        JButton plotButton = new JButton("Plot");
        JButton areaButton = new JButton("Area");
        areaStartField = new JTextField(5);
        areaEndField = new JTextField(5);

        row1.add(new JLabel("f(x):"));
        row1.add(functionInputField);
        row1.add(plotButton);
        row1.add(areaButton);
        row1.add(new JLabel("x1:"));
        row1.add(areaStartField);
        row1.add(new JLabel("x2:"));
        row1.add(areaEndField);

        // SECOND ROW
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        extremaBox = new JCheckBox("Show Extrema", true);
        inflectionBox = new JCheckBox("Show Inflection Points", true);
        derivativeBox = new JCheckBox("Show Derivative", true);
        JButton exportButton = new JButton("Export SVG");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");

        row2.add(extremaBox);
        row2.add(inflectionBox);
        row2.add(derivativeBox);
        row2.add(exportButton);
        row2.add(saveButton);
        row2.add(loadButton);

        inputPanel.add(row1);
        inputPanel.add(row2);

        add(inputPanel, BorderLayout.NORTH);

        plotPanel = new ZoomablePlotPanel();
        add(plotPanel, BorderLayout.CENTER);

        clickedPointLabel = new JLabel("Click on a point on the curve to see (x, y)");
        clickedPointLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(clickedPointLabel, BorderLayout.SOUTH);

        plotPanel.setClickListener((x, y) ->
                clickedPointLabel.setText(String.format("Clicked Point: (%.5f, %.5f)", x, y)));

        // Plot Button Logic
        plotButton.addActionListener((ActionEvent e) -> {
            plotPanel.clearAll();
            ColorUtil.reset();

            String inputText = functionInputField.getText();
            if (inputText.isEmpty()) return;

            String[] expressions = inputText.split(",");
            List<Function> functions = new ArrayList<>();

            for (String expr : expressions) {
                expr = expr.trim();
                if (!expr.isEmpty()) {
                    try {
                        Function function = FunctionParser.parse(expr);
                        function.setColor(ColorUtil.getNextColor());
                        functions.add(function);
                        plotPanel.addFunction(function);

                        if (derivativeBox.isSelected()) {
                            Function derivative = new Function() {
                                private Color color;
                                @Override
                                public double evaluate(double x) {
                                    return solver.DerivativeSolver.derivative(function, x);
                                }
                                @Override
                                public String getExpression() {
                                    return "f'(x)";
                                }
                                @Override
                                public void setColor(Color c) {
                                    this.color = c;
                                }
                                @Override
                                public Color getColor() {
                                    return color;
                                }
                            };
                            derivative.setColor(ColorUtil.getNextColor());
                            plotPanel.addFunction(derivative);
                        }

                        plotPanel.markExtremaAndInflection(function, -100, 100,
                                extremaBox.isSelected(), inflectionBox.isSelected());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Failed to parse: " + expr);
                    }
                }
            }

            for (int i = 0; i < functions.size(); i++) {
                for (int j = i + 1; j < functions.size(); j++) {
                    plotPanel.markIntersectionPoints(
                            IntersectionFinder.findIntersections(functions.get(i), functions.get(j), -100, 100, 0.01)
                    );
                }
            }

            plotPanel.repaint();
        });

        // Area Button Logic
        areaButton.addActionListener((ActionEvent e) -> {
            try {
                double x1 = Double.parseDouble(areaStartField.getText());
                double x2 = Double.parseDouble(areaEndField.getText());
                plotPanel.shadeAndCalculateArea(x1, x2);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid x1 and x2 values.");
            }
        });

        // Export SVG Button Logic
        exportButton.addActionListener((ActionEvent e) -> {
            try {
                SVGExporter.exportAsSVG(plotPanel, "plot_output.svg");
                JOptionPane.showMessageDialog(this, "✅ Exported to plot_output.svg");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Export failed: " + ex.getMessage());
            }
        });

        // Save Button Logic
        saveButton.addActionListener(e -> {
            try {
                List<String> exprs = Arrays.asList(functionInputField.getText().split(","));
                WorkspaceState state = new WorkspaceState(
                        exprs,
                        extremaBox.isSelected(),
                        inflectionBox.isSelected(),
                        derivativeBox.isSelected(),
                        areaStartField.getText(),
                        areaEndField.getText()
                );
                JFileChooser chooser = new JFileChooser();
                if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(chooser.getSelectedFile()))) {
                        oos.writeObject(state);
                        JOptionPane.showMessageDialog(this, "✅ Workspace saved.");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Save failed: " + ex.getMessage());
            }
        });

        // Load Button Logic
        loadButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(chooser.getSelectedFile()))) {
                    WorkspaceState state = (WorkspaceState) ois.readObject();
                    functionInputField.setText(String.join(",", state.expressions));
                    extremaBox.setSelected(state.showExtrema);
                    inflectionBox.setSelected(state.showInflection);
                    derivativeBox.setSelected(state.showDerivative);
                    areaStartField.setText(state.areaX1);
                    areaEndField.setText(state.areaX2);
                    JOptionPane.showMessageDialog(this, "✅ Workspace loaded. Click Plot to view.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "❌ Load failed: " + ex.getMessage());
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FunctionPlotterGUI::new);
    }
}
