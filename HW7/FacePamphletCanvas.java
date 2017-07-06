import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas
                    implements FacePamphletConstants {

    public FacePamphletCanvas() {

    }

    public void showMessage(String msg) {
        if (lastMsg != null) remove(lastMsg);
        lastMsg = new GLabel(msg);
        lastMsg.setFont(MESSAGE_FONT);
        double x = (getWidth() - lastMsg.getWidth()) / 2.0;
        double y = getHeight() - BOTTOM_MESSAGE_MARGIN;
        lastMsg.setLocation(x, y);
        add(lastMsg);
    }

    public void displayProfile(FacePamphletProfile profile) {
        removeAll();
        if (lastMsg != null) add(lastMsg);

        if (profile == null) return;

        GLabel name = new GLabel(profile.getName());
        name.setFont(PROFILE_NAME_FONT);
        name.setColor(Color.BLUE);
        add(name, LEFT_MARGIN, TOP_MARGIN + name.getHeight());

        GObject image = getProfileImage(profile.getImage());
        add(image, LEFT_MARGIN, name.getY() + IMAGE_MARGIN);

        GLabel status = new GLabel("");;
        if (profile.getStatus() != "") {
            status.setLabel(profile.getName() + " is " + profile.getStatus());
        } else {
            status.setLabel("No current status");
        }
        status.setFont(PROFILE_STATUS_FONT);
        double yStatus = image.getY() + IMAGE_HEIGHT
                         + STATUS_MARGIN + status.getHeight();
        add(status, LEFT_MARGIN, yStatus);

        GLabel friendLabel = new GLabel("Friends:");
        friendLabel.setFont(PROFILE_FRIEND_LABEL_FONT);
        double xFriend = getWidth() / 2.0;
        double yFriend = image.getY();
        add(friendLabel, xFriend, yFriend);

        int i = 1;
        Iterator<String> iterator = profile.getFriends();
        while (iterator.hasNext()) {
            GLabel friend = new GLabel(iterator.next());
            friend.setFont(PROFILE_FRIEND_FONT);
            add(friend, xFriend, yFriend + friend.getHeight() * (i++));
        }
    }

    private GObject getProfileImage(GImage image) {
        if (image == null) {
            GCompound nullImage = new GCompound();
            nullImage.add(new GRect(IMAGE_WIDTH, IMAGE_HEIGHT), 0, 0);
            GLabel imageLabel = new GLabel("No Image");
            imageLabel.setFont(PROFILE_IMAGE_FONT);
            nullImage.add(imageLabel,
                          (IMAGE_WIDTH - imageLabel.getWidth()) / 2.0,
                          (IMAGE_HEIGHT + imageLabel.getHeight()) / 2.0 );
            return nullImage;
        } else {
            image.setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
            return image;
        }
    }

    private GLabel lastMsg;
}
