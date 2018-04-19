package gameoflife.algorithm;

import gameoflife.app.GameOfLifeUI;

public class GameOfLifePerformance {
    private static final int ITERATIONS = 500;

    public static void main(String[] params) {
        GameOfLife gameOfLife = new GameOfLifeUI(params).setWaitTime(0).getGameOfLife();
        long time = System.currentTimeMillis();

        System.out.println("Started...");
        for (int i = 0; i < ITERATIONS; i++) {
            gameOfLife = gameOfLife.tick();
        }
        System.out.println(format(System.currentTimeMillis() - time));
    }

    private static String format(long time) {
        return String.format("Finished in %tM:%tS.%tL", time, time, time);
    }
}
