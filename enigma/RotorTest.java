package enigma;

import org.junit.Test;
import static org.junit.Assert.*;

/** junit tests for the rotor class */
public class RotorTest {
    /** test that the numbers I put in are being converted forwards
     * accordng to permutations
     */
    Alphabet a = new Alphabet();
    String perms = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
    Permutation p1 = new Permutation(perms, a);
    Rotor i = new Rotor("I", p1);
    Permutation riv = new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)", a);
    Rotor iv = new Rotor("iv", riv);
    Permutation p = new Permutation("(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", a);
    Rotor iii = new Rotor("III", p);

    @Test
    public void testConvertForwards() {
        i.set(5);
        assertEquals(8, i.convertForward(5));

        iv.set(11);
        assertEquals(21, iv.convertForward(8));

        iii.set(23);
        assertEquals(9, iii.convertForward(21));
    }

    /** test that the numbers I put in are being converted backwards
     * to the inverse of my permutation
     */
    @Test
    public void testConvertBackwards() {
        i.set(5);
        assertEquals(7, i.convertBackward(9));

        iv.set(11);
        assertEquals(9, iv.convertForward(25));

        iii.set(23);
        assertEquals(25, iii.convertBackward(23));
    }
}
