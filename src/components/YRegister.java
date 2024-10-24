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
}
