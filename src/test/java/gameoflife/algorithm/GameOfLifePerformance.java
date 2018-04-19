package gameoflife.algorithm;

import gameoflife.app.GameOfLifeUI;

import java.util.logging.Logger;

public final class GameOfLifePerformance {
    private static final int ITERATIONS = 500;
    private static final Logger LOG = Logger.getLogger(GameOfLifePerformance.class.getName());

    private GameOfLifePerformance() {
    }

    private void run(String[] params) {
        GameOfLife gameOfLife = new GameOfLifeUI(params).setWaitTime(0).getGameOfLife();
        final long time = System.currentTimeMillis();

        LOG.info("Started...");
        for (int i = 0; i < ITERATIONS; i++) {
            gameOfLife = gameOfLife.tick();
        }
        LOG.info(format(System.currentTimeMillis() - time));
    }

    public static void main(String[] params) {
        new GameOfLifePerformance().run(params);
    }

    private static String format(long time) {
        return String.format("Finished in %tM:%tS.%tL", time, time, time);
    }
}
