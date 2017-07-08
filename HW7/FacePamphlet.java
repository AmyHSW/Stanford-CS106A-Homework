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

        add(new JButton(COMMAND_ADD), NORTH);
        add(new JButton(COMMAND_DELETE), NORTH);
        add(new JButton(COMMAND_LOOKUP), NORTH);
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
        String name = nameText.getText().trim();
        switch (e.getActionCommand()) {
        case COMMAND_ADD: addProfile(name); break;
        case COMMAND_DELETE: deleteProfile(name); break;
        case COMMAND_LOOKUP: lookupProfile(name); break;
        case COMMAND_CHANGE_STATUS: changeStatus(statusText.getText()); break;
        case COMMAND_CHANGE_PICTURE: changePicture(pictureText.getText()); break;
        case COMMAND_ADD_FRIEND: addFriend(friendText.getText()); break;
        }
    }

    private void addProfile(String name) {
        if (name.length() == 0) return;
        if (database.containsProfile(name)) {
            canvas.showMessage("A profile with the name " + name + " alrealy exists");
        } else {
            FacePamphletProfile newProfile = new FacePamphletProfile(name);
            database.addProfile(newProfile);
            canvas.showMessage("New profile created");
        }
        currentProfile = database.getProfile(name);
        canvas.displayProfile(currentProfile);
    }

    private void deleteProfile(String name) {
        if (name.length() == 0) return;
        if (database.containsProfile(name)) {
            canvas.showMessage("Profile of " + name + " deleted");
            database.deleteProfile(name);
        } else {
            canvas.showMessage("A profile with the name " + name + " does not exist");
        }
        currentProfile = null;
        canvas.displayProfile(currentProfile);
    }

    private void lookupProfile(String name) {
        if (name.length() == 0) return;
        if (database.containsProfile(name)) {
            canvas.showMessage("Displaying " + name);
        } else {
            canvas.showMessage("A profile with the name " + name + " does not exist");
        }
        currentProfile = database.getProfile(name);
        canvas.displayProfile(currentProfile);
    }

    private void changeStatus(String status) {
        status = status.trim();
        if (status.length() == 0) return;
        if (currentProfile == null) {
            canvas.showMessage("Please select a profile to change status");
            return;
        }
        currentProfile.setStatus(status);
        canvas.showMessage("Status updated to " + status);
        canvas.displayProfile(currentProfile);
    }

    private void changePicture(String picture) {
        picture = picture.trim();
        if (picture.length() == 0) return;
        if (currentProfile == null) {
            canvas.showMessage("Please select a profile to change picture");
            return;
        }
        GImage image = null;
        try {
            image = new GImage(picture);
            currentProfile.setImage(image);
            canvas.showMessage("Picture updated");
            canvas.displayProfile(currentProfile);
        } catch (ErrorException ex) {
            canvas.showMessage("Unable to open image file: " + picture);
        }
    }

    private void addFriend(String friend) {
        if (currentProfile == null) {
            canvas.showMessage("Please select a profile to add friend");
            return;
        }
        friend = friend.trim();
        if (friend.length() == 0) return;
        if (!database.containsProfile(friend)) {
            canvas.showMessage(friend + " does not exist");
            return;
        }
        if (friend.equals(currentProfile.getName())) {
            canvas.showMessage(friend + " is the name of current profile."
                               + " Please try another name");
            return;
        }
        if (currentProfile.addFriend(friend)) {
            database.getProfile(friend).addFriend(currentProfile.getName());
            canvas.showMessage(friend + " added as a friend");
            canvas.displayProfile(currentProfile);
        } else {
            canvas.showMessage(currentProfile.getName() + " already has "
                               + friend + " as a friend");
        }
     }

    private JTextField nameText, statusText, pictureText, friendText;
    private FacePamphletCanvas canvas;
    private FacePamphletDatabase database;
    private FacePamphletProfile currentProfile;
}
