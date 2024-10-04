package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class MachineSimulatorUI extends JFrame {

    private JTextField[] gprFields, ixrFields;
    private JTextField pcField, marField, mbrField, irField, ccField, mfrField;
    private JTextArea cacheContentArea, printerArea;
    private JTextField binaryField, octalInputField, programFileField, consoleInputField;

    public MachineSimulatorUI() {
        setTitle("CSCI 6461 Machine Simulator");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(173, 216, 230));

        setLayout(new BorderLayout());

        // Initialize components
        gprFields = new JTextField[4];
        ixrFields = new JTextField[3];
        for (int i = 0; i < 4; i++) gprFields[i] = new JTextField(10);
        for (int i = 0; i < 3; i++) ixrFields[i] = new JTextField(10);

        pcField = new JTextField(10);
        marField = new JTextField(10);
        mbrField = new JTextField(10);
        irField = new JTextField(10);
        ccField = new JTextField(5);
        mfrField = new JTextField(5);
        binaryField = new JTextField(20);
        octalInputField = new JTextField(5);
        programFileField = new JTextField(30);
        consoleInputField = new JTextField(30);

        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(173, 216, 230));
        GridBagConstraints gbc = new GridBagConstraints();

        // GPR Panel
        JPanel gprPanel = createLabeledPanel("GPR", gprFields, 4);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 1; gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(gprPanel, gbc);

        // IXR Panel
        JPanel ixrPanel = createLabeledPanel("IXR", ixrFields, 3);
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.gridwidth = 1; gbc.gridheight = 2;
        mainPanel.add(ixrPanel, gbc);

        // Control Registers Panel
        JPanel controlPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        controlPanel.setBackground(new Color(173, 216, 230));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Control Registers"));
        String[] controlLabels = {"PC", "MAR", "MBR", "IR"};
        JTextField[] controlFields = {pcField, marField, mbrField, irField};
        for (int i = 0; i < controlLabels.length; i++) {
            controlPanel.add(new JLabel(controlLabels[i]));
            controlPanel.add(controlFields[i]);
        }
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        mainPanel.add(controlPanel, gbc);

        // CC and MFR Panel
        JPanel ccMfrPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        ccMfrPanel.setBackground(new Color(173, 216, 230));
        ccMfrPanel.setBorder(BorderFactory.createTitledBorder("Status"));
        ccMfrPanel.add(new JLabel("CC"));
        ccMfrPanel.add(ccField);
        ccMfrPanel.add(new JLabel("MFR"));
        ccMfrPanel.add(mfrField);
        gbc.gridx = 2; gbc.gridy = 1;
        mainPanel.add(ccMfrPanel, gbc);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBackground(new Color(173, 216, 230));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input"));
        inputPanel.add(new JLabel("Binary"));
        inputPanel.add(binaryField);
        inputPanel.add(new JLabel("Octal Input"));
        inputPanel.add(octalInputField);
        inputPanel.add(new JLabel("Program File"));
        inputPanel.add(programFileField);
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 3;
        mainPanel.add(inputPanel, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        buttonPanel.setBackground(new Color(173, 216, 230));
        String[] buttonLabels = {"Load", "Load+", "Store", "Store+", "Run", "Step", "Halt", "IPL"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            if (label.equals("IPL")) {
                button.setBackground(Color.RED);
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(new Color(100, 149, 237)); // Cornflower blue
                button.setForeground(Color.WHITE);
            }
            buttonPanel.add(button);
        }
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 3;
        mainPanel.add(buttonPanel, gbc);

        // Right Panel for Cache Content and Printer
        JPanel rightPanel = new JPanel(new GridLayout(2, 1));
        cacheContentArea = new JTextArea(10, 30);
        cacheContentArea.setEditable(false);
        printerArea = new JTextArea(5, 30);
        printerArea.setEditable(false);
        rightPanel.add(new JScrollPane(cacheContentArea));
        rightPanel.add(new JScrollPane(printerArea));

        // Console Input Panel
        JPanel consolePanel = new JPanel(new BorderLayout());
        consolePanel.setBackground(new Color(173, 216, 230));
        consolePanel.setBorder(BorderFactory.createTitledBorder("Console Input"));
        consolePanel.add(consoleInputField, BorderLayout.CENTER);

        // Add panels to frame
        add(mainPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(consolePanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createLabeledPanel(String title, JTextField[] fields, int count) {
        JPanel panel = new JPanel(new GridLayout(count + 1, 2, 5, 5));
        panel.setBackground(new Color(173, 216, 230));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(new JLabel(title));
        panel.add(new JLabel());
        for (int i = 0; i < count; i++) {
            panel.add(new JLabel(Integer.toString(i)));
            panel.add(fields[i]);
        }
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MachineSimulatorUI::new);
    }
}