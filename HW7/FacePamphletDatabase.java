import java.util.*;

public class FacePamphletDatabase implements FacePamphletConstants {

    public FacePamphletDatabase() {
        map = new HashMap<String, FacePamphletProfile>();
    }

    public void addProfile(FacePamphletProfile profile) {
        map.put(profile.getName(), profile);
    }

    public FacePamphletProfile getProfile(String name) {
        return map.get(name);
    }

    public void deleteProfile(String name) {
        if (!map.containsKey(name)) return;
        Iterator<String> it = map.get(name).getFriends();
        while (it.hasNext()) {
            FacePamphletProfile friend = map.get(it.next());
            friend.removeFriend(name);
        }
        map.remove(name);
    }

    public boolean containsProfile(String name) {
        return map.containsKey(name);
    }

    private Map<String, FacePamphletProfile> map;
}
