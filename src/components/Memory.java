package components;

public class Memory {
    private short[] memoryArray; // 16-bit words in memory
    private final int SIZE = 2048; // Maximum 2048 words
    private final int RESERVED_SIZE = 6; // Reserved addresses 0-5

    // Error flags
    private boolean outOfBoundsFlag; // Memory access flag for out-of-bounds
    private boolean trapFlag; // Trap flag for reserved memory locations
    private int trapCode; // Stores the trap code (for example, the trap vector)

    public Memory() {
        memoryArray = new short[SIZE];
        resetMemory();      // UPON POWERING UP SYSTEM, ALL ELEMENTS OF MEMORY SHOULD BE SET TO ZERO.
    }

    // Resets the entire memory
    public void resetMemory() {
        memoryArray = new short[SIZE]; // Initialize all memory to 0
        outOfBoundsFlag = false;
        trapFlag = false;
        trapCode = 0;
    }

    // Checks if the address is within the reserved memory region (0-5)
    private boolean isReservedAddress(int address) {
        return address < RESERVED_SIZE;
    }

    // Checks memory access and handles out-of-bounds and reserved locations
    public void checkMemoryAccess(int address) {
        if (address >= SIZE) {
            outOfBoundsFlag = true;
            System.out.println("Memory access out of bounds!");
        } else if (isReservedAddress(address)) {
            trapFlag = true;
            trapCode = address; // Set the trap code to the reserved address
            System.out.println("Accessed reserved memory address " + address + ". Trap triggered!");
        } else {
            outOfBoundsFlag = false;
            trapFlag = false;
        }
    }

    // Stores a value in memory, masking to 16 bits, and handling traps
    public void storeValue(int address, int value) {
        checkMemoryAccess(address);
        if (!outOfBoundsFlag && !trapFlag) {
            memoryArray[address] = (short) (value & 0xFFFF); // Mask to 16 bits
            System.out.println("Stored value " + value + " at address " + address);
        } else if (trapFlag) {
            System.out.println("Cannot store value at reserved address: " + address);
        }
    }

    // Loads a value from memory, handling reserved memory access
    public short loadValue(int address) {
        checkMemoryAccess(address);
        if (!outOfBoundsFlag && !trapFlag) {
            return memoryArray[address];
        } else if (trapFlag) {
            System.out.println("Cannot load value from reserved address: " + address);
            return -1; // Return an error value for trap access
        } else {
            return -1; // Return error for out-of-bounds access
        }
    }

    // Displays memory content at a specific address
    public void displayMemory(int address) {
        if (address >= 0 && address < SIZE) {
            System.out.println("Memory at address " + address + ": " + memoryArray[address]);
        } else {
            System.out.println("Invalid address!");
        }
    }

    // Trap handling function
    public void handleTrapCode(int trapCode) {
        System.out.println("Handling trap at reserved memory address: " + trapCode);
        // Handle specific trap behavior here based on the trapCode (e.g., jump to routine)
    }

    // Handle illegal opcodes (used in CPU when an unknown opcode is encountered)
    public void handleIllegalOpcode(int opcode) {
        System.out.println("Illegal opcode encountered: " + opcode);
        // Set appropriate flags, handle trap, or raise errors here.
    }

    public static void main(String[] args) {
        Memory memory = new Memory();

        // Example: Store value in memory
        memory.storeValue(1024, 0x1234); // Store a value at address 1024
        memory.displayMemory(1024); // Display the value stored

        // Test reserved memory addresses
        memory.storeValue(3, 0xABCD); // Attempt to store at reserved memory (trap)
        memory.loadValue(3); // Attempt to load from reserved memory (trap)
    }
}
