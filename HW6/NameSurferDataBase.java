import java.io.*;
import java.util.*;
import acm.util.*;

public class NameSurferDataBase implements NameSurferConstants {

    public NameSurferDataBase(String filename) {
        map = new HashMap<String, NameSurferEntry>();
        try {
            BufferedReader rd = new BufferedReader(new FileReader(filename));
            while (true) {
                String line = rd.readLine();
                if (line == null) break;
                NameSurferEntry entry = new NameSurferEntry(line);
                map.put(entry.getName(), entry);
            }
            rd.close();
        } catch(IOException ex) {
            throw new ErrorException(ex);
        }
    }

    public NameSurferEntry findEntry(String name) {
        String formattedName = name.substring(0, 1).toUpperCase()
                              + name.substring(1).toLowerCase();
        return map.get(formattedName);
    }

    private Map<String, NameSurferEntry> map;
}
