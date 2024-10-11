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
    public static void main(String[] args) {
        Memory memory = new Memory();
        int idk = 57472;
        short s1 = -3212;
        short s2 = (short) idk; // Properly cast to short within range
    
        memory.storeValue(0, s1);
        memory.storeValue(1, s2);
    
        int val1 = memory.loadMemoryValue(0);
        int val2 = memory.loadMemoryValue(1);
    
        System.out.println(val1 + " " + val2); // Should print 62324 and 57472
    }
}