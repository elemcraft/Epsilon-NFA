# Regular-Expression-Parser

## Environment
JDK 11, Test code would be run using JUnit 4

## Commands
* Compiled with: `javac RegexEngine.java`
* Run with: `java RegexEngine`
* Run with verbose mode: `java RegexEngine -v`

## Concept
1. Read in the regular expression
2. Check for validity of the input regex
3. Transform the regex into postfix
4. Build the NFA
    * The final NFA are constructed from smaller NFA
    * Always make sure that every NFA created has only one node as input and one node as output. Thus, every NFA can be seen as a black box
5. Put the testing string into the NFA built from the regex
    * If all the threads are dead and no threads are in the final states, the string does not match the regex
