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

        // regEx starts with an '*' or '+' or '|'
        final char first = regEx.charAt(0);
        if (first == '*' || first == '+' || first == '|') {
            return false;
        }

        for (int i = 1; i < regEx.length(); i++) {
            final char curr = regEx.charAt(i);
            final char prev = regEx.charAt(i - 1);

            // Operators are preceded by '('
            if ((curr == '*' || curr == '+' || curr == '|') && prev == '(') {
                return false;
            }

            // Two consecutive kleene star or kleene plus
            // Alternation symbol followed by kleene star or kleene plus
            if ((curr == '*' || curr == '+') && (prev == '*' || prev == '+' || prev == '|')) {
                return false;
            }

            // Two consecutive alternation symbol
            if (curr == '|' && prev == '|') {
                return false;
            }
        }

        // Invalid brackets
        int hanging = 0; // The number of unclosed left brackets
        for (int i = 0; i < regEx.length(); i++) {
            final char curr = regEx.charAt(i);
            if (curr == '(') {
                hanging++;
            } else if (curr == ')') {
                if (hanging == 0) { // Unclosed right brackets
                    return false;
                }
                hanging--;
            }
        }

        // Unclosed left brackets
        if (hanging != 0) {
            return false;
        }

        return true;
    }

    // Insert the dot symbol '.' for concatenation
    private static String insertConcatOperator(String regex) {
        String output = new String();

        for (int i = 0; i < regex.length(); i++) {
            final char curr = regex.charAt(i);
            output += curr;

            if (curr == '(' || curr == '|') {
                continue;
            }

            if (i < regex.length() - 1) {
                final char next = regex.charAt(i + 1);

                if (next == '*' || next == '+' || next == '|' || next == ')') {
                    continue;
                }

                output += '.';
            }
        }

        return output;
    }

    // Convert regular expression from infix to postfix
    private static String toPostfix(String exp) {
        // the precedence of operators
        Map<Character, Integer> pre = new HashMap<>();
        pre.put('|', 0);
        pre.put('.', 1);
        pre.put('*', 2);
        pre.put('+', 2);

        String output = new String();

        // stack that contains operators
        Stack<Character> oper = new Stack<>();

        for (char curr : exp.toCharArray()) {
            if (curr == '.' || curr == '|' || curr == '*' || curr == '+') {
                // Current character is an operator

                while (!oper.isEmpty() && oper.peek() != '('
                        && pre.get(oper.peek()) >= pre.get(curr)) {
                    output += oper.pop();
                }

                oper.push(curr);
            } else if (curr == '(' || curr == ')') {
                // Current character is a bracket

                if (curr == '(') {
                    oper.push(curr);
                } else {
                    while (oper.peek() != '(') {
                        output += oper.pop();
                    }
                    oper.pop();
                }
            } else {
                output += curr;
            }
        }

        while (oper.size() > 0) {
            output += oper.pop();
        }

        return output;
    }

    private static List<Character> getRegexSymbols(String exp) {
        Set<Character> symbols = new HashSet<>();
        for (char curr : exp.toCharArray()) {
            // Add curr character to the symbols set if it is not an operator
            if (curr != '|' && curr != '*' && curr != '+' && curr != '.') {
                symbols.add(curr);
            }
        }

        List<Character> res = new ArrayList<>(symbols);
        Collections.sort(res);
        return res;
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
        } else { // normal mode
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