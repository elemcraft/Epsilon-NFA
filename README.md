# Regular Expression Parser

## Environment
1. Java Version: JDK 11
2. External Libraries: Test code would be run using JUnit 4

## Commands
* Compiled with: `javac RegexParser.java`
* Run with: `java RegexParser`
* Run with verbose mode: `java RegexParser -v`

## Features
* Implement an epsilon non-deterministic finite state automaton (ε-NFA) as the core component of the regular expression parser. Allowing transitions without consuming any input symbols enables the automaton to be in multiple states at any moment, thus allowing the automaton to recognize patterns with varying lengths and symbols.
* Utilize depth first search algorithm to retrieve all states within the epsilon closure of the current state.
* Apply depth first search algorithm to construct the transition table for the input regular expression.
* Incorporate stack-based checking to verify balanced brackets within the input regular expression. This parser utilize an integer variable to track the number of left brackets awaiting corresponding right brackets.

## Outline
1. Read in the regular expression
2. Check the validity of the input regular expression and throw an exception if the regular expression is invalid
3. Transform the regular expression from infix to postfix
4. Build the ε-NFA
    * The final NFA are constructed from smaller NFA
    * Always make sure that every ε-NFA created has only one node as input and one node as output. Thus, every ε-NFA can be seen as a black box
5. Put the testing string into the ε-NFA built from the regex
    * If all the threads are dead and no threads are in the final states, the string does not match the regex

## NFA building blocks
### 1. Epsilon Block
<p align="center">
   <img width="700" alt="image" src="https://github.com/elemcraft/Epsilon-NFA/assets/48006644/e14b2367-c860-446c-84d4-0bb060631097">
</p>

### 2. Symbol Block
<p align="center">
   <img width="700" alt="image" src="https://github.com/elemcraft/Epsilon-NFA/assets/48006644/295e2e05-0969-4464-a2a8-15bac7e45053">
</p>

### 3. Union Block 
$R = S\mid T$
<p align="center">
   <img width="700" alt="image" src="https://github.com/elemcraft/Epsilon-NFA/assets/48006644/32a47df0-5375-4e6d-a54f-e75bc607f7f2">
</p>

### 4. Concatenation Block
$R = S⋅T$
<p align="center">
   <img width="700" alt="image" src="https://github.com/elemcraft/Epsilon-NFA/assets/48006644/2956820b-2aaf-4fbc-91d6-b32b4c60f7f4">
</p>

### 5. Repetition Block (Kleene Star)
$R = S^*$
<p align="center">
   <img width="700" alt="image" src="https://github.com/elemcraft/Epsilon-NFA/assets/48006644/c35dba09-239f-4174-a342-291258464e7b">
</p>

### 6. One Or More Block (Kleene Plus)
$R = S^+$
<p align="center">
   <img width="700" alt="image" src="https://github.com/elemcraft/Epsilon-NFA/assets/48006644/203d3a96-774f-4c42-8722-45fb226cb601">
</p>

## Input
A regular expression can consist of 
1. Symbols: lowercase and uppercase letters, numbers, spaces,
2. Operators: the alternation operator `|`, the Kleene star `*` and Kleene plus operators `+`, as well as brackets `(` and `)`

## Example: Regular Expression = $(a^+\mid b)(a^*\mid c)$
* After entering the regular expression, repeatly match input strings against regular expression
<img width="500" alt="image" src="https://github.com/elemcraft/Epsilon-NFA/assets/48006644/82e52c7e-29bf-4e97-b595-5f23c45dcb62">

## Verbose Mode Example: Regular Expression = $(a^+\mid b)(a^*\mid c)$
* The major feature for verbose mode is that NFA will not reset for every input string or character. Thus, each input starts from the current states instead of the initial states of NFA. In addition, verbose mode also prints out the transition table. In the transition table, the start state is marked with `>` symbol, and the final state is marked with `*` symbol
<img width="500" alt="image" src="https://github.com/elemcraft/Epsilon-NFA/assets/48006644/d1453ab6-b957-46b2-a1e1-a9ed995ce477">

### Example Diagram:
1. Omit epsilon transition symbol for simplicity
2. Turn symbol transition red for readability
<p align="center">
   <img width="1200" alt="image" src="https://github.com/elemcraft/Epsilon-NFA/assets/48006644/ff8eb147-cf7f-4c6b-928a-7793ebf833ad">
</p>

## Reference
[Denis Kyashif's Blog](https://deniskyashif.com/2019/02/17/implementing-a-regular-expression-engine/)

## Future Improvement
Currently, the ε-NFA interprets `.` character as the concatenation symbol. Hence, any input containing the dot character, the character would be interpreted as concatenation. Adding the function to take the dot symbol with the escape symbol can solve this problem.
