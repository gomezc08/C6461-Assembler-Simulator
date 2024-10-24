package components;

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
