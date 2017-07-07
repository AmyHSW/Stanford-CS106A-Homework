import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {

    public static void main(String[] args) {
        new NameSurfer().start(args);
    }

    public void init() {
        database = new NameSurferDataBase(NAMES_DATA_FILE);
        graph = new NameSurferGraph();
        add(graph);

        name = new JTextField(NAME_LENGTH);
        name.setActionCommand("Graph");

        JButton graphButton = new JButton("Graph");
        JButton clearButton = new JButton("Clear");

        add(new JLabel("Name "), SOUTH);
        add(name, SOUTH);
        add(graphButton, SOUTH);
        add(clearButton, SOUTH);

        addActionListeners();
        name.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Graph")) {
            NameSurferEntry entry = database.findEntry(name.getText());
            if (entry != null) {
                graph.addEntry(entry);
            }
        }
        if (e.getActionCommand().equals("Clear")) {
            graph.clear();
        }
    }

    private JTextField name;
    private NameSurferDataBase database;
    private NameSurferGraph graph;
}
