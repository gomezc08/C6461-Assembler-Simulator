package bus;

/**
 * The Bus class simulates the data bus for communication between components
 * like the CPU, memory, and ALU. It transfers data between different units.
 *
 * Functions:
 * - void sendData(int value): 
 *   Sends data through the bus.
 *   @param value The data to be transferred through the bus.
 *
 * - int receiveData(): 
 *   Receives data from the bus.
 *   @return The data currently on the bus.
 */

public class Bus {
    private int data; // The data being transferred over the bus

    public void sendData(int value) {
        this.data = value; // Load data onto the bus
    }

    public int receiveData() {
        return this.data; // Retrieve data from the bus
    }
}
