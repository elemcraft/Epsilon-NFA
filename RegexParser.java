import java.util.*;

public class RegexParser {
    /*
     * Only need to create a Scanner that's tied to System.in one time
     * and use it for all console input.
     */
    private static final Scanner userInput = new Scanner(System.in);
    public String regEx;
    public List<State> currentStates;

    RegexParser() {
        regEx = new String();
        currentStates = new ArrayList<State>();
    }

    // Behaves like setter for the "regEx" field
    private void readRegEx() {
        String regEx = userInput.nextLine();

        // Invalid regEx would print Error and exit with code 1
        if (isValid(regEx) == false) {
            System.out.println("Error");
            System.exit(1);
        }

        // Transform the regular expression to postfix
        regEx = insertConcatOperator(regEx);
        regEx = toPostfix(regEx);

        this.regEx = regEx;
    }

    // overload this function for testing
    public boolean readRegEx(String regEx) {
        // invalid regualr expression
        if (isValid(regEx) == false) {
            return false;
        }

        // parse regular expression
        regEx = insertConcatOperator(regEx);
        regEx = toPostfix(regEx);

        this.regEx = regEx;
        return true;
    }

    /*
     * a regular expression can consist of
     * lower and upper case letters,
     * numbers,
     * spaces,
     * the alternation operator '|'
     * the Kleene star '*'
     * the Kleene plus '+'
     * brackets '(', ')'
     */
    private static boolean isValid(String regEx) {
        // Special case: empty regular expression
        if (regEx.length() == 0) {
            return true;
        }

        boolean inBracket = false;

        // if the regEx starts with an '*' or '+' or '|'
        if (regEx.charAt(0) == '*' || regEx.charAt(0) == '+' || regEx.charAt(0) == '|')
            return false;

        // if operators are preceded by '('
        for (int i = 1; i < regEx.length(); i++) {
            if (regEx.charAt(i) == '*' || regEx.charAt(i) == '+' || regEx.charAt(i) == '|') {
                if (regEx.charAt(i - 1) == '(')
                    return false;
            }
        }

        // if kleene star or kleene plus is right after the alternation symbol
        for (int i = 1; i < regEx.length(); i++) {
            if (regEx.charAt(i) == '*' || regEx.charAt(i) == '+') {
                if (regEx.charAt(i - 1) == '|')
                    return false;
            }
        }

        // two consecutive kleene star or kleene plus
        for (int i = 1; i < regEx.length(); i++) {
            if (regEx.charAt(i) == '*' || regEx.charAt(i) == '+') {
                if (regEx.charAt(i - 1) == '*' || regEx.charAt(i - 1) == '+')
                    return false;
            }
        }

        // check for invalid brackets
        for (int i = 0; i < regEx.length(); i++) {
            // invalid brackets
            if (inBracket == false && regEx.charAt(i) == ')')
                return false;

            // nested brackets
            if (inBracket == true && regEx.charAt(i) == '(')
                return false;

            if (regEx.charAt(i) == '(')
                inBracket = true;
            if (regEx.charAt(i) == ')')
                inBracket = false;

            // if the bracket is not closed at the end
            if (i == regEx.length() - 1 && inBracket == true)
                return false;
        }

        return true;
    }

    // Insert the dot symbol '.' for concatenation
    private static String insertConcatOperator(String regex) {
        String output = new String();

        for (int i = 0; i < regex.length(); i++) {
            char curr = regex.charAt(i);
            output += curr;

            if (curr == '(' || curr == '|') {
                continue;
            }

            if (i < regex.length() - 1) {
                char next = regex.charAt(i + 1);

                if (next == '*' || next == '+' || next == '|' || next == ')') {
                    continue;
                }

                output += '.';
            }
        }

        return output;
    }

    // convert regular expression from infix to postfix
    private static String toPostfix(String exp) {
        // the precedence of operators
        HashMap<Character, Integer> opPrecedence = new HashMap<Character, Integer>();
        opPrecedence.put('|', 0);
        opPrecedence.put('.', 1);
        opPrecedence.put('?', 2);
        opPrecedence.put('*', 2);
        opPrecedence.put('+', 2);

        String output = new String();

        // stack that contains operators
        Stack<Character> opStack = new Stack<Character>();

        for (int i = 0; i < exp.length(); i++) {
            char token = exp.charAt(i);
            // if the current character is an operator
            if (token == '.' || token == '|' || token == '*' || token == '?' || token == '+') {
                while (opStack.size() != 0 && opStack.peek() != '('
                        && opPrecedence.get(opStack.peek()) >= opPrecedence.get(token)) {
                    output += opStack.pop();
                }

                opStack.push(token);
            }
            // if the current character is a bracket
            else if (token == '(' || token == ')') {
                if (token == '(') {
                    opStack.push(token);
                } else {
                    while (opStack.peek() != '(') {
                        output += opStack.pop();
                    }
                    opStack.pop();
                }
            } else {
                output += token;
            }
        }

        while (opStack.size() > 0) {
            output += opStack.pop();
        }

        return output;
    }

    private static List<Character> getRegexSymbols(String exp) {
        ArrayList<Character> symbols = new ArrayList<Character>();
        for (int i = 0; i < exp.length(); i++) {
            /*
             * if the character is not an operator and a new one
             * add to the symbol list
             */
            char symbol = exp.charAt(i);
            if (symbol != '|' && symbol != '*' && symbol != '+' && symbol != '.' && !symbols.contains(symbol)) {
                symbols.add(symbol);
            }
        }
        Collections.sort(symbols);
        return symbols;
    }

    public boolean matchInput(String input) {
        NFA machine = NFA.buildMachine(regEx);
        return NFA.match(machine, input);
    }

    public boolean matchInputVerbose(NFA machine, String input) {
        // if(currentStates.size() == 0) currentStates.add(machine.start);
        NFA.getEpsilonClosure(currentStates);

        if (input.length() == 0)
            return State.isAcceptable(currentStates);

        List<State> nextStates = NFA.match(machine, input.charAt(0), currentStates);
        currentStates = nextStates;

        return State.isAcceptable(currentStates);
    }

    public void reset() {
        currentStates.clear();
    }

    public static void main(String[] args) {
        RegexParser regexEngine = new RegexParser();

        // read in an regular expression
        regexEngine.readRegEx();

        // verbose mode or normal mode
        if (args.length > 0 && args[0].equals("-v")) // verbose mode
        {
            // build verbose mode state machine
            NFA machine = NFA.buildVerboseMachine(regexEngine.regEx);

            List<Character> symbols = getRegexSymbols(regexEngine.regEx);
            machine.printTransitionTable(symbols);

            System.out.println("Ready");

            // Repeatly checking input(letter by letter)
            regexEngine.currentStates.add(machine.start);
            NFA.getEpsilonClosure(regexEngine.currentStates);
            System.out.println(State.isAcceptable(regexEngine.currentStates));

            while (true) {
                String input = userInput.nextLine();
                if (input.length() == 0) {
                    System.out.println(State.isAcceptable(regexEngine.currentStates));
                    continue;
                }
                char symbol = input.charAt(0);
                List<State> nextStates = NFA.match(machine, symbol, regexEngine.currentStates);
                System.out.println(State.isAcceptable(nextStates));
                regexEngine.currentStates = nextStates;
            }
        } else // normal mode
        {
            // build state machine
            NFA machine = NFA.buildMachine(regexEngine.regEx);

            System.out.println("Ready");

            // Repeatly checking input
            while (true) {
                String input = userInput.nextLine();
                boolean result = NFA.match(machine, input);
                System.out.println(result);
            }
        }
    }
}