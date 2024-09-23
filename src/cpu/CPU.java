package cpu;

import bus.Bus;
import memory.Memory;
import registers.Register;

/**
 * The CPU class simulates the central processing unit (CPU) of a basic machine.
 * It coordinates instruction fetching, decoding, and execution by interacting with 
 * the memory, ALU, and other components. This class contains methods to run the
 * core fetch-decode-execute cycle.
 *
 * Functions:
 * - CPU(Memory memory, Bus bus): 
 *   Constructs a CPU object that initializes registers, the ALU, memory, and the bus.
 *   @param memory The memory instance where instructions and data are stored.
 *   @param bus The bus instance for data communication between components.
 *
 * - void fetch(): 
 *   Fetches the next instruction from memory using the PC (Program Counter), 
 *   loads it into the Instruction Register (IR), and increments the PC.
 *
 * - void decodeAndExecute(): 
 *   Decodes the current instruction in the IR and executes the corresponding action 
 *   by interacting with the ALU or memory.
 *
 * - void run(): 
 *   Continuously runs the fetch-decode-execute cycle in an infinite loop to simulate 
 *   the CPU execution of a program.
 *
 * - Register getRegister(int index): 
 *   Returns the general-purpose register (GPR) at the specified index.
 *   @param index The index of the register (0-3).
 *   @return The GPR at the specified index.
 *
 * - Register getIndexRegister(int index): 
 *   Returns the index register (IX) at the specified index.
 *   @param index The index of the index register (1-3).
 *   @return The IX register at the specified index.
 *
 * - int getPC(): 
 *   Returns the current value of the Program Counter (PC).
 *   @return The value of the PC.
 *
 * - void setPC(int value): 
 *   Sets the Program Counter (PC) to the specified value.
 *   @param value The new value of the PC.
 */

public class CPU {
    private int PC; // Program Counter
    private int IR; // Instruction Register
    private Register[] GPR; // General Purpose Registers (R0 - R3)
    private Register[] IX; // Index Registers (X1 - X3)
    private ALU alu;
    private Memory memory;
    private Bus bus;
    private ControlUnit controlUnit;

    /**
     * Constructs a CPU object that initializes registers, the ALU, memory, and the bus.
     * 
     * @param memory The memory instance where instructions and data are stored.
     * @param bus The bus instance for data communication between components.
     */
    public CPU(Memory memory, Bus bus) {
        this.PC = 0;
        this.IR = 0;
        this.GPR = new Register[4]; // R0 to R3
        this.IX = new Register[3];  // X1 to X3
        this.alu = new ALU();
        this.memory = memory;
        this.bus = bus;
        this.controlUnit = new ControlUnit(this, memory, bus); // Pass memory and bus to Control Unit

        // Initialize the registers
        for (int i = 0; i < 4; i++) {
            GPR[i] = new Register();
        }

        for (int i = 0; i < 3; i++) {
            IX[i] = new Register();
        }
    }

    /**
     * Fetches the next instruction from memory using the PC, loads it into the IR, 
     * and increments the PC to point to the next instruction.
     */
    public void fetch() {
        int mar = PC;
        int instruction = memory.read(mar); // Fetch instruction from memory
        IR = instruction; // Store instruction in IR
        PC++; // Move to next instruction
    }

    /**
     * Decodes the current instruction in the IR and executes the corresponding action 
     * by interacting with other components like the ALU or memory.
     */
    public void decodeAndExecute() {
        controlUnit.decodeAndExecute(IR); // Decode instruction and trigger execution
    }

    /**
     * Continuously runs the fetch-decode-execute cycle in an infinite loop to simulate 
     * the CPU execution of a program.
     */
    public void run() {
        while (true) { // Main CPU loop
            fetch();
            decodeAndExecute();
        }
    }

    /**
     * Returns the general-purpose register (GPR) at the specified index.
     * 
     * @param index The index of the register (0-3).
     * @return The GPR at the specified index.
     */
    public Register getRegister(int index) {
        if (index >= 0 && index < GPR.length) {
            return GPR[index];
        } else {
            throw new IllegalArgumentException("Invalid GPR index: " + index);
        }
    }

    /**
     * Returns the index register (IX) at the specified index.
     * 
     * @param index The index of the index register (1-3).
     * @return The IX register at the specified index.
     */
    public Register getIndexRegister(int index) {
        if (index >= 1 && index <= 3) {
            return IX[index - 1]; // IX is indexed from 1-3, internally it's 0-2
        } else {
            throw new IllegalArgumentException("Invalid IX index: " + index);
        }
    }

    /**
     * Returns the current value of the Program Counter (PC).
     * 
     * @return The value of the PC.
     */
    public int getPC() {
        return PC;
    }

    /**
     * Sets the Program Counter (PC) to the specified value.
     * 
     * @param value The new value of the PC.
     */
    public void setPC(int value) {
        this.PC = value;
    }
}
