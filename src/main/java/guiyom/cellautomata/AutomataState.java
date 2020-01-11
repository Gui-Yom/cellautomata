package guiyom.cellautomata;

public class AutomataState {

    private int width;
    private int height;
    private Rule rule;
    private boolean bound;
    private int gen;
    private byte[] cells;

    public AutomataState(int width, int height, Rule rule, boolean bound) {
        this.width = width;
        this.height = height;
        this.rule = rule;
        this.bound = bound;
    }

    public int getWidth() {
        return width;
    }

    void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    void setHeight(int height) {
        this.height = height;
    }

    public Rule getRule() {
        return rule;
    }

    void setRule(Rule rule) {
        this.rule = rule;
    }

    public boolean isBound() {
        return bound;
    }

    void setBound(boolean bound) {
        this.bound = bound;
    }

    public int getGen() {
        return gen;
    }

    void setGen(int gen) {
        this.gen = gen;
    }

    void incGen() {
        ++gen;
    }

    public byte[] getCells() {
        return cells;
    }

    void setCells(byte[] cells) {
        this.cells = cells;
    }
}
