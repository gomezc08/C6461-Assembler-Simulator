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

    public static void main(String[] args) {
        MemoryBufferRegister mbr = new MemoryBufferRegister();

        // Set the MBR to a 16-bit value (e.g., 0x1234)
        mbr.setValue((short) 0x1234);
        System.out.println(mbr); // Output: MBR: 1234

        // Retrieve the MBR value
        short value = mbr.getValue();
        System.out.println("MBR Value: " + Integer.toHexString(value & 0xFFFF)); // Output: 1234

        // Reset the MBR
        mbr.resetMBR();
        System.out.println(mbr); // Output: MBR: 0000
    }
}
