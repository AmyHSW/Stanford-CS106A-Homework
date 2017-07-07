import java.util.Arrays;

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {

    public static void main(String[] args) {
        new Yahtzee().start(args);
    }

    public void run() {
        setupGame();
        playGame();
        showResults();
    }

    /* Step1: setup */
    private void setupGame() {
        IODialog dialog = getDialog();
        while (true) {
            nPlayers = dialog.readInt("Enter number of players");
            if (nPlayers > 0 && nPlayers <= MAX_PLAYERS) break;
        }

        playerNames = new String[nPlayers];
        for (int i = 1; i <= nPlayers; i++) {
            playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
        }

        score = newMatrix(N_CATEGORIES, nPlayers, NOT_ASSIGNED);
        dice = new int[N_DICE];
        display = new YahtzeeDisplay(getGCanvas(), playerNames);
     }

    private static int[][] newMatrix(int row, int col, int value) {
        int[][] matrix = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrix[i][j] = value;
            }
        }
        return matrix;
    }

    /* Step2: play game */
    private void playGame() {
        for (int n = 0; n < N_SCORING_CATEGORIES; n++) {
            for (int i = 1; i <= nPlayers; i++) {
                rollDice(i);
                fillScorecard(i);
            }
        }
    }

    private void rollDice(int player) {
        firstRoll(player);
        for (int i = 0; i < REROLL_TIMES; i++) {
            if (!reroll()) break;
        }
    }

    private void firstRoll(int player) {
        display.printMessage(firstRollMessage(player));
        display.waitForPlayerToClickRoll(player);
        for (int i = 0; i < dice.length; i++) {
            dice[i] = rgen.nextInt(1, MAX_DOTS);
        }
        display.displayDice(dice);
    }

    private String firstRollMessage(int player) {
        return playerNames[player - 1]
               + "\'s turn! Click \"Roll Dice\" button to roll the dice.";
    }

    private boolean reroll() {
        display.printMessage(rerollMessage());
        display.waitForPlayerToSelectDice();
        boolean isRerolled = false;
        for (int i = 0; i < dice.length; i++) {
            if (display.isDieSelected(i)) {
                dice[i] = rgen.nextInt(1, MAX_DOTS);
                isRerolled = true;
            }
        }
        display.displayDice(dice);
        return isRerolled;
    }

    private String rerollMessage() {
        return "Select the dice you wish to re-roll and click \"Roll Again\".";
    }

    private void fillScorecard(int player) {
        display.printMessage("Select a category for this roll.");
        int cat = 0;
        while (true) {
            cat = display.waitForPlayerToSelectCategory();
            if (score[cat - 1][player - 1] == NOT_ASSIGNED) break;
        }
        if (isCategory(cat)) {
            score[cat - 1][player - 1] = getScore(cat);
        } else {
            score[cat - 1][player - 1] = 0;
        }
        display.updateScorecard(cat, player, score[cat - 1][player - 1]);
    }

    private boolean isCategory(int cat) {
        switch (cat) {
        case ONES: case TWOS: case THREES:
        case FOURS: case FIVES: case SIXES: return true;
        case THREE_OF_A_KIND: return isNOfAKind(3);
        case FOUR_OF_A_KIND: return isNOfAKind(4);
        case FULL_HOUSE: return isFullHouse();
        case SMALL_STRAIGHT: return isStraight(4);
        case LARGE_STRAIGHT: return isStraight(5);
        case YAHTZEE: return isNOfAKind(5);
        case CHANCE: return true;
        default: return false;
        }
    }

    private boolean isNOfAKind(int n) {
        Arrays.sort(dice);
        int count = 1;
        for (int i = 1; i < dice.length; i++) {
            if (dice[i] == dice[i - 1]) {
                count++;
            } else {
                count = 1;
            }
            if (count == n) return true;
        }
        return false;
    }

    private boolean isFullHouse() {
        Arrays.sort(dice);
        return dice[0] == dice[1] && dice[3] == dice[4]
               && (dice[2] == dice[0] || dice[2] == dice[4]);
    }

    private boolean isStraight(int n) {
        Arrays.sort(dice);
        int count = 1;
        for (int i = 1; i < dice.length; i++) {
            if (dice[i] == dice[i - 1] + 1) {
                count++;
            } else if (dice[i] != dice[i - 1]) {
                count = 1;
            }
            if (count == n) return true;
        }
        return false;
    }

    private int getScore(int cat) {
        switch (cat) {
        case ONES: case TWOS: case THREES:
        case FOURS: case FIVES: case SIXES:
            return sumOfElement(dice, cat);
        case THREE_OF_A_KIND: case FOUR_OF_A_KIND: case CHANCE:
            return sum(dice);
        case FULL_HOUSE: return SCORE_FULL_HOUSE;
        case SMALL_STRAIGHT: return SCORE_SMALL_STRAIGHT;
        case LARGE_STRAIGHT: return SCORE_LARGE_STRAIGHT;
        case YAHTZEE: return SCORE_YAHTZEE;
        default: return 0;
        }
    }

    private static int sumOfElement(int[] array, int elem) {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == elem) {
                sum += elem;
            }
        }
        return sum;
    }

    private static int sum(int[] array) {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }

    /* Step3: show results */
    private void showResults() {
        displayTotalScores();
        displayWinnerMessage();
    }

    private void displayTotalScores() {
        for (int i = 1; i <= nPlayers; i++) {
            score[UPPER_SCORE - 1][i - 1]= sumOfRows(score, ONES - 1, SIXES - 1, i - 1);
            display.updateScorecard(UPPER_SCORE, i, score[UPPER_SCORE - 1][i - 1]);

            score[LOWER_SCORE - 1][i - 1] = sumOfRows(score, THREE_OF_A_KIND - 1, CHANCE - 1, i - 1);
            display.updateScorecard(LOWER_SCORE, i, score[LOWER_SCORE - 1][i - 1]);

            if (score[UPPER_SCORE - 1][i - 1] >= 63) {
                score[UPPER_BONUS - 1][i - 1] = UPPER_BONUS_AWARD;
            } else {
                score[UPPER_BONUS - 1][i - 1] = 0;
            }
            display.updateScorecard(UPPER_BONUS, i, score[UPPER_BONUS - 1][i - 1]);

            score[TOTAL - 1][i - 1] = score[UPPER_SCORE - 1][i - 1]
                                      + score[UPPER_BONUS - 1][i - 1]
                                      + score[LOWER_SCORE - 1][i - 1];
            display.updateScorecard(TOTAL, i, score[TOTAL - 1][i - 1]);
        }
    }

    private static int sumOfRows(int[][] mat, int rowStart, int rowEnd, int col) {
        int sum = 0;
        for (int i = rowStart; i <= rowEnd; i++) {
            sum += mat[i][col];
        }
        return sum;
    }

    private void displayWinnerMessage() {
        int winner = maxIndex(score[TOTAL - 1]);
        display.printMessage(winnerMessage(playerNames[winner], score[TOTAL - 1][winner]));
    }

    private static int maxIndex(int[] array) {
        int maxIndex = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private String winnerMessage(String name, int score) {
        return "Congratulations, "
               + name
               + ", you \' are the winner with a total score of "
               + score;
    }

    private int nPlayers;
    private String[] playerNames;
    private int[][] score;
    private int[] dice;
    private YahtzeeDisplay display;
    private RandomGenerator rgen = new RandomGenerator();
}
