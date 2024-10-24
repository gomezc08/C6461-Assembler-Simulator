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
            sb.append(String.format("GPR[%d]: %d\n", i, gprs[i]));  // Display in decimal format
        }
        return sb.toString();
    }
}
