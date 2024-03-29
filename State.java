import java.util.*;

public class State {
    /*
     * A state in Thompson's NFA
     * can either have a symbol transition to at most one state
     * or epsilon transitions to up to two states
     */
    public int id;
    public boolean isFinal;
    public Transition to; // symbol transition
    public List<State> epTo; // epsilon transition

    public State(boolean isFinal) {
        id = -1;
        this.isFinal = isFinal;
        to = null;
        epTo = new ArrayList<>();
    }

    // Add an epsilon transition
    public static void addEpTo(State from, State to) {
        from.epTo.add(to);
    }

    // Add a symbol transition
    public static void addTo(State from, State to, char symbol) {
        from.to = new Transition(symbol, to);
    }

    public String toString() {
        return "q" + id;
    }
}