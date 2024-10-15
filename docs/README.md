# C6461-Assembler-Simulator

## How to Compile and Run

### On Windows:
1. **Compile**:
   Run the following command from outside the `src` directory to compile all the Java files into the `bin` directory: `javac -d bin src/components/*.java src/ui/*.java src/Assembler/*.java`

2. **Run**:
To run the `ComputerSimulatorGUI` class, use the following command: `java -cp bin ui.ComputerSimulatorGUI`


### On Mac/Linux:
1. **Compile**:
Use this command to compile: `javac -d bin src/components/*.java src/ui/*.java src/Assembler/*.java`


2. **Run**:
Use the same command to run: `java -cp bin ui.ComputerSimulatorGUI`

### Notes:
- Make sure the `bin` directory is created in your project root before compiling. If not, create it manually.
- Ensure that all your `.java` files are located in the appropriate directories under `src`.