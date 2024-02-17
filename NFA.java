import java.util.*;

public class NFA {
    private State start;
    private State end;
    private Set<State> current; // The current states that this NFA is in

    public NFA(State start, State end) {
        this.start = start;
        this.end = end;
        current = new HashSet<>();
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

    // Get the epsilon closure of the start state
    // and store them in the current field
    public void initialize() {
        current.clear();
        current.add(start);
        current = getEpClosure(current);
    }

    // Get the epsilon closure of the input state set
    public static Set<State> getEpClosure(Set<State> states) {
        Set<State> epClosure = new HashSet<>();

        // Perform dfs from every state in the current state set
        for (State state : states) {
            epsilonDFS(state, epClosure);
        }

        return epClosure;
    }

    // Depth first search from current state to get the epsilon closure
    // Only consider epislon transitions
    // Helper method for getEpClosure()
    private static void epsilonDFS(State curr, Set<State> visited) {
        if (visited.contains(curr)) {
            return;
        }

        visited.add(curr);

        for (State neighbor : curr.epTo) {
            epsilonDFS(neighbor, visited);
        }
    }

    // Traverse the nfa to see if the input word matches the regular expression
    public boolean match(String word) {
        for (char symbol : word.toCharArray()) {
            matchSymbol(symbol);
        }

        return isAcceptable();
    }

    // Update the current states for the input symbol
    private void matchSymbol(char symbol) {
        Set<State> next = new HashSet<>();

        for (State state : current) {
            State neighbor = state.to.get(symbol);
            if (neighbor != null) {
                next.add(neighbor);
            }
        }

        current = getEpClosure(next);
    }

    // Check if current states are acceptable
    public boolean isAcceptable() {
        return current.contains(end);
    }

    // Depth first search to label every state in the NFA
    private void label(State curr, List<Map<Character, List<State>>> table, Set<Character> symbols) {
        // Check if the current state has been visited
        if (curr.id != -1) {
            return;
        }

        curr.id = table.size();
        table.add(new HashMap<>());

        // Explore neighboring states
        if (curr.to.next != null) {
            symbols.add(curr.to.symbol);
            table.get(curr.id).put(curr.to.symbol, new ArrayList<>());
            table.get(curr.id).get(curr.to.symbol).add(curr.to.next);
            label(curr.to.next, table, symbols);
        } else if (!curr.epTo.isEmpty()) {
            table.get(curr.id).put('ε', curr.epTo);
            for (State neighbor : curr.epTo) {
                label(neighbor, table, symbols);
            }
        }
    }

    // Traverse the NFA to construct transition table
    private List<Map<Character, List<State>>> buildTable(Set<Character> symbols) {
        List<Map<Character, List<State>>> table = new ArrayList<>();
        label(start, table, symbols);
        return table;
    }

    // Print the transition table
    public void printTable() {
        Set<Character> symbols = new HashSet<>(); // Store all the symbols in the regEx
        List<Map<Character, List<State>>> table = buildTable(symbols);

        final String format = "%8s";
        final String separator = " | ";

        // Print header row
        System.out.print(String.format(format, "") + separator); // Print header row title
        System.out.print(String.format(format, "Epsilon") + separator);
        for (char sym : symbols) {
            System.out.print(String.format(format, Character.toString(sym)));
            System.out.print(separator);
        }
        System.out.println(); // Change line

        // Each loop print a row
        for (int curr = 0; curr < table.size(); curr++) {
            // Print the title of current row
            String title = "q" + curr;
            if (curr == start.id) { // Mark start state with '>' symbol
                title = ">" + title;
            } else if (curr == end.id) { // Mark final state with '*' symbol
                title = "*" + title;
            }
            title = String.format(format, title);
            System.out.print(title + separator);

            // Print epsilon column
            List<State> epNeighbor = table.get(curr).get('ε');
            String epEntry = new String();
            if (epNeighbor != null) {
                epEntry = epNeighbor.toString();
                // Delete the list brackets
                epEntry = epEntry.substring(1, epEntry.length() - 1);
            }
            epEntry = String.format(format, epEntry);
            System.out.print(epEntry + separator);

            // Print the content of current row
            for (char sym : symbols) {
                List<State> neighbor = table.get(curr).get(sym);
                String entry = new String();
                if (neighbor != null) {
                    entry = neighbor.toString();
                    // Delete the list brackets
                    entry = entry.substring(1, entry.length() - 1);
                }
                entry = String.format(format, entry);
                System.out.print(entry + separator);
            }
            System.out.println(); // Change line
        }
    }
}