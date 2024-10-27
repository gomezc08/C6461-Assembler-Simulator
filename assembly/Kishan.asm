Label   Opcode  Operand     Comments
        LOC     6           ;BEGIN AT LOCATION 6
        Data    170         ;PUT 170 AT LOCATION 6 (numerator for division)
        Data    5           ;PUT 5 AT LOCATION 7 (denominator for division)
        Data    1024        ;PUT 1024 AT LOCATION 8 (converted from 'End' to 1024)
        Data    0           ;PUT 0 at Location 9
        Data    12          ;PUT 12 at location 10
        Data    9           ;PUT 9 at location 11
        Data    18          ;Extra data at location 12
        Data    12          ;Extra data at location 13
        LDR     0,0,6       ;REG 0 = Contents at address 6 = 170
        LDR     2,0,7       ;REG 2 = Contents at address 7 = 5
        AMR     2,0,6       ;REG 2 = REG 2 + data at memory location 6
        SMR     2,0,6       ;REG 2 = REG 2 - data at memory location 6 
        AIR     2,0,100     ;Add immediate value 100 with REG 2
        SIR     2,0,50      ;Subtratcs 50 from REG 2     
End:    HLT                 ;STOP