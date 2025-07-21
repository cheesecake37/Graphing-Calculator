package util;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.dom.GenericDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SVGExporter {

    public static void exportAsSVG(JComponent component, String defaultFilename) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export as SVG");
        fileChooser.setFileFilter(new FileNameExtensionFilter("SVG Files", "svg"));
        fileChooser.setSelectedFile(new File(defaultFilename));

        int userSelection = fileChooser.showSaveDialog(component);
        if (userSelection != JFileChooser.APPROVE_OPTION) return;

        File svgFile = fileChooser.getSelectedFile();
        if (!svgFile.getName().toLowerCase().endsWith(".svg")) {
            svgFile = new File(svgFile.getParentFile(), svgFile.getName() + ".svg");
        }

        // Get dimensions
        int width = component.getWidth();
        int height = component.getHeight();

        // Setup SVG DOM
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        Document document = domImpl.createDocument(null, "svg", null);
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        // Paint the component onto the SVG canvas
        component.printAll(svgGenerator);

        // Apply scalable viewBox
        Element root = svgGenerator.getRoot();
        root.setAttributeNS(null, "viewBox", "0 0 " + width + " " + height);
        root.setAttributeNS(null, "preserveAspectRatio", "xMidYMid meet");
        root.removeAttribute("width");
        root.removeAttribute("height");

        // Write SVG to file
        try (FileWriter writer = new FileWriter(svgFile)) {
            svgGenerator.stream(root, writer, true, false);  // Use our modified root
            JOptionPane.showMessageDialog(component, "✅ Exported to:\n" + svgFile.getAbsolutePath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(component,
                    "❌ Failed to export SVG: " + e.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}