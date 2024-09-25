package components;

// each IX is 16 bits.
public class IndexRegisters {
    private short[] indexRegisters; // Array to hold the values for each index register

    // Constructor initializes the given number of index registers (each 16 bits)
    public IndexRegisters(int numRegisters) {
        this.indexRegisters = new short[numRegisters]; // Initialize all index registers to 0
    }

    // Method to set a specific index register value
    public void setIndexRegister(int index, short value) {
        if (index >= 0 && index < indexRegisters.length) {
            this.indexRegisters[index] = value; // Store the 16-bit value directly
        } else {
            throw new IllegalArgumentException("Invalid index register index");
        }
    }

    // Method to get the value of a specific index register
    public short getIndexRegister(int index) {
        if (index >= 0 && index < indexRegisters.length) {
            return this.indexRegisters[index];
        } else {
            throw new IllegalArgumentException("Invalid index register index");
        }
    }

    // Method to reset a specific index register to 0
    public void resetIndexRegister(int index) {
        if (index >= 0 && index < indexRegisters.length) {
            this.indexRegisters[index] = 0;
        } else {
            throw new IllegalArgumentException("Invalid index register index");
        }
    }

    // Method to reset all index registers to 0
    public void resetAllIndexRegisters() {
        for (int i = 0; i < indexRegisters.length; i++) {
            this.indexRegisters[i] = 0;
        }
    }

    // For debugging: display the values of all index registers
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indexRegisters.length; i++) {
            sb.append(String.format("Index Register[%d]: %04X\n", i, indexRegisters[i] & 0xFFFF)); // Display as 4-digit hex
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // There are 3 Index Registers, each 16 bits
        IndexRegisters ir = new IndexRegisters(3);

        // Set values for the index registers
        ir.setIndexRegister(0, (short) 0x1234);
        ir.setIndexRegister(1, (short) 0x5678);
        ir.setIndexRegister(2, (short) 0x9ABC);

        // Display the values of the index registers
        System.out.println(ir); // Output: Index Register[0]: 1234, Index Register[1]: 5678, etc.

        // Get the value of a specific index register
        System.out.println("Index Register[2] Value: " + Integer.toHexString(ir.getIndexRegister(2) & 0xFFFF)); // Output: 9ABC

        // Reset Index Register[1] to 0
        ir.resetIndexRegister(1);
        System.out.println(ir); // Output: Index Register[1]: 0000

        // Reset all index registers
        ir.resetAllIndexRegisters();
        System.out.println(ir); // Output: All registers: 0000
    }
}
