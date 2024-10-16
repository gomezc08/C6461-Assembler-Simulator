package ui;

import components.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class BackendGUI {
    private Memory memory; // Memory object
    private MemoryAddressRegister mar; // Memory Address Register
    private MemoryBufferRegister mbr; // Memory Buffer Register
    private CPU cpu; // CPU object
    private GeneralPurposeRegisters gprs; // General Purpose Registers
    private IndexRegisters ixr; // Index Registers
    private ProgramCounter pc; // Program Counter
    private JTextArea printerArea; // Text area to show messages

    public BackendGUI(JTextArea printerArea) {
        // Initialize components
        memory = new Memory();
        mar = new MemoryAddressRegister();
        mbr = new MemoryBufferRegister();
        gprs = new GeneralPurposeRegisters(4); // Assuming 4 general-purpose registers
        ixr = new IndexRegisters(3); // Assuming 3 index registers
        pc = new ProgramCounter();
        cpu = new CPU(memory, mar, mbr, gprs, ixr, pc); // Initialize the CPU
        this.printerArea = printerArea; // Initialize printer area for messages
    }

    public void loadROMFile(File file) {
        try {
            cpu.loadROMFile(file);
            printerArea.append("Loaded ROM file successfully\n");
        } catch (IOException ex) {
            printerArea.append("Error loading ROM file: " + ex.getMessage() + "\n");
        }
    }

    public void storeValue(int marValue, int mbrValue) {
        if (marValue > 5) {
            cpu.store(marValue, mbrValue);
            printerArea.append("Stored value " + mbrValue + " from MBR into memory address " + marValue + "\n");
        } else {
            printerArea.append("Memory location " + marValue + " is a reserved memory location.\n");
        }
    }

    public void resetRegisters() {
        cpu.resetRegisters(); // Reset registers using CPU's method
        printerArea.append("Registers reset successfully.\n");
    }

    public void loadValue(int marValue) {
        mar.setValue((short) marValue);
        int memoryValue = memory.loadMemoryValue(marValue);
        mbr.setValue((short) memoryValue);
        
        if (marValue > 5) {
            printerArea.append("Loaded value " + memoryValue + " from memory address " + marValue + " into MBR\n");
        } else {
            printerArea.append("Memory location " + marValue + " is a reserved memory location.\n");
        }
    }
}
