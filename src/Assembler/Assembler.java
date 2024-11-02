package Assembler;

/*
 * C6461Assembler: This class contains methods for assembling a C6461 assembly source file into machine code. 
 * The assembler follows a two-pass process: 
 * Pass One populates the label table and determines code locations, while Pass Two generates the machine code.
 * 
 * passOne(String fileName): Populates the label table and determines code location.
 * - @param fileName: The name of the source file to read.
 * - @throws IOException If an I/O error occurs.
 * 
 * passTwo(String fileName): Converts source instructions into machine code and generates the listing and load files.
 * - @param fileName: The name of the source file to read.
 * - @throws IOException If an I/O error occurs.
 * 
 * getOperandValue(String opcode, String operand): Resolves an operand (e.g., register, address) into a machine code value.
 * - @param opcode: The operation code to process (e.g., LDR, STR).
 * - @param operand: The operand part of the instruction (e.g., registers, memory address).
 * - @return String The corresponding machine code value in octal.
 * 
 * binaryToOctal(String binaryString): Converts a binary string into its octal representation.
 * - @param binaryString: The binary string to convert.
 * - @return String The converted octal value as a string.
 * 
 * isInstruction(String[] parts): Checks if a given line of the source file contains a valid instruction.
 * - @param parts: An array of strings representing a split line of source code.
 * - @return boolean True if the line contains an instruction, false otherwise.
 * 
 * writeFiles(): Writes the listing and load file outputs based on the processed instructions.
 * - @throws IOException If an I/O error occurs while writing to the files. 
 * 
 * run(String sourceFile): Executes the assembler by running both passes and writing output files.
 * @param sourceFile The name of the source file to assemble.
 * @throws IOException If an I/O error occurs during file reading or writing.
 */

import java.io.*;
import java.util.*;

import components.ProgramCounter;

public class Assembler {
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
    private static Set<String> miscellaneous = new HashSet<>();
    private static String rowFormat = "%-10s %-13s %-6s %-16s %5s";
    private static int startAddress = 0;
    private static int hltAddress = 0;
    private static ProgramCounter programCounter;  

    public Assembler() {
        programCounter = new ProgramCounter();
    }

    // initialize opcodeMap.
    static {
        // Load/Store Instructions.
        opcodeMap.put("LDR", "000001"); 
        opcodeMap.put("STR", "000010");
        opcodeMap.put("LDA", "000011");
        opcodeMap.put("LDX", "101001"); 
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

        // Miscellaneous Instructions.
        opcodeMap.put("HLT", "000000");
        opcodeMap.put("TRAP", "011110");
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
        memoryToMemory.add("AIR");
        memoryToMemory.add("SIR");

    }

    // initalize registerToRegister.
    static {
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

    // initalize miscellaneous.
    static {
        miscellaneous.add("HLT");
        miscellaneous.add("TRAP");
    }
    
    private static void passOne(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();    // skip header line
        boolean startAssigned = false;
        int lastInstructionAddress = -1;  // Track last real instruction address
    
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty() || line.trim().startsWith(";")) {
                continue;
            }
    
            int commentIndex = line.indexOf(';');
            if (commentIndex != -1) {
                line = line.substring(0, commentIndex).trim();
            }
            
            String[] parts = Arrays.stream(line.split("\\s+"))
                                  .filter(s -> !s.trim().isEmpty())
                                  .toArray(String[]::new);
            
            if (parts.length == 0) continue;
    
            // Handle LOC directive
            if (parts[0].equalsIgnoreCase("LOC")) {
                if (parts.length > 1) {
                    try {
                        int targetLocation = Integer.parseInt(parts[1]);
                        if (!startAssigned) {
                            // First LOC just sets start address
                            startAddress = targetLocation;
                            currentAddress = startAddress;
                            startAssigned = true;
                        } else {
                            // Only add LOC directive if we've seen instructions before
                            if (lastInstructionAddress != -1) {
                                programCounter.addLocDirective(lastInstructionAddress + 1, targetLocation);
                            }
                            currentAddress = targetLocation;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error: Invalid address for LOC directive");
                    }
                }
                continue;
            }
    
            // Handle labels
            if (parts[0].endsWith(":")) {
                String label = parts[0].substring(0, parts[0].length() - 1);
                labelTable.put(label, currentAddress);
            }
    
            // Track real instructions and data
            if (isInstruction(parts) || 
                parts[0].equalsIgnoreCase("Data") || 
                (parts.length > 1 && parts[1].equalsIgnoreCase("Data"))) {
                lastInstructionAddress = currentAddress;
                currentAddress++;
            }
        }
        reader.close();
    }

