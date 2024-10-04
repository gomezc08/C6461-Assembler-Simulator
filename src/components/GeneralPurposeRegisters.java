package components;

public class GeneralPurposeRegisters {
    private short[] gprs; // Array to hold the 16-bit values for each GPR

    // Constructor initializes 4 GPRs (each 16 bits)
    public GeneralPurposeRegisters(int numRegisters) {
        this.gprs = new short[numRegisters]; // Initialize all GPRs to 0
    }

    // Method to set a specific GPR value (index 0 to numRegisters-1)
    public void setGPR(int index, short value) {
        if (index >= 0 && index < gprs.length) {
            gprs[index] = value;
        } else {
            throw new IllegalArgumentException("Invalid GPR index");
        }
    }

    // Method to get the value of a specific GPR (index 0 to numRegisters-1)
    public short getGPR(int index) {
        if (index >= 0 && index < gprs.length) {
            return gprs[index];
        } else {
            throw new IllegalArgumentException("Invalid GPR index");
        }
    }

    // Method to reset a specific GPR to 0
    public void resetGPR(int index) {
        if (index >= 0 && index < gprs.length) {
            gprs[index] = 0;
        } else {
            throw new IllegalArgumentException("Invalid GPR index");
        }
    }

    // Method to reset all GPRs to 0
    public void resetAllGPRs() {
        for (int i = 0; i < gprs.length; i++) {
            gprs[i] = 0;
        }
    }

    // For debugging: display the values of all GPRs
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gprs.length; i++) {
            sb.append(String.format("GPR[%d]: %04X\n", i, gprs[i] & 0xFFFF)); // Display in hex format
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // Assume there are 4 General Purpose Registers
        GeneralPurposeRegisters gprs = new GeneralPurposeRegisters(4);

        // Set values for the GPRs
        gprs.setGPR(0, (short) 0x1234);
        gprs.setGPR(1, (short) 0x5678);
        gprs.setGPR(2, (short) 0x9ABC);
        gprs.setGPR(3, (short) 0xDEF0);

        // Display the values of the GPRs
        System.out.println(gprs); // Output: GPR[0]: 1234, GPR[1]: 5678, etc.

        // Get the value of a specific GPR
        System.out.println("GPR[2] Value: " + Integer.toHexString(gprs.getGPR(2) & 0xFFFF)); // Output: 9ABC

        // Reset GPR[1] to 0
        gprs.resetGPR(1);
        System.out.println(gprs); // Output: GPR[1]: 0000

        // Reset all GPRs
        gprs.resetAllGPRs();
        System.out.println(gprs); // Output: All GPRs: 0000
    }
}
