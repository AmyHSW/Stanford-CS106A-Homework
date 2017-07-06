import acm.graphics.*;
import java.util.*;

public class FacePamphletProfile implements FacePamphletConstants {

    public FacePamphletProfile(String name) {
        profileName = name;
        profileStatus = "";
        profileImage = null;
    }

    public String getName() {
        return profileName;
    }

    public GImage getImage() {
        return profileImage;
    }

    public void setImage(GImage image) {
        profileImage = image;
    }

    public String getStatus() {
        return profileStatus;
    }

    public void setStatus(String status) {
        profileStatus = status;
    }

    public boolean addFriend(String friend) {
        if (!friendList.contains(friend) && !friend.equals(profileName)) {
            return friendList.add(friend);
        } else {
            return false;
        }
    }

    public boolean removeFriend(String friend) {
        return friendList.remove(friend);
    }

    public Iterator<String> getFriends() {
        return friendList.iterator();
    }

    public String toString() {
        String str = profileName + " (" + profileStatus + "): ";
        if (friendList.size() > 0) str += friendList.get(0);
        for (int i = 1; i < friendList.size(); i++) {
            str += ", " + friendList.get(i);
        }
        return str;
    }

    private String profileName;
    private String profileStatus;
    private GImage profileImage;
    private ArrayList<String> friendList = new ArrayList<String>();
}
