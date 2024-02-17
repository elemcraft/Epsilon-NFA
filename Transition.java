class Transition {
    public char symbol;
    public State next;

    Transition(char symbol, State next) {
        this.symbol = symbol;
        this.next = next;
    }
}