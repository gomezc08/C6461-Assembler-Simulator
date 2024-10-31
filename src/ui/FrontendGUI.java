package ui; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class FrontendGUI extends JFrame {
    private BitPanel[][] gprPanels, ixrPanels;
    private BitPanel[] pcPanels, marPanels, mbrPanels, irPanels, ccPanels, mfPanels;
    private JTextArea cacheContentArea, printerArea;
    private JButton loadButton, loadPlusButton, storeButton, storePlusButton, clearButton, initButton;
    private BackendGUI backend;

    public FrontendGUI() {
        setTitle("CSCI 6461 Machine Simulator");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize printer area for messages
        printerArea = new JTextArea(10, 30);
        printerArea.setEditable(false);
        printerArea.setBorder(BorderFactory.createTitledBorder("Printer"));

        // Initialize backend
        backend = new BackendGUI(printerArea);
        backend.setFrontendGUI(this);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(96, 139, 193)); // Set to light purple
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Left side (GPRs and IXRs)
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(new Color(96, 139, 193)); // Light purple
        gprPanels = createRegisterPanels(4, 16); // GPR 0-3 are 16 bits each
        ixrPanels = createRegisterPanels(3, 16); // IXR 0-2 are 16 bits each

        addRegisterPanels(leftPanel, gprPanels, "GPR", gbc);
        gbc.gridy += 4; // Move to avoid overlap with IXR fields
        addRegisterPanels(leftPanel, ixrPanels, "IXR", gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        mainPanel.add(leftPanel, gbc);

        // Right side (PC, MAR, MBR, IR, CC, MFR)
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(96, 139, 193)); // Light purple
        pcPanels = createPanels(12); // PC is 12 bits
        marPanels = createPanels(12); // MAR is 12 bits
        mbrPanels = createPanels(16); // MBR is 16 bits
        irPanels = createPanels(4);  // IR is 4 bits
        ccPanels = createPanels(4);  // CC is 4 bits
        mfPanels = createPanels(4);  // MFR is 4 bits

        addSingleRegisterPanels(rightPanel, pcPanels, "PC");
        addSingleRegisterPanels(rightPanel, marPanels, "MAR");
        addSingleRegisterPanels(rightPanel, mbrPanels, "MBR");
        addSingleRegisterPanels(rightPanel, irPanels, "IR");
        addSingleRegisterPanels(rightPanel, ccPanels, "CC");
        addSingleRegisterPanels(rightPanel, mfPanels, "MFR");

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
        lowerPanel.add(new JScrollPane(cacheContentArea));
        lowerPanel.add(new JScrollPane(printerArea));
        gbc.gridy = 2;
        gbc.weighty = 0.5;
        mainPanel.add(lowerPanel, gbc);

        initActionListeners();
        backend.updateCacheDisplay();
    }

    private void clearBits() {
        for (BitPanel[] panels : gprPanels) {
            for (BitPanel panel : panels) {
                panel.setBit(false); // Reset to 0 state
            }
        }
        for (BitPanel[] panels : ixrPanels) {
            for (BitPanel panel : panels) {
                panel.setBit(false); // Reset to 0 state
            }
        }
        for (BitPanel panel : pcPanels) {
            panel.setBit(false); // Reset to 0 state
        }
        for (BitPanel panel : marPanels) {
            panel.setBit(false); // Reset to 0 state
        }
        for (BitPanel panel : mbrPanels) {
            panel.setBit(false); // Reset to 0 state
        }
        for (BitPanel panel : irPanels) {
            panel.setBit(false); // Reset to 0 state
        }
        for (BitPanel panel : ccPanels) {
            panel.setBit(false); // Reset to 0 state
        }
        for (BitPanel panel : mfPanels) {
            panel.setBit(false); // Reset to 0 state
        }
    }


    // Create register panels instead of checkboxes
    private BitPanel[][] createRegisterPanels(int count, int bitsPerRegister) {
        BitPanel[][] panels = new BitPanel[count][bitsPerRegister];
        for (int i = 0; i < count; i++) {
            panels[i] = createPanels(bitsPerRegister);
        }
        return panels;
    }

    private BitPanel[] createPanels(int size) {
        BitPanel[] panels = new BitPanel[size];
        for (int i = 0; i < size; i++) {
            panels[i] = new BitPanel();
        }
        return panels;
    }

    private void addRegisterPanels(JPanel panel, BitPanel[][] panels, String labelPrefix, GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 5, 2, 5);
        for (int i = 0; i < panels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy += 1;
            panel.add(new JLabel(labelPrefix + " " + i + ":"), gbc);
            JPanel bitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            for (BitPanel panelBit : panels[i]) {
                bitPanel.add(panelBit);
            }
            gbc.gridx = 1;
            panel.add(bitPanel, gbc);

            final int index = i;

            // Add individual clear button for each register
            JButton clearButton = new JButton("Clear " + labelPrefix + " " + index);
            clearButton.addActionListener(e -> clearRegisterBits(panels[index]));
            gbc.gridx = 2;
            panel.add(clearButton, gbc);
        }
    }

    private void addSingleRegisterPanels(JPanel panel, BitPanel[] panels, String label) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;

        panel.add(new JLabel(label + ":"), gbc);
        JPanel bitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (BitPanel panelBit : panels) {
            bitPanel.add(panelBit);
        }
        gbc.gridx = 1;
        panel.add(bitPanel, gbc);

        JButton clearButton = new JButton("Clear " + label);
        clearButton.addActionListener(e -> clearRegisterBits(panels));
        gbc.gridx = 2;
        panel.add(clearButton, gbc);
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        loadButton = new JButton("Load");
        loadPlusButton = new JButton("Load+");
        storeButton = new JButton("Store");
        storePlusButton = new JButton("Store+");
        initButton = new JButton("Initialize");
        clearButton = new JButton("Clear All"); // Add Clear button

        controlPanel.add(loadButton);
        controlPanel.add(loadPlusButton);
        controlPanel.add(storeButton);
        controlPanel.add(storePlusButton);
        controlPanel.add(initButton);
        controlPanel.add(clearButton);
        return controlPanel;
    }

    // Custom JPanel representing a bit
    class BitPanel extends JPanel {
        private boolean isSet = false;

        public BitPanel() {
            setPreferredSize(new Dimension(20, 20));
            setBackground(new Color(203, 220, 235));
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    toggleBit();
                }
            });
        }

        public void setBit(boolean value) {
            isSet = value;
            setBackground(isSet ? Color.YELLOW : new Color(203, 220, 235));
        }

        public void toggleBit() {
            isSet = !isSet;
            setBackground(isSet ? Color.YELLOW : new Color(203, 220, 235));
        }

        public boolean isSet() {
            return isSet;
        }
    }

    private void initActionListeners() {
        // Load button action
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int marValue = getRegisterValue(marPanels);
                backend.loadValue(marValue);
                update(mbrPanels, backend.getMbrValue()); // Update MBR checkboxes
            }
        });

        // Store button action
        storeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int mbrValue = getRegisterValue(mbrPanels);
                int marValue = getRegisterValue(marPanels);
                backend.storeValue(marValue, mbrValue);
            }
        });

        // Store+ button action
        storePlusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int mbrValue = getRegisterValue(mbrPanels);
                int marValue = getRegisterValue(marPanels);
                backend.storePlusValue(marValue, mbrValue);
                
                // Update MAR checkbox after incrementing in backend
                int updatedMarValue = backend.getMarValue(); 
                update(marPanels, updatedMarValue); 
            }
        });

        // Load+ button action.
        loadPlusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement Load+ functionality if needed.
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearBits(); // Call the method to clear the panels
            }
        });

        // Initialize button action
        initButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                printerArea.append("Selected file: " + selectedFile.getName() + "\n");
                backend.loadExecuteRom(selectedFile);
            }
        });
    }

        // Method to clear bits in individual register
    private void clearRegisterBits(BitPanel[] panels) {
        for (BitPanel panel : panels) {
            panel.setBit(false); // Reset the bit to 0
        }
    }

    private int getRegisterValue(BitPanel[] bitPanels) {
        int value = 0;
        for (int i = 0; i < bitPanels.length; i++) {
            if (bitPanels[i].isSet()) {
                value |= (1 << (bitPanels.length - 1 - i));
            }
        }
        return value;
    }

    private void update(BitPanel[] bitPanels, int value) {
        for (int i = 0; i < bitPanels.length; i++) {
            boolean isSet = (value & (1 << (bitPanels.length - 1 - i))) != 0;
            bitPanels[i].setBit(isSet);
            bitPanels[i].setBackground(isSet ? Color.YELLOW : new Color(203, 220, 235));
        }
    }

    public void updateAllRegisters(short[] gprValues, short[] ixrValues, int pcValue, int marValue, int mbrValue, int[] ccValues, int[] mfrValues) {
        // Update GPR checkboxes.
        for (int i = 0; i < gprValues.length; i++) {
            update(gprPanels[i], gprValues[i]);
        }

        // Update IXR checkboxes.
        for (int i = 0; i < ixrValues.length; i++) {
            update(ixrPanels[i], ixrValues[i]);
        }

        // Update CC checkboxes manually since they are 4-bit values.
        for (int i = 0; i < ccValues.length; i++) {
            ccPanels[i].setBit(ccValues[i] == 1);
            ccPanels[i].setBackground(ccValues[i] == 1 ? Color.YELLOW : new Color(203, 220, 235)); // Update color
        }

        // Update MFR checkboxes manually if needed in the same way.
        for (int i = 0; i < mfrValues.length; i++) {
            mfPanels[i].setBit(mfrValues[i] == 1);
            mfPanels[i].setBackground(mfrValues[i] == 1 ? Color.YELLOW : new Color(203, 220, 235)); // Update color
        }

        // Update other register checkboxes
        update(pcPanels, pcValue);
        update(marPanels, marValue);
        update(mbrPanels, mbrValue);
    }

    public void updateCacheContent(String cacheContent) {
        cacheContentArea.setText(cacheContent);
    }    

    // Add a getter for backend
    public BackendGUI getBackend() {
        return backend;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FrontendGUI gui = new FrontendGUI();
            BackendGUI backend = gui.getBackend();
            backend.setFrontendGUI(gui);
            gui.setVisible(true);
        });
    }
}