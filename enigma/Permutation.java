package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Manaal Siddiqui
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** my cycles for permutation. */
    private String _cycles;
    /** cycles in a list. */
    private String[] _cyclesList;
    /**whether or not it's deranged like me.*/
    private boolean deranged;

    /** Construct a permutation.
     * @param cycles of the Permutation
     * @param alphabet alphabet used for Permutation*/
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        deranged = true;
        _cyclesList = cycles.replaceAll("[)(]", " ").split(" ");

        for (int i = 0; i < _alphabet.size(); i++) {
            char alphaChar = _alphabet.getAlpha()[i];
            boolean found = false;
            for (String cycle: _cyclesList) {
                if (cycle.contains(Character.toString(alphaChar))) {
                    found = true;
                }
            }
            if (!found) {
                addCycle(Character.toString(alphaChar));
                deranged = false;
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    void addCycle(String cycle) {
        String[] cycleTemp = new String[_cyclesList.length + 1];
        for (int i = 0; i < this._cyclesList.length; i++) {
            cycleTemp[i] = this._cyclesList[i];
        }
        cycleTemp[_cyclesList.length] = cycle;
        this._cyclesList = cycleTemp;
    }
    /** get all the cycles, testing purposes.
     * @return the list of cycles */
    public String[] getCycles() {
        return this._cyclesList;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char in = _alphabet.toChar(wrap(p));
        for (String temp : _cyclesList) {
            if (temp.indexOf(in) != -1) {
                int index = temp.indexOf(in) + 1;
                if (index >= temp.length()) {
                    return _alphabet.toInt(temp.charAt(0));
                }
                char place = temp.charAt(index);
                return _alphabet.toInt(place);
            }
        }
        throw new EnigmaException("not in alpha");
    }
    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char in = _alphabet.toChar(wrap(c));
        for (String temp : _cyclesList) {
            if (temp.indexOf(in) != -1) {
                int index = temp.indexOf(in) - 1;
                if (index == -1) {
                    return _alphabet.toInt(temp.charAt(temp.length() - 1));
                }
                return _alphabet.toInt(temp.charAt(index));
            }
        }
        throw new EnigmaException("not in alpha");
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return _alphabet.toChar(permute(_alphabet.toInt(p)));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return _alphabet.toChar(invert(_alphabet.toInt(c)));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        return deranged;
    }
}
