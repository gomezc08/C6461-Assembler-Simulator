Label       Opcode      Operand         Comments
            LOC         6               ; BEGIN AT LOCATION 6
DataNum:    Data        0               ; Placeholder for numbers, first in sequence
Target:     Data        0               ; Target number input by user
Closest:    Data        0               ; Closest number to the target
Diff:       Data        65535           ; Initial large difference value
            LDA         0,0,20          ; R0 = 20 (count of numbers)
            LDA         1,0,DataNum     ; Load start address of DataNum into R1
Loop1:      IN          2,0             ; Input character from keyboard
            IN          2,0             ; Input character from keyboard
            STR         2,0,1           ; Store character in memory at R1
            AIR         1,1             ; Increment memory address for next number
            SIR         0,1             ; Decrement count in R0
            JNE         0,0,Loop1       ; Continue loop1 if register 0 is not zero
            LDA         0,0,20          ; Reload count in R0
            LDA         1,0,DataNum     ; Reset memory address in R1
PrintLoop:  LDR         2,0,1           ; Load number from memory into R2
            OUT         2,1             ; Output number to console
            AIR         1,1             ; Move to next address
            SIR         0,1             ; Decrement count
            JNE         0,0,PrintLoop   ; Continue if not done
GetTarget:  IN          3,0             ; Read target number
            STR         3,0,Target      ; Store target number in Target
            LDA         0,0,20          ; Reload count in R0
            LDA         1,0,DataNum     ; Reset start address in R1
FindClose:  LDR         2,0,1           ; Load number for comparison
            SIR         4,3             ; Calculate abs difference: |R2 - R3|
            AIR         4,0             ; Convert to absolute if needed
            JGE         4,0,SkipUpdate  ; If R4 >= Diff, skip update
            STR         2,0,Closest     ; Update closest number
            STR         4,0,Diff        ; Update smallest difference
SkipUpdate: AIR         1,1             ; Move to next memory location
            SIR         0,1             ; Decrement count
            JNE         0,0,FindClose   ; Continue if not done
            LDR         2,0,Target      ; Load target number into R2
            OUT         2,1             ; Print target number
            LDR         2,0,Closest     ; Load closest number into R2
            OUT         2,1             ; Print closest number
            LOC         1024
End:        HLT                         ; STOP