package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.HashMap;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class MovingRotorTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    /** Check that rotor has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkRotor(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, rotor.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d (%c)", ci, c),
                         ei, rotor.convertForward(ci));
            assertEquals(msg(testId, "wrong inverse of %d (%c)", ei, e),
                         ci, rotor.convertBackward(ei));
        }
    }

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setRotor(String name, HashMap<String, String> rotors,
                          String notches) {
        rotor = new MovingRotor(name, new Permutation(rotors.get(name), UPPER),
                                notches);
    }

    /* ***** TESTS ***** */

    @Test
    public void checkRotorAtA() {
        setRotor("I", NAVALA, "");
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));
    }

    @Test
    public void checkRotorAdvance() {
        setRotor("I", NAVALA, "");
        rotor.advance();
        checkRotor("Rotor I advanced", UPPER_STRING, NAVALB_MAP.get("I"));
    }

    @Test
    public void checkRotorSet() {
        setRotor("I", NAVALA, "");
        rotor.set(25);
        checkRotor("Rotor I set", UPPER_STRING, NAVALZ_MAP.get("I"));
    }
    Permutation a = new Permutation(
            "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", new Alphabet());
    Reflector I = new Reflector("I", a);
    FixedRotor _II = new FixedRotor("_II", a);
    MovingRotor _III = new MovingRotor("_III", a, "UMS");

    @Test
    public void checkReflector() {
        assertTrue(I.reflecting());
    }

    @Test
    public void checkFixed() {
        assertFalse(_II.rotates());
    }

    @Test
    public void testConverts() {
        Permutation perm = new Permutation("(ABCZ) (KG) (DEY)", UPPER);
        Rotor ror = new Rotor("I", perm);
        ror.set(7);
        assertEquals(ror.convertForward(3), 25);
        assertEquals(ror.convertBackward(25), 3);
        ror.set(25);
        assertEquals(ror.convertForward(1), 2);
        assertEquals(ror.convertBackward(2), 1);
        ror.set(0);
        assertEquals(ror.convertForward(17), 17);
        assertEquals(ror.convertBackward(17), 17);
        assertEquals(ror.convertForward(-1), 0);
        assertEquals(ror.convertBackward(0), 25);
    }

    @Test
    public void checkNotchAtA() {
        setRotor("II", NAVALA, "G");
        rotor.set(6);
        assertEquals(rotor.atNotch(), true);

        setRotor("I", NAVALA, "D");
        rotor.set(1);
        assertEquals(rotor.atNotch(), false);

        setRotor("Beta", NAVALA, "I");
        rotor.set(3);
        assertEquals(rotor.atNotch(), false);

        setRotor("VII", NAVALA, "A");
        rotor.set(0);
        assertEquals(rotor.atNotch(), true);
    }

    @Test
    public void checkNotchAdvance() {
        setRotor("II", NAVALA, "E");
        rotor.set(0);
        rotor.advance();
        assertEquals(rotor.atNotch(), false);

        setRotor("I", NAVALA, "A");
        rotor.set(25);
        rotor.advance();
        assertEquals(rotor.atNotch(), true);

    }

    @Test
    public void checkMoving() {
        assertFalse(_III.atNotch());
        assertEquals(4, _III.convertForward(0));
        assertEquals(0, _III.convertBackward(4));
        assertEquals(_III.convertBackward(18), _III.convertForward(18));
        _III.advance();
        assertFalse(_III.atNotch());
        assertEquals(5, _III.convertForward(4));
        assertEquals(6, _III.convertBackward(15));
        assertEquals(_III.convertBackward(17), _III.convertForward(17));
        _III.set(12);
        assertTrue(_III.atNotch());
        assertEquals(2, _III.convertForward(0));
        assertEquals(0, _III.convertBackward(2));
        assertEquals(_III.convertBackward(6), _III.convertForward(6));
    }



}