    private static void passTwo(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();    // skip header line
        currentAddress = startAddress;
    
        while ((line = reader.readLine()) != null) {
            // Handle empty lines
            if (line.trim().isEmpty()) {
                listingFile.add("");
                continue;
            }
    
            // Handle comment-only lines
            if (line.trim().startsWith(";")) {
                listingFile.add(line.trim());
                continue;
            }
    
            // Extract and handle comments
            String comment = "";
            int commentIndex = line.indexOf(';');
            if (commentIndex != -1) {
                comment = "\t\t" + line.substring(commentIndex).trim();
                line = line.substring(0, commentIndex).trim();
            }
    
            // Split and filter out empty strings
            String[] parts = Arrays.stream(line.split("\\s+"))
                                  .filter(s -> !s.trim().isEmpty())
                                  .toArray(String[]::new);
    
            if (parts.length == 0) {
                listingFile.add(comment);
                continue;
            }
    
            // Handle LOC directive
            if (parts[0].equalsIgnoreCase("LOC")) {
                if (parts.length > 1) {
                    currentAddress = Integer.parseInt(parts[1]);
                    listingFile.add(String.format(rowFormat, "", "", "LOC", parts[1], comment));
                }
                continue;
            }
    
            // Handle Data directive
            if (parts[0].equalsIgnoreCase("Data") || 
                (parts.length > 1 && parts[1].equalsIgnoreCase("Data"))) {
                String dataValue = "000000";
                String operand = "";
                int dataIndex = parts[0].equalsIgnoreCase("Data") ? 1 : 2;
    
                if (parts.length > dataIndex) {
                    operand = parts[dataIndex];
                    try {
                        if (labelTable.containsKey(parts[dataIndex])) {
                            dataValue = String.format("%06o", labelTable.get(parts[dataIndex]));
                        } else {
                            dataValue = String.format("%06o", Integer.parseInt(parts[dataIndex]));
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error: Invalid data value at address " + currentAddress);
                    }
                }
    
                String currentAddressOctal = String.format("%06o", currentAddress);
                listingFile.add(String.format(rowFormat, currentAddressOctal, dataValue, "Data", operand, comment));
                loadFile.add(String.format("%06o %s", currentAddress, dataValue));
                currentAddress++;
                continue;
            }
    
            // Handle regular instructions and HLT
            String opcode;
            String operands = "";
            int opcodeIndex;
    
            if (parts[0].endsWith(":")) {
                opcodeIndex = 1;
            } else {
                opcodeIndex = 0;
            }
    
            if (parts.length > opcodeIndex) {
                opcode = parts[opcodeIndex].toUpperCase();
                if (parts.length > opcodeIndex + 1) {
                    operands = parts[opcodeIndex + 1];
                }
    
                String operandValue = "";
                if (!operands.isEmpty()) {
                    StringBuilder operand = new StringBuilder();
                    String[] operand_labels = operands.split(",");
                    for (String s : operand_labels) {
                        if (labelTable.containsKey(s)) {
                            operand.append(labelTable.get(s)).append(",");
                        } else {
                            operand.append(s).append(",");
                        }
                    }
                    if (operand.length() > 0) {
                        operand.setLength(operand.length() - 1);
                        operandValue = getOperandValue(opcode, operand.toString());
                    }
                } else if (opcode.equals("HLT")) {
                    operandValue = "000000";
                }
    
                String currentAddressOctal = String.format("%06o", currentAddress);
                listingFile.add(String.format(rowFormat, currentAddressOctal, operandValue, 
                              parts[opcodeIndex], operands, comment));
                loadFile.add(String.format("%s %s", currentAddressOctal, operandValue));
                currentAddress++;
            }
        }
        reader.close();
    }

    // Resolves the operand as either a label or a direct value, handling comma-separated operands.
    // Example: 3,0,10
    private static String getOperandValue(String opcode, String operand) {
        //System.out.println("In getOperandValue, opcode: " + opcode + ", operand: " + operand);
        // define instruction fields.
        int register = 0;
        int indexRegister = 0;
        int address = 0;
        int indirectBit = 0;  
        int registerX = 0;
        int registerY = 0;
        int count = 0;
        int L_R = 0;
        int A_L = 0;
        int devID = 0;
        int trapCode;

        StringBuilder output = new StringBuilder();
        
        String opcodeBinary = "";
        String registerBinary = "";
        String indexRegisterBinary = "";
        String indirectBitBinary = "";
        String addressBinary = "";
        String binaryString = "";
        String registerXBinary = "";
        String registerYBinary = "";
        String A_LBinary = "";    
        String L_RBinary = "";    
        String countBinary = "";
        String devIDBinary = "";
        String trapCodeBinary = "";

        // Split the operand by commas.
        String[] parts = operand.split(",");
        
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

            // Case c: r, immed.
            else if(opcode.equals("AIR") || opcode.equals("SIR")) {
                register = Integer.parseInt(parts[0]);
                // Convert negative numbers to 5-bit two's complement
                int immedValue = Integer.parseInt(parts[1]);
                if (immedValue < 0) {
                    // For 5-bit field, we use 0x1F (31) as our mask
                    address = immedValue & 0x1F;  // This will handle negative numbers correctly
                } 
                else {
                    address = immedValue;
                }
            }

            // Case d: Immed.
            else if(opcode.equals("RFS")) {
                address = Integer.parseInt(parts[0]);
            }

            // Compute binary.
            opcodeBinary = opcodeMap.getOrDefault(opcode, "000000");
            registerBinary = String.format("%2s", Integer.toBinaryString(register)).replace(' ', '0');
            indexRegisterBinary = String.format("%2s", Integer.toBinaryString(indexRegister)).replace(' ', '0');
            indirectBitBinary = Integer.toBinaryString(indirectBit);
            addressBinary = String.format("%5s", Integer.toBinaryString(address)).replace(' ', '0');
            
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
        

        // CASE 2: Register to Register Instructions.
        else if(registerToRegister.contains(opcode)) {
            // case a: rx.
            registerX = Integer.parseInt(parts[0]);
            
            // case b: ry.
            if(parts.length > 1) {
                registerY = Integer.parseInt(parts[1]);
            }

            // Compute binary.
            opcodeBinary = opcodeMap.getOrDefault(opcode, "000000");
            registerXBinary = String.format("%2s", Integer.toBinaryString(registerX)).replace(' ', '0');
            registerYBinary = String.format("%2s", Integer.toBinaryString(registerY)).replace(' ', '0');

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
            register = Integer.parseInt(parts[0]);
            count = Integer.parseInt(parts[1]);
            L_R = Integer.parseInt(parts[2]);
            A_L = Integer.parseInt(parts[3]);

            // Compute binary.
            opcodeBinary = opcodeMap.getOrDefault(opcode, "000000");     // 6 bits.
            registerBinary = String.format("%2s", Integer.toBinaryString(register)).replace(' ', '0');   // 2 bits.
            A_LBinary = Integer.toBinaryString(A_L);     // 1 bit.
            L_RBinary = Integer.toBinaryString(L_R);     // 1 bit.
            countBinary = String.format("%4s", Integer.toBinaryString(count)).replace(' ', '0');     // 4 bits.

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
            register = Integer.parseInt(parts[0]);
            devID = Integer.parseInt(parts[1]);

            // Compute binary.
            opcodeBinary = opcodeMap.getOrDefault(opcode, "000000");     // 6 bits.
            registerBinary = String.format("%2s", Integer.toBinaryString(register)).replace(' ', '0');   // 2 bits.
            devIDBinary = String.format("%5s", Integer.toBinaryString(devID)).replace(' ', '0');   // 5 bits.

            // Combine all parts into a single 16-bit instruction.
            output.append(opcodeBinary);
            output.append(registerBinary);
            output.append("000");    // unused bits.
            output.append(devIDBinary);

            binaryString = output.toString();
            return binaryToOctal(binaryString);
        }

        // CASE 5: Miscellaneous Instructions.
        else if(miscellaneous.contains(opcode)) {
            // TRAP.
            if(opcode.contains("TRAP")) {
                trapCode = Integer.parseInt(parts[0]);

                // Compute binary.
                opcodeBinary = opcodeMap.getOrDefault(opcode, "000000");     // 6 bits.
                trapCodeBinary = String.format("%4s", Integer.toBinaryString(trapCode)).replace(' ', '0');     // 4 bits.

                // Combine all parts into a single 16-bit instruction.
                output.append(opcodeBinary);
                output.append("000000");    // unused bits.
                output.append(trapCodeBinary);

                binaryString = output.toString();
                return binaryToOctal(binaryString);
            }

            //HLT.
            return "000000";
        }

        // CASE 6: Default.
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

        String paddedOctalResult = String.format("%6s", octalResult).replace(' ', '0');
        return paddedOctalResult;
    }


    // Determines if the line contains a valid instruction
    private static boolean isInstruction(String[] parts) {
        if (parts.length == 0) return false;
        
        // If first part is a label, check second part
        if (parts[0].endsWith(":")) {
            return parts.length > 1 && opcodeMap.containsKey(parts[1].toUpperCase());
        }
        
        // Check if first part is an opcode
        return opcodeMap.containsKey(parts[0].toUpperCase());
    }

    // Writes the output to files.
    private static void writeFiles() throws IOException {
        BufferedWriter listWriter = new BufferedWriter(new FileWriter("output/ListingOutput.lst"));
        for (String line : listingFile) {
            listWriter.write(line);
            listWriter.newLine();
        }
        listWriter.close();

        BufferedWriter loadWriter = new BufferedWriter(new FileWriter("output/output.ld"));
        for (String line : loadFile) {
            loadWriter.write(line);
            loadWriter.newLine();
        }
        loadWriter.close();
    }

    public static int getStartAddress() {
        return startAddress;
    }

    public static int getHltAddress() {
        return hltAddress;
    }

    // Method to get the ProgramCounter instance
    public static ProgramCounter getProgramCounter() {
        return programCounter;
    }

    public static void run(String sourceFile) throws IOException {
        System.out.println("<<Running the Assembler>>");
        if (programCounter == null) {
            programCounter = new ProgramCounter();
        } else {
            programCounter.reset();
            programCounter.clearLocDirectives();
        }
        passOne(sourceFile);  
        passTwo(sourceFile);
        writeFiles();
        System.out.println("<<Finished Running the Assembler (End)>>");
    }

    // Additional helper method to check for valid line
    private static boolean isValidLine(String line) {
        line = line.trim();
        return !line.isEmpty() && !line.startsWith(";");
    }

    
    
/* 
    public static void main(String[] args) throws IOException {
        // Sample input file
        String sourceFile = "assembly/LabelsTest.asm";
        run(sourceFile);
    }*/
        
}