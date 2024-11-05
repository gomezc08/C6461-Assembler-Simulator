package components;

/*
 * GeneralPurposeRegisters: Manages an array of 16-bit general-purpose registers (GPRs),
 * providing methods to set, get, and reset individual or all registers.
 * 
 * GeneralPurposeRegisters(int numRegisters): Constructor that initializes the specified 
 * number of GPRs, setting each register to 0.
 * - @param numRegisters: The number of general-purpose registers to initialize.
 * 
 * setGPR(int index, short value): Sets the value of a specific GPR.
 * - @param index: The index of the GPR to set.
 * - @param value: The 16-bit value to assign to the GPR.
 * 
 * getGPR(int index): Retrieves the value of a specific GPR.
 * - @param index: The index of the GPR to retrieve.
 * - @return short: The 16-bit value stored in the specified GPR.
 * 
 * resetGPR(int index): Resets a specific GPR to 0.
 * - @param index: The index of the GPR to reset.
 * 
 * resetAllGPRs(): Resets all GPRs to 0.
 * 
 * toString(): Returns a string representation of all GPR values for debugging purposes.
 * - @return String: A formatted string showing each GPR index and its value.
 */

public class GeneralPurposeRegisters {
    private short[] gprs; // Array to hold the 16-bit values for each GPR

    // Constructor initializes 4 GPRs (each 16 bits)
    public GeneralPurposeRegisters(int numRegisters) {
        this.gprs = new short[numRegisters]; // Initialize all GPRs to 0
    }

    // Method to set a specific GPR value (index 0 to numRegisters-1)
    public void setGPR(int index, short value) {
        if (index >= 0 && index < gprs.length) {
            gprs[index] = value;
        } else {
            throw new IllegalArgumentException("Invalid GPR index");
        }
    }

    // Method to get the value of a specific GPR (index 0 to numRegisters-1)
    public short getGPR(int index) {
        if (index >= 0 && index < gprs.length) {
            return gprs[index];
        } else {
            throw new IllegalArgumentException("Invalid GPR index");
        }
    }

    // Method to reset a specific GPR to 0
    public void resetGPR(int index) {
        if (index >= 0 && index < gprs.length) {
            gprs[index] = 0;
        } else {
            throw new IllegalArgumentException("Invalid GPR index");
        }
    }

    // Method to reset all GPRs to 0
    public void resetAllGPRs() {
        for (int i = 0; i < gprs.length; i++) {
            gprs[i] = 0;
        }
    }

    // For debugging: display the values of all GPRs
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gprs.length; i++) {
            sb.append(String.format("GPR[%d]: %d\n", i, gprs[i]));  // Display in decimal format
        }
        return sb.toString();
    }
}
