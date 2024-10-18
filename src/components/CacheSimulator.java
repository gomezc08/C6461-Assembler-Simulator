// to run the simulator - compile then call: java -cp bin components.CacheSimulator

package components;

public class CacheSimulator {
    public static void main(String[] args) {
        Memory memory = new Memory();
        Cache cache = new Cache(memory);

        System.out.println("Initial cache state:");
        cache.printCacheState();

        // Write some data into the cache
        System.out.println("Writing data to cache:");
        cache.write(0b0000000000001001, 100);  // Address 9
        cache.write(0b0000000000011001, 200);  // Address 25
        cache.write(0b0000000000101001, 300);  // Address 41
        cache.write(0b0000000000111001, 400);  // Address 57
        cache.printCacheState();

        // Read back from cache
        System.out.println("Reading data from cache:");
        System.out.println("Address 9: " + cache.read(0b0000000000001001));
        System.out.println("Address 25: " + cache.read(0b0000000000011001));
        cache.printCacheState();

        // Trigger an eviction
        System.out.println("Triggering cache eviction:");
        cache.write(0b0000000001001001, 500);  // Address 73
        cache.printCacheState();

        // Modify some data to make cache lines dirty
        System.out.println("Modifying data (making cache lines dirty):");
        cache.write(0b0000000000011001, 250);  // Address 25
        cache.write(0b0000000000101001, 350);  // Address 41
        cache.printCacheState();

        // Trigger more evictions to see write-back behavior
        System.out.println("Triggering more evictions:");
        cache.write(0b0000000001011001, 600);  // Address 89
        cache.write(0b0000000001101001, 700);  // Address 105
        cache.printCacheState();

        // Read some evicted data to see it being brought back from memory
        System.out.println("Reading previously evicted data:");
        System.out.println("Address 9: " + cache.read(0b0000000000001001));
        cache.printCacheState();
    }
}