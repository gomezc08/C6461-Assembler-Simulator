package components;

/*
 * ProgramCounter: Manages the program counter (PC) in a 12-bit range, with support 
 * for LOC directives that allow jumps to specific target addresses.
 * 
 * ProgramCounter(): Constructor that initializes the PC to 0 and sets up the map for LOC directives.
 * 
 * addLocDirective(int currentAddress, int targetLocation): Adds a LOC directive mapping a current 
 * address to a specified target location.
 * - @param currentAddress: The address with the LOC directive.
 * - @param targetLocation: The target address to jump to when the LOC directive is encountered.
 * 
 * hasLocDirective(int address): Checks if a LOC directive exists for the given address.
 * - @param address: The address to check.
 * - @return boolean: True if a LOC directive exists for the address, false otherwise.
 * 
 * getLocTarget(int address): Retrieves the target address for a LOC directive, or returns the original 
 * address if no directive exists.
 * - @param address: The address with the potential LOC directive.
 * - @return int: The target address for the LOC directive, or the original address.
 * 
 * setPC(int value): Sets the PC to a specified value, ensuring it fits within the 12-bit range.
 * - @param value: The value to set for the PC.
 * 
 * getPC(): Retrieves the current value of the PC.
 * - @return int: The current PC value.
 * 
 * incrementPC(): Increments the PC by 1, respecting LOC directives if present.
 * 
 * reset(): Resets the PC to 0 and clears all LOC directives.
 * 
 * clearLocDirectives(): Removes all stored LOC directives.
 * 
 * toString(): Returns a string representation of the PC value in decimal and hexadecimal for debugging.
 * - @return String: A formatted string displaying the PC in decimal and 3-digit hexadecimal.
 */

import java.util.HashMap;
import java.util.Map;

public class ProgramCounter {
    private int pc; 
    private final int MAX_VALUE = 0xFFF; // Maximum value for 12 bits
    
    // Map to store LOC directive locations and their target addresses
    private Map<Integer, Integer> locDirectives;
    
    public ProgramCounter() {
        this.pc = 0;
        this.locDirectives = new HashMap<>();
    }

    // Add a new LOC directive mapping
    public void addLocDirective(int currentAddress, int targetLocation) {
        if (targetLocation >= 0 && targetLocation <= MAX_VALUE) {
            locDirectives.put(currentAddress, targetLocation);
        } else {
            throw new IllegalArgumentException("LOC target location out of range for 12-bit address");
        }
    }

    // Check if current address has a LOC directive
    public boolean hasLocDirective(int address) {
        return locDirectives.containsKey(address);
    }

    // Get the target location for a LOC directive
    public int getLocTarget(int address) {
        return locDirectives.getOrDefault(address, address);
    }

    public void setPC(int value) {
        if (value >= 0 && value <= MAX_VALUE) {
            this.pc = value & MAX_VALUE;
        } else {
            throw new IllegalArgumentException("Value out of range for 12-bit register");
        }
    }

    public int getPC() {
        return this.pc;
    }

  
    public void incrementPC() {
        //System.out.println("Incrementing PC from: " + this.pc);
        int nextPC = (this.pc + 1) & MAX_VALUE;
        // If next address has a LOC directive, jump to that location instead
        if (hasLocDirective(nextPC)) {
            this.pc = getLocTarget(nextPC);
            System.out.println("LOC directive jump to: " + this.pc);
        } else {
            this.pc = nextPC;
        }
    }

    public void reset() {
        this.pc = 0;
        this.locDirectives.clear();
    }

    // Clear all LOC directives
    public void clearLocDirectives() {
        this.locDirectives.clear();
    }

    @Override
    public String toString() {
        return String.format("PC: %d (0x%03X)", this.pc, this.pc);
    }
}