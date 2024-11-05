package components;

// not used in the final product.

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
}
