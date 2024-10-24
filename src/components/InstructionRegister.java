package components;
/*
 * Register that stores instructions
 * Bits 0-11: Operand
 * Bits 12-15: Opcode
 */
public class InstructionRegister {
    private short instruction; // 16-bit instruction (use short to hold 16 bits)

    // Constructor initializes the instruction register to 0
    public InstructionRegister() {
        this.instruction = 0;
    }

    // Method to set the instruction (ensures it's within 16-bit range)
    public void setIR(short instruction) {
        this.instruction = instruction;
    }

    // Method to get the full 16-bit instruction
    public short getIR() {
        return this.instruction;
    }

    // Method to extract the opcode (first 4 bits of the instruction)
    public int getOpcode() {
        return (instruction >> 12) & 0xF; // Right-shift by 12 bits and mask with 0xF (0000 1111)
    }

    // Method to extract the operands (last 12 bits of the instruction)
    public int getOperands() {
        return instruction & 0xFFF; // Mask with 0xFFF (0000 1111 1111 1111)
    }

    // For debugging: display the full instruction, opcode, and operands
    @Override
    public String toString() {
        return String.format("Instruction: %04X, Opcode: %X, Operands: %03X", 
                instruction & 0xFFFF, getOpcode(), getOperands());
    }
}
