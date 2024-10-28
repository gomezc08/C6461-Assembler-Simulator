package components;

import java.util.ArrayList;
import java.util.LinkedList;

public class Cache {
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
    
        // Check if the cache line with the corresponding tag is already present
        for (int i = 0; i < cacheSize; i++) {
            CacheLine line = cacheLines.get(i);
            
            // Cache hit: Update the value in the cache line
            if (line.getTag() != null && line.getTag() == tag) { 
                line.setBlock(wordOffset, value);
                line.setDirty(true);
                updateLRU(i);
                System.out.println("Cache hit! Data written to address " + address);
                return;
            }
        }
    
        // Cache miss: Load the block into the cache
        loadBlockFromMemory(address);
    
        // Update the loaded cache line with the new value
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
    
        // Write back to memory if the evicted line is dirty
        if (evictLine.isDirty()) {
            writeBackToMemory(evictLine);
        }
    
        int tag = getTag(address);
        int baseAddress = (address & ~0b111);  // Base address for the block
    
        // Load each word in the block from memory
        for (int i = 0; i < blockSize; i++) {
            int word = memory.loadMemoryValue(baseAddress + i);
            evictLine.setBlock(i, word);
        }
    
        // Update cache line metadata
        evictLine.setTag(tag);
        evictLine.setDirty(false);
        lruList.addLast(evictIndex);
    
        return evictIndex;
    }
    

    private void writeBackToMemory(CacheLine cacheLine) {
        int tag = cacheLine.getTag();
        for (int i = 0; i < blockSize; i++) {
            int wordAddress = (tag << 3) + i;
            memory.storeValue(wordAddress, cacheLine.getBlock()[i]);
        }
    }

    public String getCacheStateString() {
        StringBuilder cacheState = new StringBuilder();
        
        // For each cache line (0-3)
        for (int i = 0; i < cacheSize; i++) {
            // Add line prefix (000, 001, 002, 003)
            cacheState.append(String.format("%03d ", i));
            
            CacheLine line = cacheLines.get(i);
            int[] block = line.getBlock();
            
            // Add each word in the block with proper formatting
            for (int j = 0; j < blockSize; j++) {
                cacheState.append(String.format("%06d ", block[j]));
            }
            cacheState.append("\n");
        }
        
        return cacheState.toString();
    }
}