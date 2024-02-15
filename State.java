import java.util.*;

public class State {
    /*
     * A state in Thompson's NFA
     * can either have a symbol transition to at most one state
     * or epsilon transitions to up to two states
     */
    public int index;
    public boolean isFinal;
    public Transition to;
    public List<State> epsilonTo;

    public State(boolean isFinal) {
        index = -1;
        this.isFinal = isFinal;
        to = new Transition();
        epsilonTo = new ArrayList<>();
    }

    public static void addEpsilonTransition(State from, State to) {
        from.epsilonTo.add(to);
    }

    public static void addTransition(State from, State to, char symbol) {
        from.to = new Transition(symbol, to);
    }

    public String toString() {
        return "q" + index;
    }

    public static boolean isAcceptable(Set<State> stateSet) {
        for (State state : stateSet) {
            if (state.isFinal) {
                return true;
            }
        }
        return false;
    }
}