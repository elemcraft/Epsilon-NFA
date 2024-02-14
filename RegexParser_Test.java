import org.junit.*;

public class RegexParser_Test
{
    @Test
    public void basic()
    {
        // no operators
        // special symbols like semicolon, space, and numbers are included
        RegexEngine regexEngine = new RegexEngine();
        regexEngine.readRegEx("ko;2 q46");
        boolean output = regexEngine.matchInput("ko;2 q46");
        boolean output1 = regexEngine.matchInput("ko;2q46");
        boolean output2 = regexEngine.matchInput("ko2 q46");
        boolean output3 = regexEngine.matchInput("ko2q46");
        Assert.assertEquals(output, true);
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
    }

    @Test
    public void singleKleeneStar()
    {
        RegexEngine regexEngine = new RegexEngine();
        regexEngine.readRegEx("jsh*fq");
        boolean output1 = regexEngine.matchInput("jsh*fq");
        boolean output2 = regexEngine.matchInput("jsfq");
        boolean output3 = regexEngine.matchInput("jshfq");
        boolean output4 = regexEngine.matchInput("jshhhhhfq");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, true);
        Assert.assertEquals(output3, true);
        Assert.assertEquals(output4, true);
    }

    @Test
    public void singleKleenePlus()
    {
        RegexEngine regexEngine = new RegexEngine();
        regexEngine.readRegEx("jsh+fq");
        boolean output1 = regexEngine.matchInput("jsfq");
        boolean output2 = regexEngine.matchInput("jshfq");
        boolean output3 = regexEngine.matchInput("jshhhhhfq");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, true);
        Assert.assertEquals(output3, true);
    }

    @Test
    public void singleAlternation()
    {
        RegexEngine regexEngine = new RegexEngine();
        regexEngine.readRegEx("kofw|ew");
        boolean output1 = regexEngine.matchInput("kofw");
        boolean output2 = regexEngine.matchInput("ew");
        boolean output3 = regexEngine.matchInput("kofwew");
        boolean output4 = regexEngine.matchInput("kofw|ew");
        Assert.assertEquals(output1, true);
        Assert.assertEquals(output2, true);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, false);
    }

    @Test
    public void onePairOfBrackets()
    {
        RegexEngine regexEngine = new RegexEngine();
        regexEngine.readRegEx("(sbv)oi15");
        boolean output1 = regexEngine.matchInput("(sbv)oi15");
        boolean output2 = regexEngine.matchInput("sbvoi15");
        boolean output3 = regexEngine.matchInput("");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, true);
        Assert.assertEquals(output3, false);
    }

    @Test
    public void multipleKleeneStar()
    {
        RegexEngine regexEngine = new RegexEngine();
        regexEngine.readRegEx("62*ewqr*fs");
        boolean output1 = regexEngine.matchInput("62ewqr*fs");
        boolean output2 = regexEngine.matchInput("62*ewqrfs");
        boolean output3 = regexEngine.matchInput("6ewqfs");
        boolean output4 = regexEngine.matchInput("62222ewqrrrfs");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, true);
        Assert.assertEquals(output4, true);
    }

    @Test
    public void multipleKleenePlus()
    {
        RegexEngine regexEngine = new RegexEngine();
        regexEngine.readRegEx("gq;+mi29+wnr");
        boolean output1 = regexEngine.matchInput("gq;mi29+wnr");
        boolean output2 = regexEngine.matchInput("gq;+mi29wnr");
        boolean output3 = regexEngine.matchInput("gqmi29wnr");
        boolean output4 = regexEngine.matchInput("gq;mi2wnr");
        boolean output5 = regexEngine.matchInput("gq;;;mi2999999wnr");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, false);
        Assert.assertEquals(output5, true);
    }

    @Test
    public void multipleAlternation()
    {
        RegexEngine regexEngine = new RegexEngine();
        regexEngine.readRegEx("vqr|89r|bj,");
        boolean output1 = regexEngine.matchInput("vqr|89r|bj,");
        boolean output2 = regexEngine.matchInput("vqr");
        boolean output3 = regexEngine.matchInput("89r");
        boolean output4 = regexEngine.matchInput("bj,");
        boolean output5 = regexEngine.matchInput("vqr89rbj,");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, true);
        Assert.assertEquals(output3, true);
        Assert.assertEquals(output4, true);
        Assert.assertEquals(output5, false);
    }

    @Test
    public void multipleBrackets()
    {
        RegexEngine regexEngine = new RegexEngine();
        regexEngine.readRegEx("(,rukw35)gtr(ehrw;[])");
        boolean output1 = regexEngine.matchInput("(,rukw35)gtr(ehrw;[])");
        boolean output2 = regexEngine.matchInput("(,rukw35)gtrehrw;[]");
        boolean output3 = regexEngine.matchInput(",rukw35gtr(ehrw;[])");
        boolean output4 = regexEngine.matchInput(",rukw35gtrehrw;[]");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, true);
    }

    @Test
    public void bracketsAndKleeneStar()
    {
        RegexEngine regexEngine = new RegexEngine();
        regexEngine.readRegEx("tq4(jpl5'7)*vr3");
        boolean output1 = regexEngine.matchInput("tq4(jpl5'7)*vr3");
        boolean output2 = regexEngine.matchInput("tq4jpl5'7*vr3");
        boolean output3 = regexEngine.matchInput("tq4vr3");
        boolean output4 = regexEngine.matchInput("tq4jpl5'7vr3");
        boolean output5 = regexEngine.matchInput("tq4jpl5'7jpl5'7jpl5'7vr3");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, true);
        Assert.assertEquals(output4, true);
        Assert.assertEquals(output5, true);
    }

    @Test
    public void bracketsAndKleenePlus()
    {
        RegexEngine regexEngine = new RegexEngine();
        regexEngine.readRegEx("oq(ij2;_)+24");
        boolean output1 = regexEngine.matchInput("oq(ij2;_)+24");
        boolean output2 = regexEngine.matchInput("oqij2;_+24");
        boolean output3 = regexEngine.matchInput("oq(ij2;_)24");
        boolean output4 = regexEngine.matchInput("oq24");
        boolean output5 = regexEngine.matchInput("oqij2;_24");
        boolean output6 = regexEngine.matchInput("oqij2;_ij2;_ij2;_24");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, false);
        Assert.assertEquals(output5, true);
        Assert.assertEquals(output6, true);
    }

    @Test
    public void bracketsAndAlternation()
    {
        RegexEngine regexEngine = new RegexEngine();
        regexEngine.readRegEx("t(gt4w|ok)two");
        boolean output1 = regexEngine.matchInput("t(gt4w|ok)two");
        boolean output2 = regexEngine.matchInput("tgt4w|oktwo");
        boolean output3 = regexEngine.matchInput("t(gt4wok)two");
        boolean output4 = regexEngine.matchInput("tgt4woktwo");
        boolean output5 = regexEngine.matchInput("tgt4wtwo");
        boolean output6 = regexEngine.matchInput("toktwo");
        boolean output7 = regexEngine.matchInput("ttwo");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, false);
        Assert.assertEquals(output5, true);
        Assert.assertEquals(output6, true);
        Assert.assertEquals(output7, false);
    }

    @Test
    public void multipleDifferentOperators()
    {
        RegexEngine regexEngine = new RegexEngine();
        regexEngine.readRegEx("qh+3|gw*");
        boolean output1 = regexEngine.matchInput("qh+3|gw*");
        boolean output2 = regexEngine.matchInput("qh+3gw*");
        boolean output3 = regexEngine.matchInput("qh3|gw*");
        boolean output4 = regexEngine.matchInput("qh+3|gw");
        boolean output5 = regexEngine.matchInput("qh3gw");
        boolean output6 = regexEngine.matchInput("qh+3");
        boolean output7 = regexEngine.matchInput("gw*");
        boolean output8 = regexEngine.matchInput("q3");
        boolean output9 = regexEngine.matchInput("g");
        boolean output10 = regexEngine.matchInput("qh3");
        boolean output11 = regexEngine.matchInput("gw");
        boolean output12 = regexEngine.matchInput("qhhhhhh3");
        boolean output13 = regexEngine.matchInput("gwwwwww");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, false);
        Assert.assertEquals(output5, false);
        Assert.assertEquals(output6, false);
        Assert.assertEquals(output7, false);
        Assert.assertEquals(output8, false);
        Assert.assertEquals(output9, true);
        Assert.assertEquals(output10, true);
        Assert.assertEquals(output11, true);
        Assert.assertEquals(output12, true);
        Assert.assertEquals(output13, true);
    }

    @Test
    public void bracketsAndOperators()
    {
        RegexEngine regexEngine = new RegexEngine();
        regexEngine.readRegEx("(h*|p)(h|d)");
        boolean output1 = regexEngine.matchInput("(h*|p)(h|d)");
        boolean output2 = regexEngine.matchInput("");
        boolean output3 = regexEngine.matchInput("d");
        boolean output4 = regexEngine.matchInput("h");
        boolean output5 = regexEngine.matchInput("hhd");
        boolean output6 = regexEngine.matchInput("ph");
        boolean output7 = regexEngine.matchInput("pd");
        boolean output8 = regexEngine.matchInput("hph");
        boolean output9 = regexEngine.matchInput("phd");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, true);
        Assert.assertEquals(output4, true);
        Assert.assertEquals(output5, true);
        Assert.assertEquals(output6, true);
        Assert.assertEquals(output7, true);
        Assert.assertEquals(output8, false);
        Assert.assertEquals(output9, false);

        RegexEngine regexEngine1 = new RegexEngine();
        regexEngine1.readRegEx("(t*|f)*r*(k|y)*");
        boolean output10 = regexEngine1.matchInput("(t*|f)+r(k|y)");
        boolean output11 = regexEngine1.matchInput("");
        boolean output12 = regexEngine1.matchInput("ttt");
        boolean output13 = regexEngine1.matchInput("fffff");
        boolean output14 = regexEngine1.matchInput("tff");
        boolean output15 = regexEngine1.matchInput("rrr");
        boolean output16 = regexEngine1.matchInput("kkkk");
        boolean output17 = regexEngine1.matchInput("yy");
        boolean output18 = regexEngine1.matchInput("ty");
        boolean output19 = regexEngine1.matchInput("try");
        boolean output20 = regexEngine1.matchInput("rrry");
        boolean output21 = regexEngine1.matchInput("fry");
        boolean output22 = regexEngine1.matchInput("rk");
        boolean output23 = regexEngine1.matchInput("trk");
        boolean output24 = regexEngine1.matchInput("tk");
        boolean output25 = regexEngine1.matchInput("ky");
        Assert.assertEquals(output10, false);
        Assert.assertEquals(output11, true);
        Assert.assertEquals(output12, true);
        Assert.assertEquals(output13, true);
        Assert.assertEquals(output14, true);
        Assert.assertEquals(output15, true);
        Assert.assertEquals(output16, true);
        Assert.assertEquals(output17, true);
        Assert.assertEquals(output18, true);
        Assert.assertEquals(output19, true);
        Assert.assertEquals(output20, true);
        Assert.assertEquals(output21, true);
        Assert.assertEquals(output22, true);
        Assert.assertEquals(output23, true);
        Assert.assertEquals(output24, true);
        Assert.assertEquals(output25, true);
    }

    @Test
    public void invalidRegularExpression() {
        RegexEngine regexEngine = new RegexEngine();

        // if the regEx starts with an '*' or '+' or '|'
        boolean isValid = regexEngine.readRegEx("*ateah");
        Assert.assertEquals(isValid, false);
        isValid = regexEngine.readRegEx("+ytke");
        Assert.assertEquals(isValid, false);
        isValid = regexEngine.readRegEx("(ef");
        Assert.assertEquals(isValid, false);

        // if operators are preceded by '('
        isValid = regexEngine.readRegEx("tqh(*p2[");
        Assert.assertEquals(isValid, false);
        isValid = regexEngine.readRegEx("186re(+i0e");
        Assert.assertEquals(isValid, false);
        isValid = regexEngine.readRegEx("frw(|p");
        Assert.assertEquals(isValid, false);
        
        // if kleene star or kleene plus is right after the alternation symbol
        isValid = regexEngine.readRegEx("(ab)*|+");
        Assert.assertEquals(isValid, false);
        isValid = regexEngine.readRegEx("ow|*lp");
        Assert.assertEquals(isValid, false);

        // two consecutive kleene star or kleene plus
        isValid = regexEngine.readRegEx(";ow++");
        Assert.assertEquals(isValid, false);
        isValid = regexEngine.readRegEx("wil**");
        Assert.assertEquals(isValid, false);
        isValid = regexEngine.readRegEx("26s*+");
        Assert.assertEquals(isValid, false);

        // invalid brackets(start with closing bracket)
        isValid = regexEngine.readRegEx("ra)w");
        Assert.assertEquals(isValid, false);

        // if the bracket is not closed at the end
        isValid = regexEngine.readRegEx("aep(26");
        Assert.assertEquals(isValid, false);
    }  

    @Test
    public void verboseModeBasic() {
        RegexEngine regexEngine = new RegexEngine();
        regexEngine.readRegEx("0123456789");
        NFA machine = NFA.buildVerboseMachine(regexEngine.regEx);
        regexEngine.currentStates.add(machine.start);
        boolean output = false;
        for(int i=0; i<9; i++)
        {
            output = regexEngine.matchInputVerbose(machine, String.valueOf(i));
            Assert.assertEquals(output, false);
        }
        output = regexEngine.matchInputVerbose(machine, "9");
        Assert.assertEquals(output, true);
        
        regexEngine.reset();
        regexEngine.readRegEx("abcdefghijkl");
        machine = NFA.buildVerboseMachine(regexEngine.regEx);
        regexEngine.currentStates.add(machine.start);
        for(int i=97; i<108; i++)
        {
            output = regexEngine.matchInputVerbose(machine, String.valueOf((char)i));
            Assert.assertEquals(output, false);
        }
        output = regexEngine.matchInputVerbose(machine, "l");
        Assert.assertEquals(output, true);
        
        regexEngine.reset();
        
        String regex = new String();
        for(int i=47; i<=123; i++)
        {
            regex += Character.toString(i);
        }
        regexEngine.readRegEx(regex);
        machine = NFA.buildVerboseMachine(regexEngine.regEx);
        regexEngine.currentStates.add(machine.start);
        for(int i=47; i<123; i++)
        {
            output = regexEngine.matchInputVerbose(machine, String.valueOf((char)i));
            Assert.assertEquals(output, false);
        }
        output = regexEngine.matchInputVerbose(machine, "{");
        Assert.assertEquals(output, true);
    }
    
    @Test
    public void verboseModeSingleKleeneStar()
    {
        RegexEngine regexEngine = new RegexEngine();

        regexEngine.readRegEx("k*");
        NFA machine = NFA.buildVerboseMachine(regexEngine.regEx);
        regexEngine.currentStates.add(machine.start);
        boolean output = regexEngine.matchInputVerbose(machine, new String());
        Assert.assertEquals(output, true);
        for(int i=0; i<15; i++)
        {
            output = regexEngine.matchInputVerbose(machine, "k");
            Assert.assertEquals(output, true);
        }

        regexEngine.reset();
        regexEngine.readRegEx("gb*");
        machine = NFA.buildVerboseMachine(regexEngine.regEx);
        regexEngine.currentStates.add(machine.start);
        output = regexEngine.matchInputVerbose(machine, new String());
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "g");
        Assert.assertEquals(output, true);
        for(int i=0; i<15; i++)
        {
            output = regexEngine.matchInputVerbose(machine, "b");
            Assert.assertEquals(output, true);
        }
    }
    
    @Test
    public void verboseModeSingleKleenePlus()
    {
        RegexEngine regexEngine = new RegexEngine();

        regexEngine.readRegEx(";+");
        NFA machine = NFA.buildVerboseMachine(regexEngine.regEx);
        regexEngine.currentStates.add(machine.start);
        boolean output = regexEngine.matchInputVerbose(machine, new String());
        Assert.assertEquals(output, false);
        for(int i=0; i<15; i++)
        {
            output = regexEngine.matchInputVerbose(machine, ";");
            Assert.assertEquals(output, true);
        }
        
        regexEngine.reset();
        regexEngine.readRegEx("[]+");
        machine = NFA.buildVerboseMachine(regexEngine.regEx);
        regexEngine.currentStates.add(machine.start);
        output = regexEngine.matchInputVerbose(machine, new String());
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "[");
        Assert.assertEquals(output, false);
        for(int i=0; i<15; i++)
        {
            output = regexEngine.matchInputVerbose(machine, "]");
            Assert.assertEquals(output, true);
        }
    }
    
    @Test
    public void verboseModeSingleAlternation()
    {
        RegexEngine regexEngine = new RegexEngine();

        regexEngine.readRegEx("be|ur");
        NFA machine = NFA.buildVerboseMachine(regexEngine.regEx);
        regexEngine.currentStates.add(machine.start);
        boolean output = regexEngine.matchInputVerbose(machine, "b");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "e");
        Assert.assertEquals(output, true);
        output = regexEngine.matchInputVerbose(machine, "u");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "r");
        Assert.assertEquals(output, false);
        
        regexEngine.reset();
        regexEngine.currentStates.add(machine.start);
        output = regexEngine.matchInputVerbose(machine, "u");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "r");
        Assert.assertEquals(output, true);
        output = regexEngine.matchInputVerbose(machine, "b");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "e");
        Assert.assertEquals(output, false);
    }
    
    @Test
    public void verboseModeOnePairOfBrackets()
    {
        RegexEngine regexEngine = new RegexEngine();

        regexEngine.readRegEx("(GW5)F7");
        NFA machine = NFA.buildVerboseMachine(regexEngine.regEx);
        regexEngine.currentStates.add(machine.start);
        boolean output = regexEngine.matchInputVerbose(machine, "G");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "W");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "5");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "F");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "7");
        Assert.assertEquals(output, true);
    }
    
    @Test
    public void verboseModeMultipleBracketsAndOperators()
    {
        RegexEngine regexEngine = new RegexEngine();

        regexEngine.readRegEx("s(d+|c*)8(wr)*");
        NFA machine = NFA.buildVerboseMachine(regexEngine.regEx);
        regexEngine.currentStates.add(machine.start);
        boolean output = regexEngine.matchInputVerbose(machine, "");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "s");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "8");
        Assert.assertEquals(output, true);
        
        regexEngine.reset();
        regexEngine.currentStates.add(machine.start);
        output = regexEngine.matchInputVerbose(machine, "s");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "d");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "8");
        Assert.assertEquals(output, true);
        
        regexEngine.reset();
        regexEngine.currentStates.add(machine.start);
        output = regexEngine.matchInputVerbose(machine, "s");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "c");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "c");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "c");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "8");
        Assert.assertEquals(output, true);
        output = regexEngine.matchInputVerbose(machine, "w");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "r");
        Assert.assertEquals(output, true);
        output = regexEngine.matchInputVerbose(machine, "w");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "r");
        Assert.assertEquals(output, true);
    }

    @Test
    public void verboseModeMultipleBracketsAndOperators2()
    {
        RegexEngine regexEngine = new RegexEngine();

        regexEngine.readRegEx("s+(d+|c*)8*(wr)*");
        NFA machine = NFA.buildVerboseMachine(regexEngine.regEx);
        regexEngine.currentStates.add(machine.start);
        regexEngine.currentStates.add(machine.start);
        boolean output = regexEngine.matchInputVerbose(machine, "");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "s");
        Assert.assertEquals(output, true);
        output = regexEngine.matchInputVerbose(machine, "s");
        Assert.assertEquals(output, true);
        output = regexEngine.matchInputVerbose(machine, "8");
        Assert.assertEquals(output, true);
        
        regexEngine.reset();
        regexEngine.currentStates.add(machine.start);
        output = regexEngine.matchInputVerbose(machine, "s");
        Assert.assertEquals(output, true);
        output = regexEngine.matchInputVerbose(machine, "c");
        Assert.assertEquals(output, true);
        output = regexEngine.matchInputVerbose(machine, "8");
        Assert.assertEquals(output, true);
        output = regexEngine.matchInputVerbose(machine, "8");
        Assert.assertEquals(output, true);
        output = regexEngine.matchInputVerbose(machine, "w");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "r");
        Assert.assertEquals(output, true);
        output = regexEngine.matchInputVerbose(machine, "w");
        Assert.assertEquals(output, false);
        
        regexEngine.reset();
        regexEngine.currentStates.add(machine.start);
        output = regexEngine.matchInputVerbose(machine, "s");
        Assert.assertEquals(output, true);
        output = regexEngine.matchInputVerbose(machine, "d");
        Assert.assertEquals(output, true);
        output = regexEngine.matchInputVerbose(machine, "d");
        Assert.assertEquals(output, true);
        output = regexEngine.matchInputVerbose(machine, "w");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "r");
        Assert.assertEquals(output, true);
        output = regexEngine.matchInputVerbose(machine, "w");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "r");
        Assert.assertEquals(output, true);
    }
    
    @Test
    public void emptyRegularExpression()
    {
        RegexEngine regexEngine = new RegexEngine();
        regexEngine.readRegEx("");
        boolean output = regexEngine.matchInput("");
        boolean output1 = regexEngine.matchInput(" ");
        boolean output2 = regexEngine.matchInput("s");
        boolean output3 = regexEngine.matchInput("14");
        Assert.assertEquals(output, true);
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
    }

    @Test
    public void verboseEmptyRegularExpression()
    {
        RegexEngine regexEngine = new RegexEngine();

        regexEngine.readRegEx("");
        NFA machine = NFA.buildVerboseMachine(regexEngine.regEx);
        regexEngine.currentStates.add(machine.start);
        boolean output = regexEngine.matchInputVerbose(machine, "");
        Assert.assertEquals(output, true);
        output = regexEngine.matchInputVerbose(machine, "");
        Assert.assertEquals(output, true);
        output = regexEngine.matchInputVerbose(machine, " ");
        Assert.assertEquals(output, false);
        output = regexEngine.matchInputVerbose(machine, "");
        Assert.assertEquals(output, false);
    }
}
