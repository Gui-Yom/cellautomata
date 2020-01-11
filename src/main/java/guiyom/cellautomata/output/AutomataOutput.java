package guiyom.cellautomata.output;

import guiyom.cellautomata.AutomataState;

@FunctionalInterface
public interface AutomataOutput {

    void feed(AutomataState state);
}
