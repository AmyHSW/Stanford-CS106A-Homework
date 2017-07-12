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

    public void reset() {
        removeAll();
        drawScaffold();
        setLabelWord();
        setLabelIncorrectGuess();
    }

    private void drawScaffold() {
        double xScaffold = WIDTH / 2 - BEAM_LENGTH;
        double y0Scaffold = HEIGHT / 2
                            - BODY_LENGTH - 2 * HEAD_RADIUS
                            - ROPE_LENGTH;
        double y1Scaffold = y0Scaffold + SCAFFOLD_HEIGHT;
        add(new GLine(xScaffold, y0Scaffold, xScaffold, y1Scaffold));
        double x1Beam = xScaffold + BEAM_LENGTH;
        add(new GLine(xScaffold, y0Scaffold, x1Beam, y0Scaffold));
        double y1Rope = y0Scaffold + ROPE_LENGTH;
        add(new GLine(x1Beam, y0Scaffold, x1Beam, y1Rope));
    }

    private void setLabelWord() {
        labelWord = new GLabel("");
        labelWord.setFont("helvetica-20");
        add(labelWord, WIDTH / 4, HEIGHT - labelWord.getHeight() * 3);
    }

    private void setLabelIncorrectGuess() {
        incorrectGuess = "";
        labelIncorrectGuess = new GLabel(incorrectGuess);
        labelIncorrectGuess.setFont("Serif-16");
        add(labelIncorrectGuess, labelWord.getX(),
            labelWord.getY() + labelWord.getHeight());
    }

    public void displayWord(String word) {
        labelWord.setLabel(word);;
    }

    public void noteIncorrectGuess(char letter) {
        incorrectGuess += letter;
        labelIncorrectGuess.setLabel(incorrectGuess);
        switch (incorrectGuess.length()) {
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
        GOval head = new GOval(HEAD_RADIUS * 2, HEAD_RADIUS * 2);
        add(head, WIDTH / 2 - HEAD_RADIUS,
            HEIGHT / 2 - BODY_LENGTH - HEAD_RADIUS * 2);
    }

    private void drawBody() {
        add(new GLine(WIDTH / 2, HEIGHT / 2 - BODY_LENGTH,
                      WIDTH / 2, HEIGHT / 2));
    }

    private void drawLeftArm() {
        double xLeftElbow = WIDTH / 2 - UPPER_ARM_LENGTH;
        double yLeftElbow = HEIGHT / 2 - BODY_LENGTH + ARM_OFFSET_FROM_HEAD;
        add(new GLine(xLeftElbow, yLeftElbow,
                      xLeftElbow + UPPER_ARM_LENGTH, yLeftElbow));
        add(new GLine(xLeftElbow, yLeftElbow,
                      xLeftElbow, yLeftElbow + LOWER_ARM_LENGTH));
    }

    private void drawRightArm() {
        double xRightElbow = WIDTH / 2 + UPPER_ARM_LENGTH;
        double yRightElbow = HEIGHT / 2 - BODY_LENGTH + ARM_OFFSET_FROM_HEAD;
        add(new GLine(xRightElbow, yRightElbow,
                      xRightElbow - UPPER_ARM_LENGTH, yRightElbow));
        add(new GLine(xRightElbow, yRightElbow,
                      xRightElbow, yRightElbow + LOWER_ARM_LENGTH));
    }

    private void drawLeftLeg() {
        add(new GLine(WIDTH / 2, HEIGHT / 2,
                      WIDTH / 2 - HIP_WIDTH, HEIGHT / 2));
        add(new GLine(WIDTH / 2 - HIP_WIDTH, HEIGHT / 2,
                      WIDTH / 2 - HIP_WIDTH, HEIGHT / 2 + LEG_LENGTH));
    }

    private void drawRightLeg() {
        add(new GLine(WIDTH / 2, HEIGHT / 2,
                      WIDTH / 2 + HIP_WIDTH, HEIGHT / 2));
        add(new GLine(WIDTH / 2 + HIP_WIDTH, HEIGHT / 2,
                      WIDTH / 2 + HIP_WIDTH, HEIGHT / 2 + LEG_LENGTH));
    }

    private void drawLeftFoot() {
        add(new GLine(WIDTH / 2 - HIP_WIDTH, HEIGHT / 2 + LEG_LENGTH,
                      WIDTH / 2 - HIP_WIDTH - FOOT_LENGTH,
                      HEIGHT / 2 + LEG_LENGTH));
    }

    private void drawRightFoot() {
        add(new GLine(WIDTH / 2 + HIP_WIDTH, HEIGHT / 2 + LEG_LENGTH,
                      WIDTH / 2 + HIP_WIDTH + FOOT_LENGTH,
                      HEIGHT / 2 + LEG_LENGTH));
    }

    private String incorrectGuess;
    private GLabel labelWord;
    private GLabel labelIncorrectGuess;
}
