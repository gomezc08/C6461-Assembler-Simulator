package components;

public class CPU {

    private Memory memory;
    private GeneralPurposeRegisters gpr;
    private IndexRegisters ixr;
    private int pc;  // Program Counter
    private ConditionCode cc;

    public CPU(Memory memory, GeneralPurposeRegisters gpr, IndexRegisters ixr) {
        this.memory = memory;
        this.gpr = gpr;
        this.ixr = ixr;
        this.pc = 0;  // Initialize the Program Counter to 0
        this.cc = new ConditionCode();
    }

    // Main cycle: Fetch-Decode-Execute
    public void run() {
        boolean halt = false;
        while (!halt) {
            String binaryInstruction = fetch();
            halt = decodeAndExecute(binaryInstruction);
        }
    }

    // Fetch the instruction from memory
    private String fetch() {
        int instruction = memory.loadMemoryValue(pc);  // Fetch instruction from memory at PC
        pc++;  // Increment PC for the next instruction
        return String.format("%16s", Integer.toBinaryString(instruction)).replace(' ', '0');  // Return 16-bit binary string
    }

    // Decode and execute the instruction directly
    private boolean decodeAndExecute(String binaryInstruction) {
        String opcode = binaryInstruction.substring(0, 6);  // Extract opcode (first 6 bits)

        switch (opcode) {
            case "000001":  // LDR - Load Register from Memory
                return executeLoad(binaryInstruction);
            case "000010":  // STR - Store Register to Memory
                return executeStore(binaryInstruction);
            case "000011":  // LDA - Load Address into Register
                return executeLoadAddress(binaryInstruction);
            case "100001":  // LDX - Load Index Register
                return executeLoadIndex(binaryInstruction);
            case "101010":  // STX - Store Index Register
                return executeStoreIndex(binaryInstruction);

            // Transfer instructions
            case "001010":  // JZ
            case "001011":  // JNE
            case "001100":  // JCC
            case "001101":  // JMA
            case "001110":  // JSR
            case "001111":  // RFS
            case "010000":  // SOB
            case "010001":  // JGE
                return executeTransfer(binaryInstruction, opcode);

            // Arithmetic/Logical instructions
            case "000100":  // AMR - Add Memory to Register
            case "000101":  // SMR - Subtract Memory from Register
            case "000110":  // AIR - Add Immediate to Register
            case "000111":  // SIR - Subtract Immediate from Register
                return executeArithmetic(binaryInstruction, opcode);

            // Register to Register operations
            case "111000":  // MLT - Multiply Register
            case "111001":  // DVD - Divide Register
            case "111010":  // TRR - Test Registers
            case "111011":  // AND - Logical AND
            case "111100":  // ORR - Logical OR
            case "111101":  // NOT - Logical NOT
                return executeRegisterToRegister(binaryInstruction, opcode);

            // Shift/Rotate instructions
            case "110001":  // SRC
            case "110010":  // RRC
                return executeShiftRotate(binaryInstruction, opcode);

            // I/O instructions
            case "110011":  // IN
            case "110100":  // OUT
            case "110101":  // CHK
                return executeIO(binaryInstruction, opcode);

            case "000000":  // HLT - Halt the CPU
                return true;  // Stop execution

            default:
                throw new IllegalArgumentException("Unknown opcode: " + opcode);
        }
    }

    // Helper functions for Load/Store operations
    private boolean executeLoad(String binaryInstruction) {
        String reg = binaryInstruction.substring(6, 8);  // Extract the register bits
        String ix = binaryInstruction.substring(8, 10);  // Extract the index register bits
        String iBit = binaryInstruction.substring(10, 11);  // Extract the indirect bit
        String address = binaryInstruction.substring(11, 16);  // Extract the address bits

        int ea = calculateEffectiveAddress(ix, iBit, address);  // Calculate effective address
        int value = memory.loadMemoryValue(ea);  // Load the value from memory

        gpr.setGPR(Integer.parseInt(reg, 2), (short) value);  // Set the value into the specified register
        return false;  // Continue execution
    }

