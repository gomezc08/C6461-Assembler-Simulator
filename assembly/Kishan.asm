Label   Opcode  Operand     Comments
        LOC     6           ;BEGIN AT LOCATION 6
        Data    100         ;PUT 100 AT LOCATION 6 (numerator for division)
        Data    0           ;PUT 25 AT LOCATION 7 (denominator for division)
        Data    1024        ;PUT 1024 AT LOCATION 8 (converted from 'End' to 1024)
        Data    0           ;PUT 0 at Location 9
        Data    12          ;PUT 12 at location 10
        Data    9           ;PUT 9 at location 11
        Data    18          ;Extra data at location 12
        Data    12          ;Extra data at location 13
        LDR     0,0,6       ;REG 0 = Contents at address 6 = 100
        LDR     2,0,7       ;REG 2 = Contents at address 7 = 25
        DVD     0,2         ;REG 0 = 100 / 25 = 4, REG 1 = remainder (0)
End:    HLT                 ;STOP
