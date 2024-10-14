package components;

public class MemoryAddressRegister {
    private int address;

    public int getValue() {
        return address;
    }

    public void setValue(int address) {
        if (address >= 0 && address < 4096) { // Example: 12-bit addressing
            this.address = address;
        } else {
            throw new IllegalArgumentException("Invalid memory address");
        }
    }

    public void increment() {
        if (address < 4095) { // Example: Max address is 4095 for 12-bit addressing
            address++;
        } else {
            throw new IllegalArgumentException("Address overflow");
        }
    }

    public void clear() {
        address = 0;
    }
}