    private boolean executeStore(String binaryInstruction) {
        String reg = binaryInstruction.substring(6, 8);  // Extract the register bits
        String ix = binaryInstruction.substring(8, 10);  // Extract the index register bits
        String iBit = binaryInstruction.substring(10, 11);  // Extract the indirect bit
        String address = binaryInstruction.substring(11, 16);  // Extract the address bits

        int ea = calculateEffectiveAddress(ix, iBit, address);  // Calculate effective address
        int value = gpr.getGPR(Integer.parseInt(reg, 2));  // Get the value from the specified register

        memory.storeValue(ea, value);  // Store the value into memory
        return false;  // Continue execution
    }

    // More Load/Store operations
    private boolean executeLoadAddress(String binaryInstruction) {
        String reg = binaryInstruction.substring(6, 8);  // Extract the register bits
        String ix = binaryInstruction.substring(8, 10);
        String iBit = binaryInstruction.substring(10, 11);
        String address = binaryInstruction.substring(11, 16);

        int ea = calculateEffectiveAddress(ix, iBit, address);  // Calculate effective address
        gpr.setGPR(Integer.parseInt(reg, 2), (short) ea);  // Load effective address into the register
        return false;
    }

    private boolean executeLoadIndex(String binaryInstruction) {
        String ix = binaryInstruction.substring(6, 8);  // Extract index register bits
        String iBit = binaryInstruction.substring(8, 9);
        String address = binaryInstruction.substring(9, 16);

        int ea = calculateEffectiveAddress(ix, iBit, address);
        ixr.setIndexRegister(Integer.parseInt(ix, 2), (short) memory.loadMemoryValue(ea));  // Load memory into index register
        return false;
    }

    private boolean executeStoreIndex(String binaryInstruction) {
        String ix = binaryInstruction.substring(6, 8);  // Extract index register bits
        String address = binaryInstruction.substring(8, 16);

        int ea = calculateEffectiveAddress(ix, "0", address);  // Store index register to memory
        memory.storeValue(ea, ixr.getIndexRegister(Integer.parseInt(ix, 2)));
        return false;
    }

    // Helper function for calculating effective address
    private int calculateEffectiveAddress(String ix, String iBit, String address) {
        int baseAddress = Integer.parseInt(address, 2);  // Convert address bits to integer

        if (!ix.equals("00")) {  // If IX register is used
            int indexValue = ixr.getIndexRegister(Integer.parseInt(ix, 2));  // Get the index register value
            baseAddress += indexValue;
        }

        if (iBit.equals("1")) {  // If indirect addressing is used
            baseAddress = memory.loadMemoryValue(baseAddress);  // Use memory indirection
        }

        return baseAddress;  // Return the calculated effective address
    }

    // Add functions to handle the rest of the instructions (Transfer, Arithmetic, Register-to-Register, Shift/Rotate, I/O, etc.)
    private boolean executeTransfer(String binaryInstruction, String opcode) {
        String reg = binaryInstruction.substring(6, 8);  // Extract the register bits
        String ix = binaryInstruction.substring(8, 10);  // Extract the index register bits
        String iBit = binaryInstruction.substring(10, 11);  // Extract the indirect bit
        String address = binaryInstruction.substring(11, 16);  // Extract the address bits
    
        int ea = calculateEffectiveAddress(ix, iBit, address);  // Calculate effective address
        int regValue = gpr.getGPR(Integer.parseInt(reg, 2));  // Get the register value
    
        switch (opcode) {
            case "001010":  // JZ - Jump if Zero
                if (regValue == 0) pc = ea;  // If the register is zero, jump
                break;
            case "001011":  // JNE - Jump if Not Equal
                if (regValue != 0) pc = ea;  // If the register is not zero, jump
                break;
            case "001100":  // JCC - Jump if Condition Code
                if (cc.isOverflow()) pc = ea;
                break;
            case "001101":  // JMA - Jump to Memory Address
                pc = ea;  // Unconditional jump to address
                break;
            case "001110":  // JSR - Jump to Subroutine
                gpr.setGPR(3, (short) pc);  // Store the return address in R3
                pc = ea;  // Jump to subroutine
                break;
            case "001111":  // RFS - Return from Subroutine
                pc = gpr.getGPR(3);  // Return to the address in R3
                break;
            case "010000":  // SOB - Subtract One and Branch
                gpr.setGPR(Integer.parseInt(reg, 2), (short) (regValue - 1));  // Decrement the register
                if (regValue > 0) pc = ea;  // If still greater than zero, jump
                break;
            case "010001":  // JGE - Jump if Greater or Equal
                if (regValue >= 0) pc = ea;  // Jump if the register value is greater than or equal to zero
                break;
            default:
                throw new IllegalArgumentException("Unknown transfer opcode: " + opcode);
        }
        return false;
    }
    

