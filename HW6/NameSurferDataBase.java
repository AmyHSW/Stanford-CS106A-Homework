import java.io.*;
import java.util.*;
import acm.util.*;

public class NameSurferDataBase implements NameSurferConstants {

    public NameSurferDataBase(String filename) {
        inventory = new HashMap<String, NameSurferEntry>();
        try {
            BufferedReader rd = new BufferedReader(new FileReader(filename));
            while (true) {
                String line = rd.readLine();
                if (line == null) break;
                NameSurferEntry entry = new NameSurferEntry(line);
                inventory.put(entry.getName(), entry);
            }
            rd.close();
        } catch(IOException ex) {
            throw new ErrorException(ex);
        }
    }

    public NameSurferEntry findEntry(String name) {
        String nameModified = "";
        nameModified += Character.toUpperCase(name.charAt(0));
        for (int i = 1; i < name.length(); i++) {
            nameModified += Character.toLowerCase(name.charAt(i));
        }
        return inventory.get(nameModified);
    }

    private HashMap<String, NameSurferEntry> inventory;
}
