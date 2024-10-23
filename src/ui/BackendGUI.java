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
    private ConditionCode cc;
    private JTextArea printerArea; 
    private FrontendGUI frontendGUI;

    public BackendGUI(JTextArea printerArea) {
        // Initialize components
        memory = new Memory();
        mar = new MemoryAddressRegister();
        mbr = new MemoryBufferRegister();
        gprs = new GeneralPurposeRegisters(4); 
        ixr = new IndexRegisters(3); 
        pc = new ProgramCounter();
        cc = new ConditionCode();
        cpu = new CPU(memory, mar, mbr, gprs, ixr, pc, cc);
        this.printerArea = printerArea; 
    }

    // Load and execute the rom.
    public void loadExecuteRom(File file) {
        try {
            // load rom.
            cpu.loadROMFile(file);
            printerArea.append("Loaded ROM file successfully\n");

            // execute it.
            pc.setPC(14);       // need to find a way to do this dynamically.
            cpu.run();

            // Final checks
            System.out.println("Final GPR Values:");
            System.out.println(gprs.toString());
        
            System.out.println("Final Index Register Values:");
            System.out.println(ixr.toString());
        
            System.out.println("Final Condition Code Values:");
            System.out.println(cc.toString());

            // update the checkboxes for all components in gui.
            if (frontendGUI != null) {
                frontendGUI.updateAllRegisters(
                    getGPRValues(),
                    getIXRValues(),
                    getPCValue(),
                    getMarValue(),
                    getMbrValue()
                    //getCCValue()
                );
            }
            
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

    public short[] getGPRValues() {
        short[] gprValues = new short[4];
        gprValues[0] = gprs.getGPR(0);
        gprValues[1] = gprs.getGPR(1);
        gprValues[2] = gprs.getGPR(2);
        gprValues[3] = gprs.getGPR(3);

        return gprValues;
    }

    public short[] getIXRValues() {
        short[] ixrValues = new short[3];
        ixrValues[0] = ixr.getIndexRegister(0);
        ixrValues[1] = ixr.getIndexRegister(1);
        ixrValues[2] = ixr.getIndexRegister(2);

        return ixrValues;
    }

    public int getPCValue() {
        return pc.getPC();
    }

    /* 
    public int getCCValue() {
        return cc.getValue();
    }
    */

    public void resetRegisters() {
        cpu.resetRegisters(); 
        printerArea.append("Registers reset successfully.\n");
    }

    public void setFrontendGUI(FrontendGUI gui) {
        this.frontendGUI = gui;
    }
}