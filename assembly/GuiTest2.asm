Label   Opcode  Operand     Comments
        LOC     6           ;BEGIN AT LOCATION 6
        Data    170         ;PUT 100 AT LOCATION 6 (numerator for division)
        Data    5           ;PUT 25 AT LOCATION 7 (denominator for division)
        Data    1024        ;PUT 1024 AT LOCATION 8 (converted from 'End' to 1024)
        Data    0           ;PUT 0 at Location 9
        Data    12          ;PUT 12 at location 10
        Data    9           ;PUT 9 at location 11
        Data    18          ;Extra data at location 12
        Data    12          ;Extra data at location 13
        IN      0,0         ;INPUT A VALUE INTO GPR0 FROM DEVICE 0 (Console Input)
        OUT     0,0         ;OUTPUT THE CONTENT OF GPR0 TO DEVICE 0 (Console Output)
        CHK     1,0         ;CHECK STATUS OF DEVICE 0, STORE IN GPR1
End:    HLT                 ;STOP