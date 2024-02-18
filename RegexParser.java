import java.util.*;

public class RegexParser {
    /*
     * Only need to create a Scanner that's tied to System.in one time
     * and use it for all console input.
     */
    private final Scanner userInput;
    private String regEx;
    private NFA nfa;

    RegexParser() {
        userInput = new Scanner(System.in);
        regEx = new String();
        nfa = null;
    }

    // Getter for nfa
    public NFA getNFA() {
        return nfa;
    }

    // Behaves like setter for the "machine" field
    public void buildNFA() {
        nfa = NFA.buildAutomaton(regEx);
        nfa.initialize();
    }

    // Behaves like setter for the "regEx" field
    public void readRegEx() throws IllegalArgumentException {
        String regEx = userInput.nextLine();

        // Invalid regular expression would throw an exception
        if (isValid(regEx) == false) {
            throw new IllegalArgumentException("Invalid regular expression");
        }

        // Transform the regular expression to postfix
        regEx = insertConcatOperator(regEx);
        regEx = toPostfix(regEx);

        this.regEx = regEx;
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

        // Starts with an operator
        final char first = regEx.charAt(0);
        if (first == '*' || first == '+' || first == '|') {
            return false;
        }

        // Ends with alternation
        final char last = regEx.charAt(regEx.length() - 1);
        if (last == '|') {
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
            if ((curr == '*' || curr == '+') && (prev == '*' || prev == '+')) {
                return false;
            }

            // Two consecutive alternation symbol
            if (curr == '|' && prev == '|') {
                return false;
            }

            // Alternation followed by kleene star, kleene plus, or right bracket
            if (prev == '|' && (curr == ')' || curr == '*' || curr == '+')) {
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
        // The precedence of operators
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

    private static boolean isVerboseMode(String[] args) {
        return args.length > 0 && args[0].equals("-v");
    }

    public static void main(String[] args) {
        RegexParser parser = new RegexParser();
        parser.readRegEx(); // Read in an regular expression
        parser.buildNFA();
        final boolean verbose = isVerboseMode(args);

        // Print transition table
        if (verbose) {
            parser.nfa.printTable();
        }

        System.out.println("Ready");

        // If in verbose mode, Print if the nfa accepts the initial state
        if (verbose) {
            System.out.println(parser.nfa.isAcceptable());
        }

        // Repeatly checking input
        while (true) {
            // Reset the nfa before every input if not in verbose mode
            if (!verbose) {
                parser.nfa.initialize();
            }

            String input = parser.userInput.nextLine();
            boolean acceptable = parser.nfa.match(input);
            System.out.println(acceptable);
        }
    }
}