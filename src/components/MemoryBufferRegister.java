package components;

/*
 * MemoryBufferRegister: Represents a 16-bit memory buffer register (MBR), 
 * providing methods to set, retrieve, and reset the value.
 * 
 * MemoryBufferRegister(): Constructor that initializes the MBR to 0.
 * 
 * setValue(short value): Sets the MBR to a specified 16-bit value.
 * - @param value: The 16-bit value to store in the MBR.
 * 
 * getValue(): Retrieves the current 16-bit value stored in the MBR.
 * - @return short: The 16-bit value in the MBR.
 * 
 * resetMBR(): Resets the MBR to 0.
 * 
 * toString(): Returns a string representation of the MBR value in hexadecimal for debugging purposes.
 * - @return String: A formatted string displaying the MBR as a 4-digit hex value.
 */

// 16-bit memory buffer register
public class MemoryBufferRegister {
    private short mbr; 

    // Constructor initializes the MBR to 0
    public MemoryBufferRegister() {
        this.mbr = 0;
    }

    // Method to set the MBR value
    public void setValue(short value) {
        this.mbr = value; // Directly store the 16-bit value
    }

    // Method to get the MBR value
    public short getValue() {
        return this.mbr;
    }

    // Reset the MBR to 0
    public void resetMBR() {
        this.mbr = 0;
    }

    // For debugging: display the MBR value
    @Override
    public String toString() {
        return String.format("MBR: %04X", mbr & 0xFFFF); // Display in 4-digit hex format
    }
}
