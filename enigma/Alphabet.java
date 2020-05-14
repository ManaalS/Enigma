package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Manaal Siddiqui
 */
class Alphabet {
    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated.
     *  @param  alpha  a string representing the alphabet */
    Alphabet(String alpha) {
        for (int i = 0; i < alpha.length(); i++) {
            for (int j = 0; j < alpha.length(); j++) {
                if (alpha.charAt(i) == alpha.charAt(j) && j != i) {
                    System.out.println("error duplicates");
                    throw new EnigmaException("duplicates found");
                }
            }
        }
        this._alpha = alpha;
        alphaArray = this._alpha.toCharArray();
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _alpha.length();
    }
    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        String element = Character.toString(ch);
        return _alpha.contains(element);
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index >= size()) {
            System.out.println("index error");
            throw new EnigmaException("index out of bounds");
        }
        return alphaArray[index];
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        int result = _alpha.indexOf(ch);
        if (result == -1) {
            System.out.println("error not in alpha");
            throw new EnigmaException(ch + "not in alphabet");
        }
        return result;
    }
    /** Returns the alpha
     * array. */
    public char[] getAlpha() {
        return alphaArray;
    }

    /** A string version of the alphabet. */
    private String _alpha;

    /** an array version of the alphabet.*/
    private char[] alphaArray;
}

