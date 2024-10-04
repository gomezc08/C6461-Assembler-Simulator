package components;

public class Playground {
    public static void main(String[] args) {
        // Initialize components
        ProgramCounter pc = new ProgramCounter();
        Memory memory = new Memory();
        MemoryAddressRegister mar = new MemoryAddressRegister();
        MemoryBufferRegister mbr = new MemoryBufferRegister();
        InstructionRegister ir = new InstructionRegister();
        GeneralPurposeRegisters gprs = new GeneralPurposeRegisters(4); // Assuming 4 GPRs
        IndexRegisters indexRegisters = new IndexRegisters(3); // Assuming 3 index registers
        ConditionCode cc = new ConditionCode(); // Condition codes

        // Initialize CPU with all components
        CPU cpu = new CPU(pc, memory, mar, mbr, ir, gprs, indexRegisters, cc);

        // Load instructions and data into memory using decimal values
        memory.storeValue(6, 4131);  // LDR R0, address 35 (load 1000 into R0)
        memory.storeValue(7, 8224);  // STR R0, address 36 with the indirect bit set to 0
        memory.storeValue(35, 1000); // Data at address 35
        memory.storeValue(36, 500);  // Data at address 36 (will be overwritten)

        // Set PC to 6 (to start from the correct instruction address)
        pc.setPC(6);

        // Display initial state
        System.out.println("Initial Memory at 35: " + memory.loadValue(35));
        System.out.println("Initial Memory at 36: " + memory.loadValue(36));
        System.out.println("Initial GPR[0]: " + gprs.getGPR(0));

        // Run the fetch-decode-execute cycle for 2 instructions (LDR, STR)
        for (int i = 0; i < 2; i++) {
            cpu.fetch();  // Fetch instruction
            cpu.decodeAndExecute();  // Decode and execute it
        }

        // Display final state
        System.out.println("\nFinal Memory at 35: " + memory.loadValue(35));  // Should remain 1000
        System.out.println("Final Memory at 36: " + memory.loadValue(36));  // Should be updated to 1000
        System.out.println("Final GPR[0]: " + gprs.getGPR(0));  // Should remain 1000
    }
}
