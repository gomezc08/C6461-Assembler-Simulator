package components;

/*
 * CPU: Manages the main components of a computer simulator, including the memory, registers, 
 * cache, and condition codes. This class controls the Fetch-Decode-Execute cycle and provides
 * methods to load ROM files, store values in memory, and reset registers. Each instruction is
 * decoded and executed through direct calls to the CPUExe class.
 * 
 * CPU(Memory memory, MemoryAddressRegister mar, MemoryBufferRegister mbr, GeneralPurposeRegisters gpr, 
 * IndexRegisters ixr, ProgramCounter pc, ConditionCode cc, Cache cac): Constructs the CPU with all required components.
 * - @param memory: The memory instance connected to the CPU.
 * - @param mar: Memory Address Register for memory addressing.
 * - @param mbr: Memory Buffer Register for temporary data storage.
 * - @param gpr: General Purpose Registers for various computations.
 * - @param ixr: Index Registers for indexed addressing.
 * - @param pc: Program Counter to track the address of the next instruction.
 * - @param cc: Condition Code register to handle flags like overflow and zero.
 * - @param cac: Cache instance for storing frequently accessed data.
 * 
 * loadROMFile(File file): Loads a ROM file into memory, parsing address-data pairs in octal format.
 * - @param file: The file containing the ROM instructions.
 * - @throws IOException: If an error occurs while reading the file.
 * 
 * getCache(): Returns the Cache instance connected to the CPU.
 * - @return Cache: The cache instance associated with the CPU.
 * 
 * run(): Executes the Fetch-Decode-Execute cycle until a HLT instruction is encountered.
 * 
 * fetch(): Retrieves the next instruction from memory based on the current Program Counter (PC).
 * - @return String: The binary string representation of the instruction.
 * 
 * decode(String binaryInstruction): Decodes and executes a binary instruction based on the opcode.
 * - @param binaryInstruction: The binary string of the instruction to decode and execute.
 * - @return boolean: True if a HLT instruction is encountered, ending execution.
 * 
 * getPc(): Retrieves the current value of the Program Counter (PC).
 * - @return int: The current PC value.
 * 
 * store(int address, int value): Stores a value in memory and updates the cache with the given address.
 * - @param address: The memory address to store the value.
 * - @param value: The value to store at the specified address.
 * 
 * resetRegisters(): Resets all general-purpose registers, index registers, and control registers 
 * (PC, MAR, MBR) to their initial state.
 */

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
    private Cache cache;

    public CPU(Memory memory, MemoryAddressRegister mar, MemoryBufferRegister mbr, GeneralPurposeRegisters gpr, IndexRegisters ixr, ProgramCounter pc, ConditionCode cc, Cache cac) {
        this.memory = memory;
        this.mar = mar;
        this.mbr = mbr;
        this.gpr = gpr;  
        this.ixr = ixr;
        this.pc = pc;
        this.cc = cc;
        this.cache = cac;
        this.cpuExe = new CPUExe(memory, gpr, ixr, pc, cc, cac);
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

    public Cache getCache() {
        return this.cache;
    }    

    // Fetch-Decode-Execute Cycle.
    public void run() {
        boolean halt = false;
        while (!halt) {
            String binaryInstruction = fetch();
            halt = decode(binaryInstruction);
        }
    }

    private String fetch() {
        // First check if current PC has a LOC directive
        int currentPC = pc.getPC();
        
        if (pc.hasLocDirective(currentPC)) {
            int newLocation = pc.getLocTarget(currentPC);
            pc.setPC(newLocation);
            return fetch(); // Fetch from new location
        }
        
        // Load instruction from memory
        int instruction = memory.loadMemoryValue(currentPC);
        
        // Increment PC for next instruction
        pc.incrementPC();
        
        // Convert to binary
        String binaryInstruction = Integer.toBinaryString(instruction);
        while (binaryInstruction.length() < 16) {
            binaryInstruction = "0" + binaryInstruction;
        }
        
        return binaryInstruction;
    }


    // Decode and execute the instruction directly
    private boolean decode(String binaryInstruction) {
        String opcode = binaryInstruction.substring(0, 6);  // Extract opcode (first 6 bits)
        switch (opcode) {
            // LDR: Done!
            case "000001":  
                //System.out.println("LDR");
                return cpuExe.executeLDR(binaryInstruction);

            // STR: Done!
            case "000010": 
            //System.out.println("STR");
                return cpuExe.executeSTR(binaryInstruction);

            // LDA: Done!
            case "000011":  
            //System.out.println("LDA");
                return cpuExe.executeLDA(binaryInstruction);

            // AMR: Done!
            case "000100":  
                //System.out.println("AMR");
                return cpuExe.executeAMR(binaryInstruction);
            
            // SMR: Done!
            case "000101":  
                //System.out.println("SMR");
                return cpuExe.executeSMR(binaryInstruction);

            // AIR: Done!
            case "000110":  
                //System.out.println("AIR");
                return cpuExe.executeAIR(binaryInstruction);    
              
            // SIR: Not Done.
            case "000111":
                //System.out.println("SIR");      
                return cpuExe.executeSIR(binaryInstruction);    

            // LDX: Done!
            case "101001": 
                //System.out.println("LDX");
                return cpuExe.executeLDX(binaryInstruction);
            
            // STX: Done!
            case "101010":  
                //System.out.println("STX");
                return cpuExe.executeSTX(binaryInstruction);

            // JZ: Done!
            case "001010":  
                //System.out.println("JZ");
                return cpuExe.executeJZ(binaryInstruction);
            
            // JNE: Done!
            case "001011":
                //System.out.println("JNE");
                return cpuExe.executeJNE(binaryInstruction);
            
            // JCC: Done (not 100% confident on this one).
            case "001100":
                //System.out.println("JCC");
                return cpuExe.executeJCC(binaryInstruction, pc, cc);
            
            // JMA: Done!
            case "001101":
                //System.out.println("JMA");
                return cpuExe.executeJMA(binaryInstruction);
            
            // JSR: Done!
            case "001110":
                //System.out.println("JSR");
                return cpuExe.executeJSR(binaryInstruction);
            
            // RFS: Done but can't test (dont know about subroutines).
            case "001111":
                //System.out.println("RFS");
                return cpuExe.executeRFS(binaryInstruction);

            // SOB: Done but not tested properly.
            case "010000":
                //System.out.println("SOB");
                return cpuExe.executeSOB(binaryInstruction);

            // JGE: Done!
            case "010001":
                //System.out.println("JGE");
                return cpuExe.executeJGE(binaryInstruction);

            // MLT: Done! 
            case "111000":
                //System.out.println("MLT");
                return cpuExe.executeMLT(binaryInstruction);

            // DVD: Done!
            case "111001":
                //System.out.println("DVD");
                return cpuExe.executeDVD(binaryInstruction);

            // TRR: Done!
            case "111010":
                //System.out.println("TRR");
                return cpuExe.executeTRR(binaryInstruction);

            // AND: Done!
            case "111011":
                //System.out.println("AND");
                return cpuExe.executeAND(binaryInstruction);

            // ORR: Done!
            case "111100":
                //System.out.println("ORR");
                return cpuExe.executeORR(binaryInstruction);

            // NOT: Done!
            case "111101":
                //System.out.println("NOT");
                return cpuExe.executeNOT(binaryInstruction);

            // IN: Done!
            case "110011":
                //System.out.println("IN");
                return cpuExe.executeIN(binaryInstruction);

            // OUT: Done!
            case "110100":
                //System.out.println("OUT");
                return cpuExe.executeOUT(binaryInstruction);

            // CHK: Done!
            case "110101":
                //System.out.println("CHK");
                return cpuExe.executeCHK(binaryInstruction);

            // HLT + Data: Done!
            case "000000":  
                // HLT.
                if(Assembler.getHltAddress() == pc.getPC()-1) {
                    return true;
                    //return cpuExe.executeHLT();
                }

                // data.
                return false;

            default:
                throw new IllegalArgumentException("Unknown opcode: " + opcode);
        }
    }

    public int getPc() {
        return this.pc.getPC(); 
    }
    
    public void store(int address, int value) {
        memory.storeValue(address, value);
        cache.write(address, value);
    }

    public void resetRegisters() {
        gpr.resetAllGPRs(); // Reset all general-purpose registers
        ixr.resetAllIndexRegisters(); // Reset all index registers
        pc.reset(); // Reset the program counter
        mar.resetMAR(); // Reset memory address register
        mbr.resetMBR(); // Reset memory buffer register
    } 
}