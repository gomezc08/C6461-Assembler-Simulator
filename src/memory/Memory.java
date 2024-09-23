package memory; 

/**
 * The Memory class simulates the memory of the system, holding data and instructions.
 * It provides methods for reading from and writing to memory, as well as loading programs.
 *
 * Functions:
 * - Memory(int size): 
 *   Constructs a memory object with the specified size.
 *   @param size The size of the memory array.
 *
 * - int read(int address): 
 *   Reads the value stored at the specified memory address.
 *   @param address The memory address to read from.
 *   @return The value stored at the specified memory address.
 *
 * - void write(int address, int value): 
 *   Writes the specified value to the specified memory address.
 *   @param address The memory address to write to.
 *   @param value The value to store at the memory address.
 *
 * - void loadProgram(int[] program, int startAddress): 
 *   Loads a program into memory starting at the specified address.
 *   @param program The array of instructions representing the program.
 *   @param startAddress The memory address to start loading the program at.
 */

public class Memory {
    private int[] memoryArray; // Can be an array or some other data structure

    public Memory(int size) {
        memoryArray = new int[size]; // Initialize memory with the specified size
    }

    public int read(int address) {
        return memoryArray[address]; // Return the value at the given address
    }

    public void write(int address, int value) {
        memoryArray[address] = value; // Write the value to the given address
    }

    public void loadProgram(int[] program, int startAddress) {
        // Load a program into memory starting at the given address
        for (int i = 0; i < program.length; i++) {
            memoryArray[startAddress + i] = program[i];
        }
    }
}
