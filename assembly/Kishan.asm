Label   Opcode  Operand     Comments
        LOC     6           ;BEGIN AT LOCATION 6
        Data    10          ;PUT 10 AT LOCATION 6
        Data    3           ;PUT 3 AT LOCATION 7
        Data    1024        ;PUT 1024 AT LOCATION 8 (converted from 'End' to 1024)
        Data    0           ;PUT 0 at Location 9
        Data    12          ;PUT 12 at location 10
        Data    9           ;PUT 9 at location 11
        Data    18          ;Extra data at location 12
        Data    12          ;Extra data at location 13
        LDR     3,0,10      ;REG 3 = Contents at address 10 = 12
        STR     3,0,20      ;Store contents of reg 3 at address 20. Memory 20 = 12
        LDR     2,1,20,1    ;GPR[2] = Address[Address[20] + IXR[1]] = Address[12] = 18.
        LDA     1,0,6       ;Load the address 6 into GPR[1]. GPR[1] = 6 for address 6.
        LDX     1,8         ;Load the content at address 8 into IX1. IX1 = Address[8] = 1024.
        JCC     3,0,18      ;Jump if condition code 3 (Equal) is set to address 18
        STX     1,20        ;Store the content of IXR[1] at memory address 20
End:    HLT                 ;STOP
