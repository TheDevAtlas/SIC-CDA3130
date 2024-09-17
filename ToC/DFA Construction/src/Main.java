import java.util.*;
import java.io.*;

public class Main {
    private static class Transition {
        int toID;
        char symbol;

        public Transition(int toID, char symbol) {
            this.toID = toID;
            this.symbol = symbol;
        }
    }

    private static class State {
        int stateID;
        String name;
        boolean finalState = false;
        boolean initialState = false;
        int x; // X-coordinate for the state
        int y; // Y-coordinate for the state
        ArrayList<Transition> connectedTransitions = new ArrayList<>();

        public State(int stateID, String name, int x, int y) {
            this.stateID = stateID;
            this.name = name;
            this.x = x;
            this.y = y;
        }
    }

    private static class DFA {
        int initialStateID;
        int numStates;
        ArrayList<State> stateList = new ArrayList<>();

        public DFA() {
            this.initialStateID = 0;
            this.numStates = 0;
        }

        public void addState(State state) {
            stateList.add(state);
            numStates++;
        }

        public State getState(int stateID) {
            for (State state : stateList) {
                if (state.stateID == stateID) {
                    return state;
                }
            }
            return null;
        }

        public void setInitialState(int stateID) {
            this.initialStateID = stateID;
            getState(stateID).initialState = true;
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        DFA dfa = new DFA();
        HashMap<String, Integer> stateMap = new HashMap<>(); // Map to track unique state paths

        System.out.println("Paste list of your words, and end input by pressing Ctrl+D (Linux/Mac) or Ctrl+Z (Windows):");

        // Read all input at once
        StringBuilder inputBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            inputBuilder.append(line).append("\n");
        }
        scanner.close();

        // Convert StringBuilder to String and split into lines
        String input = inputBuilder.toString();
        String[] lines = input.split("\\R");

        int stateCounter = 0; // Sequential state counter
        int rootX = 0, rootY = 0;
        int spacingY = 100; // Minimum vertical spacing between nodes
        int spacingX = 150; // Minimum horizontal spacing between levels
        int maxY = 0; // To keep track of maximum Y used for spacing

        // Create root state
        State rootState = new State(stateCounter, "q" + stateCounter, rootX, rootY);
        dfa.addState(rootState);
        dfa.setInitialState(stateCounter);
        stateCounter++;

        // Process each line (word) individually
        for (String line : lines) {
            if (!line.trim().isEmpty()) { // Ignore blank lines
                String previousKey = ""; // Empty string to represent the start state
                int previousStateID = 0; // Start from root

                int currentX = spacingX; // Start placing first letters at x = 150
                int currentY = maxY; // Start at the current max Y to avoid overlap

                for (int i = 0; i < line.length(); i++) {
                    char currentChar = line.charAt(i);
                    String currentKey = previousKey + "-" + currentChar; // Current path key

                    // Create new state for the character if it does not exist in the specific context
                    if (!stateMap.containsKey(currentKey)) {
                        State newState = new State(stateCounter, "q" + stateCounter, currentX, currentY);
                        dfa.addState(newState);
                        stateMap.put(currentKey, stateCounter);
                        stateCounter++;

                        currentY += spacingY; // Increment Y for the next node vertically
                        maxY = Math.max(maxY, currentY); // Update maxY to prevent overlap
                    }

                    int currentStateID = stateMap.get(currentKey);

                    // If there is a previous state, add a transition
                    if (previousStateID != -1) {
                        State previousState = dfa.getState(previousStateID);

                        boolean transitionExists = false;
                        for (Transition transition : previousState.connectedTransitions) {
                            if (transition.toID == currentStateID && transition.symbol == currentChar) {
                                transitionExists = true;
                                break;
                            }
                        }

                        if (!transitionExists) {
                            previousState.connectedTransitions.add(new Transition(currentStateID, currentChar));
                        }
                    }

                    // Update for next character
                    previousStateID = currentStateID;
                    previousKey = currentKey;

                    // Move to the next set of letters horizontally
                    currentX += spacingX;
                }

                // Mark the last state of this word as a final state
                State finalState = dfa.getState(previousStateID);
                if (finalState != null) {
                    finalState.finalState = true;
                }
            }
        }

        // Generate JFLAP XML output
        String jflapXML = generateJFLAPXML(dfa);
        System.out.println(jflapXML);

        // Save the XML to a file
        try (PrintWriter out = new PrintWriter("DFA.jff")) {
            out.println(jflapXML);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String generateJFLAPXML(DFA dfa) {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\"?>\n");
        xmlBuilder.append("<structure>\n");
        xmlBuilder.append("\t<type>fa</type>\n");
        xmlBuilder.append("\t<automaton>\n");

        // Define states
        for (State state : dfa.stateList) {
            xmlBuilder.append("\t\t<state id=\"").append(state.stateID).append("\"");
            xmlBuilder.append(" name=\"").append(state.name).append("\">\n"); // Use the state name dynamically
            xmlBuilder.append("\t\t\t<x>").append(state.x).append("</x>\n");
            xmlBuilder.append("\t\t\t<y>").append(state.y).append("</y>\n");
            if (state.initialState) {
                xmlBuilder.append("\t\t\t<initial/>\n");
                xmlBuilder.append("\t\t\t<final/>\n"); /// <<<< this was killing me!!!!!!!
                // initial state needs to be herer so that I can handle the empty string, aaaaaaaaaa
            }
            if (state.finalState) {
                xmlBuilder.append("\t\t\t<final/>\n"); // Correctly mark final state // TODO - not working on initial
            }
            xmlBuilder.append("\t\t</state>\n");
        }

        // Define transitions
        for (State state : dfa.stateList) {
            for (Transition transition : state.connectedTransitions) {
                xmlBuilder.append("\t\t<transition>\n");
                xmlBuilder.append("\t\t\t<from>").append(state.stateID).append("</from>\n");
                xmlBuilder.append("\t\t\t<to>").append(transition.toID).append("</to>\n");
                xmlBuilder.append("\t\t\t<read>").append(transition.symbol).append("</read>\n");
                xmlBuilder.append("\t\t</transition>\n");
            }
        }

        xmlBuilder.append("\t</automaton>\n");
        xmlBuilder.append("</structure>\n");

        return xmlBuilder.toString();
    }
}
