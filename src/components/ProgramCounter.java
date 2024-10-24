package components;

// Program Counter class to simulate a 12-bit PC
public class ProgramCounter {
    private int pc; 
    private final int MAX_VALUE = 0xFFF; // Maximum value for 12 bits (0xFFF = 4095)
    
    // Constructor initializes the PC to 0
    public ProgramCounter() {
        this.pc = 0;
    }

    // Set the Program Counter (ensures it's within 12-bit range)
    public void setPC(int value) {
        if (value >= 0 && value <= MAX_VALUE) {
            this.pc = value & MAX_VALUE; // Apply mask to ensure only lower 12 bits are used
        } else {
            throw new IllegalArgumentException("Value out of range for 12-bit register");
        }
    }

    // Get the current value of the Program Counter
    public int getPC() {
        return this.pc;
    }

    // Increment the Program Counter by 1 (with 12-bit wrapping)
    public void incrementPC() {
        this.pc = (this.pc + 1) & MAX_VALUE; // Increment and apply the mask to wrap within 12 bits
    }

    // Reset the Program Counter to 0
    public void reset() {
        this.pc = 0;
    }

    // Print the current value of the Program Counter (for debugging)
    @Override
    public String toString() {
        return String.format("PC: %d (0x%03X)", this.pc, this.pc);
    }
}
