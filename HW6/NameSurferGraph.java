import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas
    implements NameSurferConstants, ComponentListener {

    public NameSurferGraph() {
        addComponentListener(this);
        clear();
    }

    public void clear() {
        entryList = new ArrayList<NameSurferEntry>();
        update();
    }

    public void addEntry(NameSurferEntry entry) {
        if (entryList.contains(entry)) return;
        entryList.add(entry);
        update();
    }

    private void displayEntry(NameSurferEntry entry) {
        double width = getWidth();
        double height = getHeight();
        double yBottomLine = height - GRAPH_MARGIN_SIZE;

        double xInterval = width / (double) NDECADES;
        double yInterval = (double) (height - 2 * GRAPH_MARGIN_SIZE) / MAX_RANK;

        Color color = getColor(entryList.indexOf(entry));

        double x = 0, y1 = 0, y2 = 0;

        if (entry.getRank(0) == 0) {
            y1 = yBottomLine;
        } else {
            y1 = (entry.getRank(0) - 1) * yInterval + GRAPH_MARGIN_SIZE;
        }

        for (int i = 1; i < NDECADES; i++) {
            if (entry.getRank(i) == 0) {
                y2 = yBottomLine;
            } else {
                y2 = (entry.getRank(i) - 1) * yInterval + GRAPH_MARGIN_SIZE;
            }

            GLine line = new GLine(x, y1, x + xInterval, y2);
            line.setColor(color);
            add(line);

            GLabel label = getLabel(i - 1, entry);
            label.setColor(color);
            add(label, x, y1);

            x += xInterval;
            y1 = y2;
        }
        GLabel label = getLabel(NDECADES - 1, entry);
        label.setColor(color);
        add(label, x, y1);
    }

    private GLabel getLabel(int decade, NameSurferEntry entry) {
        return new GLabel(entry.getName() + " "
                          + (entry.getRank(decade) == 0? "*" : entry.getRank(decade)));
    }

    private Color getColor(int i) {
        switch (i % 4) {
        case 0: return Color.BLACK;
        case 1: return Color.RED;
        case 2: return Color.BLUE;
        case 3: return Color.MAGENTA;
        default: return null;
        }
    }

    private void drawBackground() {
        double width = getWidth();
        double height = getHeight();

        double xInterval = width / (double) NDECADES;
        double x = 0;
        int decade = START_DECADE;
        for (int i = 0; i < NDECADES; i++) {
            add(new GLine(x, 0, x, height));
            add(new GLabel(" " + decade, x, height));
            decade += 10;
            x += xInterval;
        }

        double yTopLine = GRAPH_MARGIN_SIZE;
        double yBottomLine = height - GRAPH_MARGIN_SIZE;
        add(new GLine(0, yTopLine, width, yTopLine));
        add(new GLine(0, yBottomLine, width, yBottomLine));
    }

    public void update() {
        removeAll();
        drawBackground();
        for (int i = 0; i < entryList.size(); i++) {
            displayEntry(entryList.get(i));
        }
    }

    public void componentHidden(ComponentEvent e) { }
    public void componentMoved(ComponentEvent e) { }
    public void componentResized(ComponentEvent e) { update(); }
    public void componentShown(ComponentEvent e) { }

    private ArrayList<NameSurferEntry> entryList;
}
