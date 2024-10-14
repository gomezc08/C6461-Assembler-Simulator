package ui;

import components.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ComputerSimulatorGUI extends JFrame {
    private JTextField[] gprFields, ixrFields;
    private JTextField pcField, marField, mbrField, irField, ccField, mfField;
    private JTextArea cacheContentArea, printerArea;
    private JButton loadButton, loadPlusButton, storeButton, storePlusButton, initButton;
    private Memory memory;

    public ComputerSimulatorGUI() {
        setTitle("CSCI 6461 Machine Simulator");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        memory = new Memory(); // Initialize memory

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(221, 160, 221)); // Set to light purple
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Left side (GPRs and IXRs)
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(new Color(221, 160, 221)); // Light purple
        gprFields = createRegisterFields(4, "GPR");
        ixrFields = createRegisterFields(3, "IXR");

        // Adding GPR and IXR fields with proper vertical spacing
        addRegisterFields(leftPanel, gprFields, "GPR", gbc);
        gbc.gridy += 4; // Increase the row index to avoid overlap with IXR fields
        addRegisterFields(leftPanel, ixrFields, "IXR", gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        mainPanel.add(leftPanel, gbc);

        // Right side (PC, MAR, MBR, IR, CC, MFR, Privileged)
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(221, 160, 221)); // Light purple
        pcField = createTextField();
        marField = createTextField();
        mbrField = createTextField();
        irField = createTextField();
        ccField = createTextField();
        mfField = createTextField();
        addSingleRegisterField(rightPanel, pcField, "PC");
        addSingleRegisterField(rightPanel, marField, "MAR");
        addSingleRegisterField(rightPanel, mbrField, "MBR");
        addSingleRegisterField(rightPanel, irField, "IR");
        addSingleRegisterField(rightPanel, ccField, "CC");
        addSingleRegisterField(rightPanel, mfField, "MFR");
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        mainPanel.add(rightPanel, gbc);

        // Control buttons
        JPanel controlPanel = createControlPanel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(controlPanel, gbc);

        // Cache Content and Printer text areas (below control buttons)
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

        add(mainPanel, BorderLayout.CENTER);

        // Adding action listeners
        initActionListeners();
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 30));
        return field;
    }

    private JTextField[] createRegisterFields(int count, String prefix) {
        JTextField[] fields = new JTextField[count];
        for (int i = 0; i < count; i++) {
            fields[i] = createTextField();
        }
        return fields;
    }

    private void addRegisterFields(JPanel panel, JTextField[] fields, String labelPrefix, GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 5, 2, 5);
        for (int i = 0; i < fields.length; i++) {
            gbc.gridx = 0;
            gbc.gridy += 1;
            panel.add(new JLabel(labelPrefix + " " + i + ":"), gbc);
            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }
    }

    private void addSingleRegisterField(JPanel panel, JTextField field, String label) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        panel.add(new JLabel(label + ":"), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(221, 160, 221)); // Light purple
        loadButton = new JButton("Load");
        loadPlusButton = new JButton("Load+");
        storeButton = new JButton("Store");
        storePlusButton = new JButton("Store+");
        initButton = new JButton("Init");
        panel.add(loadButton);
        panel.add(loadPlusButton);
        panel.add(storeButton);
        panel.add(storePlusButton);
        panel.add(initButton);
        return panel;
    }

    private void initActionListeners() {
        // Load button logic: load the value from memory at address MAR and display it in MBR
        loadButton.addActionListener(e -> {
            int marValue = Integer.parseInt(marField.getText());
            int value = memory.loadMemoryValue(marValue); // Retrieve unsigned value
            //int val2 = value & 0xFFFF;
            mbrField.setText(String.valueOf(value));  // Display as unsigned
            System.out.println("Loaded value " + value + " from memory address " + marValue);
        });
    
        // Store button logic: store the value in MBR at address MAR in memory
        storeButton.addActionListener(e -> {
            int marValue = Integer.parseInt(marField.getText());
            short mbrValue = (short) Integer.parseInt(mbrField.getText());
            memory.storeValue(marValue, mbrValue);
            System.out.println("Stored value " + mbrValue + " at memory address " + marValue);
        });
    
        // Store+ button logic: store the value in MBR at address MAR, then increment MAR
        storePlusButton.addActionListener(e -> {
            int marValue = Integer.parseInt(marField.getText());
            short mbrValue = (short) Integer.parseInt(mbrField.getText());
            memory.storeValue(marValue, mbrValue);
            System.out.println("Stored value " + mbrValue + " at memory address " + marValue);
            marValue++;
            marField.setText(String.valueOf(marValue));
        });
    
        // Init button logic: open native file explorer to load ROM
        initButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                try {
                    loadROMFile(selectedFile);
                } catch (IOException ex) {
                    System.out.println("Error loading ROM file: " + ex.getMessage());
                }
            }
        });
    }

    private void loadROMFile(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        for (String line : lines) {
            String[] parts = line.split("\\s+");
            if (parts.length == 2) {
                int octalAddress = Integer.parseInt(parts[0], 8);
                int octalValue = Integer.parseInt(parts[1], 8); // here is my error.
                memory.storeValue(octalAddress, octalValue);
                System.out.println("Loaded value " + octalValue + " into memory address " + octalAddress);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ComputerSimulatorGUI gui = new ComputerSimulatorGUI();
            gui.setVisible(true);
        });
    }
}
