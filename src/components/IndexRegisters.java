package components;

/*
 * IndexRegisters: Manages an array of index registers (IXRs), providing methods to 
 * set, get, and reset individual or all index registers.
 * 
 * IndexRegisters(int numRegisters): Constructor that initializes the specified 
 * number of index registers, setting each register to 0.
 * - @param numRegisters: The number of index registers to initialize.
 * 
 * setIndexRegister(int index, short value): Sets the value of a specific index register.
 * - @param index: The index of the index register to set.
 * - @param value: The 16-bit value to assign to the index register.
 * 
 * getIndexRegister(int index): Retrieves the value of a specific index register.
 * - @param index: The index of the index register to retrieve.
 * - @return short: The 16-bit value stored in the specified index register.
 * 
 * resetAllIndexRegisters(): Resets all index registers to 0.
 * 
 * toString(): Returns a string representation of all index register values for debugging purposes.
 * - @return String: A formatted string showing each index register index and its value in hexadecimal.
 */

public class IndexRegisters {
    private short[] ixr; // Array to hold the index registers

    public IndexRegisters(int numRegisters) {
        this.ixr = new short[numRegisters]; // Initialize index registers
    }

    // Method to set a specific Index Register value
    public void setIndexRegister(int index, short value) {
        if (index >= 0 && index < ixr.length) {
            ixr[index] = value;
        } else {
            throw new IllegalArgumentException("Invalid Index Register index");
        }
    }

    // Method to get the value of a specific Index Register
    public short getIndexRegister(int index) {
        if (index >= 0 && index < ixr.length) {
            return ixr[index];
        } else {
            throw new IllegalArgumentException("Invalid Index Register index");
        }
    }

    // Method to reset all Index Registers to 0
    public void resetAllIndexRegisters() {
        for (int i = 0; i < ixr.length; i++) {
            ixr[i] = 0;
        }
    }

    // For debugging: display the values of all Index Registers
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ixr.length; i++) {
            sb.append(String.format("IXR[%d]: %04X\n", i, ixr[i] & 0xFFFF)); // Display in hex format
        }
        return sb.toString();
    }
}
