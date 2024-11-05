package components;

/*
 * CacheLine: Represents a single line in the cache, which stores a block of 8 words,
 * along with a tag to identify the memory address and a dirty bit to indicate if 
 * the cache line has been modified.
 * 
 * CacheLine(): Constructor that initializes an empty cache line with a default invalid tag, 
 * an array for the block, and a clean (non-dirty) state.
 * 
 * getTag(): Retrieves the tag stored in this cache line.
 * - @return Integer: The tag associated with the cache line.
 * 
 * setTag(Integer tag): Sets the tag for this cache line.
 * - @param tag: The memory tag associated with this cache line.
 * 
 * getBlock(): Returns the block array representing the cache line data.
 * - @return int[]: An array containing the 8 words in this cache line.
 * 
 * setBlock(int index, int value): Writes a value to a specific word in the block and marks the line as dirty.
 * - @param index: The index within the block to write the value to.
 * - @param value: The value to be written at the specified index in the block.
 * 
 * isDirty(): Checks if the cache line has been modified since it was loaded from memory.
 * - @return boolean: True if the line is dirty, false otherwise.
 * 
 * setDirty(boolean dirty): Sets the dirty status of the cache line.
 * - @param dirty: A boolean indicating if the cache line has been modified.
 */

public class CacheLine {
    private Integer tag;    // The tag portion of the address
    private int[] block;    // Each cache line holds 8 words
    private boolean dirty;  // Flag to indicate if the cache line has been modified

    public CacheLine() {
        this.tag = -1;  // Initialize with an invalid tag (e.g., -1 for "empty")
        this.block = new int[8];  // Initialize block to hold 8 words
        for(int i = 0; i < 8; i++) {
            this.block[i] = -1;
        }
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
