package components;

public class CPUExe {
    private Memory memory;
    private GeneralPurposeRegisters gpr;
    private IndexRegisters ixr;

    public CPUExe(Memory memory, GeneralPurposeRegisters gpr, IndexRegisters ixr) {
        this.memory = memory;
        this.gpr = gpr;
        this.ixr = ixr;
    }

    // LDR.
    public boolean executeLoad(String binaryInstruction) {
        // extract instructions.
        String reg_str = binaryInstruction.substring(6, 8);  
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  
        String address_str = binaryInstruction.substring(11, 16);  

        int reg = Integer.parseInt(reg_str, 2);
        int ix = Integer.parseInt(ix_str, 2);
        int iBit = Integer.parseInt(iBit_str, 2);
        int address = Integer.parseInt(address_str, 2);

        System.out.println("opcode: " + binaryInstruction.substring(0, 6));  
        System.out.println("reg: " + reg);
        System.out.println("ix: " + ix);
        System.out.println("i: " + iBit);
        System.out.println("address: " + address);
    
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  // Calculate effective address
        System.out.println("Effective Address (Ea): " + ea);
        
        int value = memory.loadMemoryValue(ea);  // Load the value from memory
        System.out.println("Value from memory: " + value);
    
        gpr.setGPR(reg, (short) value); 
        System.out.println("Updated GPR[" + reg + "] with value: " + value);
        return false;  
    }

    // STR.
    public boolean executeStore(String binaryInstruction) {
        // extract instructions.
        String reg_str = binaryInstruction.substring(6, 8);  
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  
        String address_str = binaryInstruction.substring(11, 16); 

        int reg = Integer.parseInt(reg_str, 2);
        int ix = Integer.parseInt(ix_str, 2);
        int iBit = Integer.parseInt(iBit_str, 2);
        int address = Integer.parseInt(address_str, 2);

        System.out.println("opcode: " + binaryInstruction.substring(0, 6));  
        System.out.println("reg: " + reg);
        System.out.println("ix: " + ix);
        System.out.println("i: " + iBit);
        System.out.println("address: " + address);

        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
        System.out.println("Effective Address (Ea): " + ea);

        int value = gpr.getGPR(reg); 
        System.out.println("Value from GPR" + reg + ": "  + value);

        memory.storeValue(ea, value);  // Store the value into memory
        System.out.println("Loaded into memory -> Address: " + address + ", Data: " + value);
        return false;  
    }


    // LDA.
    public boolean executeLoadAddress(String binaryInstruction) {
        // Extract the fields (register, index register, indirect bit, address)
        String reg_str = binaryInstruction.substring(6, 8);  
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  
        String address_str = binaryInstruction.substring(11, 16);  
    
        int reg = Integer.parseInt(reg_str, 2);
        int ix = Integer.parseInt(ix_str, 2);
        int iBit = Integer.parseInt(iBit_str, 2);
        int address = Integer.parseInt(address_str, 2);
    
        // Calculate the effective address (same method as LDR/STR)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
        System.out.println("Effective Address (Ea): " + ea);
    
        // Store the effective address directly in the register
        gpr.setGPR(reg, (short) ea); 
        System.out.println("Updated GPR[" + reg + "] with EA: " + ea);
    
        return false;  // Continue execution
    }

    // LDX.
    public boolean executeLoadIndex(String binaryInstruction) {
        // Extract fields.
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  // Extract indirect bit
        String address_str = binaryInstruction.substring(11, 16);  // Correct bits for the address
    
        int ix = Integer.parseInt(ix_str, 2);  // Convert IX field to integer
        int iBit = Integer.parseInt(iBit_str, 2);  // Convert I bit to integer
        int address = Integer.parseInt(address_str, 2);  // Convert address field to integer
    
        System.out.println("opcode: " + binaryInstruction.substring(0, 6));  
        System.out.println("ix: " + ix_str);
        System.out.println("i: " + iBit_str);
        System.out.println("address: " + address_str);
    
        // Calculate Effective Address (EA)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
        System.out.println("Effective Address (Ea): " + ea);
    
        // Load value from memory at effective address into the specified index register
        int value = memory.loadMemoryValue(ea);
        System.out.println("Value from memory: " + value);
    
        // update ixr.
        ixr.setIndexRegister(ix, (short) value);
        System.out.println("Updated IXR[" + ix + "] with value: " + value);
        
        return false;  // Continue execution
    }

    // STX.
    public boolean executeStoreIndex(String binaryInstruction) {
        // Extract fields.
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  
        String address_str = binaryInstruction.substring(11, 16);
    
        int ix = Integer.parseInt(ix_str, 2);  // Convert IX field to integer
        int iBit = Integer.parseInt(iBit_str, 2);  // Convert I bit to integer
        int address = Integer.parseInt(address_str, 2);  // Convert address field to integer
    
        System.out.println("opcode: " + binaryInstruction.substring(0, 6));  
        System.out.println("ix: " + ix);
        System.out.println("i: " + iBit);
        System.out.println("address: " + address);
    
        // Calculate Effective Address (EA)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
        System.out.println("Effective Address (Ea): " + ea);
    
        // Get the value from the Index Register (IX) and store it in memory at the EA
        int value = ixr.getIndexRegister(ix);
        System.out.println("Value from IXR[" + ix + "]: " + value);
    
        memory.storeValue(ea, value);  // Store the value in memory
        System.out.println("Stored in memory -> Address: " + ea + ", Data: " + value);
        
        return false;  // Continue execution
    }

    // Helper function for calculating effective address
    private int calculateEffectiveAddress(String ix, String iBit, String address) {
        int baseAddress = Integer.parseInt(address, 2);  // Convert address bits to integer
        if (!ix.equals("00")) {
            int indexValue = ixr.getIndexRegister(Integer.parseInt(ix, 2));  // Get the index register value
            baseAddress += indexValue;
        }
        if (iBit.equals("1")) {
            baseAddress = memory.loadMemoryValue(baseAddress);  // Use memory indirection
        }
        return baseAddress;
    }
}