    private boolean executeArithmetic(String binaryInstruction, String opcode) {
        String reg = binaryInstruction.substring(6, 8);  // Extract the register bits
        String ix = binaryInstruction.substring(8, 10);  // Extract the index register bits
        String iBit = binaryInstruction.substring(10, 11);  // Extract the indirect bit
        String address = binaryInstruction.substring(11, 16);  // Extract the address bits
    
        int ea = calculateEffectiveAddress(ix, iBit, address);  // Calculate effective address
        int value = memory.loadMemoryValue(ea);  // Load the value from memory
        int regValue = gpr.getGPR(Integer.parseInt(reg, 2));  // Get the register value
    
        switch (opcode) {
            case "000100":  // AMR - Add Memory to Register
                int result = regValue + value;
                gpr.setGPR(Integer.parseInt(reg, 2), (short) result);

                // Update condition codes based on the result
                cc.setOverflow(result > 32767 || result < -32768);  // Overflow for 16-bit signed integers
                cc.setUnderflow(result < 0);  // Underflow if result is negative
                cc.setEqual(result == 0);  // Equal if result is zero
                break;
            case "000101":  // SMR - Subtract Memory from Register
                result = regValue - value;
                gpr.setGPR(Integer.parseInt(reg, 2), (short) result);
                cc.updateConditionCodes(result);  // Update condition codes after subtraction
                break;
            
            case "000110":  // AIR - Add Immediate to Register
                result = regValue + ea;
                gpr.setGPR(Integer.parseInt(reg, 2), (short) result);
                cc.updateConditionCodes(result);  // Update condition codes after immediate addition
                break;
            
            case "000111":  // SIR - Subtract Immediate from Register
                result = regValue - ea;
                gpr.setGPR(Integer.parseInt(reg, 2), (short) result);
                cc.updateConditionCodes(result);  // Update condition codes after immediate subtraction
                break;
            
            default:
                throw new IllegalArgumentException("Unknown arithmetic opcode: " + opcode);
        }
        return false;
    }
    

    private boolean executeRegisterToRegister(String binaryInstruction, String opcode) {
        String regX = binaryInstruction.substring(6, 8);  // Extract Rx
        String regY = binaryInstruction.substring(8, 10);  // Extract Ry
    
        int rxValue = gpr.getGPR(Integer.parseInt(regX, 2));  // Get the value of Rx
        int ryValue = gpr.getGPR(Integer.parseInt(regY, 2));  // Get the value of Ry
    
        switch (opcode) {
            case "111000":  // MLT - Multiply
                gpr.setGPR(Integer.parseInt(regX, 2), (short) (rxValue * ryValue));
                break;
            case "111001":  // DVD - Divide
                if (ryValue == 0) {
                    cc.setDivZero(true);  // Set the DivZero flag if attempting to divide by zero
                } 
                
                else {
                    gpr.setGPR(Integer.parseInt(regX, 2), (short) (rxValue / ryValue));  // Perform division
                    cc.setDivZero(false);  // Clear the DivZero flag
                }
                break;
            case "111010":  // TRR - Test Register Equality
                cc.setEqual(rxValue == ryValue);
                break;
            case "111011":  // AND
                gpr.setGPR(Integer.parseInt(regX, 2), (short) (rxValue & ryValue));
                break;
            case "111100":  // ORR
                gpr.setGPR(Integer.parseInt(regX, 2), (short) (rxValue | ryValue));
                break;
            case "111101":  // NOT
                gpr.setGPR(Integer.parseInt(regX, 2), (short) (~rxValue));
                break;
            default:
                throw new IllegalArgumentException("Unknown register-to-register opcode: " + opcode);
        }
        return false;
    }
    

