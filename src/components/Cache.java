package components;

import java.util.ArrayList;
import java.util.LinkedList;

class Cache {
    private ArrayList<CacheLine> cacheLines;
    private int cacheSize = 4;
    private int blockSize = 8;
    private Memory memory;
    private LinkedList<Integer> lruList;

    public Cache(Memory memory) {
        this.memory = memory;
        cacheLines = new ArrayList<>(cacheSize);
        lruList = new LinkedList<>();

        for (int i = 0; i < cacheSize; i++) {
            cacheLines.add(new CacheLine());
            lruList.addLast(i);  // Add to end, so first element is LRU
        }
    }

    // Address to block conversion
    public int getBlockID(int address) {
        return address & 0b111;  // Lower 3 bits as block ID
    }

    public int getTag(int address) {
        return address >> 3;     // Remaining bits are the tag
    }

    public int read(int address) {
        int blockID = getBlockID(address);
        int tag = getTag(address);
        int wordOffset = blockID;  

        for (int i = 0; i < cacheSize; i++) {
            CacheLine line = cacheLines.get(i);

            if (line.getTag() != null && line.getTag() == tag) {  
                updateLRU(i);
                System.out.println("Cache hit at line " + i + " for address " + address);
                return line.getBlock()[wordOffset];
            }
        }

        System.out.println("Cache miss for address " + address + ". Fetching from memory.");
        return loadBlockFromMemory(address);
    }

    public void write(int address, int value) {
        int blockID = getBlockID(address);
        int tag = getTag(address);
        int wordOffset = blockID;

        for (int i = 0; i < cacheSize; i++) {
            CacheLine line = cacheLines.get(i);

            if (line.getTag() != null && line.getTag() == tag) {  
                line.setBlock(wordOffset, value);
                line.setDirty(true);
                updateLRU(i);
                System.out.println("Cache hit! Data written to address " + address);
                return;
            }
        }

        System.out.println("Cache miss for address " + address + ". Loading block from memory.");
        loadBlockFromMemory(address);
        
        // Write to the newly loaded block
        for (int i = 0; i < cacheSize; i++) {
            CacheLine line = cacheLines.get(i);
            if (line.getTag() == tag) {
                line.setBlock(wordOffset, value);
                line.setDirty(true);
                updateLRU(i);
                System.out.println("Data written to newly loaded block at address " + address);
                return;
            }
        }
    }

    private void updateLRU(int index) {
        lruList.remove((Integer) index);
        lruList.addLast(index);  // Most recently used goes to the end
    }

    private int loadBlockFromMemory(int address) {
        int evictIndex = lruList.removeFirst();  // Remove and return the LRU index
        CacheLine evictLine = cacheLines.get(evictIndex);

        if (evictLine.isDirty()) {
            System.out.println("Evicting dirty cache line " + evictIndex + ". Writing back to memory.");
            writeBackToMemory(evictLine);
        } else {
            System.out.println("Evicting clean cache line " + evictIndex + ".");
        }

        int tag = getTag(address);
        int blockID = getBlockID(address);

        for (int i = 0; i < blockSize; i++) {
            int wordAddress = (address & ~0b111) + i;
            evictLine.getBlock()[i] = memory.loadMemoryValue(wordAddress);
        }

        evictLine.setTag(tag);
        evictLine.setDirty(false);
        lruList.addLast(evictIndex);

        System.out.println("Loaded block into cache line " + evictIndex + " with tag: " + tag);
        return evictLine.getBlock()[blockID];
    }

    private void writeBackToMemory(CacheLine cacheLine) {
        int tag = cacheLine.getTag();
        for (int i = 0; i < blockSize; i++) {
            int wordAddress = (tag << 3) + i;
            memory.storeValue(wordAddress, cacheLine.getBlock()[i]);
        }
    }

    public void printCacheState() {
        System.out.println("\nCurrent Cache State:");
        for (int i = 0; i < cacheSize; i++) {
            CacheLine line = cacheLines.get(i);
            System.out.println("Line " + i + ": Tag = " + line.getTag() + 
                               ", Dirty = " + line.isDirty() + 
                               ", LRU Order = " + (cacheSize - 1 - lruList.indexOf(i)));
        }
        System.out.println();
    }
}