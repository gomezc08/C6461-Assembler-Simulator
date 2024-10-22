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
    private CPUExe cpuExe;

    public CPU(Memory memory, MemoryAddressRegister mar, MemoryBufferRegister mbr, GeneralPurposeRegisters gpr, IndexRegisters ixr, ProgramCounter pc) {
        this.memory = memory;
        this.mar = mar;
        this.mbr = mbr;
        this.gpr = gpr;  
        this.ixr = ixr;
        this.pc = pc;
        this.cpuExe = new CPUExe(memory, gpr, ixr, pc);
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
            // LDR: Done!
            case "000001":  
                System.out.println("LDR");
                return cpuExe.executeLoad(binaryInstruction);

            // STR: Done!
            case "000010": 
                System.out.println("STR");
                return cpuExe.executeStore(binaryInstruction);

            // LDA: Done!
            case "000011":  
                System.out.println("LDA");
                return cpuExe.executeLoadAddress(binaryInstruction);

            // LDX: Done!
            case "101001": 
                System.out.println("LDX");
                return cpuExe.executeLoadIndex(binaryInstruction);
            
            // STX: Done!
            case "101010":  
                System.out.println("STX");
                return cpuExe.executeStoreIndex(binaryInstruction);

            // JZ: Done!
            case "001010":  
                System.out.println("JZ");
                return cpuExe.executeJZ(binaryInstruction);
            
            // JNE: Done!
            case "001011":
                System.out.println("JNE");
                return cpuExe.executeJNE(binaryInstruction);
            
            // HLT: Done!
            case "000000":  
                return true;  // HLT - Stop execution

            default:
                throw new IllegalArgumentException("Unknown opcode: " + opcode);
        }
    }

    public int getPc() {
        return this.pc.getPC(); 
    }
    
    // the following 2 functions are for the backend gui.
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
        // INITIALIZE COMPONENTS
        Memory memory = new Memory();
        GeneralPurposeRegisters gpr = new GeneralPurposeRegisters(4); // 4 General Purpose Registers
        IndexRegisters ixr = new IndexRegisters(3); // 3 Index Registers
        ProgramCounter pc = new ProgramCounter();
        MemoryAddressRegister mar = new MemoryAddressRegister();
        MemoryBufferRegister mbr = new MemoryBufferRegister();
        pc.setPC(14); // Starting execution at address 14, based on .asm file
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
    
        // Execute instructions sequentially as per .asm file
        // Instruction 1: LDR 3,0,10
        System.out.println("Executing instruction at Address 14 (LDR 3,0,10)");
        String binaryInstruction1 = cpu.fetch();  // Fetch the instruction from memory
        cpu.decode(binaryInstruction1);  // Decode and execute the instruction
        System.out.println("GPR[3] after LDR: " + gpr.getGPR(3));
        System.out.println("\n\n");
    
        // Instruction 2: STR 3,0,20
        System.out.println("Executing instruction at Address 15 (STR 3,0,20)");
        pc.setPC(cpu.getPc());
        String binaryInstruction2 = cpu.fetch();  // Fetch the instruction from memory
        cpu.decode(binaryInstruction2);  // Decode and execute the instruction
        System.out.println("Memory at Address 20 after STR: " + memory.loadMemoryValue(20));
        System.out.println("\n\n");
    
        // Instruction 3: LDR 2,1,20,1
        System.out.println("Executing instruction at Address 16 (LDR 2,1,20,1)");
        pc.setPC(cpu.getPc());
        String binaryInstruction3 = cpu.fetch();  // Fetch the instruction from memory
        cpu.decode(binaryInstruction3);  // Decode and execute the instruction
        System.out.println("GPR[2] after LDR: " + gpr.getGPR(2));
        System.out.println("\n\n");
    
        // Instruction 4: LDA 1,0,6
        System.out.println("Executing instruction at Address 17 (LDA 1,0,6)");
        pc.setPC(cpu.getPc());
        String binaryInstruction4 = cpu.fetch();  // Fetch the instruction from memory
        cpu.decode(binaryInstruction4);  // Decode and execute the instruction
        System.out.println("GPR[1] after LDA: " + gpr.getGPR(1));
        System.out.println("\n\n");
    
        // Instruction 5: LDX 1,8
        System.out.println("Executing instruction at Address 18 (LDX 1,8)");
        pc.setPC(cpu.getPc());
        String binaryInstruction5 = cpu.fetch();  // Fetch the instruction from memory
        cpu.decode(binaryInstruction5);  // Decode and execute the instruction
        System.out.println("IXR[1] after LDX: " + ixr.getIndexRegister(1));
        System.out.println("\n\n");
    
        // Instruction 6: JNE 2,0,20
        System.out.println("Executing instruction at Address 19 (JNE 2,0,20)");
        pc.setPC(cpu.getPc());
        String binaryInstruction6 = cpu.fetch();  // Fetch the instruction from memory
        cpu.decode(binaryInstruction6);  // Decode and execute the instruction
        System.out.println("\n\n");
    
        // Instruction 7: JNE 3,0,18
        System.out.println("Executing instruction at Address 20 (JNE 3,0,18)");
        pc.setPC(cpu.getPc());
        String binaryInstruction7 = cpu.fetch();  // Fetch the instruction from memory
        cpu.decode(binaryInstruction7);  // Decode and execute the instruction
        System.out.println("\n\n");
    
        // Instruction 8: STX 1,20
        System.out.println("Executing instruction at Address 21 (STX 1,20)");
        pc.setPC(cpu.getPc());
        String binaryInstruction8 = cpu.fetch();  // Fetch the instruction from memory
        cpu.decode(binaryInstruction8);  // Decode and execute the instruction
        System.out.println("Memory at Address 20 after STX: " + memory.loadMemoryValue(20));
    
        // Final checks
        System.out.println("Final GPR Values:");
        System.out.println(gpr.toString());
        
        System.out.println("Final Index Register Values:");
        System.out.println(ixr.toString());
    }
    
}