package components;

/*
 * Memory: Represents a memory module with 2048 words of 16-bit storage, allowing for 
 * storing and loading values, as well as resetting the entire memory.
 * 
 * Memory(): Constructor that initializes memory with a default size of 2048 words, 
 * setting each word to 0.
 * 
 * resetMemory(): Resets all memory values to 0.
 * 
 * storeValue(int address, int value): Stores a 16-bit value at a specified memory address, 
 * ensuring it fits within the 16-bit range.
 * - @param address: The memory address where the value will be stored.
 * - @param value: The value to store, limited to 16 bits.
 * 
 * loadMemoryValue(int address): Loads and returns the value at a specified memory address as an unsigned 16-bit value.
 * - @param address: The memory address to load the value from.
 * - @return int: The 16-bit unsigned value at the specified address, or 0 if the address is out of bounds.
 */

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