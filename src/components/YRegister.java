package components;

public class YRegister {
    private short yRegister; // 16-bit Y register

    // Constructor initializes the Y register to 0
    public YRegister() {
        this.yRegister = 0;
    }

    // Method to set the value of the Y register
    public void setYRegister(short value) {
        this.yRegister = value;
    }

    // Method to get the value of the Y register
    public short getYRegister() {
        return this.yRegister;
    }

    // Method to reset the Y register to 0
    public void resetYRegister() {
        this.yRegister = 0;
    }

    // For debugging: display the value of the Y register in hex format
    @Override
    public String toString() {
        return String.format("Y Register: %04X", yRegister & 0xFFFF);
    }
    public static void main(String[] args) {
        // Create an instance of the YRegister class
        YRegister yRegister = new YRegister();

        // Set the Y register value to a specific 16-bit value (e.g., 0x1234)
        yRegister.setYRegister((short) 0x1234);
        System.out.println("After setting Y Register:");
        System.out.println(yRegister); // Output: Y Register: 1234

        // Get the value of the Y register
        short yValue = yRegister.getYRegister();
        System.out.println("Y Register Value: " + Integer.toHexString(yValue & 0xFFFF)); // Output: 1234

        // Reset the Y register to 0
        yRegister.resetYRegister();
        System.out.println("After resetting Y Register:");
        System.out.println(yRegister); // Output: Y Register: 0000
    }
}
