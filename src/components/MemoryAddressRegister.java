package components;

/*
 * MemoryAddressRegister: Represents a 12-bit memory address register (MAR) with methods to set, 
 * retrieve, reset, and increment the address, using masking to enforce the 12-bit range.
 * 
 * MemoryAddressRegister(): Constructor that initializes the MAR to 0.
 * 
 * setValue(short address): Sets the MAR value, ensuring it fits within the 12-bit range (0-4095).
 * - @param address: The 12-bit address value to store in the MAR.
 * 
 * getValue(): Retrieves the current 12-bit address stored in the MAR.
 * - @return int: The 12-bit address value.
 * 
 * resetMAR(): Resets the MAR to 0.
 * 
 * increment(int mar_value): Increments the MAR by 1, wrapping around to 0 if the maximum value (4095) is exceeded.
 * - @param mar_value: The value to increment from.
 * 
 * toString(): Returns a string representation of the MAR value in hexadecimal for debugging purposes.
 * - @return String: A formatted string displaying the MAR as a 3-digit hex value.
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


