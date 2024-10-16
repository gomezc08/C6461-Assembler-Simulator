package components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CPU {
    private Memory memory; // Memory object
    private MemoryAddressRegister mar; // Memory Address Register
    private MemoryBufferRegister mbr; // Memory Buffer Register
    private GeneralPurposeRegisters gprs; // General Purpose Registers
    private IndexRegisters ixr; // Index Registers
    private ProgramCounter pc; // Program Counter

    public CPU(Memory memory, MemoryAddressRegister mar, MemoryBufferRegister mbr, 
               GeneralPurposeRegisters gprs, IndexRegisters ixr, ProgramCounter pc) {
        this.memory = memory;
        this.mar = mar;
        this.mbr = mbr;
        this.gprs = gprs;
        this.ixr = ixr;
        this.pc = pc;
    }

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

    public void store(int address, int value) {
        mar.setValue((short) address); // Set MAR to the specified address
        memory.storeValue(mar.getValue(), value); // Store the value in memory at the address
    }

    public void resetRegisters() {
        gprs.resetAllGPRs(); // Reset all general-purpose registers
        ixr.resetAllIndexRegisters(); // Reset all index registers
        pc.reset(); // Reset the program counter
        mar.resetMAR(); // Reset memory address register
        mbr.resetMBR(); // Reset memory buffer register
    }
}


/*
package components;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class CPU {
    private ProgramCounter pc;
    private Memory memory;
    private MemoryAddressRegister mar;
    private MemoryBufferRegister mbr;
    private InstructionRegister ir;
    private GeneralPurposeRegisters gprs;
    private IndexRegisters indexRegisters;
    private ConditionCode cc;
    private MachineFaultRegister machineFaultRegister;

    // Set of valid opcodes for easier error handling.
    private static final Set<Integer> VALID_OPCODES = new HashSet<>();

    // for now, we are just worried about Load and Store (page 10 of documentation).
    static {
        VALID_OPCODES.add(0x1); // LDR
        VALID_OPCODES.add(0x2); // STR
    }

    public CPU(ProgramCounter pc, Memory memory, MemoryAddressRegister mar, MemoryBufferRegister mbr, InstructionRegister ir, GeneralPurposeRegisters gprs, IndexRegisters indexRegisters, ConditionCode cc, MachineFaultRegister machineFaultRegister) {
        this.pc = pc;
        this.memory = memory;
        this.mar = mar;
        this.mbr = mbr;
        this.ir = ir;
        this.gprs = gprs;
        this.indexRegisters = indexRegisters;
        this.cc = cc;
        this.machineFaultRegister = machineFaultRegister;
    }

    public void romLoader(String filePath) {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            int memoryAddress = 010; // Boot program starts at octal 10

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                short instruction = Short.parseShort(line, 8); // base 8 for octal
                memory.storeValue(memoryAddress, instruction); // Store instruction in memory
                memoryAddress++;
            }

            scanner.close();
            System.out.println("ROM loading complete.");
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    /* 
    // Fetch the next instruction from memory
    public void fetch() {
        System.out.println("\n=== FETCH PHASE ===");
        System.out.println("PC before fetch: " + pc.getPC());

        mar.setMAR(pc.getPC()); // Load the address from PC into MAR
        System.out.println("MAR set to: " + mar.getMAR());

        mbr.setMBR((short)memory.loadMemoryValue(mar.getMAR())); // Load instruction from memory into MBR
        System.out.println("Instruction loaded into MBR: " + mbr.getMBR());

        ir.setIR(mbr.getMBR()); // Load the instruction from MBR into IR
        System.out.println("Instruction loaded into IR: " + ir.getIR());

        pc.incrementPC(); // Increment PC to point to the next instruction
        System.out.println("PC after fetch: " + pc.getPC());
    }

    // Decode and execute the instruction
    public boolean decodeAndExecute() {
        System.out.println("\n=== DECODE AND EXECUTE PHASE ===");
        int instruction = ir.getIR();
        System.out.println("Instruction to decode: " + instruction);

        // turn the instruction into its binary representation.
        String binaryString = String.format("%16s", Integer.toBinaryString(instruction)).replace(' ', '0');
        System.out.println("here is the binary: " + binaryString);

        // now grab the components.
        // Extract components
        String opcodeStr = binaryString.substring(0, 6);
        String registerStr = binaryString.substring(6, 8);
        String ixStr = binaryString.substring(8, 10);
        String iStr = binaryString.substring(10, 11);
        String addressStr = binaryString.substring(11);

        // Convert to integers
        int opcode = Integer.parseInt(opcodeStr, 2);
        int reg = Integer.parseInt(registerStr, 2);
        int ix = Integer.parseInt(ixStr, 2);
        int i = Integer.parseInt(iStr, 2);
        int address = Integer.parseInt(addressStr, 2);

        // Print results
        System.out.println("Opcode (bits 0-5): " + opcode);
        System.out.println("Register (bits 6-7): " + reg);
        System.out.println("IX (bits 8-9): " + ix);
        System.out.println("I (bit 10): " + i);
        System.out.println("Address (bits 11-15): " + address);

        System.out.println("lets load the value from memory cause why not: " + memory.loadMemoryValue(address));

        // Check if the opcode is valid
        if (!isValidOpcode(opcode)) {
            System.out.println("Error: Invalid Opcode Detected - Opcode: " + opcode);
            machineFaultRegister.triggerFault("Invalid Opcode");
            return true; // Halt on invalid opcode
        }

        // Calculate Effective Address (EA)
        int effectiveAddress = calculateEffectiveAddress(address, ix, i);
        System.out.println("Effective Address: " + effectiveAddress);

        // Switch based on opcode
        switch (opcode) {
            case 0x0:
                if (instruction == 0) {  // Ensure the full instruction is zero for HLT
                    System.out.println("HLT (Halt) instruction encountered. Halting execution.");
                    return true;
                }
                System.out.println("Unknown instruction with opcode 0 encountered. Halting due to error.");
                return true;
            case 0x1: // Load (LDR)
                loadRegisterFromMemory(reg, effectiveAddress);
                break;
            case 0x2: // Store (STR)
                storeRegisterToMemory(reg, effectiveAddress);
                break;
            default:
                //memory.handleIllegalOpcode(opcode); // Handle unknown opcode
                System.out.println("Unknown opcode encountered: " + opcode);
                break;
        }

        return false; // Continue execution unless HLT is encountered
    }
        

    // Validate if the opcode is part of the valid set
    private boolean isValidOpcode(int opcode) {
        return VALID_OPCODES.contains(opcode);
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
            //nvjejcnenj 
            //mar.setMAR(effectiveAddress); // Set MAR to the effective address
            effectiveAddress = memory.loadMemoryValue(mar.getMAR()); // Follow pointer to indirect address
            System.out.println("Effective Address after Indirect Addressing: " + effectiveAddress);
            int x = memory.loadMemoryValue(address);
            System.out.println("here is my attempt at address: " + memory.loadMemoryValue(x));
        }

        return effectiveAddress;
    }
        

    // Load value from memory into the given register
    private void loadRegisterFromMemory(int reg, int address) {
        System.out.println("Loading value from memory at address: " + address);
        mar.setMAR(address); // Set MAR to the effective address
        int value = memory.loadMemoryValue(mar.getMAR()); // Load value from memory into MBR
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

*/