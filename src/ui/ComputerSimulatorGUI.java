package ui; // Ensure this matches your package structure

import javax.swing.*;
import components.Memory;
import components.MemoryAddressRegister;
import components.MemoryBufferRegister;
import components.CPU;
import components.GeneralPurposeRegisters;
import components.IndexRegisters;
import components.ProgramCounter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ComputerSimulatorGUI extends JFrame {
    private JCheckBox[][] gprCheckBoxes, ixrCheckBoxes;
    private JCheckBox[] pcCheckBoxes, marCheckBoxes, mbrCheckBoxes, irCheckBoxes, ccCheckBoxes, mfCheckBoxes;
    private JTextArea cacheContentArea, printerArea;
    private JButton loadButton, loadPlusButton, storeButton, storePlusButton, initButton;

    private Memory memory; // Memory object
    private MemoryAddressRegister mar; // Memory Address Register
    private MemoryBufferRegister mbr; // Memory Buffer Register
    private CPU cpu; // CPU object
    private GeneralPurposeRegisters gprs; // General Purpose Registers
    private IndexRegisters ixr; // Index Registers
    private ProgramCounter pc; // Program Counter

    public ComputerSimulatorGUI() {
        setTitle("CSCI 6461 Machine Simulator");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize memory, MAR, MBR, CPU, and registers
        memory = new Memory();
        mar = new MemoryAddressRegister();
        mbr = new MemoryBufferRegister();
        gprs = new GeneralPurposeRegisters(4); // Assuming 4 general-purpose registers
        ixr = new IndexRegisters(3); // Assuming 3 index registers
        pc = new ProgramCounter();
        cpu = new CPU(memory, mar, mbr, gprs, ixr, pc); // Initialize the CPU

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(221, 160, 221)); // Set to light purple
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Left side (GPRs and IXRs)
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(new Color(221, 160, 221)); // Light purple
        gprCheckBoxes = createRegisterCheckBoxes(4, 16); // GPR 0-3 are 16 bits each
        ixrCheckBoxes = createRegisterCheckBoxes(3, 16); // IXR 0-2 are 16 bits each

        addRegisterCheckBoxes(leftPanel, gprCheckBoxes, "GPR", gbc);
        gbc.gridy += 4; // Move to avoid overlap with IXR fields
        addRegisterCheckBoxes(leftPanel, ixrCheckBoxes, "IXR", gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        mainPanel.add(leftPanel, gbc);

        // Right side (PC, MAR, MBR, IR, CC, MFR, Privileged)
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(221, 160, 221)); // Light purple
        pcCheckBoxes = createCheckBoxes(12); // PC is 12 bits
        marCheckBoxes = createCheckBoxes(12); // MAR is 12 bits
        mbrCheckBoxes = createCheckBoxes(16); // MBR is 16 bits
        irCheckBoxes = createCheckBoxes(4); // IR is 4 bits
        ccCheckBoxes = createCheckBoxes(4); // CC is 4 bits
        mfCheckBoxes = createCheckBoxes(4); // MFR is 4 bits

        addSingleRegisterCheckBoxes(rightPanel, pcCheckBoxes, "PC");
        addSingleRegisterCheckBoxes(rightPanel, marCheckBoxes, "MAR");
        addSingleRegisterCheckBoxes(rightPanel, mbrCheckBoxes, "MBR");
        addSingleRegisterCheckBoxes(rightPanel, irCheckBoxes, "IR");
        addSingleRegisterCheckBoxes(rightPanel, ccCheckBoxes, "CC");
        addSingleRegisterCheckBoxes(rightPanel, mfCheckBoxes, "MFR");

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        mainPanel.add(rightPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Control buttons
        JPanel controlPanel = createControlPanel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(controlPanel, gbc);

        // Cache Content and Printer text areas
        JPanel lowerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        cacheContentArea = new JTextArea(10, 30);
        cacheContentArea.setEditable(false);
        cacheContentArea.setBorder(BorderFactory.createTitledBorder("Cache Content"));
        printerArea = new JTextArea(10, 30);
        printerArea.setEditable(false);
        printerArea.setBorder(BorderFactory.createTitledBorder("Printer"));
        lowerPanel.add(new JScrollPane(cacheContentArea));
        lowerPanel.add(new JScrollPane(printerArea));
        gbc.gridy = 2;
        gbc.weighty = 0.5;
        mainPanel.add(lowerPanel, gbc);

        initActionListeners();
    }

    private JCheckBox[][] createRegisterCheckBoxes(int count, int bitsPerRegister) {
        JCheckBox[][] checkBoxes = new JCheckBox[count][bitsPerRegister];
        for (int i = 0; i < count; i++) {
            checkBoxes[i] = createCheckBoxes(bitsPerRegister);
        }
        return checkBoxes;
    }

    private JCheckBox[] createCheckBoxes(int size) {
        JCheckBox[] checkBoxes = new JCheckBox[size];
        for (int i = 0; i < size; i++) {
            checkBoxes[i] = createCheckBox();
        }
        return checkBoxes;
    }

    private JCheckBox createCheckBox() {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setBackground(Color.LIGHT_GRAY); // Initial background color representing 0
        checkBox.setOpaque(true);
        checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (checkBox.isSelected()) {
                    checkBox.setBackground(Color.YELLOW); // Checked, represents 1
                } else {
                    checkBox.setBackground(Color.LIGHT_GRAY); // Unchecked, represents 0
                }
            }
        });
        return checkBox;
    }

    private void addRegisterCheckBoxes(JPanel panel, JCheckBox[][] checkBoxes, String labelPrefix, GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 5, 2, 5);
        for (int i = 0; i < checkBoxes.length; i++) {
            gbc.gridx = 0;
            gbc.gridy += 1;
            panel.add(new JLabel(labelPrefix + " " + i + ":"), gbc);
            JPanel bitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            for (JCheckBox checkBox : checkBoxes[i]) {
                bitPanel.add(checkBox);
            }
            gbc.gridx = 1;
            panel.add(bitPanel, gbc);
        }
    }

    private void addSingleRegisterCheckBoxes(JPanel panel, JCheckBox[] checkBoxes, String label) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        panel.add(new JLabel(label + ":"), gbc);
        JPanel bitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        for (JCheckBox checkBox : checkBoxes) {
            bitPanel.add(checkBox);
        }
        gbc.gridx = 1;
        panel.add(bitPanel, gbc);
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        loadButton = new JButton("Load");
        loadPlusButton = new JButton("Load+");
        storeButton = new JButton("Store");
        storePlusButton = new JButton("Store+");
        initButton = new JButton("Initialize");

        controlPanel.add(loadButton);
        controlPanel.add(loadPlusButton);
        controlPanel.add(storeButton);
        controlPanel.add(storePlusButton);
        controlPanel.add(initButton);

        return controlPanel;
    }

    private void initActionListeners() {
        // Load button action
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int marValue = getRegisterValue(marCheckBoxes);
                mar.setValue((short) marValue);
                int memoryValue = memory.loadMemoryValue(marValue);
                mbr.setValue((short) memoryValue);

                // CASE 1: Access valid memory.
                if (marValue > 5) {
                    updateCheckBoxes(mbrCheckBoxes, memoryValue);
                    printerArea.append("Loaded value " + memoryValue + " from memory address " + marValue + " into MBR\n");
                }
                // CASE 2: Access invalid memory.
                else {
                    printerArea.append("Memory location " + marValue + " is a reserved memory location.\n");
                }
            }
        });

        // Store button action
        storeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int mbrValue = getRegisterValue(mbrCheckBoxes);
                mbr.setValue((short) mbrValue);
                int marValue = getRegisterValue(marCheckBoxes);
                mar.setValue((short) marValue);

                // CASE 1: Access valid memory.
                if (marValue > 5) {
                    memory.storeValue(marValue, mbrValue);
                    printerArea.append("Stored value " + mbrValue + " from MBR into memory address " + marValue + "\n");
                }
                // CASE 2: Access invalid memory.
                else {
                    printerArea.append("Memory location " + marValue + " is a reserved memory location.\n");
                }
            }
        });

        // Store+ button action
        storePlusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int mbrValue = getRegisterValue(mbrCheckBoxes);
                mbr.setValue((short) mbrValue);
                int marValue = getRegisterValue(marCheckBoxes);
                mar.setValue((short) marValue);

                // CASE 1: Access valid memory.
                if (marValue > 5) {
                    memory.storeValue(marValue, mbrValue);
                    mar.increment();
                    updateCheckBoxes(marCheckBoxes, mar.getValue());
                    printerArea.append("Stored value " + mbrValue + " from MBR into memory address " + marValue + " and incremented MAR to " + mar.getValue() + "\n");
                }
                // CASE 2: Access invalid memory.
                else {
                    printerArea.append("Memory location " + marValue + " is a reserved memory location.\n");
                }
            }
        });

        // Load+ button action
        loadPlusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement Load+ functionality if needed
            }
        });

        // Initialize button action
        initButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                printerArea.append("Selected file: " + selectedFile.getName() + "\n");
                try {
                    cpu.loadROMFile(selectedFile);
                    printerArea.append("Loaded ROM file successfully\n");
                } 
                
                catch (IOException ex) {
                    printerArea.append("Error loading ROM file: " + ex.getMessage() + "\n");
                }
            }
        });
    }

    private int getRegisterValue(JCheckBox[] checkBoxes) {
        int value = 0;
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isSelected()) {
                value |= (1 << (checkBoxes.length - 1 - i));
            }
        }
        return value;
    }

    private void updateCheckBoxes(JCheckBox[] checkBoxes, int value) {
        for (int i = 0; i < checkBoxes.length; i++) {
            boolean isSet = (value & (1 << (checkBoxes.length - 1 - i))) != 0;
            checkBoxes[i].setSelected(isSet);
            checkBoxes[i].setBackground(isSet ? Color.YELLOW : Color.LIGHT_GRAY);
        }
    }

    private void resetRegisters() {
        // Reset all registers and memory states
        for (JCheckBox[] gpr : gprCheckBoxes) {
            for (JCheckBox checkBox : gpr) {
                checkBox.setSelected(false);
                checkBox.setBackground(Color.LIGHT_GRAY);
            }
        }

        for (JCheckBox[] ixr : ixrCheckBoxes) {
            for (JCheckBox checkBox : ixr) {
                checkBox.setSelected(false);
                checkBox.setBackground(Color.LIGHT_GRAY);
            }
        }

        JCheckBox[][] allRegisters = {pcCheckBoxes, marCheckBoxes, mbrCheckBoxes, irCheckBoxes, ccCheckBoxes, mfCheckBoxes};
        for (JCheckBox[] register : allRegisters) {
            for (JCheckBox checkBox : register) {
                checkBox.setSelected(false);
                checkBox.setBackground(Color.LIGHT_GRAY);
            }
        }

        memory.resetMemory(); // Clear memory
        printerArea.setText(""); // Clear printer output
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ComputerSimulatorGUI gui = new ComputerSimulatorGUI();
            gui.setVisible(true);
        });
    }
}
