package components;

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

    // Modified increment to check for LOC directives
    public void incrementPC() {
        int nextPC = (this.pc + 1) & MAX_VALUE;
        // If next address has a LOC directive, jump to that location instead
        if (hasLocDirective(nextPC)) {
            this.pc = getLocTarget(nextPC);
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