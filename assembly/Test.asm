Label       Opcode      Operand         Comments
            LOC         6               ; BEGIN AT LOCATION 6
            Data        0               ; DataNum: Placeholder for numbers, first in sequence
            Data        0               ; Target: Target number input by user
            Data        0               ; Closest: Closest number to the target
            Data        65535           ; Diff: Initial large difference value
            LDA         0,0,20          ; R0 = 20 (count of numbers)
            LDA         1,0,6           ; Load start address of DataNum into R1
            IN          2,0             ; Input character from keyboard
            STR         2,0,1           ; Store character in memory at R1
            AIR         1,1             ; Increment memory address for next number
            SIR         0,1             ; Decrement count in R0
            JNE         0,0,12          ; Jump back to IN if R0 != 0 (address 13)
            LDA         0,0,20          ; Reload count in R0
            LDA         1,0,6           ; Reset memory address in R1
            LDR         2,0,1           ; Load number from memory into R2
            OUT         2,1             ; Output number to console
            AIR         1,1             ; Move to next address
            SIR         0,1             ; Decrement count
            JNE         0,0,20          ; Jump back to LDR if R0 != 0 (address 20)
            IN          3,0             ; Read target number
            STR         3,0,7           ; Store target number in Target
            LDA         0,0,20          ; Reload count in R0
            LDA         1,0,6           ; Reset start address in R1
            LDR         2,0,1           ; Load number for comparison
            SIR         4,3             ; Calculate abs difference: |R2 - R3|
            AIR         4,0             ; Convert to absolute if needed
            JGE         4,0,28          ; Skip update if R4 >= Diff (address 28)
            STR         2,0,8           ; Update closest number in Closest
            STR         4,0,9           ; Update smallest difference in Diff
            AIR         1,1             ; Move to next memory location
            SIR         0,1             ; Decrement count
            JNE         0,0,25          ; Jump back to LDR if R0 != 0 (address 25)
            LDR         2,0,7           ; Load target number into R2
            OUT         2,1             ; Print target number
            LDR         2,0,8           ; Load closest number into R2
            OUT         2,1             ; Print closest number
            LOC         1024            ; Program termination
End:        HLT                         ; STOP