import java.util.Arrays;

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {

    public static void main(String[] args) {
        new Yahtzee().start(args);
    }

    public void run() {
        IODialog dialog = getDialog();
        while (true) {
            nPlayers = dialog.readInt("Enter number of players");
            if (nPlayers <= MAX_PLAYERS) break;
        }
        playerNames = new String[nPlayers];
        for (int i = 1; i <= nPlayers; i++) {
            playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
        }
        display = new YahtzeeDisplay(getGCanvas(), playerNames);
        playGame();
    }

    private void playGame() {
        initGame();
        while (roundsLeft > 0) {
            for (int i = 1; i <= nPlayers; i++) {
                rollDice(i);
                getScoreOfRound(i);
                display.updateScorecard(cat, i, score[i - 1][cat - 1]);
            }
            roundsLeft--;
        }
        displayTotalScores();
        display.printMessage(findWinner());
    }

    private void initGame() {
        roundsLeft = N_SCORING_CATEGORIES;
        dice = new int[N_DICE];
        score = new Integer[nPlayers][N_CATEGORIES];
    }

    private void rollDice(int player) {
        initDice();
        countValue = new int[MAX_DOTS];
        firstRoll(player);
        int nTurnsLeft = N_TURNS -1;
        while (nTurnsLeft-- > 0) {
            reRoll();
        }
    }

    private void initDice() {
        for (int i = dice.length - 1; i >= 0; i--)  {
            dice[i] = 0;
        }
    }

    private void firstRoll(int player) {
        display.printMessage(firstRollMessage(player));
        display.waitForPlayerToClickRoll(player);
        for (int iDice = dice.length - 1; iDice >= 0; iDice--) {
             dice[iDice] = rgen.nextInt(1, MAX_DOTS);
        }
        display.displayDice(dice);
    }

    private String firstRollMessage(int player) {
        return (playerNames[player - 1]
                + "\'s turn! Click \"Roll Dice\" button to roll the dice.");
    }

    private void reRoll() {
        display.printMessage(reRollMessage());
        display.waitForPlayerToSelectDice();
        for (int i = dice.length - 1; i >= 0; i--) {
            if (display.isDieSelected(i)) {
                dice[i] = rgen.nextInt(1, MAX_DOTS);
            }
        }
        display.displayDice(dice);
    }

    private String reRollMessage() {
        return ("Select the dice you wish to re-roll and click \"Roll Again\".");
    }

    private void getScoreOfRound(int player) {
        display.printMessage("Select a category for this roll.");
        while (true) {
            cat = display.waitForPlayerToSelectCategory();
            if (score[player - 1][cat - 1] == null) break;
        }
        if (checkCategory(cat)) {
            score[player - 1][cat - 1] = estimateScore();
        } else {
            score[player - 1][cat - 1] = 0;
        }
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
        for (int i = 0; i < dice.length; i++) {
            evaluate(dice[i]);
        }
        for (int i = 0; i < MAX_DOTS; i++) {
            if (countValue[i] >= 3) return true;
        }
        return false;
    }

    private boolean isFourOfAKind() {
        for (int i = 0; i < dice.length; i++) {
            evaluate(dice[i]);
        }
        for (int i = 0; i < MAX_DOTS; i++) {
            if (countValue[i] >= 4) return true;
        }
        return false;
    }

    private boolean isFullHouse() {
        if (!isThreeOfAKind()) return false;
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

    private void evaluate(int value) {
        for (int i = 1; i <= MAX_DOTS; i++) {
            if (value == i) {
                countValue[i-1]++;
                return;
            }
        }
    }

    private int estimateScore() {
        switch (cat) {
        case ONES: case TWOS: case THREES:
        case FOURS: case FIVES: case SIXES:
            return getScoreUpperCategory();
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

    private int getScoreUpperCategory() {
        int sc = 0;
        for (int iDice = dice.length - 1; iDice >= 0; iDice--) {
            if (dice[iDice] == cat) {
                sc += cat;
            }
        }
        return sc;
    }

    private int getSumValues() {
        int sum = 0;
        int iDice = 0;
        while (iDice < dice.length) {
            sum += dice[iDice++];
        }
        return sum;
    }

    private void displayTotalScores() {
        for (int i = 1; i <= nPlayers; i++) {
            score[i - 1][UPPER_SCORE - 1] = findUpperScore(i);
            display.updateScorecard(UPPER_SCORE, i, score[i - 1][UPPER_SCORE - 1]);
            score[i - 1][LOWER_SCORE - 1] = findLowerScore(i);
            display.updateScorecard(LOWER_SCORE, i, score[i - 1][LOWER_SCORE - 1]);
            if (score[i - 1][UPPER_SCORE - 1] >= 63) {
                score[i - 1][UPPER_BONUS - 1] = UPPER_BONUS_AWARD;
            }
            display.updateScorecard(UPPER_BONUS, i, score[i - 1][UPPER_BONUS - 1]);
            score[i - 1][TOTAL - 1] = score[i - 1][UPPER_SCORE - 1]
                                      + score[i - 1][UPPER_BONUS - 1]
                                      + score[i - 1][LOWER_SCORE - 1];
            display.updateScorecard(TOTAL, i, score[i - 1][TOTAL - 1]);
        }
    }

    private int findUpperScore(int player) {
        int upperScore = 0;
        for (int c = 0; c < SIXES; c++) {
            upperScore += score[player - 1][c];
        }
        return upperScore;
    }

    private int findLowerScore(int player) {
        int lowerScore = 0;
        for (int c = THREE_OF_A_KIND - 1; c < CHANCE; c++) {
            lowerScore += score[player - 1][c];
        }
        return lowerScore;
    }

    private String findWinner() {
        int max = 0;
        int winner = 0;
        for (int i = 0; i < nPlayers; i++) {
            if (score[i - 1][TOTAL - 1] > max) {
                max = score[i - 1][TOTAL - 1];
                winner = i;
            }
        }
        return ("Congratulations, "
                + playerNames[winner]
                + ", you \' are the winner with a total score of "
                + max);
    }

    private int cat;
    private int nPlayers;
    private int roundsLeft;
    private int[] dice;
    private int[] countValue;
    private Integer[][] score;
    private String[] playerNames;
    private YahtzeeDisplay display;
    private RandomGenerator rgen = new RandomGenerator();
}
