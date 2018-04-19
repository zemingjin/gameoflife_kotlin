package gameoflife.app;

import gameoflife.helper.IOHelper;

import java.util.logging.Logger;

class GameOfLifeUITest extends GameOfLifeUI {
    private static final int ITERATIONS = 500;
    private static final Logger LOG = Logger.getLogger(GameOfLifeUITest.class.getName());

    private int iterations = ITERATIONS;

    private GameOfLifeUITest(String[] params) {
        super(params);
    }

    @Override
    public boolean isContinueFlag() {
        return iterations-- >= 0;
    }

    public static void main(String[] params) {
        final long time = System.currentTimeMillis();
        new GameOfLifeUITest(params).setWaitTime(0).run();
        LOG.info("Total time: " + IOHelper.format(System.currentTimeMillis() - time));
    }
}
