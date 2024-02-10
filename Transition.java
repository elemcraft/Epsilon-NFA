class Transition
{
    char symbol;
    State next;

    Transition()
    {
        symbol = Character.MIN_VALUE;
        next = null;
    }

    Transition(char symbol, State next)
    {
        this.symbol = symbol;
        this.next = next;
    }

    public State get(char symbol)
    {
        if(symbol == this.symbol) return next;
        else return null;
    }
}