    private boolean executeShiftRotate(String binaryInstruction, String opcode) {
        String reg = binaryInstruction.substring(6, 8);  // Extract the register bits
        String count = binaryInstruction.substring(8, 12);  // Extract the count (number of shifts/rotates)
        String lr = binaryInstruction.substring(12, 13);  // Left/Right bit
        String al = binaryInstruction.substring(13, 14);  // Arithmetic/Logical bit
    
        int regValue = gpr.getGPR(Integer.parseInt(reg, 2));
        int shiftCount = Integer.parseInt(count, 2);
    
        switch (opcode) {
            case "110001":  // SRC - Shift Register
                if (al.equals("1")) {  // Arithmetic shift
                    regValue = (lr.equals("1")) ? regValue >> shiftCount : regValue << shiftCount;
                } else {  // Logical shift
                    regValue = (lr.equals("1")) ? regValue >>> shiftCount : regValue << shiftCount;
                }
                gpr.setGPR(Integer.parseInt(reg, 2), (short) regValue);
                break;
            case "110010":  // RRC - Rotate Register
                if (lr.equals("1")) {  // Rotate right
                    regValue = Integer.rotateRight(regValue, shiftCount);
                } else {  // Rotate left
                    regValue = Integer.rotateLeft(regValue, shiftCount);
                }
                gpr.setGPR(Integer.parseInt(reg, 2), (short) regValue);
                break;
            default:
                throw new IllegalArgumentException("Unknown shift/rotate opcode: " + opcode);
        }

        // After shift/rotate, update condition codes based on the new register value
        cc.updateConditionCodes(regValue);
        return false;
    }
    

    private boolean executeIO(String binaryInstruction, String opcode) {
        String reg = binaryInstruction.substring(6, 8);  // Extract the register bits
        String devID = binaryInstruction.substring(11, 16);  // Extract the device ID
    
        switch (opcode) {
            case "110011":  // IN - Input to Register
                // Simulated input device, set a value in the register
                gpr.setGPR(Integer.parseInt(reg, 2), (short) 1234);  // Example: load the input device value
                break;
            case "110100":  // OUT - Output Register to Device
                // Simulated output device, print register value
                System.out.println("Output from Register " + reg + ": " + gpr.getGPR(Integer.parseInt(reg, 2)));
                break;
            case "110101":  // CHK - Check Device Status
                // Simulate device status check, set result in the register
                gpr.setGPR(Integer.parseInt(reg, 2), (short) 1);  // Example: device is ready
                break;
            default:
                throw new IllegalArgumentException("Unknown I/O opcode: " + opcode);
        }
        return false;
    }

    public int getPc() {
        return this.pc;
    }
    

    public static void main(String[] args) {
        // Initialize memory, registers, and CPU components
        Memory memory = new Memory();
        GeneralPurposeRegisters gpr = new GeneralPurposeRegisters(4); // Assuming 4 general-purpose registers
        IndexRegisters ixr = new IndexRegisters(3); // Assuming 3 index registers
    
        // Load some test values into memory (for simulation purposes)
        memory.storeValue(0, 0b0000010000100011); // Example instruction in binary
        memory.storeValue(1, 0b0000100100101010); // Another example instruction
    
        // Create the CPU object with the initialized components
        CPU cpu = new CPU(memory, gpr, ixr);
    
        // Run the CPU simulation
        cpu.run();
    
        // Display final register values (for debugging purposes)
        System.out.println("Final GPR Values:");
        System.out.println(gpr.toString());
    
        System.out.println("Final Index Register Values:");
        System.out.println(ixr.toString());
    
        System.out.println("Program Counter: " + cpu.getPc()); // Assuming a getter for PC
    }    
    
}
