import java.util.*;

public class FacePamphletDatabase implements FacePamphletConstants {

    public FacePamphletDatabase() {
        database = new HashMap<String, FacePamphletProfile>();
    }

    public void addProfile(FacePamphletProfile profile) {
        database.put(profile.getName(), profile);
    }

    public FacePamphletProfile getProfile(String name) {
        return database.get(name);
    }

    public void deleteProfile(String name) {
        database.remove(name);
    }

    public boolean containsProfile(String name) {
        return database.containsKey(name);
    }

    private Map<String, FacePamphletProfile> database;
}
