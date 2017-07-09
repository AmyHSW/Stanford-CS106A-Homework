import acm.graphics.*;
import java.util.*;

public class FacePamphletProfile implements FacePamphletConstants {

    public FacePamphletProfile(String name) {
        this.name = name;
        status = "";
        image = null;
        friendList = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public GImage getImage() {
        return image;
    }

    public void setImage(GImage image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean addFriend(String friend) {
        if (friendList.contains(friend)) {
            return false;
        } else {
            return friendList.add(friend);
        }
    }

    public boolean removeFriend(String friend) {
        return friendList.remove(friend);
    }

    public Iterator<String> getFriends() {
        return friendList.iterator();
    }

    public String toString() {
        String str = name + " (" + status + "): ";
        if (friendList.size() > 0) str += friendList.get(0);
        for (int i = 1; i < friendList.size(); i++) {
            str += ", " + friendList.get(i);
        }
        return str;
    }

    private final String name;
    private String status;
    private GImage image;
    private ArrayList<String> friendList;
}
