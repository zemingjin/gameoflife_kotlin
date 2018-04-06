package gameoflife.app;

import gameoflife.algorithm.Boundary;
import gameoflife.algorithm.GameOfLife;
import gameoflife.helper.IOHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GameOfLifeUI extends JComponent implements KeyEventPostProcessor {
    private static final int WAIT_TIME = 100;
    private static final int MAX_CELL_SIZE = 100;
    private static final int MIN_CELL_SIZE = 6;
    private static final String OPT_STEP = "-s";
    private static final String OPT_WAIT = "-w";
    private static final Logger LOG = Logger.getLogger(GameOfLifeUI.class.getName());

    private final GameOfLife gameOfLife = new GameOfLife();
    private final JFrame window = new JFrame();
    private int cellSize = MAX_CELL_SIZE;
    private boolean continueFlag = true;
    private int evolveToggle = 1;
    private boolean automaton = true;
    private String path;
    private Boundary dimension;
    private int iteration;
    private int waitTime;

    private GameOfLifeUI(String[] params) {
        Optional.of(params)
                .filter(p -> p.length > 0)
                .map(this::setup)
                .orElseThrow(() -> new RuntimeException("Missing seeds"));
    }

    private GameOfLifeUI setup(String[] params) {
        path = params[0];
        gameOfLife.seedGame(IOHelper.loadSeeds(path));
        automaton = isAutomaton(params);
        waitTime = getWaitTime(params);
        dimension = gameOfLife.getDimension();
        return this;
    }

    private int getWaitTime(String[] params) {
        return Stream.of(params)
                .filter(param -> param.startsWith(OPT_WAIT))
                .map(param -> Integer.parseInt(param.substring(OPT_WAIT.length())))
                .findFirst()
                .orElse(WAIT_TIME);
    }

    private boolean isAutomaton(String[] params) {
        return !Arrays.asList(params).contains(OPT_STEP);
    }

    private void run() {
        setupKeyboardListener();
        setupFrame();

        while (continueFlag) {
            repaint();
            waitAWhile();
            if (isContinueToEvolve()) {
                evolve();
            }
        }

        close();
    }

    private void close() {
        window.setVisible(false);
        window.dispose();
    }

    private void setupKeyboardListener() {
        KeyboardFocusManager
                .getCurrentKeyboardFocusManager()
                .addKeyEventPostProcessor(this);
    }

    private int getCellSize() {
        return Math.max(Math.min(Math.min(getScreenSize().height * 3 / 4 / dimension.getY(),
                                          getScreenSize().width  * 3 / 4 / dimension.getX()),
                                 MAX_CELL_SIZE),
                        MIN_CELL_SIZE);
    }

    private void setupFrame() {
        setCellSize(getCellSize());

        final int width = dimension.getX() * cellSize;
        final int height = dimension.getY() * cellSize;

        setSize(width, height);
        setFocusable(true);
        window.setTitle(getTitle(path));
        window.setResizable(false);
        window.setVisible(true);
        window.setBounds(getHorizontalPosition(width), getVerticalPosition(height),
                         getFrameWidth(width), getFrameHeight(height));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.getContentPane().add(this);
    }

    private int getHorizontalPosition(int panelWidth) {
        return (getScreenSize().width - getFrameWidth(panelWidth)) / 2;
    }

    private int getVerticalPosition(int panelHeight) {
        return (getScreenSize().height - getFrameHeight(panelHeight)) / 2;
    }

    private Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    private int getFrameWidth(int panelWidth) {
        return panelWidth + getInsetValue(insets -> insets.left + insets.right);
    }

    private int getFrameHeight(int panelHeight) {
        return panelHeight + getInsetValue(insets -> insets.top + insets.bottom);
    }

    private int getInsetValue(Function<Insets, Integer> getter) {
        return getter.apply(window.getInsets());
    }

    private void evolve() {
        gameOfLife.evolve();
        evolveToggle++;
        window.setTitle(String.format("%s - #%d", path, iteration++));
    }

    private boolean isContinueToEvolve() {
        return automaton || evolveToggle == 0;
    }

    @Override
    public void paint(Graphics graphics) {
        IntStream.range(0, dimension.getY())
                .forEach(y -> paintRow(graphics, y));
    }

    private void paintRow(Graphics graphics, int y) {
        IntStream.range(0, dimension.getX())
                .forEach(x -> paintCell(graphics, x, y));
    }

    private void paintCell(Graphics graphics, int x, int y) {
        fillCell(graphics, x, y);
        drawGrid(graphics, x, y);
    }

    private void fillCell(Graphics graphics, int x, int y) {
        graphics.setColor(getColor(x, y));
        graphics.fillRect(getFillPosition(x), getFillPosition(y), getFillSize(), getFillSize());
    }

    private int getFillPosition(int at) {
        return getCellPosition(at) + 1;
    }

    private int getFillSize() {
        return cellSize - 2;
    }

    private void drawGrid(Graphics graphics, int x, int y) {
        graphics.setColor(getForeground());
        graphics.drawRect(getCellPosition(x), getCellPosition(y), cellSize, cellSize);
    }

    private int getCellPosition(int at) {
        return at * cellSize;
    }

    private Color getColor(int x, int y) {
        return gameOfLife.isLiveCell(x, y) ? getForeground() : getBackground();
    }

    private void waitAWhile() {
        try {
            synchronized (this) {
                wait(waitTime);
            }
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private void setCellSize(int value) {
        this.cellSize = Math.min(MIN_CELL_SIZE, value);
    }

    @Override
    public boolean postProcessKeyEvent(KeyEvent e) {
        boolean result = false;
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            continueFlag = false;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            automaton = e.isControlDown();
            evolveToggle = (evolveToggle < 2) ? evolveToggle + 1 : 0;
            e.consume();
            result = true;
        }
        return result;
    }

    private String getTitle(String path) {
        return String.format("Seed: %s", path);
    }

    public static void main(String[] params) {
        new GameOfLifeUI(params).run();
    }
}
