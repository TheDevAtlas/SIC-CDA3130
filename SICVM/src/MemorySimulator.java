import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.TreeMap;

public class MemorySimulator extends JFrame {
    private JTextArea memoryDisplay, registerDisplay, microStepLogDisplay;
    private TreeMap<Integer, String> memory = new TreeMap<>();
    private JButton stepButton, microstepButton, loadFileButton, toggleDisplayButton;
    private boolean displayInHex = true;  // Default display mode is hexadecimal
    private JTextArea operationLogDisplay;
    private int currentAddress = 0;
    private int currentLine = 0; // To keep track of the current line
    private TreeMap<Integer, Pair<String, Long>> cache = new TreeMap<>();
    private static int CACHE_SIZE = 5;
    private JTextArea cacheDisplay;
    private int[] registers = new int[32];
    private int pc = 0;
    private int hi = 0;
    private int lo = 0;

    static class Pair<K, V> {
        K first;
        V second;

        public Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }
    }

    public MemorySimulator() {
        super("Memory Simulator");
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new GridLayout(2, 2));


        memoryDisplay = new JTextArea();
        memoryDisplay.setEditable(false);
        add(new JScrollPane(memoryDisplay));

        operationLogDisplay = new JTextArea();
        operationLogDisplay.setEditable(false);
        add(new JScrollPane(operationLogDisplay));

        registerDisplay = new JTextArea();
        registerDisplay.setEditable(false);
        add(new JScrollPane(registerDisplay));

        cacheDisplay = new JTextArea();
        cacheDisplay.setEditable(false);
        add(new JScrollPane(cacheDisplay));

        microStepLogDisplay = new JTextArea();
        microStepLogDisplay.setEditable(false);
        add(new JScrollPane(microStepLogDisplay));

        JPanel buttonPanel = new JPanel();
        stepButton = new JButton("Step");
        microstepButton = new JButton("Microstep");
        loadFileButton = new JButton("Load File");
        toggleDisplayButton = new JButton("Toggle Display (Hex/Bin)");

        buttonPanel.add(loadFileButton);
        buttonPanel.add(stepButton);
        buttonPanel.add(microstepButton);
        buttonPanel.add(toggleDisplayButton);
        add(buttonPanel, BorderLayout.SOUTH);

        stepButton.addActionListener(this::step);
        microstepButton.addActionListener(this::microstep);
        loadFileButton.addActionListener(this::openFileChooser);
        toggleDisplayButton.addActionListener(this::toggleDisplayMode);
    }

    private String getFromCacheOrMemory(int address) {
        Pair<String, Long> cachedValue = cache.get(address);
        if (cachedValue != null) {
            cachedValue.second = System.nanoTime();
            return cachedValue.first;
        } else {
            String memoryValue = memory.get(address);
            insertIntoCache(address, memoryValue);
            return memoryValue;
        }
    }

    private void insertIntoCache(int address, String value) {
        if (cache.size() >= CACHE_SIZE) {
            Integer lruKey = getLeastRecentlyUsedKey();
            cache.remove(lruKey);
        }
        cache.put(address, new Pair<>(value, System.nanoTime()));
        updateCacheDisplay();
    }

    private Integer getLeastRecentlyUsedKey() {
        Pair<Integer, Long> lru = null;
        for (var entry : cache.entrySet()) {
            if (lru == null || entry.getValue().second < lru.second) {
                lru = new Pair<>(entry.getKey(), entry.getValue().second);
            }
        }
        return lru != null ? lru.first : null;
    }

    private void updateCacheDisplay() {
        StringBuilder builder = new StringBuilder();
        cache.forEach((address, cachedValue) -> {
            String formattedValue = displayInHex ? cachedValue.first : new BigInteger(cachedValue.first, 16).toString(2);
            builder.append(String.format("0x%08X: %s\n", address, formattedValue));
        });
        cacheDisplay.setText(builder.toString());
    }

    private void openFileChooser(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Memory File");
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            loadMemoryFromFile(selectedFile.getAbsolutePath());
            updateMemoryDisplay();
        }
    }
    private void updateRegisterDisplay() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < registers.length; i++) {
            builder.append(String.format("R%d: %d\n", i, registers[i]));
        }
        builder.append(String.format("HI: %d\n", hi));
        builder.append(String.format("LO: %d\n", lo));
        registerDisplay.setText(builder.toString());
    }

    private void loadMemoryFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            memory.clear();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(": ");
                int address = Integer.parseInt(parts[0].substring(2), 16);
                memory.put(address, parts[1].trim());
            }
            if (!memory.isEmpty()) {
                currentAddress = memory.firstKey();
            }
            pc = currentAddress;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMemoryDisplay() {
        StringBuilder builder = new StringBuilder();
        final int[] lineCounter = {0};
        memory.forEach((address, value) -> {
            String formattedValue = displayInHex ? value : new BigInteger(value, 16).toString(2);
            if (lineCounter[0] == currentLine) {
                // If it's in cache, then add "(in cache)" next to the address
                builder.append(String.format("-> 0x%08X (%s): %s\n", address, cache.containsKey(address) ? "in cache" : "", formattedValue)); // Mark the current line
            } else {
                builder.append(String.format("   0x%08X (%s): %s\n", address, cache.containsKey(address) ? "in cache" : "", formattedValue));
            }
            lineCounter[0]++; // Increment the counter within the array
        });
        memoryDisplay.setText(builder.toString());
    }



    private void toggleDisplayMode(ActionEvent e) {
        displayInHex = !displayInHex;
        updateMemoryDisplay();
        toggleDisplayButton.setText(displayInHex ? "Toggle Display (Hex)" : "Toggle Display (Bin)");
        updateCacheDisplay();
    }

    private void step(ActionEvent e) {
        if (memory.containsKey(currentAddress)) {
            String binaryString = getFromCacheOrMemory(currentAddress);
            executeOperation(binaryString);
            pc += 4;
            currentAddress = pc;
            Integer nextAddress = memory.higherKey(currentAddress);
//            if (nextAddress != null) {
//                currentAddress = nextAddress;
//            } else {
//                JOptionPane.showMessageDialog(this, "Reached the end of memory.", "Step Info", JOptionPane.INFORMATION_MESSAGE);
//            }
        } else {
            JOptionPane.showMessageDialog(this, "End of program or invalid address", "Step Info", JOptionPane.ERROR_MESSAGE);
        }
        currentLine = memory.headMap(currentAddress).size();
        updateMemoryDisplay();
        updateRegisterDisplay();
    }

    private enum MicrostepState {
        IF, ID, EX, MEM, WB, DONE
    }

    private MicrostepState currentMicrostep = MicrostepState.IF;
    private String currentBitstring;
    private String nextOpcode;

    private void microstep(ActionEvent e) {
        if (memory.containsKey(currentAddress)) {
            switch (currentMicrostep) {
                case IF:
                    currentBitstring = getFromCacheOrMemory(currentAddress);
                    microStepLogDisplay.append("Instruction Fetch operation at address: " + currentAddress + " with data: " + currentBitstring + "\n");
                    nextOpcode = currentBitstring.substring(0, 6);
                    currentMicrostep = MicrostepState.ID;
                    break;
                case ID:
                    microStepLogDisplay.append("Instruction Decode operation at address: " + currentAddress + " with data: " + currentBitstring + "\n");
                    currentMicrostep = MicrostepState.EX;
                    break;
                case EX:
                    microStepLogDisplay.append("Execute operation at address: " + currentAddress + " with data: " + currentBitstring + "\n");
                    executeOperation(currentBitstring);
                    pc += 4;
                    currentAddress = pc;
                    currentMicrostep = MicrostepState.MEM;
                    break;
                case MEM:
                    if (nextOpcode.equals("100011") || nextOpcode.equals("101011")) {
                        microStepLogDisplay.append("Memory Access operation at address: " + currentAddress + " with data: " + currentBitstring + "\n");
                    }
                    currentMicrostep = MicrostepState.WB;
                    break;
                case WB:
                    microStepLogDisplay.append("Write Back operation at address: " + currentAddress + " with data: " + currentBitstring + "\n");
                    currentMicrostep = MicrostepState.DONE;
                    break;
                case DONE:
                    Integer nextAddress = memory.higherKey(currentAddress);
                    if (nextAddress != null) {
                        currentAddress = nextAddress;
                    } else {
                        JOptionPane.showMessageDialog(this, "Reached end of memory.", "Microstep Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                    currentMicrostep = MicrostepState.IF;
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(this, "End of memory or invalid address", "Microstep Info", JOptionPane.ERROR_MESSAGE);
        }
        currentLine = memory.headMap(currentAddress).size();
        updateMemoryDisplay();
        updateRegisterDisplay();
        updateCacheDisplay();
    }

    // take in binary data and return what the operation is and the registers that are being used
    private String getOperation(String binaryData) {
        String opcode = binaryData.substring(0, 6);
        int rs = Integer.parseInt(binaryData.substring(6, 11), 2);
        int rt = Integer.parseInt(binaryData.substring(11, 16), 2);
        int rd = Integer.parseInt(binaryData.substring(16, 21), 2);
        int shamt = Integer.parseInt(binaryData.substring(21, 26), 2);
        int immediate = Integer.parseInt(binaryData.substring(16, 32), 2);
        String funct = binaryData.substring(26, 32);
        int signExtendedImmediate = immediate << 16 >> 16; // Sign extension for immediate

        switch (opcode) {
            case "000000": // R-type operations
                switch (funct) {
                    case "100101": // OR
                        return "OR R" + rd + ", R" + rs + ", R" + rt;
                    case "100100": // AND
                        return "AND R" + rd + ", R" + rs + ", R" + rt;
                    case "100000": // ADD
                        return "ADD R" + rd + ", R" + rs + ", R" + rt;
                    case "100010": // SUB
                        return "SUB R" + rd + ", R" + rs + ", R" + rt;
                    case "100110": // XOR
                        return "XOR R" + rd + ", R" + rs + ", R" + rt;
                    case "011010": // DIV
                        return "DIV R" + rs + ", R" + rt;
                    case "011011": // DIVU
                        return "DIVU R" + rs + ", R" + rt;
                    case "011000": // MULT
                        return "MULT R" + rs + ", R" + rt;
                    case "011001": // MULTU
                        return "MULTU R" + rs + ", R" + rt;
                    case "010000": // MFHI
                        return "MFHI R" + rd;
                    case "010010": // MFLO
                        return "MFLO R" + rd;
                    case "100111": // NOR
                        return "NOR R" + rd + ", R" + rs;
                    default:
                        return "Unknown R-type operation";
                }
            case "001000": // ADDI
                return "ADDI R" + rt + ", R" + rs + ", " + signExtendedImmediate;
            case "001010": // SLTI
                return "SLTI R" + rt + ", R" + rs + ", " + signExtendedImmediate;
            case "101011": // STORE WORD (SW)
                return "SW R" + rt + ", " + immediate + "(R" + rs + ")";
            case "100011": // LOAD WORD (LW)
                return "LW R" + rt + ", " + immediate + "(R" + rs + ")";
            case "100000": // LB
                return "LB R" + rt + ", " + signExtendedImmediate + "(R" + rs + ")";
            case "100001": // LH
                return "LH R" + rt + ", " + signExtendedImmediate + "(R" + rs + ")";
            case "000100": // BEQ
                return "BEQ R" + rs + ", R" + rt + ", " + immediate;
            case "000101": // BNE
                return "BNE R" + rs + ", R" + rt + ", " + immediate;
            case "000010": // JUMP (J)
                int address = (Integer.parseInt(binaryData.substring(6), 2) << 2);
                String hexAddress = "0x" + Integer.toHexString(address);
                return "JUMP TO " + hexAddress;
            case "000011": // JAL (Jump and Link)
                return "JAL " + Integer.parseInt(binaryData.substring(6), 2);
            default:
                return "Unknown operation";
        }
    }


    private void executeOperation(String binaryData) {
        String opcode = binaryData.substring(0, 6);
        int rs = Integer.parseInt(binaryData.substring(6, 11), 2);
        int rt = Integer.parseInt(binaryData.substring(11, 16), 2);
        int rd = Integer.parseInt(binaryData.substring(16, 21), 2);
        int shamt = Integer.parseInt(binaryData.substring(21, 26), 2);
        int immediate = Integer.parseInt(binaryData.substring(16, 32), 2);
        String funct = binaryData.substring(26, 32);
        int signExtendedImmediate = immediate << 16 >> 16; // Sign extension for immediate

        switch (opcode) {
            case "000000": // R-type operations
                switch (funct) {
                    case "100101": // OR
                        registers[rd] = registers[rs] | registers[rt];
                        operationLogDisplay.append("Executing OR operation: " + getOperation(binaryData) + "\n");
                        break;
                    case "100100": // AND
                        registers[rd] = registers[rs] & registers[rt];
                        operationLogDisplay.append("Executing AND operation: " + getOperation(binaryData) + "\n");
                        break;
                    case "100000": // ADD
                        registers[rd] = registers[rs] + registers[rt];
                        operationLogDisplay.append("Executing ADD operation: " + getOperation(binaryData) + "\n");
                        break;
                    case "100010": // SUB
                        registers[rd] = registers[rs] - registers[rt];
                        operationLogDisplay.append("Executing SUB operation: " + getOperation(binaryData) + "\n");
                        break;
                    case "100110": // XOR
                        registers[rd] = registers[rs] ^ registers[rt];
                        operationLogDisplay.append("Executing XOR operation: " + getOperation(binaryData) + "\n");
                        break;
                    case "011010": // DIV
                        // store in hi and lo
                        if (registers[rt] != 0) { // Prevent division by zero
                            lo = registers[rs] / registers[rt]; // Quotient
                            hi = registers[rs] % registers[rt]; // Remainder
                            operationLogDisplay.append("Executing DIV operation: " + getOperation(binaryData) + "\n");
                        } else {
                            operationLogDisplay.append("Division by zero error in DIV operation: " + getOperation(binaryData) + "\n");
                        }
                        break;
                    case "011011": // DIVU
                        if (registers[rt] != 0) {
                            lo = Integer.divideUnsigned(registers[rs], registers[rt]); // Unsigned quotient
                            hi = Integer.remainderUnsigned(registers[rs], registers[rt]); // Unsigned remainder
                            operationLogDisplay.append("Executing DIVU operation: " + getOperation(binaryData) + "\n");
                        } else {
                            operationLogDisplay.append("Division by zero error in DIVU operation: " + getOperation(binaryData) + "\n");
                        }
                        break;
                    case "011000": // MULT
                        long result = (long) registers[rs] * (long) registers[rt];
                        lo = (int) result; // Lower 32 bits
                        hi = (int) (result >> 32); // Upper 32 bits
                        operationLogDisplay.append("Executing MULT operation: " + getOperation(binaryData) + "\n");
                        break;
                    case "011001": // MULTU
                        registers[rd] = registers[rs] * registers[rt];
                        operationLogDisplay.append("Executing MUL operation: " + getOperation(binaryData) + "\n");
                        break;
                    case "010000": // MFHI
                        registers[rd] = hi;
                        operationLogDisplay.append("Executing MFHI operation: " + getOperation(binaryData) + "\n");
                        break;
                    case "010010": // MFLO
                        registers[rd] = lo;
                        operationLogDisplay.append("Executing MFLO operation: " + getOperation(binaryData) + "\n");
                        break;
                    case "100111": // NOR
                        registers[rd] = ~registers[rs];
                        operationLogDisplay.append("Executing NOT operation: " + getOperation(binaryData) + "\n");
                        break;
                    default:
                        operationLogDisplay.append("Unknown R-type operation: " + binaryData + "\n");
                        break;
                }
                break;
            case "001000": // ADDI
                registers[rt] = registers[rs] + signExtendedImmediate;
                operationLogDisplay.append("Executing ADDI operation: " + getOperation(binaryData) + "\n");
                break;
            case "001010": // SLTI
                registers[rt] = (registers[rs] < signExtendedImmediate) ? 1 : 0;
                operationLogDisplay.append("Executing SLTI operation: " + getOperation(binaryData) + "\n");
                break;
            case "101011": // STORE WORD (SW)
                memory.put(registers[rs] + immediate, Integer.toString(registers[rt]));
                operationLogDisplay.append("Executing STORE operation: " + getOperation(binaryData) + "\n");
                break;
            case "100011": // LOAD WORD (LW)
                registers[rt] = Integer.parseInt(memory.getOrDefault(registers[rs] + immediate, "0"));
                operationLogDisplay.append("Executing LOAD operation: " + getOperation(binaryData) + "\n");
                break;
            case "100000": // LB
                registers[rt] = Integer.parseInt(memory.getOrDefault(registers[rs] + signExtendedImmediate, "0"));
                operationLogDisplay.append("Executing LB operation: " + getOperation(binaryData) + "\n");
                break;
            case "100001": // LH
                registers[rt] = Integer.parseInt(memory.getOrDefault(registers[rs] + signExtendedImmediate, "0"));
                operationLogDisplay.append("Executing LH operation: " + getOperation(binaryData) + "\n");
                break;
            case "000100": // BEQ
                if (registers[rs] == registers[rt]) {
                    pc += 4 + (immediate << 2) - 4;
                }
                operationLogDisplay.append("Executing BEQ operation: " + getOperation(binaryData) + "\n");
                break;
            case "000101": // BNE
                if (registers[rs] != registers[rt]) {
                    pc += 4 + (immediate << 2) - 4;
                }
                operationLogDisplay.append("Executing BNE operation: " + getOperation(binaryData) + "\n");
                break;
            case "000010": // JUMP (J)
                pc = (Integer.parseInt(binaryData.substring(6), 2) << 2) - 4;
                operationLogDisplay.append("Executing JUMP operation: " + getOperation(binaryData) + "\n");
                break;
            case "000011": // JAL (Jump and Link)
                registers[31] = pc + 8;
                pc = (pc & 0xF0000000) | ((Integer.parseInt(binaryData.substring(6), 2) << 2)) - 4;
                operationLogDisplay.append("Executing JAL operation: " + getOperation(binaryData) + "\n");
                break;
            default:
                operationLogDisplay.append("Unknown operation: " + binaryData + "\n");
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemorySimulator().setVisible(true));
    }
}