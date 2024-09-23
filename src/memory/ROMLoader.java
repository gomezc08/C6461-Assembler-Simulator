package memory;

/**
 * The ROMLoader class simulates the process of loading a boot program into memory.
 * It reads a predefined program (either from a file or hardcoded) and places it in memory
 * at a specific address for the CPU to execute.
 *
 * Functions:
 * - ROMLoader(Memory memory): 
 *   Constructs a ROMLoader object to load programs into the memory.
 *   @param memory The memory instance where the program will be loaded.
 *
 * - void loadBootProgram(int[] program): 
 *   Loads the boot program into memory starting at address 0.
 *   @param program The array of instructions representing the boot program.
 */

public class ROMLoader {
    private Memory memory;

    public ROMLoader(Memory memory) {
        this.memory = memory;
    }

    public void loadBootProgram(int[] program) {
        // Load the ROM boot program into memory
        memory.loadProgram(program, 0); // Assuming the boot program starts at address 0
    }
}
