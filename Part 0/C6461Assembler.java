import java.io.*;
import java.util.*;

public class C6461Assembler {
    // Holds label locations
    private static Map<String, Integer> labelTable = new HashMap<>();
    private static List<String> listingFile = new ArrayList<>();
    private static List<String> loadFile = new ArrayList<>();
    private static int currentAddress = 0;
    private static Map<String, String> opcodeMap = new HashMap<>();
    private static Set<String> memoryToMemory = new HashSet<>();
    private static Set<String> registerToRegister = new HashSet<>();
    private static Set<String> shiftRotate = new HashSet<>();
    private static Set<String> inputOutput = new HashSet<>();

    // initialize opcodeMap.
    static {
        // Load/Store Instructions.
        opcodeMap.put("LDR", "000001"); 
        opcodeMap.put("STR", "000010");
        opcodeMap.put("LDA", "000011");
        opcodeMap.put("LDX", "100001"); 
        opcodeMap.put("STX", "101010"); 
    
        // Transfer Instructions.
        opcodeMap.put("JZ", "001010");  
        opcodeMap.put("JNE", "001011");  
        opcodeMap.put("JCC", "001100");  
        opcodeMap.put("JMA", "001101");  
        opcodeMap.put("JSR", "001110");  
        opcodeMap.put("RFS", "001111");  
        opcodeMap.put("SOB", "010000");  
        opcodeMap.put("JGE", "010001");

        // Arithmetic/Logical Instructions.
        opcodeMap.put("AMR", "000100");  
        opcodeMap.put("SMR", "000101");  
        opcodeMap.put("AIR", "000110");  
        opcodeMap.put("SIR", "000111"); 

        // Register to Register Operations.
        opcodeMap.put("MLT", "111000"); 
        opcodeMap.put("DVD", "111001"); 
        opcodeMap.put("TRR", "111010"); 
        opcodeMap.put("AND", "111011"); 
        opcodeMap.put("ORR", "111100");
        opcodeMap.put("NOT", "111101"); 

        // Shift/Rotate Operations.
        opcodeMap.put("SRC", "110001"); 
        opcodeMap.put("RRC", "110010"); 

        // I/O Operations.
        opcodeMap.put("IN", "110011");  
        opcodeMap.put("OUT", "110100"); 
        opcodeMap.put("CHK", "110101"); 

        // Halt.
        opcodeMap.put("HLT", "000000");
    }

    // initialize memoryToMemory instructions.
    static {
        memoryToMemory.add("LDR");
        memoryToMemory.add("STR");
        memoryToMemory.add("LDA");
        memoryToMemory.add("LDX");
        memoryToMemory.add("STX");
        memoryToMemory.add("JZ");
        memoryToMemory.add("JNE");
        memoryToMemory.add("JCC");
        memoryToMemory.add("JMA");
        memoryToMemory.add("JSR");
        memoryToMemory.add("RFS");
        memoryToMemory.add("SOB");
        memoryToMemory.add("JGE");
        memoryToMemory.add("AMR");
        memoryToMemory.add("SMR");

    }

    // initalize registerToRegister.
    static {
        registerToRegister.add("AIR");
        registerToRegister.add("SIR");
        registerToRegister.add("MLT");
        registerToRegister.add("DVD");
        registerToRegister.add("TRR");
        registerToRegister.add("AND");
        registerToRegister.add("ORR");
        registerToRegister.add("NOT");
    }

    // initalize shiftRotate.
    static {
        shiftRotate.add("SRC");
        shiftRotate.add("RRC");
    }

    // initalize inputOutput.
    static {
        inputOutput.add("IN");
        inputOutput.add("OUT");
        inputOutput.add("CHK");
    }
    
