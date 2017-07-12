import acm.graphics.*;

public class HangmanCanvas extends GCanvas {

    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 600;

    private static final int WIDTH = APPLICATION_WIDTH;
    private static final int HEIGHT = APPLICATION_HEIGHT;
    private static final int SCAFFOLD_HEIGHT = 360;
    private static final int BEAM_LENGTH = 144;
    private static final int ROPE_LENGTH = 18;
    private static final int HEAD_RADIUS = 36;
    private static final int BODY_LENGTH = 144;
    private static final int ARM_OFFSET_FROM_HEAD = 28;
    private static final int UPPER_ARM_LENGTH = 72;
    private static final int LOWER_ARM_LENGTH = 44;
    private static final int HIP_WIDTH = 36;
    private static final int LEG_LENGTH = 108;
    private static final int FOOT_LENGTH = 28;
    private static final String WORD_FONT = "Helvetica-20";
    private static final String WRONG_GUESS_FONT = "Serif-16";

    private GLabel wordLabel;
    private String wrongGuess;
    private GLabel wrongGuessLabel;

    public void reset() {
        removeAll();
        drawScaffold();
        addLabels();
    }

    private void drawScaffold() {
        double xScaffold = WIDTH / 2.0 - BEAM_LENGTH;
        double y0Scaffold = HEIGHT / 2.0 - BODY_LENGTH - 2.0 * HEAD_RADIUS - ROPE_LENGTH;
        double y1Scaffold = y0Scaffold + SCAFFOLD_HEIGHT;
        add(new GLine(xScaffold, y0Scaffold, xScaffold, y1Scaffold));

        double x0Beam = xScaffold;
        double x1Beam = xScaffold + BEAM_LENGTH;
        double yBeam = y0Scaffold;
        add(new GLine(x0Beam, yBeam, x1Beam, yBeam));

        double xRope = x1Beam;
        double y0Rope = y0Scaffold;
        double y1Rope = y0Scaffold + ROPE_LENGTH;
        add(new GLine(xRope, y0Rope, xRope, y1Rope));
    }

    private void addLabels() {
        wordLabel = new GLabel("");
        wordLabel.setFont(WORD_FONT);
        add(wordLabel, WIDTH / 4.0, HEIGHT - wordLabel.getHeight() * 3.0);

        wrongGuess = "";
        wrongGuessLabel = new GLabel(wrongGuess);
        wrongGuessLabel.setFont(WRONG_GUESS_FONT);
        add(wrongGuessLabel,
            wordLabel.getX(),
            wordLabel.getY() + wrongGuessLabel.getHeight());
    }

    public void displayWord(String word) {
        wordLabel.setLabel(word);
    }

    public void updateWrongGuess(char letter) {
        wrongGuess += letter;
        wrongGuessLabel.setLabel(wrongGuess);

        switch (wrongGuess.length()) {
        case 1: drawHead(); break;
        case 2: drawBody(); break;
        case 3: drawLeftArm(); break;
        case 4: drawRightArm(); break;
        case 5: drawLeftLeg(); break;
        case 6: drawRightLeg(); break;
        case 7: drawLeftFoot(); break;
        case 8: drawRightFoot(); break;
        }
    }

    private void drawHead() {
        GOval head = new GOval(HEAD_RADIUS * 2.0, HEAD_RADIUS * 2.0);
        add(head,
            WIDTH / 2.0 - HEAD_RADIUS,
            HEIGHT / 2.0 - BODY_LENGTH - HEAD_RADIUS * 2.0);
    }

    private void drawBody() {
        add(new GLine(WIDTH / 2.0, HEIGHT / 2.0 - BODY_LENGTH,
                      WIDTH / 2.0, HEIGHT / 2.0));
    }

    private void drawLeftArm() {
        double xLeftElbow = WIDTH / 2.0 - UPPER_ARM_LENGTH;
        double yLeftElbow = HEIGHT / 2.0 - BODY_LENGTH + ARM_OFFSET_FROM_HEAD;
        add(new GLine(xLeftElbow, yLeftElbow,
                      xLeftElbow + UPPER_ARM_LENGTH, yLeftElbow));
        add(new GLine(xLeftElbow, yLeftElbow,
                      xLeftElbow, yLeftElbow + LOWER_ARM_LENGTH));
    }

    private void drawRightArm() {
        double xRightElbow = WIDTH / 2.0 + UPPER_ARM_LENGTH;
        double yRightElbow = HEIGHT / 2.0 - BODY_LENGTH + ARM_OFFSET_FROM_HEAD;
        add(new GLine(xRightElbow, yRightElbow,
                      xRightElbow - UPPER_ARM_LENGTH, yRightElbow));
        add(new GLine(xRightElbow, yRightElbow,
                      xRightElbow, yRightElbow + LOWER_ARM_LENGTH));
    }

    private void drawLeftLeg() {
        add(new GLine(WIDTH / 2.0, HEIGHT / 2.0, WIDTH / 2.0 - HIP_WIDTH, HEIGHT / 2.0));
        add(new GLine(WIDTH / 2.0 - HIP_WIDTH, HEIGHT / 2.0,
                      WIDTH / 2.0 - HIP_WIDTH, HEIGHT / 2.0 + LEG_LENGTH));
    }

    private void drawRightLeg() {
        add(new GLine(WIDTH / 2.0, HEIGHT / 2.0, WIDTH / 2.0 + HIP_WIDTH, HEIGHT / 2.0));
        add(new GLine(WIDTH / 2.0 + HIP_WIDTH, HEIGHT / 2.0,
                      WIDTH / 2.0 + HIP_WIDTH, HEIGHT / 2.0 + LEG_LENGTH));
    }

    private void drawLeftFoot() {
        add(new GLine(WIDTH / 2.0 - HIP_WIDTH,
                      HEIGHT / 2.0 + LEG_LENGTH,
                      WIDTH / 2.0 - HIP_WIDTH - FOOT_LENGTH,
                      HEIGHT / 2.0 + LEG_LENGTH));
    }

    private void drawRightFoot() {
        add(new GLine(WIDTH / 2.0 + HIP_WIDTH,
                      HEIGHT / 2.0 + LEG_LENGTH,
                      WIDTH / 2.0 + HIP_WIDTH + FOOT_LENGTH,
                      HEIGHT / 2.0 + LEG_LENGTH));
    }

}
