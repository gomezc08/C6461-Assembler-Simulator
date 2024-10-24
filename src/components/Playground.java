package components;

import Assembler.Assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class Playground {
    public static void main(String[] args) {
        System.out.println("Playground not functional for part 1 of project");
    }
}
/*
public class Playground {

    private CPU cpu;
    private Memory memory;
    private ProgramCounter pc;

    public Playground() {
        // Initialize components
        pc = new ProgramCounter();
        memory = new Memory();
        MemoryAddressRegister mar = new MemoryAddressRegister();
        MemoryBufferRegister mbr = new MemoryBufferRegister();
        InstructionRegister ir = new InstructionRegister();
        GeneralPurposeRegisters gprs = new GeneralPurposeRegisters(4); // Assuming 4 GPRs
        IndexRegisters indexRegisters = new IndexRegisters(3); // Assuming 3 index registers
        ConditionCode cc = new ConditionCode(); // Condition codes
        MachineFaultRegister machineFaultRegister = new MachineFaultRegister(); // Add MachineFaultRegister

        // Initialize CPU with all components
        cpu = new CPU(pc, memory, mar, mbr, ir, gprs, indexRegisters, cc, machineFaultRegister); // Pass machineFaultRegister
    }

    public void assembleAndLoadProgram(String asmFilePath) {
        try {
            // Step 1: Assemble the .asm file to generate the .ld file
            Assembler.run(asmFilePath);  // Static call to run method

            // Step 2: Load the generated .ld file into memory
            String ldFilePath = "output/LoadFile.ld";  // The assumed output file location
            loadProgram(ldFilePath);
        } catch (IOException e) {
            System.out.println("Error during assembly: " + e.getMessage());
        }
    }

    public void loadProgram(String filePath) {
        // Simulate ROM Loader: Load program from file into memory
        int firstAddress = -1;  // To store the first address
        int fileLength = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by spaces to separate address and instruction
                String[] parts = line.split("\\s+");
                if (parts.length == 2) {
                    int address = Integer.parseInt(parts[0], 8); // Parse address as octal
                    int instruction = Integer.parseInt(parts[1], 8); // Parse instruction as octal
                    
                    // Store the first address (if not set)
                    if (firstAddress == -1) {
                        firstAddress = address; // Capture the first address
                    }
                    
                    memory.storeValue(address, instruction); // Store instruction in memory
                }
                fileLength++;
            }
            System.out.println("Program loaded into memory.");
            
            // Set the PC to the first instruction address (dynamically)
            if (firstAddress != -1) {
                pc.setPC(fileLength + firstAddress);  // Dynamically set PC to first instruction address
            } else {
                System.out.println("Error: No instructions found in program file.");
            }
            
        } catch (IOException e) {
            System.out.println("Error loading program: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Number format exception: " + e.getMessage());
        }
    } 
    public void simulateUserOperations() {
        // Step 2: Simulate user playing around with memory and registers.
        // The user will store values into specific memory locations, then load from those addresses.

        // Store values into memory (addresses 46 and 47 in octal)
        memory.storeValue(46, 50);   // Store 50 in memory address 46
        memory.storeValue(47, 100);  // Store 100 in memory address 47

        System.out.println("\n=== SIMULATION PHASE ===");
        System.out.println("Stored value 50 in memory address 46 (octal)");
        System.out.println("Stored value 100 in memory address 47 (octal)");

        // Load value from memory address 46 into register 0 (R0)
        cpu.fetch();  // Simulate fetching the instruction
        cpu.decodeAndExecute();  // Simulate decoding and executing LDR instruction to load from memory

        // Fetch and execute the next instruction (in this case, loading from 47 into another register)
        pc.incrementPC();  // Move to the next instruction
        cpu.fetch();
        cpu.decodeAndExecute();
    }

    public void run() {
        // Step 1: load ROM.
        assembleAndLoadProgram("assembly/LoadStore.asm");

        // octal address = octal 10 + length of boot program + octal 10.
        pc.setPC(pc.getPC() + 16);      // 46.
    
        // Step 2: Simulate user playing around with memory and stuff beginning at address 46.
        simulateUserOperations();

        // Run fetch-decode-execute cycle
        boolean halt = false;
        while (!halt) {
            cpu.fetch();  // Fetch the next instruction
            halt = cpu.decodeAndExecute();  // Decode and execute (returns true if HLT is hit)
        }
    
        System.out.println("Program execution finished.");
    }

    public static void main(String[] args) {
        Playground playground = new Playground();
        playground.run(); // Start the simulation
    }
}
*/
