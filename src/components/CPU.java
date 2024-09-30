package components;

public class CPU {
    private ProgramCounter pc;
    private Memory memory;
    private MemoryAddressRegister mar;
    private MemoryBufferRegister mbr;
    private InstructionRegister ir;
    private GeneralPurposeRegisters gprs;
    private IndexRegisters indexRegisters;  // Add Index Registers for effective address calculation

    public CPU(ProgramCounter pc, Memory memory, MemoryAddressRegister mar, MemoryBufferRegister mbr, InstructionRegister ir, GeneralPurposeRegisters gprs, IndexRegisters indexRegisters) {
        this.pc = pc;
        this.memory = memory;
        this.mar = mar;
        this.mbr = mbr;
        this.ir = ir;
        this.gprs = gprs;
        this.indexRegisters = indexRegisters;  // Initialize the index registers
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
        int ix = (instruction >> 3) & 0x3; // Extract the index register (2 bits)
        int i = (instruction >> 2) & 0x1; // Extract the indirect bit (1 bit)
        int address = instruction & 0x3F; // Extract the memory address (lower 6 bits)

        // Calculate Effective Address (EA) considering indexing and indirect addressing
        int effectiveAddress = calculateEffectiveAddress(address, ix, i);

        switch (opcode) {
            case 0x1: // Load (LDR)
                loadRegisterFromMemory(reg, effectiveAddress);
                break;
            case 0x2: // Store (STR)
                storeRegisterToMemory(reg, effectiveAddress);
                break;
            default:
                System.out.println("Unknown opcode: " + opcode);
                break;
        }
    }

    // Effective Address Calculation
    private int calculateEffectiveAddress(int address, int ix, int i) {
        int effectiveAddress = address;

        // Indexed Addressing (using Index Registers)
        if (ix > 0) {
            effectiveAddress += indexRegisters.getIndexRegister(ix - 1); // Index registers start from 0 in implementation
        }

        // Indirect Addressing (follow pointer)
        if (i == 1) {
            mar.setMAR(effectiveAddress); // Set MAR to the effective address
            effectiveAddress = memory.loadValue(mar.getMAR()); // Get value from memory for indirect addressing
        }

        return effectiveAddress;
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
