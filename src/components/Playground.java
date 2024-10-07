package components;

import Assembler.Assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

        // Initialize CPU with all components
        cpu = new CPU(pc, memory, mar, mbr, ir, gprs, indexRegisters, cc);
    }

    public void assembleAndLoadProgram(String asmFilePath) {
        try {
            // Step 1: Assemble the .asm file to generate the .ld file
            Assembler.run(asmFilePath);  // Static call to run method

            // Step 2: Load the generated .ld file into memory
            String ldFilePath = "output/LoadFile.ld";  // The assumed output file location
            loadProgram(ldFilePath);
        } 
        
        catch (IOException e) {
            System.out.println("Error during assembly: " + e.getMessage());
        }
    }

    public void loadProgram(String filePath) {
        // Simulate ROM Loader: Load program from file into memory starting at octal 10
        int address = 010; // Starting address in octal
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by spaces to separate address and instruction
                String[] parts = line.split("\\s+");
                if (parts.length == 2) {
                    address = Integer.parseInt(parts[0], 8); // Parse address as octal
                    int instruction = Integer.parseInt(parts[1], 8); // Parse instruction as octal
                    memory.storeValue(address, instruction);
                }
            }
            System.out.println("Program loaded into memory.");
        } catch (IOException e) {
            System.out.println("Error loading program: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Number format exception: " + e.getMessage());
        }
    }
    

    public void run() {
        // Assemble and load the program (LoadStore.asm) into memory
        assembleAndLoadProgram("assembly/LoadStore.asm");
        // Set PC to the starting point of the program (octal 20, for example)
        pc.setPC(020); // After boot program

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
