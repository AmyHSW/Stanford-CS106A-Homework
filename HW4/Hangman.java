import acm.program.*;
import acm.util.*;

public class Hangman extends ConsoleProgram {

    public static final int APPLICATION_WIDTH = 800;
    public static final int APPLICATION_HEIGHT = 600;

    private static final int MAX_GUESS = 8;

    public static void main(String[] args) {
        new Hangman().start(args);
    }

    public void init() {
        canvas = new HangmanCanvas();
        add(canvas);
    }

    public void run() {
        startNewGame();
        guessesLeft = MAX_GUESS;
        while (guessesLeft > 0) {
            println("The word now looks like this: " + presentWord);
            println("You have " + guessesLeft + " guesses left.");
            String guess = readLine("Your guess: ");
            if (guess.length() != 1 || (!Character.isLetter(guess.charAt(0)))) {
                println("Invalid guess!");
            } else {
                checkForGuess(guess.charAt(0));
            }
            canvas.displayWord(presentWord);
            if (presentWord.indexOf('-') == -1) break;
        }
        reportResults();
    }

    private void startNewGame() {
        canvas.reset();
        println("Welcome to Hangman!");
        lexicon = new HangmanLexicon();
        int index = rgen.nextInt(lexicon.getWordCount());
        word = lexicon.getWord(index);
        presentWord = "";
        for (int i = 0; i < word.length(); i++) {
            presentWord += '-';
        }
    }

    private void checkForGuess(char ch) {
        if (Character.isLowerCase(ch)) {
            ch = Character.toUpperCase(ch);
        }
        if (word.indexOf(ch) != -1 && presentWord.indexOf(ch) == -1) {
            println("That guess is correct.");
            for (int i = 0; i < word.length(); i++) {
                if (ch == word.charAt(i)) {
                    presentWord = presentWord.substring(0, i)
                                  + ch
                                  + presentWord.substring(i + 1);
                }
            }
        } else if (word.indexOf(ch) == -1){
            guessesLeft--;
            canvas.noteIncorrectGuess(ch);
            println("There are no " + ch +"' s in the word.");
        }
    }

    private void reportResults() {
        if (presentWord.indexOf('-') != -1) {
            println("You're completely hung.");
            println("The word was: " + word);
            println("You lose.");
        } else {
            println("You guessed the word: " + word);
            println("You win.");
        }
    }

    private int guessesLeft;
    private String word;
    private String presentWord;
    private HangmanLexicon lexicon;
    private HangmanCanvas canvas;
    private RandomGenerator rgen = RandomGenerator.getInstance();
}
