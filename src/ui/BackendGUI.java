package ui;

import components.*;
import javax.swing.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import Assembler.Assembler;

public class BackendGUI {
    private Memory memory; 
    private MemoryAddressRegister mar; 
    private MemoryBufferRegister mbr; 
    private CPU cpu; 
    private GeneralPurposeRegisters gprs; 
    private IndexRegisters ixr; 
    private ProgramCounter pc; 
    private ConditionCode cc;
    private Cache cache;
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
        cache = new Cache(memory);
        cpu = new CPU(memory, mar, mbr, gprs, ixr, pc, cc, cache);
        this.printerArea = printerArea; 
    }

    // Load and execute the rom.
    public void loadExecuteRom(File file) {
        try {
            // assemble the asm file.
            System.out.println("assembly/" + file.getName());
            Assembler.run("assembly/" + file.getName());
            
            // load rom.
            cpu.loadROMFile(new File("output/output.ld"));
            printerArea.append("Loaded ROM file successfully\n");

            // execute it.
            pc.setPC(Assembler.getStartAddress());    // pc = first real instruction address - 1
            cpu.run();
                        
            // update the checkboxes for all components in gui.
            updateGUIFields();
            updateCacheDisplay();
        } 
        catch (IOException ex) {
            printerArea.append("Error loading ROM file: " + ex.getMessage() + "\n");
        }
    }

    // LOAD.
    public void loadValue(int marValue) {
        mar.setValue((short) marValue);
        int address = mar.getValue();
        int memoryValue = memory.loadMemoryValue(address);
        System.out.println("Mar is"+mar+"and Memory value is"+ memoryValue);
        mbr.setValue((short) memoryValue);
        
        if (marValue > 5) {
            printerArea.append("Loaded value " + memoryValue + " from memory address " + marValue + " into MBR\n");
            updateGUIFields();
            updateCacheDisplay();
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
            updateCacheDisplay();
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
            mar.increment(marValue); 
            updateCacheDisplay();
        } 
        
        else {
            printerArea.append("Memory location " + marValue + " is a reserved memory location.\n");
        }
    }

    public void updateGUIFields() {
        if (frontendGUI != null) {
            frontendGUI.updateAllRegisters(
                getGPRValues(),
                getIXRValues(),
                getPCValue(),
                getMarValue(),
                getMbrValue(),
                getCCValue(),
                getMfrValues()
            );
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

    
    public int[] getCCValue() {
        int[] ccValues = new int[4];
        ccValues[0] = cc.isOverflow() ? 1 : 0;
        ccValues[1] = cc.isUnderflow() ? 1 : 0;
        ccValues[2] = cc.isDivZero() ? 1 : 0;
        ccValues[3] = cc.isEqual() ? 1 : 0;

        return ccValues;
    }

    // not implemented yet.
    public int[] getMfrValues() {
        int[] mfrValues = new int[4];
        return mfrValues;
    }

    public void updateCacheDisplay() {
        if (frontendGUI != null && cpu.getCache() != null) {
            frontendGUI.updateCacheContent(cache.getCacheStateString());
        }
    }     
    

    public void resetRegisters() {
        cpu.resetRegisters(); 
        printerArea.append("Registers reset successfully.\n");
    }

    public void setFrontendGUI(FrontendGUI gui) {
        this.frontendGUI = gui;
    }
}