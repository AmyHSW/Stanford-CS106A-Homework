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

        addInteractors();
    }

    private void addInteractors() {
        add(new JLabel("Name "), SOUTH);

        nameField = new JTextField(NAME_LENGTH);
        nameField.setActionCommand("Graph");
        nameField.addActionListener(this);
        add(nameField, SOUTH);

        add(new JButton("Graph"), SOUTH);
        add(new JButton("Clear"), SOUTH);
        addActionListeners();
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Graph")) {
            String name = nameField.getText().trim();
            if (name.length() == 0) return;
            NameSurferEntry entry = database.findEntry(name);
            if (entry == null) return;
            graph.addEntry(entry);
        } else if (cmd.equals("Clear")) {
            graph.clear();
        }
        graph.update();
    }

    private NameSurferDataBase database;
    private NameSurferGraph graph;
    private JTextField nameField;
}