    // Pass One: Populate the label table and determine code location.
    private static void passOne(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();    // initialize to first line to ignore the headers.

        // OUTER LOOP: reading each line of source file.
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\s+");

            // Handle LOC: change the current address before processing labels or instructions
            if (parts[1].equalsIgnoreCase("LOC")) {
                currentAddress = Integer.parseInt(parts[2]);
            }

            // Handling labels: add to labelTable.
            if (parts.length > 0 && parts[0].endsWith(":")) {
                String label = parts[0].substring(0, parts[0].length() - 1);
                labelTable.put(label, currentAddress);
            }

            // Handling Instructions: increment address.
            else if (isInstruction(parts)) {
                currentAddress++;
            }
        }
        reader.close();
    }

    // Pass Two: Convert to machine code and generate load/listing files.
    private static void passTwo(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();
        currentAddress = 0;

        while ((line = reader.readLine()) != null) {
            // Handling comments.
            StringBuilder buildingComment = new StringBuilder();
            String comment = "";
            int commentIndex = line.indexOf(';');
            if (commentIndex != -1) {
                buildingComment.append("\t\t");
                buildingComment.append(line.substring(commentIndex).trim()); // Grab comment including the semicolon
                comment = buildingComment.toString();
            }

            String[] parts = line.split("\\s+");

            // Handle LOC: change the current address.
            if (parts[1].equalsIgnoreCase("LOC")) {
                listingFile.add(String.format("%s %s %s %s %s", "", "", parts[1], parts[2], comment));
                currentAddress = Integer.parseInt(parts[2]);
            } 

            // Handle Data: store a value in memory.
            else if (parts[1].equalsIgnoreCase("Data")) {
                String dataValue;

                // Operand is a label (e.g., "End").
                if (labelTable.containsKey(parts[2])) {
                    dataValue = String.format("%06o", labelTable.get(parts[2])); 
                } 
                // Operand is a direct value (e.g., "10").
                else {
                    dataValue = String.format("%06o", Integer.parseInt(parts[2])); 
                }

                // Update(s).
                listingFile.add(String.format("%06o %s %s %s %s", currentAddress, dataValue, parts[1], parts[2], comment));
                loadFile.add(String.format("%06o %s", currentAddress, dataValue));
                currentAddress++;
            }

            // Handle actual instructions: LDR, LDX, etc.
            else if (isInstruction(parts)) {
                String opcode = parts[1].toUpperCase();
                String machineCodeStr = opcodeMap.get(opcode);

                // Convert opcode to integer (since it's currently in binary string format)
                if (machineCodeStr != null) {
                    // Calculate the operand value
                    String operandValue = "";
                    if (parts.length > 2) { // Ensure there is an operand
                        operandValue = getOperandValue(opcode, parts[2]); // Handle operands
                    }
                    //System.out.print(currentAddress + " : ");
                    //System.out.println(String.format("%06o", currentAddress));
                    // Combine opcode and operand
                    StringBuilder output = new StringBuilder();
                    output.append(String.format("%06o", currentAddress));
                    output.append(" ");
                    output.append(operandValue);
                    
                    listingFile.add(String.format("%s %s %s %s %s", "",output.toString(), parts[1], parts[2], comment));
                    loadFile.add(output.toString());
                    currentAddress++;
                } 

                // Handle invalid instructions
                else {
                    listingFile.add(String.format("%06o %s %s", currentAddress, "null", comment));
                    loadFile.add(String.format("%06o %s", currentAddress, "null"));
                }
            }

            else if(parts[1].equalsIgnoreCase("HLT")) {
                listingFile.add(String.format("%06o %s %s %s %s %s", currentAddress, "000000", parts[0], parts[1], parts[2], comment));
                loadFile.add(String.format("%06o %s", currentAddress, "000000"));
            }
        }
        reader.close();
    }

    // Resolves the operand as either a label or a direct value, handling comma-separated operands.
    // Example: 3,0,10
    private static String getOperandValue(String opcode, String operand) {
        // set default values.
        int register = 0, indexRegister = 0, address = 0, indirectBit = 0;  
        int registerX = 0, registerY = 0;
        int count = 0, L_R = 0, A_L = 0;
        int devID = 0;

        StringBuilder output = new StringBuilder();
        String binaryString = "";

        // Split the operand by commas
        String[] parts = operand.split(",");
        if (parts.length > 0) {
            // For RFS, the operand is just an immediate value.
            /* 
             * Opcode = 6 bits
             * IX, I = 3 bits (000)
             * Immediate = 5 bits
             * Unused = 2 bits (00)
            */
            if (opcode.equals("RFS")) {
                StringBuilder sb = new StringBuilder();

                String opcodeBinary = opcodeMap.get("RFS"); 

                // RFS ignores IX and I fields, so we set them to 00 and 0 respectively.
                // Convert index register to binary (2 bits)
                String indexRegisterBinary = String.format("%2s", Integer.toBinaryString(indexRegister)).replace(' ', '0');
                
                // Convert indirect bit to binary (1 bit)
                String indirectBitBinary = Integer.toBinaryString(indirectBit);

                int immediate = Integer.parseInt(operand); 
                String immediateBitBinary = String.format("%5s", Integer.toBinaryString(immediate)).replace(' ', '0');

                int unused = 0;
                String unusedBitBinary = String.format("%2s", Integer.toBinaryString(unused)).replace(' ', '0');

                sb.append(opcodeBinary);
                sb.append(indexRegisterBinary);
                sb.append(indirectBitBinary);
                sb.append(immediateBitBinary);
                sb.append(unusedBitBinary);

                return binaryToOctal(sb.toString());
            }

            // CASE 1: Memory to Memory Instruction.
            if(memoryToMemory.contains(opcode)) {
                // Case a: r, x, address[,I].
                if (opcode.equals("LDR") || opcode.equals("STR") || opcode.equals("LDA") || opcode.equals("JZ") || opcode.equals("JNE") || opcode.equals("SOB") || opcode.equals("JGE") || opcode.equals("JCC") || opcode.equals("AMR") || opcode.equals("SMR")) {
                    register = Integer.parseInt(parts[0]);
                    indexRegister = Integer.parseInt(parts[1]);
                    address = Integer.parseInt(parts[2]);
    
                    // Indirect bit.
                    if (parts.length > 3) {
                        indirectBit = Integer.parseInt(parts[3]);  
                    }
                } 
    
                // Case b: x, address[,I].
                else if (opcode.equals("LDX") || opcode.equals("STX") || opcode.equals("JMA") || opcode.equals("JSR") || opcode.equals("JMA")) {
                    indexRegister = Integer.parseInt(parts[0]);
                    address = Integer.parseInt(parts[1]);
    
                    // Indirect bit.
                    if (parts.length > 2) {
                        indirectBit = Integer.parseInt(parts[2]);
                    }
                }
            
                else if(opcode.equals("HLT")) {
                    return "000000";
                }

                // Compute binary.
                String opcodeBinary = opcodeMap.getOrDefault(opcode, "000000");
                String registerBinary = String.format("%2s", Integer.toBinaryString(register)).replace(' ', '0');
                String indexRegisterBinary = String.format("%2s", Integer.toBinaryString(indexRegister)).replace(' ', '0');
                String indirectBitBinary = Integer.toBinaryString(indirectBit);
                String addressBinary = String.format("%5s", Integer.toBinaryString(address)).replace(' ', '0');
                
                // Combine all parts into a single 16-bit instruction
                output.append(opcodeBinary);
                output.append(registerBinary);
                output.append(indexRegisterBinary);
                output.append(indirectBitBinary);
                output.append(addressBinary);

                // After creating the binary string
                binaryString = output.toString();
                return binaryToOctal(binaryString);
            }
        }

        // CASE 2: Register to Register Instructions.
        else if(registerToRegister.contains(opcode)) {
            // case a: rx.
            registerX = Integer.parseInt(parts[0]);
            
            // case b: ry.
            if(parts.length > 1) {
                registerY = Integer.parseInt(parts[1]);
            }

            // Compute binary.
            String opcodeBinary = opcodeMap.getOrDefault(opcode, "000000");
            String registerXBinary = String.format("%2s", Integer.toBinaryString(registerX)).replace(' ', '0');
            String registerYBinary = String.format("%2s", Integer.toBinaryString(registerY)).replace(' ', '0');

            // Combine all parts into a single 16-bit instruction.
            output.append(opcodeBinary);
            output.append(registerXBinary);
            output.append(registerYBinary);
            output.append("000000");    // unused bits.

            binaryString = output.toString();
            return binaryToOctal(binaryString);
        }

        // CASE 3: Shift and Rotate Instructions.
        else if(shiftRotate.contains(opcode)) {
            register = Integer.parseInt(parts[1]);
            count = Integer.parseInt(parts[2]);
            A_L = Integer.parseInt(parts[3]);
            L_R = Integer.parseInt(parts[4]);

            // Compute binary.
            String opcodeBinary = opcodeMap.getOrDefault(opcode, "000000");     // 6 bits.
            String registerBinary = String.format("%2s", Integer.toBinaryString(register)).replace(' ', '0');   // 2 bits.
            String A_LBinary = Integer.toBinaryString(A_L);     // 1 bit.
            String L_RBinary = Integer.toBinaryString(L_R);     // 1 bit.
            String countBinary = String.format("%4s", Integer.toBinaryString(count)).replace(' ', '0');     // 4 bits.

            // Combine all parts into a single 16-bit instruction.
            output.append(opcodeBinary);
            output.append(registerBinary);
            output.append(A_LBinary);
            output.append(L_RBinary);
            output.append("00");    // unused bits.
            output.append(countBinary);

            binaryString = output.toString();
            return binaryToOctal(binaryString);
        }

        // CASE 4: I/O Instructions.
        else if(inputOutput.contains(opcode)) {
            devID = Integer.parseInt(parts[1]);

            // Compute binary.
            String opcodeBinary = opcodeMap.getOrDefault(opcode, "000000");     // 6 bits.
            String registerBinary = String.format("%2s", Integer.toBinaryString(register)).replace(' ', '0');   // 2 bits.
            String devIDBinary = String.format("%5s", Integer.toBinaryString(devID)).replace(' ', '0');   // 5 bits.

            // Combine all parts into a single 16-bit instruction.
            output.append(opcodeBinary);
            output.append(registerBinary);
            output.append("000");    // unused bits.
            output.append(devIDBinary);

            binaryString = output.toString();
            return binaryToOctal(binaryString);
        }

        // CASE 5: Default.
        return "";
    }

    private static String binaryToOctal(String binaryString) {
        // Convert to octal
        StringBuilder octalBuilder = new StringBuilder();
        for (int i = binaryString.length(); i > 0; i -= 3) {
            int startIndex = Math.max(0, i - 3);
            String group = binaryString.substring(startIndex, i);
            
            // Pad the group with leading zeros if it's less than 3 bits
            while (group.length() < 3) {
                group = "0" + group;
            }
            
            int octalDigit = Integer.parseInt(group, 2);
            octalBuilder.insert(0, Integer.toString(octalDigit));
        }

        String octalResult = octalBuilder.toString();
        //System.out.println(octalResult);
        
        String paddedOctalResult = String.format("%6s", octalResult).replace(' ', '0');
        return paddedOctalResult;
    }


    // Determines if the line contains a valid instruction
    private static boolean isInstruction(String[] parts) {
        // Ensure there are enough parts to process
        if (parts.length > 1) {
            // Check for assembler directives (LOC, Data)
            if (parts[1].equalsIgnoreCase("LOC") || parts[1].equalsIgnoreCase("Data")) {
                return false; // Not an instruction, but a directive
            }

            // Check if the first part is a label or an empty string (ignore if true)
            return !parts[0].endsWith(":") && parts.length > 1; // Check that there is a proper opcode in parts[1]
        }
        return false;
    }

    // Writes the output to files.
    private static void writeFiles() throws IOException {
        BufferedWriter listWriter = new BufferedWriter(new FileWriter("ListingOutput.lst"));
        for (String line : listingFile) {
            listWriter.write(line);
            listWriter.newLine();
        }
        listWriter.close();

        BufferedWriter loadWriter = new BufferedWriter(new FileWriter("LoadFile.ld"));
        for (String line : loadFile) {
            loadWriter.write(line);
            loadWriter.newLine();
        }
        loadWriter.close();
    }

    public static void main(String[] args) throws IOException {
        // Sample input file
        String sourceFile = "SourceFile.asm";
        passOne(sourceFile);
        passTwo(sourceFile);
        writeFiles();
    }
}