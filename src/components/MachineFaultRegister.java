package components;

public class MachineFaultRegister {
    private byte mfr; // Use a byte to hold the 4-bit value

    private final byte MAX_VALUE = 0xF; // Maximum value for 4 bits (15)

    // Constructor initializes the MFR to 0
    public MachineFaultRegister() {
        this.mfr = 0;
    }

    // Method to set the MFR value (ensures it's within 4-bit range)
    public void setMFR(int value) {
        if (value >= 0 && value <= MAX_VALUE) {
            this.mfr = (byte) (value & MAX_VALUE); // Apply mask to ensure only lower 4 bits are used
        } else {
            throw new IllegalArgumentException("Value out of range for 4-bit register");
        }
    }

    // Method to get the MFR value (only the lower 4 bits)
    public int getMFR() {
        return this.mfr & MAX_VALUE; // Mask to ensure we return only the lower 4 bits
    }

    // Reset the MFR to 0
    public void resetMFR() {
        this.mfr = 0;
    }

    // For debugging: display the MFR value in binary (for clarity)
    @Override
    public String toString() {
        return String.format("MFR: %04d (binary: %4s)", mfr, Integer.toBinaryString(mfr & MAX_VALUE)).replace(' ', '0');
    }

    public static void main(String[] args) {
        MachineFaultRegister mfr = new MachineFaultRegister();

        // Set a valid 4-bit value (e.g., 0xA or 10 in decimal)
        mfr.setMFR(10);
        System.out.println(mfr); // Output: MFR: 1010 (binary: 1010)

        // Retrieve the MFR value
        int value = mfr.getMFR();
        System.out.println("MFR Value: " + value); // Output: 10

        // Set an out-of-range value (e.g., 20) and catch the exception
        try {
            mfr.setMFR(20); // This should throw an exception since 20 > 15
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage()); // Output: Value out of range for 4-bit register
        }

        // Reset the MFR
        mfr.resetMFR();
        System.out.println(mfr); // Output: MFR: 0000 (binary: 0000)
    }
}