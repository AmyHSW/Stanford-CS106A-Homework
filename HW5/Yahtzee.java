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

    private int[][] newMatrix(int row, int col, int value) {
        int[][] matrix = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrix[i][j] = value;
            }
        }
        return matrix;
    }

    private void playGame() {
        for (int n = 0; n < N_SCORING_CATEGORIES; n++) {
            for (int i = 1; i <= nPlayers; i++) {
                rollDice(i);
                getScoreOfRound(i);
            }
        }
    }

    private void showResults() {
        displayTotalScores();
        display.printMessage(findWinner());
    }

    private void rollDice(int player) {
        firstRoll(player);
        for (int i = 0; i < N_TURNS - 1; i++) {
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

    private void getScoreOfRound(int player) {
        display.printMessage("Select a category for this roll.");
        int cat = 0;
        while (true) {
            cat = display.waitForPlayerToSelectCategory();
            if (score[cat - 1][player - 1] == NOT_ASSIGNED) break;
        }
        if (checkCategory(cat)) {
            score[cat - 1][player - 1] = estimateScore(cat);
        } else {
            score[cat - 1][player - 1] = 0;
        }
        display.updateScorecard(cat, player, score[cat - 1][player - 1]);
    }

    private boolean checkCategory(int cat) {
        switch (cat) {
        case ONES: case TWOS: case THREES:
        case FOURS: case FIVES: case SIXES:
            return true;
        case THREE_OF_A_KIND: return isThreeOfAKind();
        case FOUR_OF_A_KIND: return isFourOfAKind();
        case FULL_HOUSE: return isFullHouse();
        case SMALL_STRAIGHT: return isSmallStraight();
        case LARGE_STRAIGHT: return isLargeStraight();
        case YAHTZEE: return isYahtzee();
        case CHANCE: return true;
        default: return false;
        }
    }

    private boolean isThreeOfAKind() {
        int[] countValue = new int[MAX_DOTS];
        for (int i = 0; i < dice.length; i++) {
            evaluate(countValue, dice[i]);
        }
        for (int i = 0; i < MAX_DOTS; i++) {
            if (countValue[i] >= 3) return true;
        }
        return false;
    }

    private boolean isFourOfAKind() {
        int[] countValue = new int[MAX_DOTS];
        for (int i = 0; i < dice.length; i++) {
            evaluate(countValue, dice[i]);
        }
        for (int i = 0; i < MAX_DOTS; i++) {
            if (countValue[i] >= 4) return true;
        }
        return false;
    }

    private boolean isFullHouse() {
        if (!isThreeOfAKind()) return false;
        int[] countValue = new int[MAX_DOTS];
        for (int i = 0; i < dice.length; i++) {
            evaluate(countValue, dice[i]);
        }
        for (int i = 0; i < MAX_DOTS; i++) {
            if (countValue[i] == 1) return false;
        }
        return true;
    }

    private boolean isSmallStraight() {
        Arrays.sort(dice);
        int prev = -1;
        int error = 0;
        for (int i = 0; i < dice.length; i++) {
            if (prev == -1 || dice[i] == prev + 1) {
                prev = dice[i];
            } else {
                if (++error > 1) return false;
                if (prev != dice[i]) {
                    prev = dice[i];
                }
            }
        }
        return true;
    }

    private boolean isLargeStraight() {
        Arrays.sort(dice);
        int prev = -1;
        for (int i = 0; i < dice.length; i++) {
            if (prev == -1 || dice[i] == prev + 1) {
                prev = dice[i];
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean isYahtzee() {
        int prev = dice[0];
        for (int i = 1; i < dice.length; i++) {
            if (dice[i] != prev) return false;
        }
        return true;
    }

    private void evaluate(int[] countValue, int value) {
        countValue[value - 1]++;
    }

    private int estimateScore(int cat) {
        switch (cat) {
        case ONES: case TWOS: case THREES:
        case FOURS: case FIVES: case SIXES:
            return getScoreUpperCategory(cat);
        case THREE_OF_A_KIND: case FOUR_OF_A_KIND: case CHANCE:
            return getSumValues();
        case FULL_HOUSE:
            return SCORE_FULL_HOUSE;
        case SMALL_STRAIGHT:
            return SCORE_SMALL_STRAIGHT;
        case LARGE_STRAIGHT:
            return SCORE_LARGE_STRAIGHT;
        case YAHTZEE: return SCORE_YAHTZEE;
        default: return 0;
        }
    }

    private int getScoreUpperCategory(int cat) {
        int score = 0;
        for (int i = 0; i < dice.length; i++) {
            if (dice[i] == cat) {
                score += cat;
            }
        }
        return score;
    }

    private int getSumValues() {
        int sum = 0;
        for (int i = 0; i < dice.length; i++) {
            sum += dice[i];
        }
        return sum;
    }

    private void displayTotalScores() {
        for (int i = 1; i <= nPlayers; i++) {
            score[UPPER_SCORE - 1][i - 1]= findUpperScore(i);
            display.updateScorecard(UPPER_SCORE, i, score[UPPER_SCORE - 1][i - 1]);
            score[LOWER_SCORE - 1][i - 1] = findLowerScore(i);
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

    private int findUpperScore(int player) {
        int upperScore = 0;
        for (int c = 0; c < SIXES; c++) {
            upperScore += score[c][player - 1];
        }
        return upperScore;
    }

    private int findLowerScore(int player) {
        int lowerScore = 0;
        for (int c = THREE_OF_A_KIND - 1; c < CHANCE; c++) {
            lowerScore += score[c][player - 1];
        }
        return lowerScore;
    }

    private String findWinner() {
        int max = 0;
        int winner = 0;
        for (int i = 1; i <= nPlayers; i++) {
            if (score[TOTAL - 1][i - 1] > max) {
                max = score[TOTAL - 1][i - 1];
                winner = i - 1;
            }
        }
        return "Congratulations, "
               + playerNames[winner]
               + ", you \' are the winner with a total score of "
               + max;
    }

    private int nPlayers;
    private String[] playerNames;
    private int[][] score;
    private int[] dice;
    private YahtzeeDisplay display;
    private RandomGenerator rgen = new RandomGenerator();
}
