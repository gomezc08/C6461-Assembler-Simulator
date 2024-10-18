package components;

public class CacheLine {
    private Integer tag;    // The tag portion of the address
    private int[] block;    // Each cache line holds 8 words
    private boolean dirty;  // Flag to indicate if the cache line has been modified

    public CacheLine() {
        this.tag = -1;  // Initialize with an invalid tag (e.g., -1 for "empty")
        this.block = new int[8];  // Initialize block to hold 8 words
        this.dirty = false;       // Cache line initially unmodified
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public int[] getBlock() {
        return block;
    }

    public void setBlock(int index, int value) {
        this.block[index] = value;
        this.dirty = true;  // Any write makes this line dirty
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
