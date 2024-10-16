package ui;

import components.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class BackendGUI {
    private Memory memory; 
    private MemoryAddressRegister mar; 
    private MemoryBufferRegister mbr; 
    private CPU cpu; 
    private GeneralPurposeRegisters gprs; 
    private IndexRegisters ixr; 
    private ProgramCounter pc; 
    private JTextArea printerArea; 

    public BackendGUI(JTextArea printerArea) {
        // Initialize components
        memory = new Memory();
        mar = new MemoryAddressRegister();
        mbr = new MemoryBufferRegister();
        gprs = new GeneralPurposeRegisters(4); 
        ixr = new IndexRegisters(3); 
        pc = new ProgramCounter();
        cpu = new CPU(memory, mar, mbr, gprs, ixr, pc); 
        this.printerArea = printerArea; 
    }

    // LOAD ROM.
    public void loadROMFile(File file) {
        try {
            cpu.loadROMFile(file);
            printerArea.append("Loaded ROM file successfully\n");
        } 
        catch (IOException ex) {
            printerArea.append("Error loading ROM file: " + ex.getMessage() + "\n");
        }
    }

    // LOAD.
    public void loadValue(int marValue) {
        mar.setValue((short) marValue);
        int memoryValue = memory.loadMemoryValue(marValue);
        mbr.setValue((short) memoryValue);
        
        if (marValue > 5) {
            printerArea.append("Loaded value " + memoryValue + " from memory address " + marValue + " into MBR\n");
        } 
        else {
            printerArea.append("Memory location " + marValue + " is a reserved memory location.\n");
        }
    }

    // STORE. 
    public void storeValue(int marValue, int mbrValue) {
        if (marValue > 5) {
            cpu.store(marValue, mbrValue);
            printerArea.append("Stored value " + mbrValue + " from MBR into memory address " + marValue + "\n");
        } 
        else {
            printerArea.append("Memory location " + marValue + " is a reserved memory location.\n");
        }
    }

    // STORE+.
    public void storePlusValue(int marValue, int mbrValue) {
        if (marValue > 5) {
            cpu.store(marValue, mbrValue);
            printerArea.append("Stored value " + mbrValue + " from MBR into memory address " + marValue + "\n");
            mar.increment(); 
        } 
        
        else {
            printerArea.append("Memory location " + marValue + " is a reserved memory location.\n");
        }
    }

    public int getMarValue() {
        return mar.getValue(); 
    }

    public int getMbrValue() {
        return mbr.getValue(); 
    }

    public void resetRegisters() {
        cpu.resetRegisters(); 
        printerArea.append("Registers reset successfully.\n");
    }
}