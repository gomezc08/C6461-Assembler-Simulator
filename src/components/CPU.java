package components;

public class CPU {
    private ProgramCounter pc;
    private Memory memory;
    private MemoryAddressRegister mar;
    private MemoryBufferRegister mbr;
    private InstructionRegister ir;
    private GeneralPurposeRegisters gprs;
    private IndexRegisters indexRegisters;
    private ConditionCode cc;

    public CPU(ProgramCounter pc, Memory memory, MemoryAddressRegister mar, MemoryBufferRegister mbr, InstructionRegister ir, GeneralPurposeRegisters gprs, IndexRegisters indexRegisters, ConditionCode cc) {
        this.pc = pc;
        this.memory = memory;
        this.mar = mar;
        this.mbr = mbr;
        this.ir = ir;
        this.gprs = gprs;
        this.indexRegisters = indexRegisters;
        this.cc = cc;
    }

    // Fetch the next instruction from memory
    public void fetch() {
        System.out.println("=== FETCH PHASE ===");
        System.out.println("PC before fetch: " + pc.getPC());

        mar.setMAR(pc.getPC()); // Load the address from PC into MAR
        System.out.println("MAR set to: " + mar.getMAR());

        mbr.setMBR(memory.loadValue(mar.getMAR())); // Load instruction from memory into MBR
        System.out.println("Instruction loaded into MBR: " + mbr.getMBR());

        ir.setIR(mbr.getMBR()); // Load the instruction from MBR into IR
        System.out.println("Instruction loaded into IR: " + ir.getIR());

        pc.incrementPC(); // Increment PC to point to the next instruction
        System.out.println("PC after fetch: " + pc.getPC());
    }

    // Decode and execute the instruction
    public void decodeAndExecute() {
        System.out.println("=== DECODE AND EXECUTE PHASE ===");
        int instruction = ir.getIR(); // Get the instruction from IR
        System.out.println("Instruction to decode: " + instruction);

        int opcode = (instruction >> 12) & 0xF; // Extract the opcode (upper 4 bits)
        int reg = (instruction >> 6) & 0x7; // Extract the register (middle 3 bits)
        int ix = (instruction >> 3) & 0x3; // Extract the index register (2 bits)
        int i = (instruction >> 2) & 0x1; // Extract the indirect bit (1 bit)
        int address = instruction & 0xFFF; // Extract the memory address (lower 12 bits)

        System.out.println("Decoded opcode: " + opcode);
        System.out.println("Decoded register: " + reg);
        System.out.println("Decoded index register: " + ix);
        System.out.println("Decoded indirect bit: " + i);
        System.out.println("Decoded memory address: " + address);

        // Calculate Effective Address (EA)
        int effectiveAddress = calculateEffectiveAddress(address, ix, i);
        System.out.println("Effective Address: " + effectiveAddress);

        switch (opcode) {
            case 0x1: // Load (LDR)
                loadRegisterFromMemory(reg, effectiveAddress);
                break;
            case 0x2: // Store (STR)
                storeRegisterToMemory(reg, effectiveAddress);
                break;
            default:
                memory.handleIllegalOpcode(opcode); // Handle unknown opcode
                System.out.println("Unknown opcode encountered: " + opcode);
                break;
        }
    }

    // Calculate Effective Address (EA)
    private int calculateEffectiveAddress(int address, int ix, int i) {
        int effectiveAddress = address;

        // Indexed Addressing (use Index Registers)
        if (ix > 0) {
            effectiveAddress += indexRegisters.getIndexRegister(ix - 1);
            System.out.println("Effective Address after Indexing: " + effectiveAddress);
        }

        // Indirect Addressing (follow pointer)
        if (i == 1) {
            mar.setMAR(effectiveAddress); // Set MAR to the effective address
            effectiveAddress = memory.loadValue(mar.getMAR()); // Follow pointer to indirect address
            System.out.println("Effective Address after Indirect Addressing: " + effectiveAddress);
        }

        return effectiveAddress;
    }

    // Load value from memory into the given register
    private void loadRegisterFromMemory(int reg, int address) {
        System.out.println("Loading value from memory at address: " + address);
        mar.setMAR(address); // Set MAR to the effective address
        int value = memory.loadValue(mar.getMAR()); // Load value from memory into MBR
        gprs.setGPR(reg, (short) value); // Store the value in the specified register
        cc.updateConditionCodes(value); // Update condition codes based on the loaded value
        System.out.println("Loaded value " + value + " into GPR[" + reg + "]");
    }

    // Store value from the given register into memory
    private void storeRegisterToMemory(int reg, int address) {
        System.out.println("Storing value from GPR[" + reg + "] into memory at address: " + address);
        mar.setMAR(address); // Set MAR to the effective address
        int value = gprs.getGPR(reg); // Get value from the specified register
        memory.storeValue(mar.getMAR(), value); // Store the value in memory at MAR address
        cc.updateConditionCodes(value); // Update condition codes based on the stored value
        System.out.println("Stored value " + value + " from GPR[" + reg + "] into memory at address: " + address);
    }

    
    
}
