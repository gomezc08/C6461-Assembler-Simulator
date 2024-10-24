package ui; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FrontendGUI extends JFrame {
    private JCheckBox[][] gprCheckBoxes, ixrCheckBoxes;
    private JCheckBox[] pcCheckBoxes, marCheckBoxes, mbrCheckBoxes, irCheckBoxes, ccCheckBoxes, mfCheckBoxes;
    private JTextArea cacheContentArea, printerArea;
    private JButton loadButton, loadPlusButton, storeButton, storePlusButton, initButton;
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

        // Right side (PC, MAR, MBR, IR, CC, MFR)
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
        checkBox.setBackground(Color.LIGHT_GRAY);
        checkBox.setOpaque(true);
        checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (checkBox.isSelected()) {
                    checkBox.setBackground(Color.YELLOW);
                } else {
                    checkBox.setBackground(Color.LIGHT_GRAY);
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
        
        // Add main label (PC:, MAR:, etc.)
        panel.add(new JLabel(label + ":"), gbc);
        
        // Special handling for CC and MFR
        if (label.equals("CC") || label.equals("MFR")) {
            JPanel bitPanel = new JPanel(new GridBagLayout());
            bitPanel.setBackground(Color.WHITE);  // White background
            GridBagConstraints bitGbc = new GridBagConstraints();
            bitGbc.anchor = GridBagConstraints.WEST;
            bitGbc.insets = new Insets(0, 0, 0, 0);
            
            // Get labels based on register type
            String[] labels = label.equals("CC") 
                ? new String[]{"O", "U", "D", "E"}
                : new String[]{"M", "OP", "TR", "R"};
            
            // Create container for labels and checkboxes
            JPanel checkboxGroup = new JPanel(new GridBagLayout());
            checkboxGroup.setBackground(Color.WHITE);
            
            // Add labels
            for (int i = 0; i < labels.length; i++) {
                JLabel bitLabel = new JLabel(labels[i]);
                bitLabel.setHorizontalAlignment(SwingConstants.CENTER);
                GridBagConstraints labelGbc = new GridBagConstraints();
                labelGbc.gridx = i;
                labelGbc.gridy = 0;
                labelGbc.insets = new Insets(0, 5, 0, 5);  // Add some horizontal spacing
                checkboxGroup.add(bitLabel, labelGbc);
            }
            
            // Add checkboxes
            for (int i = 0; i < checkBoxes.length; i++) {
                GridBagConstraints boxGbc = new GridBagConstraints();
                boxGbc.gridx = i;
                boxGbc.gridy = 1;
                boxGbc.insets = new Insets(0, 5, 0, 5);  // Match label spacing
                checkboxGroup.add(checkBoxes[i], boxGbc);
            }
            
            // Add padding to center the group in the white panel
            bitPanel.add(Box.createHorizontalStrut(50), bitGbc);  // Left padding
            bitGbc.gridx = 1;
            bitPanel.add(checkboxGroup, bitGbc);
            bitGbc.gridx = 2;
            bitPanel.add(Box.createHorizontalStrut(50), bitGbc);  // Right padding
            
            gbc.gridx = 1;
            panel.add(bitPanel, gbc);
        } else {
            // Original handling for other registers
            JPanel bitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            bitPanel.setBackground(Color.WHITE);
            for (JCheckBox checkBox : checkBoxes) {
                bitPanel.add(checkBox);
            }
            gbc.gridx = 1;
            panel.add(bitPanel, gbc);
        }
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
                backend.loadValue(marValue);
                updateCheckBoxes(marCheckBoxes, marValue);
                updateCheckBoxes(mbrCheckBoxes, backend.getMbrValue()); // Update MBR checkboxes
            }
        });

        // Store button action
        storeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int mbrValue = getRegisterValue(mbrCheckBoxes);
                int marValue = getRegisterValue(marCheckBoxes);
                backend.storeValue(marValue, mbrValue);
            }
        });

        // Store+ button action
        storePlusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int mbrValue = getRegisterValue(mbrCheckBoxes);
                int marValue = getRegisterValue(marCheckBoxes);
                backend.storePlusValue(marValue, mbrValue);
                
                // Update MAR checkbox after incrementing in backend
                int updatedMarValue = backend.getMarValue(); 
                updateCheckBoxes(marCheckBoxes, updatedMarValue); 
            }
        });

        // Load+ button action.
        loadPlusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement Load+ functionality if needed.
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

    public void updateAllRegisters(short[] gprValues, short[] ixrValues, int pcValue, int marValue, int mbrValue) {
        // Update GPR checkboxes
        for (int i = 0; i < gprValues.length; i++) {
            updateCheckBoxes(gprCheckBoxes[i], gprValues[i]);
        }

        // Update IXR checkboxes
        System.out.println("here are ixr length: " + ixrValues.length); 
        for (int i = 0; i < ixrValues.length; i++) {
            updateCheckBoxes(ixrCheckBoxes[i], ixrValues[i]);
        }
            

        // Update other register checkboxes
        updateCheckBoxes(pcCheckBoxes, pcValue);
        updateCheckBoxes(marCheckBoxes, marValue);
        updateCheckBoxes(mbrCheckBoxes, mbrValue);
        //updateCheckBoxes(ccCheckBoxes, ccValue);
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
