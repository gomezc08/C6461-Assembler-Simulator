package components;

/*
 * Use masking to trim 32 bits into 12 bits
 * Original number:          0011 1111 1111 1111 1111 1111 1111 1111
 * Mask:                     0000 0000 0000 0000 0000 1111 1111 1111
 * -----------------------------------------------------------------
 * Result (AND operation):   0000 0000 0000 0000 0000 1111 1111 1111
 * 
 */


public class MemoryAddressRegister {
    private int mar; // Use an int to hold the 12-bit address (we'll mask to ensure it's 12 bits)
    private final int MAX_VALUE = 0xFFF; // Maximum value for 12 bits (4095)
    //private int address;

    // Constructor initializes the MAR to 0
    public MemoryAddressRegister() {
        this.mar = 0;
    }

    // Method to set the MAR value (ensures it's within 12-bit range)
    public void setValue(short address) {
        // ensure its within the right space.
        // APPLY MASK TO ENSURE 12 BITS.
        if (address >= 0 && address <= MAX_VALUE) {
            this.mar = address & MAX_VALUE; 
        } 
        
        else {
            throw new IllegalArgumentException("Address out of range for 12-bit register");
        }
    }

    // Method to get the MAR value (12-bit address)
    public int getValue() {
        return this.mar;
    }

    // Method to reset the MAR to 0
    public void resetMAR() {
        this.mar = 0;
    }

    public void increment(int mar_value) {
        if (mar < MAX_VALUE) { // MAX_VALUE is 4095 for 12-bit
            this.mar = mar_value+1;
        } 
        
        else {
            mar = 0; // Wrap around to 0
            System.out.println("MAR wrapped around to 0");
        }
    }

    // For debugging: display the MAR value
    @Override
    public String toString() {
        return String.format("MAR: %03X", mar); // Display as a 3-digit hex value
    }
}


