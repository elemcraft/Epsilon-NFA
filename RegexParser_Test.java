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
        parser.buildNFA();

        parser.getNFA().initializeNFA();
        boolean output = parser.getNFA().match("ko;2 q46");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("ko;2q46");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("ko2 q46");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("ko2q46");
        Assert.assertEquals(output, false);
    }

    @Test
    public void emptyRegEx() {
        RegexParser parser = new RegexParser();
        parser.buildNFA();

        parser.getNFA().initializeNFA();
        boolean output = parser.getNFA().match("");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("a");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("*");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("|");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("+");
        Assert.assertEquals(output, false);
    }

    @Test
    public void singleKleeneStar() {
        setUserInput("jsh*fq");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        parser.getNFA().initializeNFA();
        boolean output = parser.getNFA().match("jsh*fq");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("jsfq");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("jshfq");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("jshhhhhfq");
        Assert.assertEquals(output, true);
    }

    @Test
    public void singleKleenePlus() {
        setUserInput("jsh+fq");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        parser.getNFA().initializeNFA();
        boolean output = parser.getNFA().match("jsfq");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("jshfq");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("jshhhhhfq");
        Assert.assertEquals(output, true);
    }

    @Test
    public void singleAlternation() {
        setUserInput("kofw|ew");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        parser.getNFA().initializeNFA();
        boolean output = parser.getNFA().match("kofw");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("ew");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("kofwew");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("kofw|ew");
        Assert.assertEquals(output, false);
    }

    @Test
    public void onePairOfBrackets() {
        setUserInput("(sbv)oi15");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        parser.getNFA().initializeNFA();
        boolean output = parser.getNFA().match("(sbv)oi15");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("sbvoi15");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("");
        Assert.assertEquals(output, false);
    }

    @Test
    public void multipleKleeneStar() {
        setUserInput("62*ewqr*fs");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        parser.getNFA().initializeNFA();
        boolean output = parser.getNFA().match("62ewqr*fs");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("62*ewqrfs");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("6ewqfs");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("62222ewqrrrfs");
        Assert.assertEquals(output, true);
    }

    @Test
    public void multipleKleenePlus() {
        setUserInput("gq;+mi29+wnr");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        parser.getNFA().initializeNFA();
        boolean output = parser.getNFA().match("gq;mi29+wnr");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("gq;+mi29wnr");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("gqmi29wnr");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("gq;mi2wnr");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("gq;;;mi2999999wnr");
        Assert.assertEquals(output, true);
    }

    @Test
    public void multipleAlternation() {
        setUserInput("vqr|89r|bj,");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        parser.getNFA().initializeNFA();
        boolean output = parser.getNFA().match("vqr|89r|bj,");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("vqr");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("89r");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("bj,");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("vqr89rbj,");
        Assert.assertEquals(output, false);
    }

    @Test
    public void multipleBrackets() {
        setUserInput("(,rukw35)gtr(ehrw;[])");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        parser.getNFA().initializeNFA();
        boolean output = parser.getNFA().match("(,rukw35)gtr(ehrw;[])");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("(,rukw35)gtrehrw;[]");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match(",rukw35gtr(ehrw;[])");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match(",rukw35gtrehrw;[]");
        Assert.assertEquals(output, true);
    }

    @Test
    public void bracketsAndKleeneStar() {
        setUserInput("tq4(jpl5'7)*vr3");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        parser.getNFA().initializeNFA();
        boolean output = parser.getNFA().match("tq4(jpl5'7)*vr3");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("tq4jpl5'7*vr3");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("tq4vr3");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("tq4jpl5'7vr3");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("tq4jpl5'7jpl5'7jpl5'7vr3");
        Assert.assertEquals(output, true);
    }

    @Test
    public void bracketsAndKleenePlus() {
        setUserInput("oq(ij2;_)+24");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        parser.getNFA().initializeNFA();
        boolean output = parser.getNFA().match("oq(ij2;_)+24");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("oqij2;_+24");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("oq(ij2;_)24");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("oq24");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("oqij2;_24");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("oqij2;_ij2;_ij2;_24");
        Assert.assertEquals(output, true);
    }

    @Test
    public void bracketsAndAlternation() {
        setUserInput("t(gt4w|ok)two");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        parser.getNFA().initializeNFA();
        boolean output = parser.getNFA().match("t(gt4w|ok)two");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("tgt4w|oktwo");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("t(gt4wok)two");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("tgt4woktwo");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("tgt4wtwo");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("toktwo");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("ttwo");
        Assert.assertEquals(output, false);
    }

    @Test
    public void multipleDifferentOperators() {
        setUserInput("qh+3|gw*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        parser.getNFA().initializeNFA();
        boolean output = parser.getNFA().match("qh+3|gw*");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("qh+3gw*");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("qh3|gw*");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("qh+3|gw");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("qh3gw");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("qh+3");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("gw*");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("q3");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("g");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("qh3");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("gw");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("qhhhhhh3");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("gwwwwww");
        Assert.assertEquals(output, true);
    }

    @Test
    public void bracketsAndOperators() {
        setUserInput("(h*|p)(h|d)");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        parser.getNFA().initializeNFA();
        boolean output = parser.getNFA().match("(h*|p)(h|d)");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("d");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("h");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("hhd");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("ph");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("pd");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("hph");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("phd");
        Assert.assertEquals(output, false);
    }

    @Test
    public void bracketsAndOperators2() {
        setUserInput("(t*|f)*r*(k|y)*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        parser.getNFA().initializeNFA();
        boolean output = parser.getNFA().match("(t*|f)+r(k|y)");
        Assert.assertEquals(output, false);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("ttt");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("fffff");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("tff");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("rrr");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("kkkk");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("yy");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("ty");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("try");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("rrry");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("fry");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("rk");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("trk");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("tk");
        Assert.assertEquals(output, true);

        parser.getNFA().initializeNFA();
        output = parser.getNFA().match("ky");
        Assert.assertEquals(output, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void startWithKleenePlus() {
        setUserInput("+a");
        RegexParser parser = new RegexParser();
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

    @Test
    public void verbose_Basic() {
        setUserInput("0123456789");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        for (int i = 0; i < 9; i++) {
            parser.getNFA().match(Integer.toString(i));
            Assert.assertEquals(parser.getNFA().isAcceptable(), false);
        }
        parser.getNFA().match("9");
        Assert.assertEquals(parser.getNFA().isAcceptable(), true);
    }

    @Test
    public void verbose_Basic2() {
        setUserInput("abcdefghijklmnopqrstuvwxyz");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        for (int i = 0; i < 25; i++) {
            parser.getNFA().match(Character.toString(i + 'a'));
            Assert.assertEquals(parser.getNFA().isAcceptable(), false);
        }
        parser.getNFA().match("z");
        Assert.assertEquals(parser.getNFA().isAcceptable(), true);
    }

    @Test
    public void verbose_SingleKleeneStar() {
        setUserInput("k*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().isAcceptable(), true);

        for (int i = 0; i < 100; i++) {
            parser.getNFA().match("k".repeat(i));
            Assert.assertEquals(parser.getNFA().isAcceptable(), true);
        }
    }

    @Test
    public void verbose_SingleKleeneStar2() {
        setUserInput("ab*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("a");
        Assert.assertEquals(parser.getNFA().isAcceptable(), true);

        for (int i = 0; i < 100; i++) {
            parser.getNFA().match("b".repeat(i));
            Assert.assertEquals(parser.getNFA().isAcceptable(), true);
        }
    }

    @Test
    public void verbose_SingleKleenePlus() {
        setUserInput("k+");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("k".repeat(0));
        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        for (int i = 1; i < 100; i++) {
            parser.getNFA().match("k".repeat(i));
            Assert.assertEquals(parser.getNFA().isAcceptable(), true);
        }
    }

    @Test
    public void verbose_SingleKleenePlus2() {
        setUserInput("fh+");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("f");
        Assert.assertEquals(parser.getNFA().isAcceptable(), false);
        
        parser.getNFA().match("h".repeat(0));
        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        for (int i = 1; i < 100; i++) {
            parser.getNFA().match("h".repeat(i));
            Assert.assertEquals(parser.getNFA().isAcceptable(), true);
        }
    }

    @Test
    public void verbose_SingleAlternation() {
        setUserInput("ab|dc");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("a");
        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("b");
        Assert.assertEquals(parser.getNFA().isAcceptable(), true);
    }

    @Test
    public void verbose_SingleAlternation2() {
        setUserInput("ab|dc");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("d");
        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("c");
        Assert.assertEquals(parser.getNFA().isAcceptable(), true);
    }

    @Test
    public void verbose_OnePairOfBrackets() {
        setUserInput("(ab)c");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("a");
        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("b");
        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("c");
        Assert.assertEquals(parser.getNFA().isAcceptable(), true);

        parser.getNFA().match("a");
        Assert.assertEquals(parser.getNFA().isAcceptable(), false);
    }

    @Test
    public void verbose_MultipleBracketsAndOperators() {
        setUserInput("s(d+|c*)8(wr)*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("s8");
        Assert.assertEquals(parser.getNFA().isAcceptable(), true);

        parser.getNFA().match("wr");
        Assert.assertEquals(parser.getNFA().isAcceptable(), true);

        parser.getNFA().match("w");
        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("r");
        Assert.assertEquals(parser.getNFA().isAcceptable(), true);
    }

    @Test
    public void verbose_MultipleBracketsAndOperators2() {
        setUserInput("s(d+|c*)8(wr)*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("s");
        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("ddd");
        Assert.assertEquals(parser.getNFA().isAcceptable(), false);
        
        parser.getNFA().match("8");
        Assert.assertEquals(parser.getNFA().isAcceptable(), true);
    }

    @Test
    public void verbose_MultipleBracketsAndOperators3() {
        setUserInput("s(d+|c*)8(wr)*");
        RegexParser parser = new RegexParser();
        parser.readRegEx();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("s");
        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("ccc");
        Assert.assertEquals(parser.getNFA().isAcceptable(), false);
        
        parser.getNFA().match("8");
        Assert.assertEquals(parser.getNFA().isAcceptable(), true);

        parser.getNFA().match("w");
        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("r");
        Assert.assertEquals(parser.getNFA().isAcceptable(), true);

        parser.getNFA().match("wr");
        Assert.assertEquals(parser.getNFA().isAcceptable(), true);
    }

    @Test
    public void verbose_emptyRegEx() {
        RegexParser parser = new RegexParser();
        parser.buildNFA();

        Assert.assertEquals(parser.getNFA().isAcceptable(), true);
        
        parser.getNFA().match("");
        Assert.assertEquals(parser.getNFA().isAcceptable(), true);

        parser.getNFA().match(" ");
        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("");
        Assert.assertEquals(parser.getNFA().isAcceptable(), false);

        parser.getNFA().match("a");
        Assert.assertEquals(parser.getNFA().isAcceptable(), false);
    }
}