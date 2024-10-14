package components;

public class MemoryBufferRegister {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void clear() {
        value = 0;
    }
}