package components;

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

    public static void main(String[] args) {
        // Testing IndexRegisters
        IndexRegisters ixr = new IndexRegisters(3); // Assume 3 Index Registers

        // Set values for Index Registers
        ixr.setIndexRegister(0, (short) 0x1234);
        ixr.setIndexRegister(1, (short) 0x5678);
        ixr.setIndexRegister(2, (short) 0x9ABC);

        // Display the values of Index Registers
        System.out.println(ixr);  // Should print values in hex format

        // Reset all Index Registers
        ixr.resetAllIndexRegisters();
        System.out.println(ixr);  // Should print all zeros
    }
}
