import java.util.*;

public class NFA 
{
    public State start;
    public State end;

    private int numOfStates;
    private List<State> states;

    public NFA(State start, State end) {
        numOfStates = 0;
        states = new ArrayList<State>();
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
        State start =  new State(false);
        State end = new State(true);
        State.addTransition(start, end, symbol);
    
        return new NFA(start, end);
    }

    // Building block: concatenation
    public static NFA concat(NFA first, NFA second) {
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
        // Special case
        if(postfixExp.length() == 0) {
            return fromEpsilon();
        }
        
        Stack<NFA> blockStack = new Stack<NFA>();

        for (int i=0; i<postfixExp.length(); i++) {
            char token = postfixExp.charAt(i);

            if(token == '*') {
                blockStack.push(closure(blockStack.pop()));
            }
            else if(token == '+') {
                blockStack.push(oneOrMore(blockStack.pop()));
            }
            else if (token == '|') {
                NFA right = blockStack.pop();
                NFA left = blockStack.pop();
                blockStack.push(union(left, right));
            } 
            else if (token == '.') {
                NFA right = blockStack.pop();
                NFA left = blockStack.pop();
                blockStack.push(concat(left, right));
            }
            else {
                blockStack.push(fromSymbol(token));
            }
        }

        /*
         * In the end, there would only be one element in the blockStack
         * and the element is the final NFA
         */
        return blockStack.pop();
    }

    public static NFA buildVerboseMachine(String postfixExp) {
        NFA machine = buildMachine(postfixExp);
        labelStates(machine);
        return machine;
    }

    /*
     * There are only two types of states in Thompson's model
     * 1. Ones with 1 or 2 ε-transitions 
     * 2. Ones with a transition on a symbol.
     * 
     * recursively go to the next states
     * until it requires a symbol instead of epsilon to go to the next state
     */
    /*
     * kind of like epsilon closure, 
     * but nextStates only contains fringe states of epsilon closure
     */
    private static void addNextState(State state, List<State> nextStates, List<State> visited) {
        if (state.epsilonTransitions.size()>0) {
            for(State st : state.epsilonTransitions) {
                if(!visited.contains(st)) {
                    visited.add(st);
                    addNextState(st, nextStates, visited);
                }
            }
        } 
        else {
            nextStates.add(state);
        }
    }

    public static void getEpsilonClosure(List<State> stateSet)
    {
        while(true) {
            // newStates serves as a temperary container to prevent concurrent modification exception
            List<State> newStates = new ArrayList<State>();
            
            // search for new states that are not currently in the stateSet
            for(State state : stateSet) {
                for(State next : state.epsilonTransitions){
                    if(!stateSet.contains(next)){
                        newStates.add(next);
                    }
                }
            }

            if(newStates.size() == 0) break;

            // add the new states to the stateSet
            for(State state : newStates) {
                if(!stateSet.contains(state)) {
                    stateSet.add(state);
                }
            }
        }
    }

    // overload match method for verbose mode machine to check the input character by character
    public static List<State> match(NFA nfa, char letter, List<State> currentStates)
    {
        List<State> nextStates = new ArrayList<State>();

        for(State state : currentStates) {
            State next = state.transition.get(letter);
            if(next!=null) nextStates.add(next);
        }

        getEpsilonClosure(nextStates);

        // Loop through the current states to see if any state in the state set is a final state
        return nextStates;
    }

    // Traverse the nfa to see if the input word match the regular expression
    public static boolean match(NFA nfa, String word) {
        List<State> currentStates = new ArrayList<State>();
        addNextState(nfa.start, currentStates, new ArrayList<State>());

        for (char symbol : word.toCharArray()) {
            List<State> nextStates = new ArrayList<State>();

            for (State state : currentStates) {
                State nextState = state.transition.get(symbol);
                if (nextState!=null) {
                    addNextState(nextState, nextStates, new ArrayList<State>());
                }
            }
            currentStates = nextStates;
        }
        
        return State.isAcceptable(currentStates);
    }

    private static int labelStates(NFA machine)
    {
        machine.numOfStates = 0;
        machine.states.clear();
        label(machine.start, machine);

        return machine.numOfStates;
    }

    private static void label(State state, NFA machine)
    {
        // check if the current state has been visited
        if(machine.states.contains(state)) return;

        state.index = machine.numOfStates;
        machine.numOfStates++;
        machine.states.add(state);

        // go to next states
        if(state.transition.next!=null) label(state.transition.next, machine);

        for(State st : state.epsilonTransitions) {
            label(st, machine);
        }
    }

    private String[][] getTransitionTable(List<Character> symbols)
    {
        String[][] table = new String[numOfStates][symbols.size()+1]; // add one for the epsilon column
        for(String[] row : table) {
            Arrays.fill(row, new String());
        }
        
        for(State st : states) {
            // epsilon transitions
            for(State epTrans : st.epsilonTransitions) {
                if(table[st.index][0].length() != 0) table[st.index][0] += ",";
                table[st.index][0] += epTrans.toString();
            }

            // symbol transition
            if(st.transition.symbol!=Character.MIN_VALUE) {
                if(table[st.index][symbols.indexOf(st.transition.symbol)+1].length() != 0)
                {
                    table[st.index][symbols.indexOf(st.transition.symbol)+1] += ",";
                }
                table[st.index][symbols.indexOf(st.transition.symbol)+1] += st.transition.next.toString();
            }
        }

        return table;
    }

    public void printTransitionTable(List<Character> symbols) {
        String[][] table = getTransitionTable(symbols);

        // find the max length of all the strings
        int maxLength = "Epsilon".length();
        for(String[] row : table) {
            for(String str : row) {
                if(str.length() > maxLength) maxLength = str.length();
            }
        }
        String format = "%"+maxLength+"s";
        String separator = " | ";

        // Print header row
        System.out.print("    "+separator);
        System.out.print(String.format(format, "Epsilon") + separator);
        for(char sym : symbols) {
            System.out.print(String.format(format, Character.toString(sym)));
            System.out.print(separator);
        }
        System.out.println();

        // for each loop print a row
        for(int row=0; row<table.length; row++) {
            String counterStr = new String();
            if(row == start.index) { // Mark start state
                counterStr = String.format("%7s", ">q"+row+separator);
            }
            else if(row == end.index) { // Mark final state
                counterStr = String.format("%7s", "*q"+row+separator);
            }
            else{
                counterStr = String.format("%7s", "q"+row+separator);
            }
            System.out.print(counterStr);

            for(String str : table[row]) {
                str = String.format(format, str);
                System.out.print(str + separator);
            }
            System.out.println("");
        }
    }
}