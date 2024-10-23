package components;

public class CPUExe {
    private Memory memory;
    private GeneralPurposeRegisters gpr;
    private IndexRegisters ixr;
    private ProgramCounter pc;
    private ConditionCode cc;

    public CPUExe(Memory memory, GeneralPurposeRegisters gpr, IndexRegisters ixr, ProgramCounter pc, ConditionCode cc) {
        this.memory = memory;
        this.gpr = gpr;
        this.ixr = ixr;
        this.pc = pc;
        this.cc = cc;
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

    // JZ.
    public boolean executeJZ(String binaryInstruction) {
        // Extract the fields (register, index register, indirect bit, address)
        String reg_str = binaryInstruction.substring(6, 8);  
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  
        String address_str = binaryInstruction.substring(11, 16);  

        int reg = Integer.parseInt(reg_str, 2);
        int ix = Integer.parseInt(ix_str, 2);
        int iBit = Integer.parseInt(iBit_str, 2);
        int address = Integer.parseInt(address_str, 2);

        // Get the contents of the register
        int regValue = gpr.getGPR(reg);
        System.out.println("Value in GPR[" + reg + "]: " + regValue);
        
        // Calculate the Effective Address (EA)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
        System.out.println("Effective Address (Ea): " + ea);

        // If the value in the register is zero, jump to the EA, otherwise increment the PC
        if (regValue == 0) {
            pc.setPC(ea);  // Update PC to EA
            System.out.println("Jumping to address: " + ea);
        } 
        
        else {
            pc.incrementPC();  // Increment PC to next instruction
            System.out.println("Register not zero, moving to next instruction.");
        }

        return false;  // Continue execution
    }

    // JNE.
    public boolean executeJNE(String binaryInstruction) {
        // Extract the fields (register, index register, indirect bit, address)
        String reg_str = binaryInstruction.substring(6, 8);  
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  
        String address_str = binaryInstruction.substring(11, 16);  

        int reg = Integer.parseInt(reg_str, 2);
        int ix = Integer.parseInt(ix_str, 2);
        int iBit = Integer.parseInt(iBit_str, 2);
        int address = Integer.parseInt(address_str, 2);

        // Get the contents of the register
        int regValue = gpr.getGPR(reg);
        System.out.println("Value in GPR[" + reg + "]: " + regValue);
        
        // Calculate the Effective Address (EA)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
        System.out.println("Effective Address (Ea): " + ea);

        // If the value in the register is NOT zero, jump to the EA, otherwise increment the PC
        if (regValue != 0) {
            pc.setPC(ea);  // Update PC to EA
            System.out.println("Jumping to address: " + ea);
        } 
        
        else {
            pc.incrementPC();  // Increment PC to next instruction
            System.out.println("Register is zero, moving to next instruction.");
        }

        return false;  // Continue execution
    }

    // JCC.
    public boolean executeJCC(String binaryInstruction, ProgramCounter pc, ConditionCode cc) {
        // Extract the fields (condition code, index register, indirect bit, address)
        String conditionCodeStr = binaryInstruction.substring(6, 8);  
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  
        String address_str = binaryInstruction.substring(11, 16);  

        int conditionCodeIndex = Integer.parseInt(conditionCodeStr, 2);
        int ix = Integer.parseInt(ix_str, 2);
        int iBit = Integer.parseInt(iBit_str, 2);
        int address = Integer.parseInt(address_str, 2);

        // Get the effective address (EA)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);
        System.out.println("Effective Address (Ea): " + ea);

        // Check the condition based on the condition code index
        boolean conditionMet = false;
        switch (conditionCodeIndex) {
            case 0:
                conditionMet = cc.isOverflow();
                System.out.println("Checking Overflow flag: " + conditionMet);
                break;
            case 1:
                conditionMet = cc.isUnderflow();
                System.out.println("Checking Underflow flag: " + conditionMet);
                break;
            case 2:
                conditionMet = cc.isDivZero();
                System.out.println("Checking DivZero flag: " + conditionMet);
                break;
            case 3:
                conditionMet = cc.isEqual();
                System.out.println("Checking Equal flag: " + conditionMet);
                break;
            default:
                System.out.println("Unknown condition code index: " + conditionCodeIndex);
                return false;
        }

        // Jump if the condition is met
        if (conditionMet) {
            pc.setPC(ea);
            System.out.println("Condition met, jumping to address: " + ea);
        } 
        
        else {
            pc.incrementPC();  // Move to next instruction if condition is not met
            System.out.println("Condition not met, moving to next instruction.");
        }

        return false;  // Continue execution
    }

    // JMA.
    public boolean executeJMA(String binaryInstruction) {
        // Extract the index register, indirect bit, and address from the instruction
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  
        String address_str = binaryInstruction.substring(11, 16);  

        // Calculate the Effective Address (EA)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
        System.out.println("Effective Address (Ea): " + ea);

        // Set the Program Counter (PC) to the effective address
        pc.setPC(ea);
        System.out.println("Jumping to address: " + ea);

        return false;  // Continue execution
    }

    // JSR.
    public boolean executeJSR(String binaryInstruction) {
        // Extract the fields: index register, indirect bit, address
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  
        String address_str = binaryInstruction.substring(11, 16);  

        int ix = Integer.parseInt(ix_str, 2);
        int iBit = Integer.parseInt(iBit_str, 2);
        int address = Integer.parseInt(address_str, 2);

        // Calculate the Effective Address (EA)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
        System.out.println("Effective Address (Ea): " + ea);

        // Step 1: Save the return address (PC + 1) in GPR[3]
        gpr.setGPR(3, (short) (pc.getPC()));  
        System.out.println("Saved return address (PC+1) in GPR[3]: " + pc.getPC());

        // Step 2: Set the PC to the EA (jump to the subroutine)
        pc.setPC(ea);  
        System.out.println("Jumping to subroutine at address: " + ea);

        return false;  // Continue execution
    }

    // RFS.
    public boolean executeRFS(String binaryInstruction) {
        // Extract immediate value from the instruction
        String immed_str = binaryInstruction.substring(11, 16);  // Immediate value is in the last 5 bits
        int immed = Integer.parseInt(immed_str, 2);  // Convert immediate field to integer

        // Load the immediate value into GPR[0]
        gpr.setGPR(0, (short) immed);
        System.out.println("Loaded immediate value " + immed + " into GPR[0].");

        // Set the PC to the value stored in GPR[3] (return address)
        int returnAddress = gpr.getGPR(3);
        pc.setPC(returnAddress);
        System.out.println("Returning to address " + returnAddress + " from subroutine.");

        return false;  // Continue execution
    }

    // SOB.
    public boolean executeSOB(String binaryInstruction) {
        // Extract register, index register, indirect bit, and address fields
        String reg_str = binaryInstruction.substring(6, 8);  
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  
        String address_str = binaryInstruction.substring(11, 16);  
    
        int reg = Integer.parseInt(reg_str, 2);
        int ix = Integer.parseInt(ix_str, 2);
        int iBit = Integer.parseInt(iBit_str, 2);
        int address = Integer.parseInt(address_str, 2);
    
        // Get the current value of the register and decrement it by 1
        int regValue = gpr.getGPR(reg);
        regValue -= 1;
        gpr.setGPR(reg, (short) regValue);
    
        System.out.println("Decrementing GPR[" + reg + "] to: " + regValue);
    
        // Calculate the effective address (EA) if needed
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
        System.out.println("Effective Address (Ea): " + ea);
    
        // Check if the register value is greater than 0
        if (regValue > 0) {
            pc.setPC(ea);  // Jump to the effective address
            System.out.println("Register value is greater than 0, jumping to address: " + ea);
        } else {
            pc.incrementPC();  // Move to the next instruction
            System.out.println("Register value is less than or equal to 0, moving to next instruction.");
        }
    
        return false;  // Continue execution
    }    

    // JGE.
    public boolean executeJGE(String binaryInstruction) {
        // Extract fields from the binary instruction
        String reg_str = binaryInstruction.substring(6, 8);  // Register field
        String ix_str = binaryInstruction.substring(8, 10);  // Index register field
        String iBit_str = binaryInstruction.substring(10, 11);  // Indirect bit field
        String address_str = binaryInstruction.substring(11, 16);  // Address field
    
        int reg = Integer.parseInt(reg_str, 2);  // Convert register field to integer
        int ix = Integer.parseInt(ix_str, 2);  // Convert index register field to integer
        int iBit = Integer.parseInt(iBit_str, 2);  // Convert indirect bit field to integer
        int address = Integer.parseInt(address_str, 2);  // Convert address field to integer
    
        // Get the value in the register
        int regValue = gpr.getGPR(reg);
        System.out.println("Value in GPR[" + reg + "]: " + regValue);
        
        // Calculate Effective Address (EA)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
        System.out.println("Effective Address (Ea): " + ea);
    
        // If the register value is greater than or equal to 0, set PC to EA
        if (regValue >= 0) {
            pc.setPC(ea);
            System.out.println("Jumping to address: " + ea + " because GPR[" + reg + "] >= 0");
        } else {
            pc.incrementPC();  // Else, increment the PC to the next instruction
            System.out.println("GPR[" + reg + "] is less than 0, moving to next instruction.");
        }
    
        return false;  // Continue execution
    }

    // MLT.
    public boolean executeMLT(String binaryInstruction) {
        // Extract Rx and Ry fields
        String rx_str = binaryInstruction.substring(6, 8);  
        String ry_str = binaryInstruction.substring(8, 10);

        int rx = Integer.parseInt(rx_str, 2);
        int ry = Integer.parseInt(ry_str, 2);

        // Validate Rx and Ry to be 0 or 2, per the instruction definition.
        if ((rx != 0 && rx != 2) || (ry != 0 && ry != 2)) {
            throw new IllegalArgumentException("MLT operation can only be performed with Rx and Ry as 0 or 2.");
        }

        // Get the contents of the registers Rx and Ry
        int valueRx = gpr.getGPR(rx);
        int valueRy = gpr.getGPR(ry);

        // Multiply the values of Rx and Ry
        long result = (long) valueRx * (long) valueRy; // Use long to handle overflow
        String result32Bits = String.format("%32s", Long.toBinaryString(result & 0xFFFFFFFFL)).replace(' ', '0');  // Ensure 32 bits
        System.out.println("32-bit result: " + result32Bits);

        // Split the result into high and low 16-bit parts
        String highOrderBits = result32Bits.substring(0, 16);
        String lowOrderBits = result32Bits.substring(16, 32);

        int high = (short) Integer.parseInt(highOrderBits, 2);  // High order bits (as signed 16-bit)
        int low = (short) Integer.parseInt(lowOrderBits, 2);  // Low order bits (as signed 16-bit)

        // Store the results: Rx holds the high order bits, Rx+1 holds the low order bits
        gpr.setGPR(rx, (short) high);
        gpr.setGPR(rx + 1, (short) low);

        // Overflow occurs if the high-order bits exceed 16-bit range
        if (high != 0) {  // Check if high-order bits are non-zero (indicating overflow)
            cc.setOverflow(true);  // Set overflow flag
            System.out.println("Overflow occurred during MLT operation.");
        } else {
            cc.setOverflow(false);  // No overflow
        }

        System.out.println("MLT executed: " + valueRx + " * " + valueRy);
        System.out.println("High order bits (Rx): " + highOrderBits);
        System.out.println("Low order bits (Rx+1): " + lowOrderBits);

        return false;  // Continue execution
    }

    // DVD.
    public boolean executeDVD(String binaryInstruction) {
        // Extract Rx and Ry fields
        String rx_str = binaryInstruction.substring(6, 8);  
        String ry_str = binaryInstruction.substring(8, 10);
    
        int rx = Integer.parseInt(rx_str, 2);
        int ry = Integer.parseInt(ry_str, 2);
    
        // Validate Rx and Ry to be 0 or 2, per the instruction definition.
        if ((rx != 0 && rx != 2) || (ry != 0 && ry != 2)) {
            throw new IllegalArgumentException("DVD operation can only be performed with Rx and Ry as 0 or 2.");
        }
    
        // Get the contents of the registers Rx and Ry
        int valueRx = gpr.getGPR(rx);
        int valueRy = gpr.getGPR(ry);

        System.out.println("here is ry: " +valueRy);
    
        // Check for divide by zero
        if (valueRy == 0) {
            cc.setDivZero(true);  // Set divide by zero flag
            System.out.println("Division by zero error during DVD operation.");
            return false;  // Stop execution due to divide by zero
        }
    
        // Perform division and get quotient and remainder
        int quotient = valueRx / valueRy;
        int remainder = valueRx % valueRy;
    
        // Store the quotient in Rx and the remainder in Rx+1
        gpr.setGPR(rx, (short) quotient);
        gpr.setGPR(rx + 1, (short) remainder);
    
        System.out.println("DVD executed: " + valueRx + " / " + valueRy);
        System.out.println("Quotient (Rx): " + quotient);
        System.out.println("Remainder (Rx+1): " + remainder);
    
        return false;  // Continue execution
    }    

}