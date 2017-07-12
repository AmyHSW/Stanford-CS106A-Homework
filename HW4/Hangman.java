import acm.program.*;
import acm.util.*;

public class Hangman extends ConsoleProgram {

    public static final int APPLICATION_WIDTH = 800;
    public static final int APPLICATION_HEIGHT = 600;
    private static final int MAX_GUESS = 8;

    private HangmanCanvas canvas;
    private String word;
    private String guessWord;

    public static void main(String[] args) {
        new Hangman().start(args);
    }

    public void run() {
        initGame();
        playGame();
        showResults();
    }

    private void initGame() {
        println("Welcome to Hangman!");

        canvas = new HangmanCanvas();
        canvas.reset();
        add(canvas);

        HangmanLexicon lexicon = new HangmanLexicon();
        RandomGenerator rgen = RandomGenerator.getInstance();
        int index = rgen.nextInt(lexicon.getWordCount());
        word = lexicon.getWord(index);

        guessWord = "";
        for (int i = 0; i < word.length(); i++) {
            guessWord += '-';
        }
    }

    private void playGame() {
        for (int n = MAX_GUESS; n > 0; n--) {
            println("The word now looks like this: " + guessWord);
            println("You have " + n + " guesses left.");

            String guess = readLine("Your guess: ");
            if (!isValidGuess(guess) || isCorrectGuess(guess)) n++;

            canvas.displayWord(guessWord);
            if (guessWord.indexOf('-') == -1) break;
        }
    }

    private boolean isValidGuess(String guess) {
        if (guess.length() != 1 || !Character.isLetter(guess.charAt(0))) {
            println("Invalid guess!");
            return false;
        }
        return true;
    }

    private boolean isCorrectGuess(String guess) {
        char ch = Character.toUpperCase(guess.charAt(0));
        if (guessWord.indexOf(ch) != -1) {
            println("You already guessed that letter. Try another letter.");
            return true;
        }

        if (word.indexOf(ch) != -1) {
            println("That guess is correct.");
            for (int i = 0; i < word.length() - 1; i++) {
                if (ch == word.charAt(i)) {
                    guessWord = guessWord.substring(0, i)
                                + ch
                                + guessWord.substring(i + 1);
                }
            }
            if (ch == word.charAt(word.length() - 1)) {
                guessWord = guessWord.substring(0, word.length() - 1) + ch;
            }
            return true;
        } else {
            canvas.updateWrongGuess(ch);
            println("There are no " + ch + "'s in the word.");
            return false;
        }
    }

    private void showResults() {
        if (guessWord.indexOf('-') == -1) {
            println("You guessed the word: " + word);
            println("You win.");
        } else {
            println("You're completely hung.");
            println("The word was: " + word);
            println("You lose.");
        }
    }
}
