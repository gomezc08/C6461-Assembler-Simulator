package components;

/*
 * MachineFaultRegister: Represents a 4-bit machine fault register (MFR) used to handle 
 * machine faults, allowing for setting, resetting, and retrieving fault codes.
 * 
 * MachineFaultRegister(): Constructor that initializes the MFR to 0.
 * 
 * triggerFault(String faultMessage): Displays a machine fault message.
 * - @param faultMessage: A message describing the fault.
 * 
 * setMFR(int value): Sets the MFR value, ensuring it's within the 4-bit range (0-15).
 * - @param value: The integer value to store in the MFR.
 * 
 * getMFR(): Retrieves the MFR value, limited to the lower 4 bits.
 * - @return int: The 4-bit MFR value.
 * 
 * resetMFR(): Resets the MFR to 0.
 * 
 * toString(): Returns a string representation of the MFR value in binary for debugging purposes.
 * - @return String: A formatted string showing the MFR in binary.
 */

public class MachineFaultRegister {
    private byte mfr; // Use a byte to hold the 4-bit value

    private final byte MAX_VALUE = 0xF; // Maximum value for 4 bits (15)

    // Constructor initializes the MFR to 0
    public MachineFaultRegister() {
        this.mfr = 0;
    }

    // Handle the machine fault based on the fault message
    public void triggerFault(String faultMessage) {
        System.out.println("Machine Fault Triggered: " + faultMessage);
    }

    // Method to set the MFR value (ensures it's within 4-bit range)
    public void setMFR(int value) {
        if (value >= 0 && value <= MAX_VALUE) {
            this.mfr = (byte) (value & MAX_VALUE); // Apply mask to ensure only lower 4 bits are used
        } else {
            throw new IllegalArgumentException("Value out of range for 4-bit register");
        }
    }

    // Method to get the MFR value (only the lower 4 bits)
    public int getMFR() {
        return this.mfr & MAX_VALUE; // Mask to ensure we return only the lower 4 bits
    }

    // Reset the MFR to 0
    public void resetMFR() {
        this.mfr = 0;
    }

    // For debugging: display the MFR value in binary (for clarity)
    @Override
    public String toString() {
        return String.format("MFR: %04d (binary: %4s)", mfr, Integer.toBinaryString(mfr & MAX_VALUE)).replace(' ', '0');
    }
}