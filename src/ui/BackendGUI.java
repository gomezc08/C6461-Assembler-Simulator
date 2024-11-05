package ui;

/*
 * BackendGUI: Provides the backend functionality for the GUI, handling memory, CPU, 
 * and register operations. This class interacts with components such as the Memory, 
 * Program Counter, and Condition Code, while updating the GUI based on actions 
 * like loading, storing, and executing instructions.
 * 
 * BackendGUI(JTextArea printerArea): Initializes all components and sets up the printer area for output.
 * - @param printerArea: The text area for displaying output messages in the GUI.
 * 
 * loadExecuteRom(File file): Loads a ROM file and executes it, updating the CPU's Program Counter 
 * and running the assembled code.
 * - @param file: The ROM file to load and execute.
 * 
 * loadValue(int marValue): Loads a value from memory into the Memory Buffer Register (MBR) based 
 * on the specified MAR value and updates the GUI.
 * - @param marValue: The address to load from.
 * 
 * storeValue(int marValue, int mbrValue): Stores a value from the MBR into memory at the specified 
 * MAR address, updating the cache display and GUI.
 * - @param marValue: The memory address to store the value.
 * - @param mbrValue: The value to store.
 * 
 * storePlusValue(int marValue, int mbrValue): Stores a value in memory and increments the MAR, updating the cache and GUI.
 * - @param marValue: The initial address to store the value.
 * - @param mbrValue: The value to store.
 * 
 * updateGUIFields(): Updates all register and condition code displays in the GUI.
 * 
 * getMarValue(): Retrieves the current value of the Memory Address Register (MAR).
 * - @return int: The MAR value.
 * 
 * getMbrValue(): Retrieves the current value of the Memory Buffer Register (MBR).
 * - @return int: The MBR value.
 * 
 * getGPRValues(): Retrieves the values of all General Purpose Registers (GPRs).
 * - @return short[]: Array containing GPR values.
 * 
 * getIXRValues(): Retrieves the values of all Index Registers (IXRs).
 * - @return short[]: Array containing IXR values.
 * 
 * getPCValue(): Retrieves the current value of the Program Counter (PC).
 * - @return int: The PC value.
 * 
 * getCCValue(): Retrieves the Condition Code values as an array of integers.
 * - @return int[]: Array with 1 if the condition is met, 0 otherwise for each condition code flag.
 * 
 * getMfrValues(): Retrieves the Machine Fault Register values (not yet implemented).
 * - @return int[]: Array representing MFR values.
 * 
 * updateCacheDisplay(): Updates the cache content display in the frontend GUI.
 * 
 * resetRegisters(): Resets all CPU registers and provides feedback in the printer area.
 * 
 * setFrontendGUI(FrontendGUI gui): Links the frontend GUI to enable communication with the backend.
 * - @param gui: The frontend GUI instance to connect.
 */

import components.*;
import javax.swing.*;

import java.io.File;
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
    // In BackendGUI.loadExecuteRom():
    public void loadExecuteRom(File file) {
        try {
            // Run assembler
            Assembler.run("assembly/" + file.getName());
            
            // Get the configured PC from assembler
            ProgramCounter assemblerPC = Assembler.getProgramCounter();
            
            // Update our CPU's PC with the assembler's PC
            this.pc = assemblerPC;
            this.cpu = new CPU(memory, mar, mbr, gprs, ixr, assemblerPC, cc, cache);
            
            // Load ROM and execute
            cpu.loadROMFile(new File("output/output.ld"));
            pc.setPC(Assembler.getStartAddress());
            cpu.run();
            
            updateGUIFields();
            updateCacheDisplay();
        } catch (IOException ex) {
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