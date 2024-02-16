import java.util.*;

public class NFA {
    public State start;
    private State end;

    private int count; // total number of states
    private Set<State> states;

    public NFA(State start, State end) {
        count = 0;
        states = new HashSet<>();
        this.start = start;
        this.end = end;
    }

    // Building block: epsilon transition
    private static NFA fromEpsilon() {
        State start = new State(false);
        State end = new State(true);
        State.addEpsilonTransition(start, end);

        return new NFA(start, end);
    }

    // Building block: symbol transition
    private static NFA fromSymbol(char symbol) {
        State start = new State(false);
        State end = new State(true);
        State.addTransition(start, end, symbol);

        return new NFA(start, end);
    }

    // Building block: concatenation
    private static NFA concat(NFA first, NFA second) {
        State.addEpsilonTransition(first.end, second.start);
        first.end.isFinal = false;

        return new NFA(first.start, second.end);
    }

    // Building block: union(alternation)
    private static NFA union(NFA first, NFA second) {
        State start = new State(false);
        State.addEpsilonTransition(start, first.start);
        State.addEpsilonTransition(start, second.start);

        State end = new State(true);
        State.addEpsilonTransition(first.end, end);
        first.end.isFinal = false;
        State.addEpsilonTransition(second.end, end);
        second.end.isFinal = false;

        return new NFA(start, end);
    }

    // Building block: closure(kleene star)
    private static NFA closure(NFA nfa) {
        State start = new State(false);
        State end = new State(true);

        State.addEpsilonTransition(start, end);
        State.addEpsilonTransition(start, nfa.start);

        State.addEpsilonTransition(nfa.end, end);
        State.addEpsilonTransition(nfa.end, nfa.start);
        nfa.end.isFinal = false;

        return new NFA(start, end);
    }

    // Building block: one or more (kleene plus)
    private static NFA oneOrMore(NFA nfa) {
        State start = new State(false);
        State end = new State(true);

        State.addEpsilonTransition(start, nfa.start);
        State.addEpsilonTransition(nfa.end, end);
        State.addEpsilonTransition(nfa.end, nfa.start);
        nfa.end.isFinal = false;

        return new NFA(start, end);
    }

    // Assemble the building blocks
    public static NFA buildMachine(String postfixExp) {
        // Special case: empty regular expression
        if (postfixExp.length() == 0) {
            return fromEpsilon();
        }

        Stack<NFA> blocks = new Stack<NFA>();
        for (int i = 0; i < postfixExp.length(); i++) {
            final char curr = postfixExp.charAt(i);

            // Transform every character and operators into building blocks
            NFA block = null;
            if (curr == '*') {
                block = closure(blocks.pop());
            } else if (curr == '+') {
                block = oneOrMore(blocks.pop());
            } else if (curr == '|') {
                NFA right = blocks.pop();
                NFA left = blocks.pop();
                block = union(left, right);
            } else if (curr == '.') {
                NFA right = blocks.pop();
                NFA left = blocks.pop();
                block = concat(left, right);
            } else {
                block = fromSymbol(curr);
            }

            // Store building blocks in a stack
            blocks.push(block);
        }

        /*
         * In the end, there would only be one element in the blockStack
         * and the element is the final NFA
         */
        return blocks.pop();
    }

    // Get the epsilon closure of the input state set
    public static Set<State> getEpClosure(Set<State> states) {
        Set<State> epClosure = new HashSet<>();

        // Perform dfs from every state in the current state set
        for (State state : states) {
            dfs(state, epClosure);
        }

        return epClosure;
    }

    // Depth first search from current state to get the epsilon closure
    private static void dfs(State curr, Set<State> visited) {
        if (visited.contains(curr)) {
            return;
        }

        visited.add(curr);

        for (State neighbor : curr.epsilonTo) {
            dfs(neighbor, visited);
        }
    }

    // Traverse the nfa to see if the input word matches the regular expression
    public boolean match(String word) {
        Set<State> current = new HashSet<>();
        current.add(start);
        current = getEpClosure(current);

        for (char symbol : word.toCharArray()) {
            Set<State> next = new HashSet<>();
            for (State state : current) {
                State neighbor = state.to.get(symbol);
                if (neighbor != null) {
                    next.add(neighbor);
                }
            }
            current = getEpClosure(next);
        }

        return State.isAcceptable(current);
    }

    // Overload match method for verbose mode
    // to check the input character by character
    public static Set<State> match(NFA nfa, char symbol, Set<State> current) {
        Set<State> nextStates = new HashSet<>();

        for (State state : current) {
            State next = state.to.get(symbol);
            if (next != null) {
                nextStates.add(next);
            }
        }

        return getEpClosure(nextStates);
    }

    public int labelStates() {
        label(start);
        return count;
    }

    private void label(State curr) {
        // Check if the current state has been visited
        if (curr == null || states.contains(curr)) {
            return;
        }

        curr.index = count;
        count++;
        states.add(curr);

        // Explore neighboring states
        label(curr.to.next);
        for (State st : curr.epsilonTo) {
            label(st);
        }
    }

    // Construct transition table
    private String[][] buildTable(List<Character> symbols) {
        String[][] table = new String[count][symbols.size() + 1]; // add one for the epsilon column
        for (String[] row : table) {
            Arrays.fill(row, new String());
        }

        for (State st : states) {
            // epsilon transitions
            for (State epTrans : st.epsilonTo) {
                if (table[st.index][0].length() != 0)
                    table[st.index][0] += ",";
                table[st.index][0] += epTrans.toString();
            }

            // symbol transition
            if (st.to.symbol != Character.MIN_VALUE) {
                if (table[st.index][symbols.indexOf(st.to.symbol) + 1].length() != 0) {
                    table[st.index][symbols.indexOf(st.to.symbol) + 1] += ",";
                }
                table[st.index][symbols.indexOf(st.to.symbol) + 1] += st.to.next.toString();
            }
        }

        return table;
    }

    public void printTransitionTable(List<Character> symbols) {
        String[][] table = buildTable(symbols);

        // find the max length of all the strings
        int maxLength = "Epsilon".length();
        for (String[] row : table) {
            for (String str : row) {
                if (str.length() > maxLength)
                    maxLength = str.length();
            }
        }
        String format = "%" + maxLength + "s";
        String separator = " | ";

        // Print header row
        System.out.print("    " + separator);
        System.out.print(String.format(format, "Epsilon") + separator);
        for (char sym : symbols) {
            System.out.print(String.format(format, Character.toString(sym)));
            System.out.print(separator);
        }
        System.out.println();

        // for each loop print a row
        for (int row = 0; row < table.length; row++) {
            String counterStr = new String();
            if (row == start.index) { // Mark start state
                counterStr = String.format("%7s", ">q" + row + separator);
            } else if (row == end.index) { // Mark final state
                counterStr = String.format("%7s", "*q" + row + separator);
            } else {
                counterStr = String.format("%7s", "q" + row + separator);
            }
            System.out.print(counterStr);

            for (String str : table[row]) {
                str = String.format(format, str);
                System.out.print(str + separator);
            }
            System.out.println("");
        }
    }
}