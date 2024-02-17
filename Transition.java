class Transition {
    public char symbol;
    public State next;

    Transition() {
        symbol = Character.MIN_VALUE;
        next = null;
    }

    Transition(char symbol, State next) {
        this.symbol = symbol;
        this.next = next;
    }
}