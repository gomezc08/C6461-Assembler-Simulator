package cpu;

/**
 * The ALU (Arithmetic Logic Unit) class performs arithmetic and logical operations 
 * for the CPU. It handles operations like addition, subtraction, AND, and OR.
 *
 * Functions:
 * - int add(int operand1, int operand2): 
 *   Adds two integers.
 *   @param operand1 The first integer operand.
 *   @param operand2 The second integer operand.
 *   @return The result of adding operand1 and operand2.
 *
 * - int subtract(int operand1, int operand2): 
 *   Subtracts the second integer from the first.
 *   @param operand1 The first integer operand.
 *   @param operand2 The second integer operand.
 *   @return The result of subtracting operand2 from operand1.
 *
 * - int and(int operand1, int operand2): 
 *   Performs a bitwise AND operation between two integers.
 *   @param operand1 The first integer operand.
 *   @param operand2 The second integer operand.
 *   @return The result of a bitwise AND between operand1 and operand2.
 *
 * - int or(int operand1, int operand2): 
 *   Performs a bitwise OR operation between two integers.
 *   @param operand1 The first integer operand.
 *   @param operand2 The second integer operand.
 *   @return The result of a bitwise OR between operand1 and operand2.
 */

public class ALU {
    public int add(int operand1, int operand2) {
        return operand1 + operand2;
    }

    public int subtract(int operand1, int operand2) {
        return operand1 - operand2;
    }

    public int and(int operand1, int operand2) {
        return operand1 & operand2;
    }

    public int or(int operand1, int operand2) {
        return operand1 | operand2;
    }

    // Add more operations as needed
}
