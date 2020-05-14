package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.Arrays;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author Manaal Siddiqui
 */
public class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    private Permutation perms;
    private String alphas = UPPER_STRING;


    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet b = new Alphabet();
        Permutation permm = new Permutation("", b);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, permm, b);
    }

    @Test
    public void testGoodAlphabet() {
        Alphabet myAlpha = new Alphabet("AJHKFiT0YG!");
        assertEquals(11, myAlpha.size());
        assertTrue(myAlpha.contains('F'));
        assertFalse(myAlpha.contains('B'));
        assertFalse(myAlpha.contains('@'));
        assertTrue(myAlpha.contains('!'));
        assertEquals('K', myAlpha.toChar(3));
        assertEquals(6, myAlpha.toInt('T'));
        assertEquals('0', myAlpha.toChar(7));
        assertEquals('!', myAlpha.toChar(10));
        assertEquals(10, myAlpha.toInt('!'));

        Alphabet myAlpha2 = new Alphabet("QWERTYUIOP");
        assertEquals(10, myAlpha2.size());
    }

    @Test(expected = EnigmaException.class)
    public void testBadAlphaPerm() {
        Alphabet myAlpha = new Alphabet("AJHKFXFTJYG");
        Alphabet bad = new Alphabet("AJHKF(FTJYG");
        Alphabet bad2 = new Alphabet("Argherijg*");
        Alphabet bad3 = new Alphabet("A B C D E F                G");
        Alphabet bad4 = new Alphabet("ASD)FGHJKL");
        Permutation bad5 = new Permutation("(A B C)", new Alphabet());
        Permutation bad6 = new Permutation("(AB  C)", new Alphabet());
        Permutation bad7 = new Permutation("(AB!C)", new Alphabet());
    }

    @Test(expected = EnigmaException.class)
    public void testAlphabetContains() {
        Alphabet myAlpha2 = new Alphabet("QWERTYUIOP");
        myAlpha2.toChar(21);
        myAlpha2.toInt('A');
    }


    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        p.invert(5);
        p.invert('F');
        p.permute('F');
        p.permute(5);
    }
    @Test
    public void testInvertCharacters() {
        Alphabet a = new Alphabet();
        String cycles = "(AEIOUBCDFG) (HJKLQSVX) (MNPRTW) (YZ)";
        Permutation p = new Permutation(cycles, a);
        assertEquals('F', p.invert('G'));
        assertEquals('X', p.invert('H'));
        assertEquals('W', p.invert('M'));
        assertEquals('Y', p.invert('Z'));

        Permutation p1 = new Permutation("(Z)", new Alphabet("Z"));
        assertEquals('Z', p1.invert('Z'));

        Alphabet six = new Alphabet("ANOPTD");
        Permutation p2 = new Permutation("(ATP)  (DNO)", six);
        assertEquals('T', p2.invert('P'));
        assertEquals('A', p2.invert('T'));
        assertEquals('N', p2.invert('O'));
    }
    @Test
    public void testInvertIntegers() {
        Alphabet abc = new Alphabet();
        String cyc = "(AEIOUBCDFG)   (HJKLQSVX) (MNPRTW) (YZ)";
        Permutation p = new Permutation(cyc, abc);
        assertEquals(6, p.invert(0));
        assertEquals(23, p.invert(7));
        assertEquals(22, p.invert(12));
        assertEquals(25, p.invert(24));
        assertEquals(6, p.invert(26));

        Permutation p1 = new Permutation("(ATP) (DNO)",
                new Alphabet("ANOPTD"));
        assertEquals(5, p1.invert(1));
        assertEquals(4, p1.invert(3));
    }

    @Test
    public void testPermuteCharacters() {
        Alphabet e = new Alphabet();
        Permutation p = new Permutation("(AEIOUBCDFG) (HLQSVX)  (MNPRTW) (YZ)",
                e);
        assertEquals(4, p.permute(0));
        assertEquals(0, p.permute(6));
        assertEquals(3, p.permute(2));
        assertEquals(7, p.permute(23));
        assertEquals(12, p.permute(22));
        assertEquals(15, p.permute(13));
        assertEquals(24, p.permute(25));
        assertEquals(9, p.permute(9));
        assertEquals(10, p.permute(10));
        assertEquals(3, p.permute(28));

        Permutation p1 = new Permutation("(YPSLI)", new Alphabet("PSILY"));
        assertEquals(1, p1.permute(0));
        assertEquals(4, p1.permute(7));
    }

    @Test
    public void testMapToSelf() {
        Alphabet a = new Alphabet();
        Permutation p = new Permutation("(AEIOUBCDFG) (HLQSVX) (MNPTW) (Y)", a);
        assertEquals('R', p.permute('R'));
        assertEquals('Y', p.permute('Y'));
        assertEquals('Z', p.permute('Z'));
        assertEquals('A', p.permute('G'));
        assertEquals('F', p.invert('G'));
        assertEquals('Z', p.invert('Z'));
        assertEquals('Y', p.invert('Y'));
        assertEquals('R', p.invert('R'));
    }

    @Test
    public void testCyclesList() {
        Alphabet a = new Alphabet();
        Permutation p = new Permutation("(QET) (FJAP) (BNMZ)", a);
        boolean check = (Arrays.asList(p.getCycles()).contains("C"));
        assertTrue(check);
    }

    @Test
    public void testPermuteIntegers() {
        Alphabet a = new Alphabet();
        Permutation p = new Permutation("(AEIOUBCDFG)  (HLQSVX) (MNPRTW) (YZ)",
                a);
        assertEquals('E', p.permute('A'));
        assertEquals('A', p.permute('G'));
        assertEquals('D', p.permute('C'));
        assertEquals('H', p.permute('X'));
        assertEquals('M', p.permute('W'));
        assertEquals('P', p.permute('N'));
        assertEquals('Z', p.permute('Y'));
        assertEquals('J', p.permute('J'));
        assertEquals('K', p.permute('K'));

        Permutation p1 = new Permutation("(B)",
                new Alphabet("B"));
        assertEquals('B', p1.permute('B'));
        assertEquals(0, p1.permute(0));
    }

    @Test
    public void testSize() {
        Alphabet b = new Alphabet();
        assertEquals(26, b.size());
        Alphabet alpha1 = new Alphabet("B");
        assertEquals(1, alpha1.size());
        Alphabet alpha2 = new Alphabet("ANOPTD");
        assertEquals(6, alpha2.size());
    }

    @Test
    public void testDerangement() {
        Alphabet a = new Alphabet();
        String cy = "(AB) (CDX) (EHUIJMT) (FKNPYWS) (GLOVQRZ)";
        Permutation p1 = new Permutation(cy, a);
        assertTrue(p1.derangement());

        String sic = "(CDX) (EHUIJMT) (FKNPYWS) (GLOVQRZ) (A)";
        Permutation p2 = new Permutation("sic", a);
        assertFalse(p2.derangement());
    }
}
