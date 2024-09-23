package registers;

/**
 * The Register class simulates a CPU register, which stores an integer value.
 * Registers are used to hold intermediate values and data for the CPU's operations.
 *
 * Functions:
 * - Register(): 
 *   Constructs a Register object, initializing its value to 0.
 *
 * - int getValue(): 
 *   Retrieves the current value stored in the register.
 *   @return The value stored in the register.
 *
 * - void setValue(int value): 
 *   Sets the register to the specified value.
 *   @param value The value to store in the register.
 */

public class Register {
    private int value;

    public Register() {
        this.value = 0; // Initialize register to 0
    }
    

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}