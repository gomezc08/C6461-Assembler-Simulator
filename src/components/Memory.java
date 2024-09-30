package components;

public class Memory {
    private short[] memoryArray; // Use short to reflect 16-bit words
    private final int SIZE = 2048;  // Max 2048 words in memory
    private boolean R;  // Reserved location access flag
    private boolean TR; // Trap code flag
    private boolean OP; // Opcode error flag
    private boolean M;  // Memory access flag (out of bounds)

    // Condition codes
    private boolean O;  // Overflow flag
    private boolean U;  // Underflow flag
    private boolean D;  // Divide by zero flag
    private boolean EQ; // Equality flag
    private boolean ODD; // Odd parity flag
    private int storeCount = 0; // Counter for store + button

    public Memory() {
        memoryArray = new short[SIZE]; // 16-bit words
        resetMemory();
    }

    public void resetMemory() {
        memoryArray = new short[SIZE]; // Re-initialize array for reset
        // Reset flags
        R = false;
        TR = false;
        OP = false;
        M = false;
        O = false;
        U = false;
        D = false;
        EQ = false;
        ODD = false;
        storeCount = 0;
    }

    public void checkMemoryAccess(int address) {
        if (address >= SIZE) {
            M = true;
            System.out.println("Memory access out of bounds! M flag is set.");
        } else {
            M = false;
        }
    }

    public void storeValue(int address, int value) {
        checkMemoryAccess(address);
        if (!M) {
            memoryArray[address] = (short)(value & 0xFFFF); // Mask to 16 bits
            storeCount++;
            System.out.println("Stored value " + value + " at address " + address);
        }
    }

    public short loadValue(int address) {
        checkMemoryAccess(address);
        if (!M) {
            R = true;
            return memoryArray[address];
        } else {
            return -1; // Invalid memory access
        }
    }

    public void checkConditionCodes(int result) {
        O = (result > 65535);  // Overflow if result exceeds 16-bit
        U = (result < -32768); // Underflow for negative
        EQ = (result == 0);    // Equality flag
        ODD = (result % 2 != 0); // Odd parity check
        if (result == 0) {
            D = true;  // Divide by zero flag if result is zero
        }
    }

    public void displayMemory(int address) {
        if (address >= 0 && address < SIZE) {
            System.out.println("Memory at address " + address + ": " + memoryArray[address]);
        } else {
            System.out.println("Invalid address!");
        }
    }

    public void handleIllegalOpcode(int opcode) {
        OP = true;
        System.out.println("Illegal opcode encountered! OP flag is set.");
    }

    public void handleTrapCode(int trapCode) {
        TR = true;
        System.out.println("Illegal trap code encountered! TR flag is set.");
    }

    public void runProcess(int address) {
        if (!M) {
            R = true;
            loadValue(address);
        }
    }

    public void storeButtonPressed(int address, int value) {
        storeValue(address, value);
    }

    public void storePlusButtonPressed(int address, int value) {
        storeValue(address, value);
        storeCount++;
        System.out.println("Store count incremented to " + storeCount);
    }

    public static void main(String[] args) {
        Memory memory = new Memory();
        memory.storeValue(1024, 0x1234); // Example of interacting with Memory directly
        memory.displayMemory(1024);
    }
}
