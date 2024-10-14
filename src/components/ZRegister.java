package components;

public class ZRegister {
    private short zRegister; // 16-bit Z register

    // Constructor initializes the Z register to 0
    public ZRegister() {
        this.zRegister = 0;
    }

    // Method to set the value of the Z register
    public void setZRegister(short value) {
        this.zRegister = value;
    }

    // Method to get the value of the Z register
    public short getZRegister() {
        return this.zRegister;
    }

    // Method to reset the Z register to 0
    public void resetZRegister() {
        this.zRegister = 0;
    }

    // For debugging: display the value of the Z register in hex format
    @Override
    public String toString() {
        return String.format("Z Register: %04X", zRegister & 0xFFFF);
    }

    public static void main(String[] args) {
        // Create an instance of the ZRegister class
        ZRegister zRegister = new ZRegister();

        // Set the Z register value to a specific 16-bit value (e.g., 0x5678)
        zRegister.setZRegister((short) 0x5678);
        System.out.println("After setting Z Register:");
        System.out.println(zRegister); // Output: Z Register: 5678

        // Get the value of the Z register
        short zValue = zRegister.getZRegister();
        System.out.println("Z Register Value: " + Integer.toHexString(zValue & 0xFFFF)); // Output: 5678

        // Reset the Z register to 0
        zRegister.resetZRegister();
        System.out.println("After resetting Z Register:");
        System.out.println(zRegister); // Output: Z Register: 0000
    }
}
