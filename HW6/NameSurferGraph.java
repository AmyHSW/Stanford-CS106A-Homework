import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas
    implements NameSurferConstants, ComponentListener {

    public NameSurferGraph() {
        entryList = new ArrayList<NameSurferEntry>();
        addComponentListener(this);
    }

    public void clear() {
        entryList = new ArrayList<NameSurferEntry>();
    }

    public void addEntry(NameSurferEntry entry) {
        if (entryList.contains(entry)) return;
        entryList.add(entry);
    }

    public void update() {
        removeAll();
        drawBackground();
        for (int i = 0; i < entryList.size(); i++) {
            displayEntry(entryList.get(i));
        }
    }

    private void drawBackground() {
        double width = getWidth();
        double height = getHeight();

        double xInterval = width / NDECADES;

        double x = 0;
        int decade = START_DECADE;
        for (int i = 0; i < NDECADES; i++) {
            add(new GLine(x, 0, x, height));
            add(new GLabel(" " + decade, x, height));
            x += xInterval;
            decade += 10;
        }

        double yTopLine = GRAPH_MARGIN_SIZE;
        double yBottomLine = height - GRAPH_MARGIN_SIZE;
        add(new GLine(0, yTopLine, width, yTopLine));
        add(new GLine(0, yBottomLine, width, yBottomLine));
    }

    private void displayEntry(NameSurferEntry entry) {
        Color color = COLORS[entryList.indexOf(entry) % COLORS.length];

        double width = getWidth();
        double xInterval = width / NDECADES;

        double x = 0;
        double y1 = getY(entry.getRank(0));
        for (int i = 1; i < NDECADES; i++) {
            double y2 = getY(entry.getRank(i));
            addLine(x, y1, x + xInterval, y2, color);
            addLabel(entry, i - 1, x, y1, color);
            x += xInterval;
            y1 = y2;
        }
        addLabel(entry, NDECADES - 1, x, y1, color);
    }

    private double getY(int rank) {
        double height = getHeight();
        double yBottomLine = height - GRAPH_MARGIN_SIZE;
        double yInterval = (height - 2 * GRAPH_MARGIN_SIZE) / MAX_RANK;

        if (rank == 0) {
            return yBottomLine;
        } else {
            return (rank - 1) * yInterval + GRAPH_MARGIN_SIZE;
        }
    }

    private void addLine(double x1, double y1, double x2, double y2, Color color) {
        GLine line = new GLine(x1, y1, x2, y2);
        line.setColor(color);
        add(line);
    }

    private void addLabel(NameSurferEntry entry, int decade,
                          double x, double y, Color color) {
        GLabel label =
            new GLabel(entry.getName()
                       + " "
                       + (entry.getRank(decade) == 0 ? "*" : entry.getRank(decade)));
        label.setColor(color);
        add(label, x, y);
    }

    public void componentHidden(ComponentEvent e) { }
    public void componentMoved(ComponentEvent e) { }
    public void componentResized(ComponentEvent e) { update(); }
    public void componentShown(ComponentEvent e) { }

    private ArrayList<NameSurferEntry> entryList;
}
