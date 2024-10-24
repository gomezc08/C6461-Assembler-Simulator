package components;

public class Memory {
    private short[] memoryArray; // 16-bit words in memory
    private final int SIZE = 2048; // Maximum 2048 words

    public Memory() {
        memoryArray = new short[SIZE];
        resetMemory();
    }

    // Resets the entire memory
    public void resetMemory() {
        for (int i = 0; i < SIZE; i++) {
            memoryArray[i] = 0; // Set all memory values to 0
        }
    }

    // Store value in memory as a 16-bit word, applying masking to ensure it fits in the 16-bit range
    public void storeValue(int address, int value) {
        if (address >= 0 && address < memoryArray.length) {
            memoryArray[address] = (short) (value & 0xFFFF);  // Mask value to 16 bits
        }
    }

    // Load a value from memory and interpret it as an unsigned 16-bit value
    public int loadMemoryValue(int address) {
        if (address >= 0 && address < memoryArray.length) {
            return memoryArray[address] & 0xFFFF;  // Return unsigned 16-bit value
        } 
        else {
            return 0; // Return 0 if address is out of bounds
        }
    }
}