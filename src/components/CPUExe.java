package components;

import java.io.FileInputStream;
import java.io.IOException;

/*
 * CPUExe: Handles the execution of instructions for the computer simulator.
 * Provides methods to execute a variety of assembly instructions, including 
 * load, store, jump, arithmetic, and I/O operations.
 * 
 * CPUExe(Memory memory, GeneralPurposeRegisters gpr, IndexRegisters ixr, ProgramCounter pc, 
 * ConditionCode cc, Cache c): Constructs the execution handler with the required components.
 * - @param memory: The memory instance connected to the CPUExe.
 * - @param gpr: General Purpose Registers for computation.
 * - @param ixr: Index Registers for addressing.
 * - @param pc: Program Counter for tracking the next instruction.
 * - @param cc: Condition Code register to handle overflow, zero, etc.
 * - @param c: Cache instance for memory operations.
 * 
 * calculateEffectiveAddress(String ix, String iBit, String address): Helper function 
 * to calculate the effective address based on index registers and indirect addressing.
 * - @param ix: Index register field from the instruction.
 * - @param iBit: Indirect addressing bit.
 * - @param address: Address field from the instruction.
 * - @return int: The calculated effective address.
 * 
 * executeLDR(String binaryInstruction): Execute Load Register (LDR) instruction.
 * 
 * executeSTR(String binaryInstruction): Execute Store Register (STR) instruction.
 * 
 * executeLDA(String binaryInstruction): Execute Load Address (LDA) instruction.
 * 
 * executeLDX(String binaryInstruction): Execute Load Index Register (LDX) instruction.
 * 
 * executeSTX(String binaryInstruction): Execute Store Index Register (STX) instruction.
 * 
 * executeJZ(String binaryInstruction): Execute Jump if Zero (JZ) instruction.
 * 
 * executeJNE(String binaryInstruction): Execute Jump if Not Equal (JNE) instruction.
 * 
 * executeJCC(String binaryInstruction, ProgramCounter pc, ConditionCode cc): Execute Jump if Condition Code (JCC) instruction.
 * 
 * executeJMA(String binaryInstruction): Execute Jump to Address (JMA) instruction.
 * 
 * executeJSR(String binaryInstruction): Execute Jump to Subroutine (JSR) instruction.
 * 
 * executeRFS(String binaryInstruction): Execute Return from Subroutine (RFS) instruction.
 * 
 * executeSOB(String binaryInstruction): Execute Subtract One and Branch (SOB) instruction.
 * 
 * executeJGE(String binaryInstruction): Execute Jump if Greater than or Equal (JGE) instruction.
 * 
 * executeAMR(String binaryInstruction): Execute Add Memory to Register (AMR) instruction.
 * 
 * executeSMR(String binaryInstruction): Execute Subtract Memory from Register (SMR) instruction.
 * 
 * executeAIR(String binaryInstruction): Execute Add Immediate to Register (AIR) instruction.
 * 
 * executeSIR(String binaryInstruction): Execute Subtract Immediate from Register (SIR) instruction.
 * 
 * executeMLT(String binaryInstruction): Execute Multiply (MLT) instruction.
 * 
 * executeDVD(String binaryInstruction): Execute Divide (DVD) instruction.
 * 
 * executeTRR(String binaryInstruction): Execute Test the Equality of Registers (TRR) instruction.
 * 
 * executeAND(String binaryInstruction): Execute Logical AND (AND) instruction.
 * 
 * executeORR(String binaryInstruction): Execute Logical OR (ORR) instruction.
 * 
 * executeNOT(String binaryInstruction): Execute Logical NOT (NOT) instruction.
 * 
 * executeIN(String binaryInstruction): Execute Input (IN) instruction.
 * 
 * executeOUT(String binaryInstruction): Execute Output (OUT) instruction.
 * 
 * executeCHK(String binaryInstruction): Execute Check Device Status (CHK) instruction.
 * 
 * executeHLT(): Execute Halt (HLT) instruction.
 * - @return boolean: True to stop execution.
 */

