package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MachineSimulatorUI extends JFrame {

    // UI Components
    private JTextField[] gprFields;     // General Purpose Registers (GPR 0-3)
    private JTextField[] ixrFields;     // Index Registers (IXR 1-3)
    private JTextField pcField, marField, mbrField, irField; // Register fields
    private JTextArea cacheContentArea; // Cache content display
    private JTextArea printerArea;      // Printer area
    private JTextField consoleInputField; // Console input field
    private JTextField binaryField, octalInputField; // Binary and octal input

    public MachineSimulatorUI() {
        setTitle("CSCI 6461 Machine Simulator");
        setSize(800, 600); // Set initial size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Initialize UI components
        gprFields = new JTextField[4]; // 4 GPRs
        ixrFields = new JTextField[3]; // 3 IXRs

        for (int i = 0; i < 4; i++) {
            gprFields[i] = new JTextField(5);
        }

        for (int i = 0; i < 3; i++) {
            ixrFields[i] = new JTextField(5);
        }

        pcField = new JTextField(5);
        marField = new JTextField(5);
        mbrField = new JTextField(5);
        irField = new JTextField(5);
        binaryField = new JTextField(10);
        octalInputField = new JTextField(5);

        cacheContentArea = new JTextArea(10, 20);
        cacheContentArea.setEditable(false);

        printerArea = new JTextArea(5, 20);
        printerArea.setEditable(false);

        consoleInputField = new JTextField(20);

        // Layout Setup
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(3, 1)); // Main panel

        // Create panel for GPR and IXR
        JPanel registerPanel = new JPanel(new GridLayout(2, 1));

        // GPR panel
        JPanel gprPanel = new JPanel(new GridLayout(5, 2));
        gprPanel.add(new JLabel("GPR"));
        gprPanel.add(new JLabel());
        for (int i = 0; i < 4; i++) {
            gprPanel.add(new JLabel("R" + i));
            gprPanel.add(gprFields[i]);
        }

        // IXR panel
        JPanel ixrPanel = new JPanel(new GridLayout(4, 2));
        ixrPanel.add(new JLabel("IXR"));
        ixrPanel.add(new JLabel());
        for (int i = 0; i < 3; i++) {
            ixrPanel.add(new JLabel("X" + (i + 1)));
            ixrPanel.add(ixrFields[i]);
        }

        registerPanel.add(gprPanel);
        registerPanel.add(ixrPanel);

        // Create panel for control registers (PC, MAR, MBR, IR)
        JPanel controlRegPanel = new JPanel(new GridLayout(4, 2));
        controlRegPanel.add(new JLabel("PC"));
        controlRegPanel.add(pcField);
        controlRegPanel.add(new JLabel("MAR"));
        controlRegPanel.add(marField);
        controlRegPanel.add(new JLabel("MBR"));
        controlRegPanel.add(mbrField);
        controlRegPanel.add(new JLabel("IR"));
        controlRegPanel.add(irField);

        // Panel for binary and octal input
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Binary"));
        inputPanel.add(binaryField);
        inputPanel.add(new JLabel("Octal Input"));
        inputPanel.add(octalInputField);

        // Panel for cache content and printer
        JPanel cachePrinterPanel = new JPanel(new GridLayout(2, 1));
        cachePrinterPanel.add(new JScrollPane(cacheContentArea)); // Scroll for cache content
        cachePrinterPanel.add(new JScrollPane(printerArea));      // Scroll for printer area

        // Panel for console input
        JPanel consolePanel = new JPanel(new FlowLayout());
        consolePanel.add(new JLabel("Console Input"));
        consolePanel.add(consoleInputField);

        // Buttons for operations (Load, Store, Run, Halt, etc.)
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4));
        String[] buttonLabels = {"Load", "Load+", "Store", "Store+", "Run", "Step", "Halt", "IPL"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            buttonPanel.add(button);
            // Add action listener to buttons (placeholder for now)
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(label + " button clicked");
                }
            });
        }

        // Add panels to the main panel
        mainPanel.add(registerPanel);
        mainPanel.add(controlRegPanel);
        mainPanel.add(inputPanel);

        // Add everything to the frame
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(cachePrinterPanel, BorderLayout.EAST);
        add(consolePanel, BorderLayout.NORTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MachineSimulatorUI());
    }
}
