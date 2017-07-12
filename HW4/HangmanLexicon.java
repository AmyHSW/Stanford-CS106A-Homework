import acm.util.*;
import java.io.*;
import java.util.*;

public class HangmanLexicon {

    private static final String LONG_LEXICON = "HangmanLexicon.txt";
    private static final String SHORT_LEXICON = "ShortLexicon.txt";

    public HangmanLexicon() {
        wordList = new ArrayList<String>();
        try {
            BufferedReader rd
                = new BufferedReader(new FileReader(LONG_LEXICON));
            while (true) {
                String word = rd.readLine();
                if (word == null) break;
                wordList.add(word);
            }
            rd.close();
        } catch (IOException ex) {
            throw new ErrorException(ex);
        }
    }

    public int getWordCount() {
        return wordList.size();
    }

    public String getWord(int index) {
        return wordList.get(index);
    }

    private final ArrayList<String> wordList;
}
