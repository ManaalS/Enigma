package enigma;

import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;

/** Class that represents a complete enigma machine.
 *  @author Manaal Siddiqui
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    private final Alphabet _alphabet;
    /** additional. */
    private int _nRotors;
    /** additional. */
    private int _nPawls;
    /** additional. */
    private Collection<Rotor> _theRotors;
    /** additional. */
    private Permutation _plugboard;
    /** additional. */
    private Rotor[] myrotors;

    /**construct a machine given.
     * @param alpha The alphabet for the configuration
     * @param numRotors the number of rotors in machine
     * @param pawls the number of moving rotors
     * @param allRotors all the rotors
     */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _nRotors = numRotors;
        _nPawls = pawls;
        ArrayList<String> rotorNames = new ArrayList<>();
        for (Rotor r : allRotors) {
            rotorNames.add(r.name());
        }
        _theRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _nRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _nPawls;
    }

    /** set the rings given an input String.
     * @param rings input.
     */
    void setRings(String rings) {
        boolean first = true;
        int j = 0;
        for (Rotor r : myrotors) {
            if (first) {
                r.setRing(0);
                first = false;
            } else {
                r.setRing(rings.charAt(j));
                j += 1;
            }
        }
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        myrotors = new Rotor[numRotors()];
        for (int i = 0; i < rotors.length; i++) {
            for (int j = i + 1; j < rotors.length; j++) {
                if (rotors[i].equalsIgnoreCase(rotors[j])) {
                    throw new EnigmaException("Duplicated Rotor");
                }
            }
        }
        HashMap<String, Rotor> mapRotors = new HashMap<String, Rotor>();
        for (Rotor rotor : _theRotors) {
            mapRotors.put(rotor.name().toUpperCase(), rotor);
        }
        for (int a = 0; a < rotors.length; a++) {
            String key = rotors[a].toUpperCase();
            if (mapRotors.containsKey(key)) {
                myrotors[a] = mapRotors.get(key);
            } else {
                System.out.println("error bad rotor");
                throw new EnigmaException("Bad rotor name");
            }
        }

        if (rotors.length != myrotors.length) {
            System.out.println("error amt of motors doesnt match capacitory");
            throw new EnigmaException("the amount of rotors to be added "
                    + "must match the capacity of the machine");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            System.out.println("wrong setting");
            throw new EnigmaException("Wrong initial setting");
        }
        for (int i = 1; i < numRotors(); i++) {
            char curr = setting.charAt(i - 1);
            if (_alphabet.contains(curr)) {
                myrotors[i].set(curr);
            } else {
                System.out.println("error not in alpha");
                throw new EnigmaException("setting must be in Alphabet");
            }
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        moveRotors();
        int result = _plugboard.permute(c);
        for (int i = numRotors() - 1; i >= 0; i--) {
            result = myrotors[i].convertForward(result);
        }
        for (int i = 1; i < numRotors(); i++) {
            result = myrotors[i].convertBackward(result);
        }
        result = _plugboard.permute(result);
        return result;
    }

    /** This will advance all my rotors depending on if they're at
     * a notch or if its right neighbor is. */
    void moveRotors() {
        for (int i = numRotors() - numPawls(); i < numRotors(); i++) {
            if (myrotors[i].rotates()) {
                if (i == numRotors() - 1) {
                    myrotors[i].advance();
                } else if (myrotors[i + 1].atNotch()) {
                    myrotors[i].advance();
                } else if (myrotors[i].atNotch()
                        && i != numRotors() - numPawls()) {
                    myrotors[i].advance();
                }
            }
        }
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        for (int i = 0; i < msg.length(); i++) {
            char ch = msg.charAt(i);
            if (Character.isWhitespace(ch)) {
                continue;
            }
            int code = _alphabet.toInt(ch);
            int encrypted = convert(code);
            result += _alphabet.toChar(encrypted);
        }
        return result;
    }

    /** get all the rotors
     * in myrotors.
     * @return Rotor
     */
    public Rotor[] getRotors() {
        return myrotors;
    }
}
