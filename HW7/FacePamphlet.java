import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphlet extends Program
                    implements FacePamphletConstants {

    public static void main(String[] args) {
        new FacePamphlet().start(args);
    }

    public void init() {
        database = new FacePamphletDatabase();

        canvas = new FacePamphletCanvas();
        add(canvas);

        addNorthInteractors();
        addWestInteractors();
        addActionListeners();
    }

    private void addNorthInteractors() {
        add(new JLabel("Name"), NORTH);

        nameText = new JTextField(TEXT_FIELD_SIZE);
        add(nameText, NORTH);

        add(new JButton(COMMAND_ADD), NORTH);
        add(new JButton(COMMAND_DELETE), NORTH);
        add(new JButton(COMMAND_LOOKUP), NORTH);
    }

    private void addWestInteractors() {
        statusText = new JTextField(TEXT_FIELD_SIZE);
        statusText.setActionCommand(COMMAND_CHANGE_STATUS);
        statusText.addActionListener(this);
        add(statusText, WEST);
        add(new JButton(COMMAND_CHANGE_STATUS), WEST);

        add(new JLabel(EMPTY_LABEL_TEXT), WEST);

        pictureText = new JTextField(TEXT_FIELD_SIZE);
        pictureText.setActionCommand(COMMAND_CHANGE_PICTURE);
        pictureText.addActionListener(this);
        add(pictureText, WEST);
        add(new JButton(COMMAND_CHANGE_PICTURE), WEST);

        add(new JLabel(EMPTY_LABEL_TEXT), WEST);

        friendText = new JTextField(TEXT_FIELD_SIZE);
        friendText.setActionCommand(COMMAND_ADD_FRIEND);
        friendText.addActionListener(this);
        add(friendText, WEST);
        add(new JButton(COMMAND_ADD_FRIEND), WEST);
    }

    public void actionPerformed(ActionEvent e) {
        String name = nameText.getText().trim();
        switch (e.getActionCommand()) {
        case COMMAND_ADD: addProfile(name); break;
        case COMMAND_DELETE: deleteProfile(name); break;
        case COMMAND_LOOKUP: lookupProfile(name); break;
        case COMMAND_CHANGE_STATUS: changeStatus(statusText.getText().trim()); break;
        case COMMAND_CHANGE_PICTURE: changePicture(pictureText.getText().trim()); break;
        case COMMAND_ADD_FRIEND: addFriend(friendText.getText().trim()); break;
        }
        canvas.refresh(currentProfile, currentMsg);
    }

    private void addProfile(String name) {
        if (name.length() == 0) return;
        if (database.containsProfile(name)) {
            currentMsg = "A profile with the name " + name + " alrealy exists";
        } else {
            FacePamphletProfile newProfile = new FacePamphletProfile(name);
            database.addProfile(newProfile);
            currentMsg = "New profile created";
        }
        currentProfile = database.getProfile(name);
    }

    private void deleteProfile(String name) {
        if (name.length() == 0) return;
        if (database.containsProfile(name)) {
            database.deleteProfile(name);
            currentMsg = "Profile of " + name + " deleted";
        } else {
            currentMsg = "A profile with the name " + name + " does not exist";
        }
        currentProfile = null;
    }

    private void lookupProfile(String name) {
        if (name.length() == 0) return;
        if (database.containsProfile(name)) {
            currentMsg = "Displaying " + name;
        } else {
            currentMsg = "A profile with the name " + name + " does not exist";
        }
        currentProfile = database.getProfile(name);
    }

    private void changeStatus(String status) {
        if (status.length() == 0) return;
        if (currentProfile == null) {
            currentMsg = "Please select a profile to change status";
        } else {
            currentProfile.setStatus(status);
            currentMsg = "Status updated to " + status;
        }
    }

    private void changePicture(String picture) {
        if (picture.length() == 0) return;
        if (currentProfile == null) {
            currentMsg = "Please select a profile to change picture";
        } else {
            try {
                GImage image = new GImage(picture);
                currentProfile.setImage(image);
                currentMsg = "Picture updated";
            } catch (ErrorException ex) {
                currentMsg = "Unable to open image file: " + picture;
            }
        }
    }

    private void addFriend(String friend) {
        if (friend.length() == 0) return;
        if (currentProfile == null) {
            currentMsg = "Please select a profile to add friend";
        } else if (!database.containsProfile(friend)) {
            currentMsg = friend + " does not exist";
        } else if (friend.equals(currentProfile.getName())) {
            currentMsg = friend + " is the name of current profile."
                         + " Please try another name";
        } else if (currentProfile.addFriend(friend)) {
            database.getProfile(friend).addFriend(currentProfile.getName());
            currentMsg = friend + " added as a friend";
        } else {
            currentMsg = currentProfile.getName() + " already has "
                         + friend + " as a friend";
        }
    }

    private FacePamphletDatabase database;
    private FacePamphletCanvas canvas;
    private JTextField nameText, statusText, pictureText, friendText;

    private FacePamphletProfile currentProfile;
    private String currentMsg;
}
