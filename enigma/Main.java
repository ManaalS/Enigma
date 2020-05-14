package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.NoSuchElementException;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Manaal Siddiqui
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        _config = getInput(args[0]);
        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }
        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME.
     * @param name2 */
    private Scanner getInput(String name2) {
        try {
            return new Scanner(new File(name2));
        } catch (IOException excp) {
            throw error("could not open %s", name2);
        }
    }

    /** Return a PrintStream writing to the file named NAME.
     * @param name1 */
    private PrintStream getOutput(String name1) {
        try {
            return new PrintStream(new File(name1));
        } catch (IOException excp) {
            throw error("could not open %s", name1);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine enigma = readConfig();
        if (!_input.hasNext()) {
            throw new EnigmaException("Input empty.");
        }
        int count = 0;
        boolean hasSetting = false;
        while (_input.hasNextLine()) {
            String input = _input.nextLine();
            if (input.indexOf('*') != -1) {
                hasSetting = true;
                setUp(enigma, input);
                count++;
            } else if (input.isEmpty()) {
                _output.println();
            } else if (!hasSetting) {
                throw new EnigmaException("message without setting.");
            } else {
                String output = enigma.convert(input);
                printMessageLine(output);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            if (!_config.hasNext()) {
                throw new EnigmaException("wrong configuration format");
            }
            ArrayList<Rotor> everyRotor = new ArrayList<>();
            String alphabet = _config.next();
            _alphabet = new Alphabet(alphabet);
            int numRotors = _config.nextInt();
            int pawls = _config.nextInt();
            while (_config.hasNext()) {
                everyRotor.add(this.readRotor());
            }
            _allRotors = everyRotor;
            return new Machine(_alphabet, numRotors, pawls, everyRotor);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String thename = _config.next();
            String type = _config.next();
            String cycles = "";
            while (_config.hasNext("\\(.*\\)")) {
                cycles += _config.next();
            }
            if (type.charAt(0) == 'M') {
                return new MovingRotor(thename,
                        new Permutation(cycles,
                                _alphabet), type.substring(1));
            } else if (type.charAt(0) == 'N') {
                return new FixedRotor(thename,
                        new Permutation(cycles, _alphabet));
            } else if (type.charAt(0) == 'R') {
                return new Reflector(thename,
                        new Permutation(cycles, _alphabet));
            } else {
                throw new EnigmaException("wrong rotor");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }
    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Scanner reader = new Scanner(settings);
        String[] rotors = new String[M.numRotors()];
        String starting;
        String plugboard = "";
        if (!reader.hasNext("[*]")) {
            throw new EnigmaException("bad input format");
        }
        reader.next();
        for (int i = 0; i < M.numRotors(); i++) {
            rotors[i] = reader.next();
        }
        for (int i = 0; i < rotors.length - 1; i++) {
            for (int j = i + 1; j < rotors.length; j++) {
                if (rotors[i].equals(rotors[j])) {
                    throw new EnigmaException("Repeated Rotor");
                }
            }
        }
        M.insertRotors(rotors);
        if (!M.getRotors()[0].reflecting()) {
            throw new EnigmaException("First Rotor isnt reflector");
        }
        starting = reader.next();
        M.setRotors(starting);
        if (starting.length() != rotors.length - 1) {
            throw new EnigmaException("settings not the right length.");
        }
        String temp1;
        while (reader.hasNext()) {
            temp1 = reader.next();
            if (temp1.charAt(0) != '(') {
                M.setRings(temp1);
            } else {
                plugboard = plugboard.concat(temp1 + " ");
            }
        }
        Permutation plug = new Permutation(plugboard, _alphabet);
        M.setPlugboard(plug);
    }


    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i += 5) {
            int cap = msg.length() - i;
            if (cap <= 5) {
                _output.println(msg.substring(i, i + cap));
            } else {
                _output.print(msg.substring(i, i + 5) + " ");
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** input messages. */
    private Scanner _input;

    /** another input thing. */
    private Scanner _input2;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** An ArrayList of ALL usable rotors. */
    private ArrayList<Rotor> _allRotors = new ArrayList<>();
}
