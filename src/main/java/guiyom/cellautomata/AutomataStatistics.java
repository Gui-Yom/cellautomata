package guiyom.cellautomata;

import java.text.DecimalFormat;

/**
 * Holds statistics about the current game instance.
 */
public class AutomataStatistics {

    private int gen = 0;
    private long cellCount;
    private long aliveCells;

    private long elapsedTime = 0;
    private long startTime = 0;

    public int getGen() {
        return gen;
    }

    void setGen(int n) {
        gen = n;
    }

    public long getCellCount() {
        return cellCount;
    }

    void setCellCount(long cellCount) {
        this.cellCount = cellCount;
    }

    public long getAliveCells() {
        return aliveCells;
    }

    void setAliveCells(long aliveCells) {
        this.aliveCells = aliveCells;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    void startRecordTime() {
        startTime = System.currentTimeMillis();
    }

    void stopRecordTime() {
        elapsedTime += System.currentTimeMillis() - startTime;
    }

    @Override
    public String toString() {

        double speed = gen * 1000 / (double) elapsedTime;
        return String.format("Alive cells : %d. Compute time : %d ms. Ran %d steps on %d cells. Speed : %s gen/s. Performance : %s",
            getAliveCells(),
            elapsedTime,
            gen,
            getCellCount(),
            new DecimalFormat("#.##").format(speed),
            new DecimalFormat("#.##").format(gen * getCellCount() / Math.pow((double) elapsedTime, 2)));
    }
}
