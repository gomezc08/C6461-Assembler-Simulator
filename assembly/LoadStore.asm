Label   Opcode  Operand     Comments
        LOC     10          ; Start at memory address 10
        LDR     2,2,10      ; R2 GETS value at address 10
        LDR     1,2,10,1    ; R1 GETS value at address 18 (indirect)
        LDA     0,0,0       ; R0 GETS 0 to set CONDITION CODE
        STR     2,2,15      ; Store value of R2 into address 15
        STR     1,2,16      ; Store value of R1 into address 16
        LDR     3,0,12      ; Load value from address 12 into R3
        STR     3,1,20      ; Store R3 into address 20 with index register 1
        LOC     1024
End:    HLT                 ; Stop execution