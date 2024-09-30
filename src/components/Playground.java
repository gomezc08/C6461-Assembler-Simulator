package components;

public class Playground {
    public static void main(String[] args) {
        // Initialize components
        ProgramCounter pc = new ProgramCounter();
        Memory memory = new Memory();
        MemoryAddressRegister mar = new MemoryAddressRegister();
        MemoryBufferRegister mbr = new MemoryBufferRegister();
        InstructionRegister ir = new InstructionRegister();
        GeneralPurposeRegisters gprs = new GeneralPurposeRegisters(4);
        CPU cpu = new CPU(pc, memory, mar, mbr, ir, gprs);

        // Load a simple program into memory (example instructions)
        // Instruction format: opcode(4 bits), reg(3 bits), address(12 bits)
        // For simplicity: 
        // 0x1234 -> LDR R1, address 0x034  (opcode 1 for LDR, R1 = reg 1)
        // 0x2345 -> STR R2, address 0x045  (opcode 2 for STR, R2 = reg 2)
        memory.storeValue(0, 0x1234);  // Instruction at address 0
        memory.storeValue(1, 0x2345);  // Instruction at address 1
        memory.storeValue(0x34, 1000); // Data at memory address 0x34 (52 in decimal)

        // Display initial memory and register states
        System.out.println("Initial Memory at 0x34: " + memory.loadValue(0x34));
        System.out.println("Initial GPR[1]: " + gprs.getGPR(1));

        // Start CPU Fetch-Decode-Execute cycle
        for (int i = 0; i < 2; i++) {  // Simulate 2 instructions
            cpu.fetch();  // Fetch the instruction
            cpu.decodeAndExecute();  // Decode and execute it
        }

        // Display the state of registers and memory after execution
        System.out.println("\nAfter execution:");
        System.out.println("Memory at 0x45: " + memory.loadValue(0x45));
        System.out.println("GPR[1]: " + gprs.getGPR(1));
        System.out.println("GPR[2]: " + gprs.getGPR(2));
    }
}
