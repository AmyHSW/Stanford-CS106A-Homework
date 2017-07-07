public class NameSurferEntry implements NameSurferConstants {

    public NameSurferEntry(String line) {
        int nameEnd = line.indexOf(" ");
        name = line.substring(0, nameEnd);

        rank = new int[NDECADES];
        int rankStart;
        int rankEnd = nameEnd;
        for (int i = 0; i < NDECADES - 1; i++) {
            rankStart = rankEnd + 1;
            rankEnd = line.indexOf(" ", rankStart);
            rank[i] = Integer.parseInt(line.substring(rankStart, rankEnd));
        }
        rank[NDECADES - 1] = Integer.parseInt(line.substring(rankEnd + 1));
    }

    public String getName() {
        return name;
    }

    public int getRank(int decade) {
        return rank[decade];
    }

    public String toString() {
        String str = name + " [" + rank[0];
        for (int i = 1; i < NDECADES; i++) {
            str += " " + rank[i];
        }
        str += "]";
        return str;
    }

    private String name;
    private int[] rank;
}

