package cpu;

import memory.Memory;
import registers.Register;
import bus.Bus;

/**
 * The ControlUnit class manages the decoding and execution of octal machine code 
 * instructions during runtime. It takes an octal instruction from the Instruction Register (IR), 
 * decodes its opcode, and directs the CPU to perform the required operation.
 *
 * Functions:
 * - ControlUnit(CPU cpu): 
 *   Constructs a ControlUnit object responsible for decoding and executing instructions.
 *   @param cpu The CPU object that the control unit manages.
 *
 * - void decodeAndExecute(int instruction): 
 *   Decodes the given octal instruction and executes the corresponding operation 
 *   by interacting with the ALU, registers, and memory.
 *   @param instruction The octal instruction to decode and execute.
 *
 * - private int extractOpcode(int instruction): 
 *   Extracts the opcode from the octal instruction.
 *   @param instruction The octal instruction.
 *   @return The opcode extracted from the instruction.
 *
 * - private int extractRegister(int instruction): 
 *   Extracts the register number from the instruction.
 *   @param instruction The octal instruction.
 *   @return The register number extracted from the instruction.
 *
 * - private int extractAddress(int instruction): 
 *   Extracts the memory address from the instruction.
 *   @param instruction The octal instruction.
 *   @return The memory address extracted from the instruction.
 */
public class ControlUnit {
    private CPU cpu;
    private Memory memory;
    private Bus bus;

    /**
     * Constructs a ControlUnit object responsible for decoding and executing instructions.
     * 
     * @param cpu The CPU object that the control unit manages.
     */
    public ControlUnit(CPU cpu, Memory memory, Bus bus) {
        this.cpu = cpu;
        this.memory = memory;
        this.bus = bus;
    }

    /**
     * Decodes the given octal instruction and executes the corresponding operation.
     * 
     * @param instruction The octal instruction to decode and execute.
     */
    public void decodeAndExecute(int instruction) {
        int opcode = extractOpcode(instruction);

        switch (opcode) {
            // Example: LDX operation (octal 10)
            case 0x10: 
                int indexRegister = extractRegister(instruction);
                int address = extractAddress(instruction);
                loadIndexRegister(indexRegister, address);
                break;
            
            // JZ operation
            case 0x25: 
                jumpIfZero(instruction);
                break;
            // More opcodes for other operations (e.g., MLT, NOT, etc.)
            default:
                System.out.println("Unknown instruction!");
        }
    }

    /**
     * Extracts the opcode from the octal instruction.
     * 
     * @param instruction The octal instruction.
     * @return The opcode extracted from the instruction.
     */
    private int extractOpcode(int instruction) {
        return (instruction & 0xFC00) >> 10; // Extract opcode (adjust for octal)
    }

    /**
     * Extracts the register number from the instruction.
     * 
     * @param instruction The octal instruction.
     * @return The register number extracted from the instruction.
     */
    private int extractRegister(int instruction) {
        return (instruction & 0x03C0) >> 6; // Extract register (assuming bits 6-9 for register)
    }

    /**
     * Extracts the address from the instruction.
     * 
     * @param instruction The octal instruction.
     * @return The memory address extracted from the instruction.
     */
    private int extractAddress(int instruction) {
        return instruction & 0x003F; // Extract address (assuming bits 0-5 for address)
    }

    /**
     * Loads data from the specified memory address into the specified index register.
     * 
     * @param indexRegister The index register to load data into.
     * @param address The memory address to load data from.
     */
    private void loadIndexRegister(int indexRegister, int address) {
        int data = memory.read(address); // Fetch data from memory
        cpu.getIndexRegister(indexRegister).setValue(data); // Store data in the index register
    }

    /**
     * Performs a conditional jump if the specified register contains zero.
     * 
     * @param instruction The JZ (Jump if Zero) instruction to execute.
     */
    private void jumpIfZero(int instruction) {
        int register = extractRegister(instruction);
        int address = extractAddress(instruction);

        if (cpu.getRegister(register).getValue() == 0) {
            cpu.setPC(address); // Set Program Counter to the address if register is zero
        }
    }
}