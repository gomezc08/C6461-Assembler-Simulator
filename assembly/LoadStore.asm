Label   Opcode  Operand     Comments
        LOC     8           ;BEGIN AT LOCATION 8
        Data    10          ;PUT 10 AT LOCATION 9
        Data    3           ;PUT 3 AT LOCATION 10
        Data    End         ;PUT 1024 AT LOCATION 11
        Data    0
        Data    12
        Data    9
        Data    18
        Data    12
        LDX     2,7         ;X2 GETS 3
        JZ      3,2,30
        MLT     0,2         ; test1
        NOT     1           ; test2
        SRC     2,3,1,0     ; test3
        OUT     2,1         ; test4
        SIR     1,10        ;new test!!!
        LDR     3,0,10      ;R3 GETS 12
        LDR     2,2,10      ;R2 GETS 12
        LDR     1,2,10,1    ;R1 GETS 18
        LDA     0,0,0       ;R0 GETS 0 to set CONDITION CODE
        LDX     1,8         ;X1 GETS 1024
        JZ      0,1,0       ;JUMP TO End IF R0 = 0
        LOC     1024
End:    HLT                 ;STOP 