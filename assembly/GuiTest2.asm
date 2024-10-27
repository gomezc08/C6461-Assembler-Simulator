Label   Opcode  Operand     Comments
        LOC     6           ; BEGIN AT LOCATION 6
        Data    170         ; PUT 170 AT LOCATION 6
        LDR     0,0,6       ; LOAD VALUE 170 FROM MEMORY LOCATION 6 INTO GPR[0]
        SIR     0,0,15      ; SUBTRACT IMMEDIATE VALUE 15 FROM GPR[0] (170 - 15 = 155)
        SIR     0,0,200     ; SUBTRACT IMMEDIATE VALUE 200 FROM GPR[0] (155 - 200 = -45, expect underflow)
        SIR     0,0,0       ; IMMEDIATE VALUE 0, SHOULD DO NOTHING
End:    HLT                 ; STOP