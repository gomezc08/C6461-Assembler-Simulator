package components;

public class CPU {
    private ProgramCounter pc;
    private Memory memory;
    private MemoryAddressRegister mar;
    private MemoryBufferRegister mbr;
    private InstructionRegister ir;
    private GeneralPurposeRegisters gprs;
    
    public CPU(ProgramCounter pc, Memory memory, MemoryAddressRegister mar, MemoryBufferRegister mbr, InstructionRegister ir, GeneralPurposeRegisters gprs) {
        this.pc = pc;
        this.memory = memory;
        this.mar = mar;
        this.mbr = mbr;
        this.ir = ir;
        this.gprs = gprs;
    }

    // Fetch the next instruction from memory
    public void fetch() {
        mar.setMAR(pc.getPC()); // Load the address from PC into MAR
        mbr.setMBR(memory.loadValue(mar.getMAR())); // Load instruction from memory into MBR
        ir.setIR(mbr.getMBR()); // Load the instruction from MBR into IR
        pc.incrementPC(); // Increment PC to point to the next instruction
    }

    // Decode and execute the instruction
    public void decodeAndExecute() {
        int instruction = ir.getIR(); // Get the instruction from IR
        int opcode = (instruction >> 12) & 0xF; // Extract the opcode (upper 4 bits)
        int reg = (instruction >> 6) & 0x7; // Extract the register (middle 3 bits)
        int address = instruction & 0xFFF; // Extract the memory address (lower 12 bits)

        switch (opcode) {
            case 0x1: // Example: Load (LDR)
                loadRegisterFromMemory(reg, address);
                break;
            case 0x2: // Example: Store (STR)
                storeRegisterToMemory(reg, address);
                break;
            default:
                System.out.println("Unknown opcode: " + opcode);
                break;
        }
    }

    // Load value from memory into the given register
    private void loadRegisterFromMemory(int reg, int address) {
        mar.setMAR(address); // Set MAR to the effective address
        int value = memory.loadValue(mar.getMAR()); // Load value from memory into MBR
        gprs.setGPR(reg, (short) value); // Store the value in the specified register
        System.out.println("Loaded value " + value + " into GPR[" + reg + "]");
    }

    // Store value from the given register into memory
    private void storeRegisterToMemory(int reg, int address) {
        mar.setMAR(address); // Set MAR to the effective address
        int value = gprs.getGPR(reg); // Get value from the specified register
        memory.storeValue(mar.getMAR(), value); // Store the value in memory at MAR address
        System.out.println("Stored value " + value + " from GPR[" + reg + "] into memory");
    }
}
