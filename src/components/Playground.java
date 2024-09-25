package components;

public class Playground {
    public static void main(String[] args) {
        // Initialize components
        GeneralPurposeRegisters gprs = new GeneralPurposeRegisters(4);  // 4 GPRs, 16 bits each
        MemoryAddressRegister mar = new MemoryAddressRegister();        // 12 bits MAR
        MemoryBufferRegister mbr = new MemoryBufferRegister();          // 16 bits MBR
        MachineFaultRegister mfr = new MachineFaultRegister();          // 4 bits MFR
        IndexRegisters indexRegisters = new IndexRegisters(3);          // 3 index registers, 16 bits each
        YRegister yRegister = new YRegister();                          // 16 bits Y register
        ZRegister zRegister = new ZRegister();                          // 16 bits Z register

        // Play around with General Purpose Registers (GPRs)
        gprs.setGPR(0, (short) 0x1234); // Set GPR[0]
        gprs.setGPR(1, (short) 0x5678); // Set GPR[1]
        System.out.println("GPRs after setting values:");
        System.out.println(gprs);

        // Play around with Memory Address Register (MAR)
        mar.setMAR(1024); // Set MAR to address 1024
        System.out.println("MAR after setting address:");
        System.out.println(mar);

        // Play around with Memory Buffer Register (MBR)
        mbr.setMBR((short) 0xABCD); // Set MBR to 0xABCD
        System.out.println("MBR after setting value:");
        System.out.println(mbr);

        // Play around with Machine Fault Register (MFR)
        mfr.setMFR(5); // Set MFR to 5 (binary 0101)
        System.out.println("MFR after setting fault code:");
        System.out.println(mfr);

        // Play around with Index Registers
        indexRegisters.setIndexRegister(0, (short) 0x1000); // Set Index Register 0
        indexRegisters.setIndexRegister(1, (short) 0x2000); // Set Index Register 1
        System.out.println("Index Registers after setting values:");
        System.out.println(indexRegisters);

        // Play around with Y Register
        yRegister.setYRegister((short) 0xFACE); // Set Y register
        System.out.println("Y Register after setting value:");
        System.out.println(yRegister);

        // Play around with Z Register
        zRegister.setZRegister((short) 0xBEEF); // Set Z register
        System.out.println("Z Register after setting value:");
        System.out.println(zRegister);

        // Simulate a full reset of all components
        System.out.println("\nResetting all components...\n");

        gprs.resetAllGPRs();
        mar.resetMAR();
        mbr.resetMBR();
        mfr.resetMFR();
        indexRegisters.resetAllIndexRegisters();
        yRegister.resetYRegister();
        zRegister.resetZRegister();

        // Display all components after reset
        System.out.println("Components after reset:");
        System.out.println("GPRs:");
        System.out.println(gprs);
        System.out.println("MAR:");
        System.out.println(mar);
        System.out.println("MBR:");
        System.out.println(mbr);
        System.out.println("MFR:");
        System.out.println(mfr);
        System.out.println("Index Registers:");
        System.out.println(indexRegisters);
        System.out.println("Y Register:");
        System.out.println(yRegister);
        System.out.println("Z Register:");
        System.out.println(zRegister);
    }
}
