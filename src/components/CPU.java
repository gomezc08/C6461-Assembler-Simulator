package components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import Assembler.Assembler;

public class CPU {
    private Memory memory;
    private GeneralPurposeRegisters gpr;  // Consistent name for GPR
    private IndexRegisters ixr;
    private ProgramCounter pc;  // Program Counter
    private ConditionCode cc;
    private MemoryAddressRegister mar; // Memory Address Register
    private MemoryBufferRegister mbr; // Memory Buffer Register

    public CPU(Memory memory, MemoryAddressRegister mar, MemoryBufferRegister mbr, GeneralPurposeRegisters gpr, IndexRegisters ixr, ProgramCounter pc) {
        this.memory = memory;
        this.mar = mar;
        this.mbr = mbr;
        this.gpr = gpr;  
        this.ixr = ixr;
        this.pc = pc;
    }   

    // loads rom file.
    public void loadROMFile(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");  // Assuming space separates address and data
                int address = Integer.parseInt(parts[0], 8);  // Address part
                int data = Integer.parseInt(parts[1], 8);     // Data part

                memory.storeValue(address, data);
            }
        }
    }


    // Fetch-Decode-Execute Cycle.
    public void run() {
        boolean halt = false;
        while (!halt) {
            String binaryInstruction = fetch();
            halt = decode(binaryInstruction);
        }
    }

    // Fetch the instruction from memory
    private String fetch() {
        int instruction = memory.loadMemoryValue(pc.getPC());
        pc.incrementPC();
        
        // Convert the octal instruction to binary, ensuring 16 bits
        // First convert to a proper binary integer
        String binaryInstruction = Integer.toBinaryString(instruction);
        
        // Pad to 16 bits
        while (binaryInstruction.length() < 16) {
            binaryInstruction = "0" + binaryInstruction;
        }
        
        System.out.println("Fetched Instruction (octal): " + Integer.toOctalString(instruction));
        System.out.println("Fetched Instruction (binary): " + binaryInstruction);
        
        return binaryInstruction;
    }

    // Decode and execute the instruction directly
    private boolean decode(String binaryInstruction) {
        String opcode = binaryInstruction.substring(0, 6);  // Extract opcode (first 6 bits)
        System.out.print("We will be executing: ");

        switch (opcode) {
            // LDR: Works!
            case "000001":  
                System.out.println("LDR");
                return executeLoad(binaryInstruction);
            // STR: Works!
            case "000010": 
                System.out.println("STR");
                return executeStore(binaryInstruction);
    
            // HLT: Works!
            case "000000":  
                return true;  // Stop execution

            default:
                throw new IllegalArgumentException("Unknown opcode: " + opcode);
        }
    }

    private boolean executeLoad(String binaryInstruction) {
        // extract instructions.
        String reg_str = binaryInstruction.substring(6, 8);  
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  
        String address_str = binaryInstruction.substring(11, 16);  

        int reg = Integer.parseInt(reg_str, 2);
        int ix = Integer.parseInt(ix_str, 2);
        int iBit = Integer.parseInt(iBit_str, 2);
        int address = Integer.parseInt(address_str, 2);

        System.out.println("opcode: " + binaryInstruction.substring(0, 6));  
        System.out.println("reg: " + reg);
        System.out.println("ix: " + ix);
        System.out.println("i: " + iBit);
        System.out.println("address: " + address);
    
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  // Calculate effective address
        System.out.println("Effective Address (Ea): " + ea);
        
        int value = memory.loadMemoryValue(ea);  // Load the value from memory
        System.out.println("Value from memory: " + value);
    
        gpr.setGPR(reg, (short) value); 
        System.out.println("Updated GPR[" + reg + "] with value: " + value);
        return false;  // Continue execution
    }

    private boolean executeStore(String binaryInstruction) {
        // extract instructions.
        String reg_str = binaryInstruction.substring(6, 8);  
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  
        String address_str = binaryInstruction.substring(11, 16); 

        int reg = Integer.parseInt(reg_str, 2);
        int ix = Integer.parseInt(ix_str, 2);
        int iBit = Integer.parseInt(iBit_str, 2);
        int address = Integer.parseInt(address_str, 2);

        System.out.println("opcode: " + binaryInstruction.substring(0, 6));  
        System.out.println("reg: " + reg);
        System.out.println("ix: " + ix);
        System.out.println("i: " + iBit);
        System.out.println("address: " + address);

        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
        System.out.println("Effective Address (Ea): " + ea);

        int value = gpr.getGPR(reg); 
        System.out.println("Value from GPR" + reg + ": "  + value);

        memory.storeValue(ea, value);  // Store the value into memory
        System.out.println("Loaded into memory -> Address: " + address + ", Data: " + value);
        return false;  // Continue execution
    }

    // Helper function for calculating effective address
    private int calculateEffectiveAddress(String ix, String iBit, String address) {
        int baseAddress = Integer.parseInt(address, 2);  // Convert address bits to integer

        if (!ix.equals("00")) {  // If IX register is used
            int indexValue = ixr.getIndexRegister(Integer.parseInt(ix, 2));  // Get the index register value
            baseAddress += indexValue;
        }

        if (iBit.equals("1")) {  // If indirect addressing is used
            baseAddress = memory.loadMemoryValue(baseAddress);  // Use memory indirection
        }

        return baseAddress;  // Return the calculated effective address
    }


    public int getPc() {
        return this.pc.getPC(); 
    }
    
    public void store(int address, int value) {
        mar.setValue((short) address); // Set MAR to the specified address
        memory.storeValue(mar.getValue(), value); // Store the value in memory at the address
    }

    public void resetRegisters() {
        gpr.resetAllGPRs(); // Reset all general-purpose registers
        ixr.resetAllIndexRegisters(); // Reset all index registers
        pc.reset(); // Reset the program counter
        mar.resetMAR(); // Reset memory address register
        mbr.resetMBR(); // Reset memory buffer register
    }

    public static void main(String[] args) {
        // INITIALIZE.
        Memory memory = new Memory();
        GeneralPurposeRegisters gpr = new GeneralPurposeRegisters(4); // 4 General Purpose Registers
        IndexRegisters ixr = new IndexRegisters(3); // 3 Index Registers
        ProgramCounter pc = new ProgramCounter();
        MemoryAddressRegister mar = new MemoryAddressRegister();
        MemoryBufferRegister mbr = new MemoryBufferRegister();
        pc.setPC(14);
        CPU cpu = new CPU(memory, mar, mbr, gpr, ixr, pc);
    
        try {
            File romFile = new File("output/Kishan.ld");
    
            // Step 1: Assemble the Kishan.asm file...
            Assembler.run("assembly/Kishan.asm");
    
            // Step 2: Load the ROM file into memory.
            cpu.loadROMFile(romFile);
    
            // Print the loaded memory content for debugging.
            System.out.println("Memory Content after loading the ROM file:");
            for (int i = 6; i <= 17; i++) {
                System.out.println("Memory Address " + i + ": " + memory.loadMemoryValue(i));
            }
        } catch (IOException e) {
            System.out.println("Error during assembly: " + e.getMessage());
        }
        System.out.println("\n\n");

        // Perform on LDR 3,0,10
        System.out.println("Executing instruction at Address 14 (LDR 3,0,10)");
        String binaryInstruction1 = cpu.fetch();  // Fetch the instruction from memory
        cpu.decode(binaryInstruction1);  // Decode and execute the instruction
        System.out.println("GPR[3] after LDR: " + gpr.getGPR(3));
        System.out.println("\n\n");
    
        // Perform on STR 3,0,20
        System.out.println("Executing instruction at Address 15 (STR 3,0,20)");
        pc.setPC(cpu.getPc());
        String binaryInstruction2 = cpu.fetch();  // Fetch the instruction from memory
        cpu.decode(binaryInstruction2);  // Decode and execute the instruction
        System.out.println("Memory at Address 16 after STR: " + Integer.toOctalString(memory.loadMemoryValue(16)));
        System.out.println("\n\n");
    
        // Perform on LDR 2,0,20
        System.out.println("Executing instruction at Address 16 (LDR 2,1,20,1)");
        pc.setPC(cpu.getPc());
        String binaryInstruction3 = cpu.fetch();  // Fetch the instruction from memory
        cpu.decode(binaryInstruction3);  // Decode and execute the instruction
        System.out.println("GPR[2] after LDR: " + gpr.getGPR(2));
        System.out.println("\n\n");
    
        // Final checks
        System.out.println("Final GPR Values:");
        System.out.println(gpr.toString());
    
        System.out.println("Final Index Register Values:");
        System.out.println(ixr.toString());
    
    }
}