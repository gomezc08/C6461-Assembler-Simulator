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
        STR     3,0,20      ;Store contents of reg 3 at address 20 instead of 16
        LDR     2,0,6       ;Reg 2 = contents at address 6 = 10
End:    HLT                 ;STOP 