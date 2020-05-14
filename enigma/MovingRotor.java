
package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Manaal Siddiqui
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    /** the current notches. */
    private String _notches;
    /** the original notches. */
    private String _originalNotches;

    /** Construct me a moving motor given.
     * @param name The name of the MovingRotor
     * @param perm The permutation for the movingrotor
     * @param  notches The notches of the rotor
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _originalNotches = notches;
    }

    /** reset notches. */
    public void resetNotches() {
        _notches = _originalNotches;
    }

    @Override
    boolean atNotch() {
        int r = permutation().wrap(setting());
        char reference = permutation().alphabet().toChar(r);
        return (_notches.indexOf(reference) > -1);
    }

    /** shift notches by a given char.
     * @param ringStr */
    public void shiftNotches(char ringStr) {
        String newNotches = "";
        for (int i = 0; i < _notches.length(); i++) {
            int curNotchInt = alphabet().toInt(_notches.charAt(i));
            int ringInt = alphabet().toInt(ringStr);
            char notchChar = alphabet().toChar(permutation()
                    .wrap(curNotchInt - ringInt));
            newNotches += notchChar;
        }
        _notches = newNotches;
    }

    @Override
    void advance() {
        set(permutation().wrap(setting() + 1));
    }

    @Override
    boolean rotates() {
        return true;
    }
}
