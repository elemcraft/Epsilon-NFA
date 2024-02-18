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
    private static NFA epNFA() {
        State start = new State(false);
        State end = new State(true);
        State.addEpTo(start, end);

        return new NFA(start, end);
    }

    // Building block: symbol transition
    private static NFA symbolNFA(char symbol) {
        State start = new State(false);
        State end = new State(true);
        State.addTo(start, end, symbol);

        return new NFA(start, end);
    }

    // Building block: concatenation
    private static NFA concat(NFA first, NFA second) {
        State.addEpTo(first.end, second.start);
        first.end.isFinal = false;

        return new NFA(first.start, second.end);
    }

    // Building block: union(alternation)
    private static NFA union(NFA first, NFA second) {
        State start = new State(false);
        State.addEpTo(start, first.start);
        State.addEpTo(start, second.start);

        State end = new State(true);
        State.addEpTo(first.end, end);
        first.end.isFinal = false;
        State.addEpTo(second.end, end);
        second.end.isFinal = false;

        return new NFA(start, end);
    }

    // Building block: repetition(kleene star)
    private static NFA repetition(NFA nfa) {
        State start = new State(false);
        State end = new State(true);

        State.addEpTo(start, end);
        State.addEpTo(start, nfa.start);

        State.addEpTo(nfa.end, end);
        State.addEpTo(nfa.end, nfa.start);
        nfa.end.isFinal = false;

        return new NFA(start, end);
    }

    // Building block: one or more (kleene plus)
    private static NFA oneOrMore(NFA nfa) {
        State start = new State(false);
        State end = new State(true);

        State.addEpTo(start, nfa.start);
        State.addEpTo(nfa.end, end);
        State.addEpTo(nfa.end, nfa.start);
        nfa.end.isFinal = false;

        return new NFA(start, end);
    }

    // Assemble the building blocks
    public static NFA buildAutomaton(String postfixExp) {
        // Special case: empty regular expression
        if (postfixExp.length() == 0) {
            return epNFA();
        }

        Stack<NFA> blocks = new Stack<NFA>();
        for (int i = 0; i < postfixExp.length(); i++) {
            final char curr = postfixExp.charAt(i);

            // Transform every character and operators into building blocks
            NFA block = null;
            if (curr == '*') {
                block = repetition(blocks.pop());
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
                block = symbolNFA(curr);
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
    private static Set<State> getEpClosure(Set<State> states) {
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
            if (state.to != null && state.to.symbol == symbol) {
                next.add(state.to.next);
            }
        }

        current = getEpClosure(next);
    }

    // Check if current states are acceptable
    public boolean isAcceptable() {
        return current.contains(end);
    }

    // Depth first search to label every state in the NFA
    // Helper function for buildTable()
    private void label(State curr, List<Map<Character, List<State>>> table, Set<Character> symbols) {
        // Check if the current state has been visited
        if (curr.id != -1) {
            return;
        }

        curr.id = table.size();
        Map<Character, List<State>> neighbors = new HashMap<>(); // The neighbor list for current state
        table.add(neighbors);

        // Explore neighboring states
        if (curr.to != null) {
            symbols.add(curr.to.symbol);
            neighbors.put(curr.to.symbol, Arrays.asList(curr.to.next));
            label(curr.to.next, table, symbols);
        } else if (!curr.epTo.isEmpty()) {
            neighbors.put('ε', curr.epTo);
            for (State neighbor : curr.epTo) {
                label(neighbor, table, symbols);
            }
        }
    }

    // Traverse the NFA to construct transition table
    private List<Map<Character, List<State>>> buildTable(Set<Character> symbols) {
        // In table,
        // each entry stores the neighbor list of the state whose id is the same as
        // entry index
        // the neighbor list are hashmaps
        // key is the symbol required to transit to neighbors
        // value is the neighbor state list
        List<Map<Character, List<State>>> table = new ArrayList<>();
        label(start, table, symbols);
        return table;
    }

    // Helper function for printTable()
    // Prints the content of the entry after transforming it into a fixed length
    // string
    // Also prints the separator to the right of current entry
    private static void printEntry(String content) {
        final String len = "%10s"; // For each entry, the string length is set to 10
        final String separator = " | ";
        System.out.print(String.format(len, content) + separator);
    }

    // Print the transition table
    public void printTable() {
        Set<Character> symbols = new HashSet<>(); // Store all the symbols in the regEx
        List<Map<Character, List<State>>> table = buildTable(symbols);

        // Print header row
        printEntry(""); // Print header row title, which is an empty entry
        printEntry("Epsilon"); // Print the header for epsilon column
        for (char sym : symbols) {
            printEntry(Character.toString(sym));
        }
        System.out.println(); // Change line

        // Print the transition table row by row
        for (int curr = 0; curr < table.size(); curr++) {
            // Print the title of current row
            String title = "q" + curr;
            if (curr == start.id) { // Mark the start state with '>' symbol
                title = ">" + title;
            } else if (curr == end.id) { // Mark the final state with '*' symbol
                title = "*" + title;
            }
            printEntry(title);

            // Print epsilon column
            List<State> epNeighbors = table.get(curr).get('ε');
            String epEntry = new String();
            if (epNeighbors != null) {
                epEntry = epNeighbors.toString();
                epEntry = epEntry.substring(1, epEntry.length() - 1); // Delete the list brackets
            }
            printEntry(epEntry);

            // Print the content of current row
            for (char sym : symbols) {
                List<State> neighbor = table.get(curr).get(sym);
                String entry = new String();
                if (neighbor != null) {
                    entry = neighbor.toString();
                    entry = entry.substring(1, entry.length() - 1); // Delete the list brackets
                }
                printEntry(entry);
            }
            System.out.println(); // Change line
        }
    }
}