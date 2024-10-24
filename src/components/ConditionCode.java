package components;

/* 
only using the last 4 bits: 0000 XXXX
example: 0000 1010
 conditionCode (byte):
|  Bit 7  |  Bit 6  |  Bit 5  |  Bit 4  |  Bit 3  |  Bit 2  |  Bit 1  |  Bit 0  |
|    0    |    0    |    0    |    0    |  Equal  | DivZero | Underfl | Overfl |
*/

public class ConditionCode {
    private byte conditionCode; // Use a byte to hold all 4 flags (since byte is 8 bits, we only use the lower 4)

    // Bitmask constants for each condition
    private final byte OVERFLOW = 0b0001;   // Bit 0 (0001)
    private final byte UNDERFLOW = 0b0010;  // Bit 1 (0010)
    private final byte DIVZERO = 0b0100;    // Bit 2 (0100)
    private final byte EQUAL = 0b1000;      // Bit 3 (1000)

    // Constructor initializes all flags to 0 (false)
    public ConditionCode() {
        this.conditionCode = 0; // All flags are initially 0
    }

    public void updateConditionCodes(int result) {
        this.setOverflow(result > 32767 || result < -32768); // 16-bit overflow
        this.setUnderflow(result < 0); // Underflow if result is negative
        this.setEqual(result == 0); // Equal to zero
        this.setDivZero(result == 0); // Divide by zero can be contextually set
    }

    // Set Overflow flag
    public void setOverflow(boolean flag) {
        if (flag) {
            conditionCode |= OVERFLOW; // Set the Overflow flag (bit 0)
        } else {
            conditionCode &= ~OVERFLOW; // Clear the Overflow flag (bit 0)
        }
    }

    // Set Underflow flag
    public void setUnderflow(boolean flag) {
        if (flag) {
            conditionCode |= UNDERFLOW; // Set the Underflow flag (bit 1)
        } else {
            conditionCode &= ~UNDERFLOW; // Clear the Underflow flag (bit 1)
        }
    }

    // Set Division by Zero flag
    public void setDivZero(boolean flag) {
        if (flag) {
            conditionCode |= DIVZERO; // Set the Division by Zero flag (bit 2)
        } else {
            conditionCode &= ~DIVZERO; // Clear the Division by Zero flag (bit 2)
        }
    }

    // Set Equal flag
    public void setEqual(boolean flag) {
        if (flag) {
            conditionCode |= EQUAL; // Set the Equal flag (bit 3)
        } else {
            conditionCode &= ~EQUAL; // Clear the Equal flag (bit 3)
        }
    }

    // Check if Overflow flag is set
    public boolean isOverflow() {
        return (conditionCode & OVERFLOW) != 0; // Check bit 0
    }

    // Check if Underflow flag is set
    public boolean isUnderflow() {
        return (conditionCode & UNDERFLOW) != 0; // Check bit 1
    }

    // Check if Division by Zero flag is set
    public boolean isDivZero() {
        return (conditionCode & DIVZERO) != 0; // Check bit 2
    }

    // Check if Equal flag is set
    public boolean isEqual() {
        return (conditionCode & EQUAL) != 0; // Check bit 3
    }

    // Reset all flags to 0
    public void reset() {
        this.conditionCode = 0;
    }

    // New method to get the binary representation of the conditionCode byte
    public String getBinaryRepresentation() {
        // Convert conditionCode to an 8-bit binary string
        return String.format("0000 %4s", Integer.toBinaryString(conditionCode & 0xFF)).replace(' ', '0');
    }

    // For debugging: display the current status of all flags
    @Override
    public String toString() {
        return String.format("Condition Codes: Overflow=%b, Underflow=%b, DivZero=%b, Equal=%b",
                isOverflow(), isUnderflow(), isDivZero(), isEqual());
    }
}