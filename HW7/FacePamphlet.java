import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphlet extends ConsoleProgram
                    implements FacePamphletConstants {

    public static void main(String[] args) {
        new FacePamphlet().start(args);
    }

    public void init() {
        canvas = new FacePamphletCanvas();
        add(canvas);
        addNorthInteractors();
        addWestInteractors();
        addActionListeners();
        database = new FacePamphletDatabase();
    }

    private void addNorthInteractors() {
        add(new JLabel("Name"), NORTH);
        nameText = new JTextField(TEXT_FIELD_SIZE);
        nameText.addActionListener(this);
        add(nameText, NORTH);
        add(new JButton("Add"), NORTH);
        add(new JButton("Delete"), NORTH);
        add(new JButton("Lookup"), NORTH);
    }

    private void addWestInteractors() {
        statusText = new JTextField(TEXT_FIELD_SIZE);
        statusText.setActionCommand(COMMAND_CHANGE_STATUS);
        add(statusText, WEST);
        statusText.addActionListener(this);
        add(new JButton(COMMAND_CHANGE_STATUS), WEST);
        add(new JLabel(EMPTY_LABEL_TEXT), WEST);

        pictureText = new JTextField(TEXT_FIELD_SIZE);
        pictureText.setActionCommand(COMMAND_CHANGE_PICTURE);
        add(pictureText, WEST);
        pictureText.addActionListener(this);
        add(new JButton(COMMAND_CHANGE_PICTURE), WEST);
        add(new JLabel(EMPTY_LABEL_TEXT), WEST);

        friendText = new JTextField(TEXT_FIELD_SIZE);
        friendText.setActionCommand(COMMAND_ADD_FRIEND);
        add(friendText, WEST);
        friendText.addActionListener(this);
        add(new JButton(COMMAND_ADD_FRIEND), WEST);
    }

    public void actionPerformed(ActionEvent e) {
        String name = nameText.getText();
        switch (e.getActionCommand()) {
        case "Add": addProfile(name); break;
        case "Delete": deleteProfile(name); break;
        case "Lookup": lookupProfile(name); break;
        }
    }

    private void addProfile(String name) {
        if (database.containsProfile(name)) {
            println("Add: profile for " + name + "alrealy exists: "
                    + database.getProfile(name));
        } else {
            FacePamphletProfile newProfile = new FacePamphletProfile(name);
            database.addProfile(newProfile);
            println("Add: new profile: " + newProfile);
        }
    }

    private void deleteProfile(String name) {
        if (database.containsProfile(name)) {
            println("Delete: " + database.getProfile(name));
            database.deleteProfile(name);
        } else {
            println("Delete: profile with name (" + name + ") does not exist.");
        }
    }

    private void lookupProfile(String name) {
        if (database.containsProfile(name)) {
            println("Lookup: " + database.getProfile(name));
        } else {
            println("Lookup: profile with name (" + name + ") does not exist.");
        }
    }

    private JTextField nameText, statusText, pictureText, friendText;
    private FacePamphletCanvas canvas;
    private FacePamphletDatabase database;
}
