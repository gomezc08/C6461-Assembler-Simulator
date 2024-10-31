Label       Opcode      Operand         Comments
            LOC         6               ; Data section starts here
DataNum:    Data        0               ; Memory address 6 - Placeholder for numbers
Target:     Data        0               ; Memory address 7 - Target number
Closest:    Data        0               ; Memory address 8 - Closest number
Diff:       Data        65535           ; Memory address 9 - Large initial difference
Count:      Data        20              ; Memory address 10 - Count of numbers
            LOC         20              ; Start of program instructions
            LDA         0,0,10          ; Load count (20) into R0 from Count address (10)
            LDA         1,0,6           ; Load start address of DataNum into R1
InputLoop:  IN          2,0             ; Input character from keyboard into R2
            STR         2,0,1           ; Store character in memory at R1
            AIR         1,1             ; Increment R1 to point to next memory location
            SIR         0,1             ; Decrement count in R0
            JNE         0,0,14          ; Jump back to InputLoop (address 24) if R0 != 0
            LDA         0,0,10          ; Reload count in R0 from address 10
            LDA         1,0,6           ; Reset address in R1 to start of DataNum
PrintLoop:  LDR         2,0,1           ; Load number from memory into R2
            OUT         2,1             ; Output number to console
            AIR         1,1             ; Increment R1 to point to next memory location
            SIR         0,1             ; Decrement count in R0
            JNE         0,0,21          ; Jump back to PrintLoop (address 32) if R0 != 0
            IN          3,0             ; Read target number into R3
            STR         3,0,7           ; Store target number in Target (address 7)
            LDA         0,0,10          ; Reload count in R0 from address 10
            LDA         1,0,6           ; Reset address in R1 to start of DataNum
CompareLoop:LDR         2,0,1           ; Load number for comparison into R2
            SIR         4,3             ; Calculate difference: R4 = R2 - R3
            AIR         4,0             ; Convert R4 to absolute if needed (set to abs value)
            JGE         4,0,44          ; Skip if R4 >= Diff (address 44)
            STR         2,0,8           ; Update Closest with value from R2 (address 8)
            STR         4,0,9           ; Update Diff with value from R4 (address 9)
            AIR         1,1             ; Increment R1 to point to next memory location
            SIR         0,1             ; Decrement count in R0
            JNE         0,0,30          ; Jump back to CompareLoop (address 40) if R0 != 0
            LDR         2,0,7           ; Load target number into R2 from Target
            OUT         2,1             ; Print target number
            LDR         2,0,8           ; Load closest number into R2 from Closest
            OUT         2,1             ; Print closest number
            LOC         1024            ; Program termination
End:        HLT                         ; Stop execution