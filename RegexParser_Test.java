import java.io.ByteArrayInputStream;

import org.junit.*;

public class RegexParser_Test {
    private static void setUserInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    public void basic() {
        // no operators
        // special symbols like semicolon, space, and numbers are included
        setUserInput("ko;2 q46");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        boolean output = parser.matchInput("ko;2 q46");
        boolean output1 = parser.matchInput("ko;2q46");
        boolean output2 = parser.matchInput("ko2 q46");
        boolean output3 = parser.matchInput("ko2q46");
        Assert.assertEquals(output, true);
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
    }

    @Test
    public void singleKleeneStar() {
        setUserInput("jsh*fq");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        boolean output1 = parser.matchInput("jsh*fq");
        boolean output2 = parser.matchInput("jsfq");
        boolean output3 = parser.matchInput("jshfq");
        boolean output4 = parser.matchInput("jshhhhhfq");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, true);
        Assert.assertEquals(output3, true);
        Assert.assertEquals(output4, true);
    }

    @Test
    public void singleKleenePlus() {
        setUserInput("jsh+fq");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        boolean output1 = parser.matchInput("jsfq");
        boolean output2 = parser.matchInput("jshfq");
        boolean output3 = parser.matchInput("jshhhhhfq");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, true);
        Assert.assertEquals(output3, true);
    }

    @Test
    public void singleAlternation() {
        setUserInput("kofw|ew");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        boolean output1 = parser.matchInput("kofw");
        boolean output2 = parser.matchInput("ew");
        boolean output3 = parser.matchInput("kofwew");
        boolean output4 = parser.matchInput("kofw|ew");
        Assert.assertEquals(output1, true);
        Assert.assertEquals(output2, true);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, false);
    }

    @Test
    public void onePairOfBrackets() {
        setUserInput("(sbv)oi15");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        boolean output1 = parser.matchInput("(sbv)oi15");
        boolean output2 = parser.matchInput("sbvoi15");
        boolean output3 = parser.matchInput("");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, true);
        Assert.assertEquals(output3, false);
    }

    @Test
    public void multipleKleeneStar() {
        setUserInput("62*ewqr*fs");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        boolean output1 = parser.matchInput("62ewqr*fs");
        boolean output2 = parser.matchInput("62*ewqrfs");
        boolean output3 = parser.matchInput("6ewqfs");
        boolean output4 = parser.matchInput("62222ewqrrrfs");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, true);
        Assert.assertEquals(output4, true);
    }

    @Test
    public void multipleKleenePlus() {
        setUserInput("gq;+mi29+wnr");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        boolean output1 = parser.matchInput("gq;mi29+wnr");
        boolean output2 = parser.matchInput("gq;+mi29wnr");
        boolean output3 = parser.matchInput("gqmi29wnr");
        boolean output4 = parser.matchInput("gq;mi2wnr");
        boolean output5 = parser.matchInput("gq;;;mi2999999wnr");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, false);
        Assert.assertEquals(output5, true);
    }

    @Test
    public void multipleAlternation() {
        setUserInput("vqr|89r|bj,");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        boolean output1 = parser.matchInput("vqr|89r|bj,");
        boolean output2 = parser.matchInput("vqr");
        boolean output3 = parser.matchInput("89r");
        boolean output4 = parser.matchInput("bj,");
        boolean output5 = parser.matchInput("vqr89rbj,");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, true);
        Assert.assertEquals(output3, true);
        Assert.assertEquals(output4, true);
        Assert.assertEquals(output5, false);
    }

    @Test
    public void multipleBrackets() {
        setUserInput("(,rukw35)gtr(ehrw;[])");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        boolean output1 = parser.matchInput("(,rukw35)gtr(ehrw;[])");
        boolean output2 = parser.matchInput("(,rukw35)gtrehrw;[]");
        boolean output3 = parser.matchInput(",rukw35gtr(ehrw;[])");
        boolean output4 = parser.matchInput(",rukw35gtrehrw;[]");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, true);
    }

    @Test
    public void bracketsAndKleeneStar() {
        setUserInput("tq4(jpl5'7)*vr3");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        boolean output1 = parser.matchInput("tq4(jpl5'7)*vr3");
        boolean output2 = parser.matchInput("tq4jpl5'7*vr3");
        boolean output3 = parser.matchInput("tq4vr3");
        boolean output4 = parser.matchInput("tq4jpl5'7vr3");
        boolean output5 = parser.matchInput("tq4jpl5'7jpl5'7jpl5'7vr3");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, true);
        Assert.assertEquals(output4, true);
        Assert.assertEquals(output5, true);
    }

    @Test
    public void bracketsAndKleenePlus() {
        setUserInput("oq(ij2;_)+24");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        boolean output1 = parser.matchInput("oq(ij2;_)+24");
        boolean output2 = parser.matchInput("oqij2;_+24");
        boolean output3 = parser.matchInput("oq(ij2;_)24");
        boolean output4 = parser.matchInput("oq24");
        boolean output5 = parser.matchInput("oqij2;_24");
        boolean output6 = parser.matchInput("oqij2;_ij2;_ij2;_24");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, false);
        Assert.assertEquals(output5, true);
        Assert.assertEquals(output6, true);
    }

    @Test
    public void bracketsAndAlternation() {
        setUserInput("t(gt4w|ok)two");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        boolean output1 = parser.matchInput("t(gt4w|ok)two");
        boolean output2 = parser.matchInput("tgt4w|oktwo");
        boolean output3 = parser.matchInput("t(gt4wok)two");
        boolean output4 = parser.matchInput("tgt4woktwo");
        boolean output5 = parser.matchInput("tgt4wtwo");
        boolean output6 = parser.matchInput("toktwo");
        boolean output7 = parser.matchInput("ttwo");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, false);
        Assert.assertEquals(output4, false);
        Assert.assertEquals(output5, true);
        Assert.assertEquals(output6, true);
        Assert.assertEquals(output7, false);
    }

    @Test
    public void multipleDifferentOperators() {
        setUserInput("qh+3|gw*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        boolean output1 = parser.matchInput("qh+3|gw*");
        boolean output2 = parser.matchInput("qh+3gw*");
        boolean output3 = parser.matchInput("qh3|gw*");
        boolean output4 = parser.matchInput("qh+3|gw");
        boolean output5 = parser.matchInput("qh3gw");
        boolean output6 = parser.matchInput("qh+3");
        boolean output7 = parser.matchInput("gw*");
        boolean output8 = parser.matchInput("q3");
        boolean output9 = parser.matchInput("g");
        boolean output10 = parser.matchInput("qh3");
        boolean output11 = parser.matchInput("gw");
        boolean output12 = parser.matchInput("qhhhhhh3");
        boolean output13 = parser.matchInput("gwwwwww");
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
    public void bracketsAndOperators() {
        setUserInput("(h*|p)(h|d)");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        boolean output1 = parser.matchInput("(h*|p)(h|d)");
        boolean output2 = parser.matchInput("");
        boolean output3 = parser.matchInput("d");
        boolean output4 = parser.matchInput("h");
        boolean output5 = parser.matchInput("hhd");
        boolean output6 = parser.matchInput("ph");
        boolean output7 = parser.matchInput("pd");
        boolean output8 = parser.matchInput("hph");
        boolean output9 = parser.matchInput("phd");
        Assert.assertEquals(output1, false);
        Assert.assertEquals(output2, false);
        Assert.assertEquals(output3, true);
        Assert.assertEquals(output4, true);
        Assert.assertEquals(output5, true);
        Assert.assertEquals(output6, true);
        Assert.assertEquals(output7, true);
        Assert.assertEquals(output8, false);
        Assert.assertEquals(output9, false);
    }

    @Test
    public void bracketsAndOperators2() {
        setUserInput("(t*|f)*r*(k|y)*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        boolean output10 = parser.matchInput("(t*|f)+r(k|y)");
        boolean output11 = parser.matchInput("");
        boolean output12 = parser.matchInput("ttt");
        boolean output13 = parser.matchInput("fffff");
        boolean output14 = parser.matchInput("tff");
        boolean output15 = parser.matchInput("rrr");
        boolean output16 = parser.matchInput("kkkk");
        boolean output17 = parser.matchInput("yy");
        boolean output18 = parser.matchInput("ty");
        boolean output19 = parser.matchInput("try");
        boolean output20 = parser.matchInput("rrry");
        boolean output21 = parser.matchInput("fry");
        boolean output22 = parser.matchInput("rk");
        boolean output23 = parser.matchInput("trk");
        boolean output24 = parser.matchInput("tk");
        boolean output25 = parser.matchInput("ky");
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

    @Test(expected = IllegalArgumentException.class)
    public void startWithKleenePlus() {
        setUserInput("+a");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void startWithAlternation() {
        setUserInput("|a");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void startWithKleeneStar() {
        setUserInput("*a");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void kleeneStarPrecededByLeftBracket() {
        setUserInput("(*a)");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void kleenePlusPrecededByLeftBracket() {
        setUserInput("(+a)");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void alternationPrecededByLeftBracket() {
        setUserInput("(|a)");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void consecutiveOperators() {
        setUserInput("a**");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void consecutiveOperators2() {
        setUserInput("a++");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void consecutiveOperators3() {
        setUserInput("a||b");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void consecutiveOperators4() {
        setUserInput("a+*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void consecutiveOperators5() {
        setUserInput("a*+");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void unclosedLeftBracket() {
        setUserInput("(a");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void unclosedRightBracket() {
        setUserInput("a)");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void unclosedBrackets() {
        setUserInput(")a(");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void endWithAlternation() {
        setUserInput("a|");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void alternationFollowedByRightBracket() {
        setUserInput("(a|)");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void alternationFollowedByKleeneStar() {
        setUserInput("a|*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }

    @Test(expected = IllegalArgumentException.class)
    public void alternationFollowedByKleenePlus() {
        setUserInput("a|+");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
    }
    
    ///============================================================
    // @Test
    // public void verboseModeBasic() {
    //     RegexParser parser = new RegexParser();
    //     parser.readRegEx("0123456789");
    //     NFA machine = NFA.buildVerboseMachine(parser.regEx);
    //     parser.currentStates.add(machine.start);
    //     boolean output = false;
    //     for (int i = 0; i < 9; i++) {
    //         output = parser.matchInputVerbose(machine, String.valueOf(i));
    //         Assert.assertEquals(output, false);
    //     }
    //     output = parser.matchInputVerbose(machine, "9");
    //     Assert.assertEquals(output, true);

    //     parser.reset();
    //     parser.readRegEx("abcdefghijkl");
    //     machine = NFA.buildVerboseMachine(parser.regEx);
    //     parser.currentStates.add(machine.start);
    //     for (int i = 97; i < 108; i++) {
    //         output = parser.matchInputVerbose(machine, String.valueOf((char) i));
    //         Assert.assertEquals(output, false);
    //     }
    //     output = parser.matchInputVerbose(machine, "l");
    //     Assert.assertEquals(output, true);

    //     parser.reset();

    //     String regex = new String();
    //     for (int i = 47; i <= 123; i++) {
    //         regex += Character.toString(i);
    //     }
    //     parser.readRegEx(regex);
    //     machine = NFA.buildVerboseMachine(parser.regEx);
    //     parser.currentStates.add(machine.start);
    //     for (int i = 47; i < 123; i++) {
    //         output = parser.matchInputVerbose(machine, String.valueOf((char) i));
    //         Assert.assertEquals(output, false);
    //     }
    //     output = parser.matchInputVerbose(machine, "{");
    //     Assert.assertEquals(output, true);
    // }

    // @Test
    // public void verboseModeSingleKleeneStar() {
    //     RegexParser parser = new RegexParser();

    //     parser.readRegEx("k*");
    //     NFA machine = NFA.buildVerboseMachine(parser.regEx);
    //     parser.currentStates.add(machine.start);
    //     boolean output = parser.matchInputVerbose(machine, new String());
    //     Assert.assertEquals(output, true);
    //     for (int i = 0; i < 15; i++) {
    //         output = parser.matchInputVerbose(machine, "k");
    //         Assert.assertEquals(output, true);
    //     }

    //     parser.reset();
    //     parser.readRegEx("gb*");
    //     machine = NFA.buildVerboseMachine(parser.regEx);
    //     parser.currentStates.add(machine.start);
    //     output = parser.matchInputVerbose(machine, new String());
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "g");
    //     Assert.assertEquals(output, true);
    //     for (int i = 0; i < 15; i++) {
    //         output = parser.matchInputVerbose(machine, "b");
    //         Assert.assertEquals(output, true);
    //     }
    // }

    // @Test
    // public void verboseModeSingleKleenePlus() {
    //     RegexParser parser = new RegexParser();

    //     parser.readRegEx(";+");
    //     NFA machine = NFA.buildVerboseMachine(parser.regEx);
    //     parser.currentStates.add(machine.start);
    //     boolean output = parser.matchInputVerbose(machine, new String());
    //     Assert.assertEquals(output, false);
    //     for (int i = 0; i < 15; i++) {
    //         output = parser.matchInputVerbose(machine, ";");
    //         Assert.assertEquals(output, true);
    //     }

    //     parser.reset();
    //     parser.readRegEx("[]+");
    //     machine = NFA.buildVerboseMachine(parser.regEx);
    //     parser.currentStates.add(machine.start);
    //     output = parser.matchInputVerbose(machine, new String());
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "[");
    //     Assert.assertEquals(output, false);
    //     for (int i = 0; i < 15; i++) {
    //         output = parser.matchInputVerbose(machine, "]");
    //         Assert.assertEquals(output, true);
    //     }
    // }

    // @Test
    // public void verboseModeSingleAlternation() {
    //     RegexParser parser = new RegexParser();

    //     parser.readRegEx("be|ur");
    //     NFA machine = NFA.buildVerboseMachine(parser.regEx);
    //     parser.currentStates.add(machine.start);
    //     boolean output = parser.matchInputVerbose(machine, "b");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "e");
    //     Assert.assertEquals(output, true);
    //     output = parser.matchInputVerbose(machine, "u");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "r");
    //     Assert.assertEquals(output, false);

    //     parser.reset();
    //     parser.currentStates.add(machine.start);
    //     output = parser.matchInputVerbose(machine, "u");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "r");
    //     Assert.assertEquals(output, true);
    //     output = parser.matchInputVerbose(machine, "b");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "e");
    //     Assert.assertEquals(output, false);
    // }

    // @Test
    // public void verboseModeOnePairOfBrackets() {
    //     RegexParser parser = new RegexParser();

    //     parser.readRegEx("(GW5)F7");
    //     NFA machine = NFA.buildVerboseMachine(parser.regEx);
    //     parser.currentStates.add(machine.start);
    //     boolean output = parser.matchInputVerbose(machine, "G");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "W");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "5");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "F");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "7");
    //     Assert.assertEquals(output, true);
    // }

    // @Test
    // public void verboseModeMultipleBracketsAndOperators() {
    //     RegexParser parser = new RegexParser();

    //     parser.readRegEx("s(d+|c*)8(wr)*");
    //     NFA machine = NFA.buildVerboseMachine(parser.regEx);
    //     parser.currentStates.add(machine.start);
    //     boolean output = parser.matchInputVerbose(machine, "");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "s");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "8");
    //     Assert.assertEquals(output, true);

    //     parser.reset();
    //     parser.currentStates.add(machine.start);
    //     output = parser.matchInputVerbose(machine, "s");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "d");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "8");
    //     Assert.assertEquals(output, true);

    //     parser.reset();
    //     parser.currentStates.add(machine.start);
    //     output = parser.matchInputVerbose(machine, "s");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "c");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "c");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "c");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "8");
    //     Assert.assertEquals(output, true);
    //     output = parser.matchInputVerbose(machine, "w");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "r");
    //     Assert.assertEquals(output, true);
    //     output = parser.matchInputVerbose(machine, "w");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "r");
    //     Assert.assertEquals(output, true);
    // }

    // @Test
    // public void verboseModeMultipleBracketsAndOperators2() {
    //     RegexParser parser = new RegexParser();

    //     parser.readRegEx("s+(d+|c*)8*(wr)*");
    //     NFA machine = NFA.buildVerboseMachine(parser.regEx);
    //     parser.currentStates.add(machine.start);
    //     parser.currentStates.add(machine.start);
    //     boolean output = parser.matchInputVerbose(machine, "");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "s");
    //     Assert.assertEquals(output, true);
    //     output = parser.matchInputVerbose(machine, "s");
    //     Assert.assertEquals(output, true);
    //     output = parser.matchInputVerbose(machine, "8");
    //     Assert.assertEquals(output, true);

    //     parser.reset();
    //     parser.currentStates.add(machine.start);
    //     output = parser.matchInputVerbose(machine, "s");
    //     Assert.assertEquals(output, true);
    //     output = parser.matchInputVerbose(machine, "c");
    //     Assert.assertEquals(output, true);
    //     output = parser.matchInputVerbose(machine, "8");
    //     Assert.assertEquals(output, true);
    //     output = parser.matchInputVerbose(machine, "8");
    //     Assert.assertEquals(output, true);
    //     output = parser.matchInputVerbose(machine, "w");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "r");
    //     Assert.assertEquals(output, true);
    //     output = parser.matchInputVerbose(machine, "w");
    //     Assert.assertEquals(output, false);

    //     parser.reset();
    //     parser.currentStates.add(machine.start);
    //     output = parser.matchInputVerbose(machine, "s");
    //     Assert.assertEquals(output, true);
    //     output = parser.matchInputVerbose(machine, "d");
    //     Assert.assertEquals(output, true);
    //     output = parser.matchInputVerbose(machine, "d");
    //     Assert.assertEquals(output, true);
    //     output = parser.matchInputVerbose(machine, "w");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "r");
    //     Assert.assertEquals(output, true);
    //     output = parser.matchInputVerbose(machine, "w");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "r");
    //     Assert.assertEquals(output, true);
    // }

    // @Test
    // public void emptyRegularExpression() {
    //     RegexParser parser = new RegexParser();
    //     parser.readRegEx("");
    //     boolean output = parser.matchInput("");
    //     boolean output1 = parser.matchInput(" ");
    //     boolean output2 = parser.matchInput("s");
    //     boolean output3 = parser.matchInput("14");
    //     Assert.assertEquals(output, true);
    //     Assert.assertEquals(output1, false);
    //     Assert.assertEquals(output2, false);
    //     Assert.assertEquals(output3, false);
    // }

    // @Test
    // public void verboseEmptyRegularExpression() {
    //     RegexParser parser = new RegexParser();

    //     parser.readRegEx("");
    //     NFA machine = NFA.buildVerboseMachine(parser.regEx);
    //     parser.currentStates.add(machine.start);
    //     boolean output = parser.matchInputVerbose(machine, "");
    //     Assert.assertEquals(output, true);
    //     output = parser.matchInputVerbose(machine, "");
    //     Assert.assertEquals(output, true);
    //     output = parser.matchInputVerbose(machine, " ");
    //     Assert.assertEquals(output, false);
    //     output = parser.matchInputVerbose(machine, "");
    //     Assert.assertEquals(output, false);
    // }
}