import java.lang.Math;
import java.util.Scanner;

public class CPUExe {
    private Memory memory;
    private GeneralPurposeRegisters gpr;
    private IndexRegisters ixr;
    private ProgramCounter pc;
    private ConditionCode cc;
    private Cache cache;

    public CPUExe(Memory memory, GeneralPurposeRegisters gpr, IndexRegisters ixr, ProgramCounter pc, ConditionCode cc, Cache c) {
        this.memory = memory;
        this.gpr = gpr;
        this.ixr = ixr;
        this.pc = pc;
        this.cc = cc;
        this.cache = c;
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
    public boolean executeLDR(String binaryInstruction) {
        // extract instructions.
        String reg_str = binaryInstruction.substring(6, 8);  
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  
        String address_str = binaryInstruction.substring(11, 16);  

        int reg = Integer.parseInt(reg_str, 2);
        //int ix = Integer.parseInt(ix_str, 2);
        //int iBit = Integer.parseInt(iBit_str, 2);
        //int address = Integer.parseInt(address_str, 2);
    
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  // Calculate effective address
        
        int value = memory.loadMemoryValue(ea);  // Load the value from memory
    
        gpr.setGPR(reg, (short) value); 
        cache.write(ea, value);
        return false;  
    }

    // STR.
    public boolean executeSTR(String binaryInstruction) {
        // extract instructions.
        String reg_str = binaryInstruction.substring(6, 8);  
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  
        String address_str = binaryInstruction.substring(11, 16); 

        int reg = Integer.parseInt(reg_str, 2);
        //int ix = Integer.parseInt(ix_str, 2);
        //int iBit = Integer.parseInt(iBit_str, 2);
        //int address = Integer.parseInt(address_str, 2);


        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  

        int value = gpr.getGPR(reg); 

        memory.storeValue(ea, value);  // Store the value into memory
        //System.out.println("we have stored: " + value + " in address: " + ea);
        cache.write(ea, value);
        return false;  
    }


    // LDA.
    public boolean executeLDA(String binaryInstruction) {
        // Extract the fields (register, index register, indirect bit, address)
        String reg_str = binaryInstruction.substring(6, 8);  
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  
        String address_str = binaryInstruction.substring(11, 16);  
    
        int reg = Integer.parseInt(reg_str, 2);
        //int ix = Integer.parseInt(ix_str, 2);
        //int iBit = Integer.parseInt(iBit_str, 2);
        //int address = Integer.parseInt(address_str, 2);
    
        // Calculate the effective address (same method as LDR/STR)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  

        System.out.println("We are loading \t" + reg + "from address \t" + ea);
    
        // Store the effective address directly in the register
        gpr.setGPR(reg, (short) ea); 
        
    
        return false;  // Continue execution
    }

    // LDX.
    public boolean executeLDX(String binaryInstruction) {
        // Extract fields.
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  // Extract indirect bit
        String address_str = binaryInstruction.substring(11, 16);  // Correct bits for the address
    
        int ix = Integer.parseInt(ix_str, 2);  // Convert IX field to integer
        //int iBit = Integer.parseInt(iBit_str, 2);  // Convert I bit to integer
        //int address = Integer.parseInt(address_str, 2);  // Convert address field to integer
    
        // Calculate Effective Address (EA)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
    
        // Load value from memory at effective address into the specified index register
        int value = memory.loadMemoryValue(ea);
    
        // update ixr.
        ixr.setIndexRegister(ix, (short) value);
        
        return false;  // Continue execution
    }

    // STX.
    public boolean executeSTX(String binaryInstruction) {
        // Extract fields.
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  
        String address_str = binaryInstruction.substring(11, 16);
    
        int ix = Integer.parseInt(ix_str, 2);  // Convert IX field to integer
        //int iBit = Integer.parseInt(iBit_str, 2);  // Convert I bit to integer
        //int address = Integer.parseInt(address_str, 2);  // Convert address field to integer
    
        // Calculate Effective Address (EA)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
    
        // Get the value from the Index Register (IX) and store it in memory at the EA
        int value = ixr.getIndexRegister(ix);
    
        memory.storeValue(ea, value);  // Store the value in memory
        cache.write(ea, value);
        
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
        //int ix = Integer.parseInt(ix_str, 2);
        //int iBit = Integer.parseInt(iBit_str, 2);
        //int address = Integer.parseInt(address_str, 2);

        // Get the contents of the register
        int regValue = gpr.getGPR(reg);
        
        // Calculate the Effective Address (EA)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
        System.out.println("Calculated Effective Address: " + ea);

        // If the value in the register is zero, jump to the EA, otherwise we just continue.
        if (regValue == 0) {
            pc.setPC(ea);  // Update PC to EA
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
        //int ix = Integer.parseInt(ix_str, 2);
        //int iBit = Integer.parseInt(iBit_str, 2);
        //int address = Integer.parseInt(address_str, 2);

        // Get the contents of the register
        int regValue = gpr.getGPR(reg);
        
        // Calculate the Effective Address (EA)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
        System.out.println("Calculated Effective Address: " + ea);

        // If the value in the register is NOT zero, jump to the EA, otherwise increment the PC
        if (regValue != 0) {
            pc.setPC(ea);  // Update PC to EA
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
        //int ix = Integer.parseInt(ix_str, 2);
        //int iBit = Integer.parseInt(iBit_str, 2);
        //int address = Integer.parseInt(address_str, 2);

        // Get the effective address (EA)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);
        System.out.println("Calculated Effective Address: " + ea);

        // Check the condition based on the condition code index
        boolean conditionMet = false;
        switch (conditionCodeIndex) {
            case 0:
                conditionMet = cc.isOverflow();
                break;
            case 1:
                conditionMet = cc.isUnderflow();
                break;
            case 2:
                conditionMet = cc.isDivZero();
                break;
            case 3:
                conditionMet = cc.isEqual();
                break;
            default:
                return false;
        }

        // Jump if the condition is met
        if (conditionMet) {
            pc.setPC(ea);
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
        System.out.println("Calculated Effective Address: " + ea);

        // Set the Program Counter (PC) to the effective address
        pc.setPC(ea);

        return false;  // Continue execution
    }

    // JSR.
    public boolean executeJSR(String binaryInstruction) {
        // Extract the fields: index register, indirect bit, address
        String ix_str = binaryInstruction.substring(8, 10);  
        String iBit_str = binaryInstruction.substring(10, 11);  
        String address_str = binaryInstruction.substring(11, 16);  

        //int ix = Integer.parseInt(ix_str, 2);
        //int iBit = Integer.parseInt(iBit_str, 2);
        //int address = Integer.parseInt(address_str, 2);

        // Calculate the Effective Address (EA)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
        System.out.println("Calculated Effective Address: " + ea);

        // Step 1: Save the return address (PC + 1) in GPR[3]
        gpr.setGPR(3, (short) (pc.getPC()));  

        // Step 2: Set the PC to the EA (jump to the subroutine)
        pc.setPC(ea);  

        return false;  // Continue execution
    }

    // RFS.
    public boolean executeRFS(String binaryInstruction) {
        // Extract immediate value from the instruction
        String immed_str = binaryInstruction.substring(11, 16);  // Immediate value is in the last 5 bits
        int immed = Integer.parseInt(immed_str, 2);  // Convert immediate field to integer

        // Load the immediate value into GPR[0]
        gpr.setGPR(0, (short) immed);

        // Set the PC to the value stored in GPR[3] (return address)
        int returnAddress = gpr.getGPR(3);
        pc.setPC(returnAddress);

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
        //int ix = Integer.parseInt(ix_str, 2);
        //int iBit = Integer.parseInt(iBit_str, 2);
        //int address = Integer.parseInt(address_str, 2);
    
        // Get the current value of the register and decrement it by 1
        int regValue = gpr.getGPR(reg);
        regValue -= 1;
        gpr.setGPR(reg, (short) regValue);
    
    
        // Calculate the effective address (EA) if needed
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
    
        // Check if the register value is greater than 0
        if (regValue > 0) {
            pc.setPC(ea);  // Jump to the effective address
        } 
        
        else {
            pc.incrementPC();  // Move to the next instruction
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
        //int ix = Integer.parseInt(ix_str, 2);  // Convert index register field to integer
        //int iBit = Integer.parseInt(iBit_str, 2);  // Convert indirect bit field to integer
        //int address = Integer.parseInt(address_str, 2);  // Convert address field to integer
    
        // Get the value in the register
        int regValue = gpr.getGPR(reg);
        
        // Calculate Effective Address (EA)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  
    
        // If the register value is greater than or equal to 0, set PC to EA
        if (regValue >= 0) {
            pc.setPC(ea);
        } 
        else {
            pc.incrementPC();  // Else, increment the PC to the next instruction
        }
    
        return false;  // Continue execution
    }

    // AMR.
    public boolean executeAMR(String binaryInstruction) {
        // Extract fields from the binary instruction
        String reg_str = binaryInstruction.substring(6, 8);  // Register field
        String ix_str = binaryInstruction.substring(8, 10);  // Index register field
        String iBit_str = binaryInstruction.substring(10, 11);  // Indirect bit field
        String address_str = binaryInstruction.substring(11, 16);  // Address field
    
        int reg = Integer.parseInt(reg_str, 2);  // Convert register field to integer
        //int ix = Integer.parseInt(ix_str, 2);  // Convert index register field to integer
        //int iBit = Integer.parseInt(iBit_str, 2);  // Convert indirect bit field to integer
        //int address = Integer.parseInt(address_str, 2);  // Convert address field to integer
    
        // Get the value in the register
        int regValue = gpr.getGPR(reg);
        
        // Calculate Effective Address (EA)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  

        //load the value present in effective address of memroy
        int value = memory.loadMemoryValue(ea);
        
        //add the value from  memory with register
        int result =  value + regValue ;

        gpr.setGPR( reg, (short) result);
        return false;  // Continue execution
    }

    // SMR.
    public boolean executeSMR(String binaryInstruction) {
        // Extract fields from the binary instruction
        String reg_str = binaryInstruction.substring(6, 8);  // Register field
        String ix_str = binaryInstruction.substring(8, 10);  // Index register field
        String iBit_str = binaryInstruction.substring(10, 11);  // Indirect bit field
        String address_str = binaryInstruction.substring(11, 16);  // Address field
    
        int reg = Integer.parseInt(reg_str, 2);  // Convert register field to integer
        //int ix = Integer.parseInt(ix_str, 2);  // Convert index register field to integer
        //int iBit = Integer.parseInt(iBit_str, 2);  // Convert indirect bit field to integer
        //int address = Integer.parseInt(address_str, 2);  // Convert address field to integer
    
        // Get the value in the register
        int regValue = gpr.getGPR(reg);
        
        // Calculate Effective Address (EA)
        int ea = calculateEffectiveAddress(ix_str, iBit_str, address_str);  

        //load the value present in effective address of memroy
        int value = memory.loadMemoryValue(ea);
        int result = 0;
        boolean flag  = cc.isUnderflow();

        System.out.println("Initial state of underflow is: "+ flag);

        if (flag){
            System.out.println("We are adding  Register value: \t" + regValue + "\t and value from memory: \t" + value);

            result = regValue + value;
            System.out.println("The result of the addition is "  + result);

        }

        else{
        //substract the value from  memory with register
         System.out.println("We are substracting  Register value: \t" + regValue + "\t and value from memory: \t" + value);
         result =  regValue - value;
         System.out.println("The result of the substraction is "  + result);

        }

        
        cc.updateConditionCodes(result);

        //System.out.println("Status of underflow is:" + cc.isUnderflow());

        gpr.setGPR( reg, (short) result);
        return false;  // Continue execution
    }


    // AIR (Add Immediate to Register)
    public boolean executeAIR(String binaryInstruction) {
        String reg_str = binaryInstruction.substring(6, 8);  // Register field
        String offset_str = binaryInstruction.substring(11, 16);  // Immediate Value

        int reg = Integer.parseInt(reg_str, 2);  // Convert register field to integer
        int immed = Integer.parseInt(offset_str, 2);  // Convert address field to integer

        // Get the value in the register
        int regValue = gpr.getGPR(reg);

        // Add the immediate value to the register value
        int result = regValue + immed;

        // Store the result back in the register
        gpr.setGPR(reg, (short) result);

        return false;  // Continue execution
    } 

    //SIR
    public boolean executeSIR(String binaryInstruction) {
        String reg_str = binaryInstruction.substring(6, 8);  // Register field
        String ix_str = binaryInstruction.substring(8, 10);  // Index register field
        String iBit_str = binaryInstruction.substring(10, 11);  // Indirect bit field
        String immed_str = binaryInstruction.substring(11, 16);  // Immediate Value

    
        int reg = Integer.parseInt(reg_str, 2);  // Convert register field to integer
        //int ix = Integer.parseInt(ix_str, 2);  // Convert index register field to integer
        //int iBit = Integer.parseInt(iBit_str, 2);  // Convert indirect bit field to integer
        //int immed = Integer.parseInt(immed_str, 2);  // Convert address field to integer 

        int immed = calculateEffectiveAddress(ix_str, iBit_str, immed_str);

        // Get the value in the register
        int regValue = gpr.getGPR(reg);
        
        // do nothing if immed is 0.
        if (immed == 0) {
            return false;
        }

        else if (regValue == 0) {
            //register = 0,load r with -immed
            regValue = -(immed);
            cc.setUnderflow(true);
            gpr.setGPR( reg, (short) Math.abs(regValue));
            return false;
        }
        
        int result =   regValue - immed;
        if(result < 0) {
            //if the result is negative, set the underflow to 1
            cc.setUnderflow(true);
        }

        else {
            cc.setUnderflow(false);
        }
        gpr.setGPR( reg, (short) Math.abs(result));
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
        } 
        else {
            cc.setOverflow(false);  // No overflow
        }

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
    
        // Check for divide by zero
        if (valueRy == 0) {
            cc.setDivZero(true);  // Set divide by zero flag
            return false;  // Stop execution due to divide by zero
        }
    
        // Perform division and get quotient and remainder
        int quotient = valueRx / valueRy;
        int remainder = valueRx % valueRy;
    
        // Store the quotient in Rx and the remainder in Rx+1
        gpr.setGPR(rx, (short) quotient);
        gpr.setGPR(rx + 1, (short) remainder);
    
        return false;  // Continue execution
    }    

    // TRR: this one sees if two registers are equal (pretty cool).
    public boolean executeTRR(String binaryInstruction) {
        // Extract Rx and Ry fields
        String rx_str = binaryInstruction.substring(6, 8);
        String ry_str = binaryInstruction.substring(8, 10);

        int rx = Integer.parseInt(rx_str, 2);
        int ry = Integer.parseInt(ry_str, 2);

        // Get the contents of the registers Rx and Ry
        int valueRx = gpr.getGPR(rx);
        int valueRy = gpr.getGPR(ry);

        // Check if the values in Rx and Ry are equal
        if (valueRx == valueRy) {
            cc.setEqual(true);  // Set the Equal flag (bit 3 in condition code)
        } 
        else {
            cc.setEqual(false);  // Clear the Equal flag
        }

        return false;  // Continue execution
    }

    // AND.
    public boolean executeAND(String binaryInstruction) {
        // Extract Rx and Ry fields
        String rx_str = binaryInstruction.substring(6, 8);  
        String ry_str = binaryInstruction.substring(8, 10);

        int rx = Integer.parseInt(rx_str, 2);
        int ry = Integer.parseInt(ry_str, 2);

        // Get the contents of the registers Rx and Ry
        int valueRx = gpr.getGPR(rx);
        int valueRy = gpr.getGPR(ry);
        int result = 0;

        // Perform the logical AND operation
        if (valueRx>valueRy) {

            if (valueRy<0) { 
                result = valueRx;
                cc.updateConditionCodes(valueRy);
            }

            else{
                result = valueRy;
            }
        }    
            
            else
            {
                if(valueRx<0)
                {
                    result = valueRy;
                    cc.updateConditionCodes(valueRx);
                }

                else{
                    result = valueRx;

                }
            }
               
        
        System.out.println("The value of the AND Operation is:"+ result);

        // Store the result in Rx
        gpr.setGPR(rx, (short)result);

        return false;   // Continue execution
    }

    // ORR.
    public boolean executeORR(String binaryInstruction) {
        // Extract Rx and Ry fields
        String rx_str = binaryInstruction.substring(6, 8);  
        String ry_str = binaryInstruction.substring(8, 10);

        int rx = Integer.parseInt(rx_str, 2);
        int ry = Integer.parseInt(ry_str, 2);

        // Validate Rx and Ry to be within valid range (0-3)
        if (rx < 0 || rx > 3 || ry < 0 || ry > 3) {
            throw new IllegalArgumentException("ORR operation requires valid Rx and Ry in range [0, 3].");
        }

        // Get the contents of the registers Rx and Ry
        int valueRx = gpr.getGPR(rx);
        int valueRy = gpr.getGPR(ry);

        // Perform the logical OR operation
        int result = valueRx | valueRy;

        // Store the result back into Rx
        gpr.setGPR(rx, (short)result);

        return false; // Continue execution
    }

    // NOT.
    public boolean executeNOT(String binaryInstruction) {
        // Extract Rx field
        String rx_str = binaryInstruction.substring(6, 8);  
        int rx = Integer.parseInt(rx_str, 2);

        // Get the value in the Rx register
        int valueRx = gpr.getGPR(rx);

        // Perform logical NOT on the value of Rx
        int result = ~valueRx;

        // Store the result back into Rx (handling 16-bit wrapping)
        gpr.setGPR(rx, (short) result);

        return false; // Continue execution
    }

    public boolean executeIN(String binaryInstruction) {
        Scanner scanner = new Scanner(System.in);
    
        // Extract Rx and DevID fields from the binary instruction
        String rx_str = binaryInstruction.substring(6, 8);
        String devid_str = binaryInstruction.substring(11, 16);
    
        int rx = Integer.parseInt(rx_str, 2);  // Register to store the input
        int devid = Integer.parseInt(devid_str, 2);  // Device ID
    
        // Simulate input based on device ID
        int input = 0;
    
        if (devid == 0) {  // Console Keyboard
            while (true) {
                System.out.print("Enter a number: ");
                if (scanner.hasNextInt()) {
                    int tempInput = scanner.nextInt();
                    if (tempInput >= 0 && tempInput <= 65535) {
                        input = tempInput;
                        break;
                    } else {
                        System.out.println("Error: Number must be between 0 and 65535.");
                    }
                } else {
                    System.out.println("Error: Invalid input. Please enter a whole number.");
                    scanner.next();  // Clear invalid input
                }
            }
        } 
        
        // Card Reader (File Input).
        else if (devid == 2) { 
            // Prompt for file path
            System.out.print("Enter file path: ");
            String filePath = scanner.next();
    
            System.out.print("Here is the input sentence: ");
            try (FileInputStream fileReader = new FileInputStream(filePath)) {
                int memoryAddress = 200;  // Starting memory address for file contents
                int charRead;
    
                // Read characters from the file and store them into memory
                while ((charRead = fileReader.read()) != -1) {
                    // Store character in memory
                    char lowerCase = Character.toLowerCase((char)charRead);
                    memory.storeValue(memoryAddress++, (short) lowerCase);
                    System.out.print(lowerCase);
    
                    // Check for memory overflow
                    if (memoryAddress >= 2048) { 
                        System.out.println("Error: Memory overflow while reading the file.");
                        return false;
                    }
                }
                System.out.println("File contents successfully stored in memory starting at address 100.");
            } 
            
            catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
                return false;
            }
        } 

        // Search word input.
        else if (devid == 3) { 
            System.out.print("\nEnter target word: ");
            String word = scanner.next();
            int memoryAddress = 100;
            
            for (char c : word.toCharArray()) {
                char lowerCase = Character.toLowerCase((char)c);
                memory.storeValue(memoryAddress++, (short) lowerCase);
            }

            // store period at the end.
            memory.storeValue(memoryAddress, (short) 46); 
        }
        
        else {
            System.out.println("Device ID " + devid + " not supported for IN operation.");
            return false;
        }
    
        // Store the input in the specified register Rx
        gpr.setGPR(rx, (short) input);
    
        return false;  // Continue execution
    }
    
    


    // OUT.
    public boolean executeOUT(String binaryInstruction) {
        // Extract Rx and DevID fields from the binary instruction
        String rx_str = binaryInstruction.substring(6, 8);
        String devid_str = binaryInstruction.substring(11, 16);

        int rx = Integer.parseInt(rx_str, 2);  // Register from which to output
        int devid = Integer.parseInt(devid_str, 2);  // Device ID

        // Get the value from the specified register
        int output = gpr.getGPR(rx);  

        switch (devid) {
            case 0:
                System.out.println("Error: OUT operation not supported for Console Keyboard (Device ID 0).");
                break;

            case 1:
                // Valid output to Console Printer
                System.out.println("Output from GPR[" + rx + "] to Console Printer (Device ID 1): " + output);
                break;

            case 2:
                // Print ASCII for character: Kishan, test to see if this is good.
                System.out.print((char) output);
                break;

            default:
                if (devid >= 3 && devid <= 31) {
                    System.out.println("Error: OUT operation not supported for Device ID " + devid + ".");
                } else {
                    throw new IllegalArgumentException("Invalid Device ID for OUT operation. DevID should be between 0 and 31.");
                }
                break;
        }

        return false;  // Continue execution
    }

    // CHK.
    public boolean executeCHK(String binaryInstruction) {
        // Extract Rx and DevID fields from the binary instruction
        String rx_str = binaryInstruction.substring(6, 8);
        String devid_str = binaryInstruction.substring(11, 16);

        int rx = Integer.parseInt(rx_str, 2);  // Register to store the device status
        int devid = Integer.parseInt(devid_str, 2);  // Device ID

        // Simulate checking device status based on device ID
        int deviceStatus = 0;

        // Console Keyboard.
        if (devid == 0) {  
            deviceStatus = 1;  
        } 

        // Console Printer
        else if (devid == 1) {  
            deviceStatus = 1;  
        } 

        // Card Reader
        else if (devid == 2) {  
            deviceStatus = 1; 
        } 

        else {
            System.out.println("Device ID " + devid + " not supported for CHK operation.");
            return false;  // Unsupported device, continue execution
        }

        // Store the device status in the specified register Rx
        gpr.setGPR(rx, (short)deviceStatus);

        System.out.println("CHK executed: Stored device status " + deviceStatus + " in GPR[" + rx + "] for Device ID " + devid);
        return false;  // Continue execution
    }

    public boolean executeHLT() {
        return true;
    }
}