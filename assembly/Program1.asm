Label       Opcode      Operand         Comments
            LOC         6               ; BEGIN AT LOCATION 6
DATA_NUMS   Data        0               ; Placeholder for numbers, first in sequence
TARGET_NUM  Data        0               ; Target number input by user
CLOSEST_NUM Data        0               ; Closest number to the target
CLOSEST_DIF Data        65535           ; Initial large difference value
            LDA         0,0,20          ; R0 = 20 (count of numbers)
            LDA         1,0,DATA_NUMS   ; Load start address of DATA_NUMS into R1
READ_LOOP   IN          2,0             ; Input character from keyboard
            STR         2,0,1           ; Store character in memory at R1
            AIR         1,1             ; Increment memory address for next number
            SIR         0,1             ; Decrement count in R0
            JNZ         READ_LOOP       ; Loop until all numbers are read
            LDA         0,0,20          ; Reload count in R0
            LDA         1,0,DATA_NUMS   ; Reset memory address in R1
PRINT_LOOP  LDR         2,0,1           ; Load number from memory into R2
            OUT         2,1             ; Output number to console
            AIR         1,1             ; Move to next address
            SIR         0,1             ; Decrement count
            JNZ         PRINT_LOOP      ; Continue if not done
GET_TARGET  IN          3,0             ; Read target number
            STR         3,0,TARGET_NUM  ; Store target number in TARGET_NUM
            LDA         0,0,20          ; Reload count in R0
            LDA         1,0,DATA_NUMS   ; Reset start address in R1
FIND_CLOSE  LDR         2,0,1           ; Load number for comparison
            SIR         4,3             ; Calculate abs difference: |R2 - R3|
            AIR         4,0             ; Convert to absolute if needed
            JGE         4,0,SKIP_UPDATE ; If R4 >= CLOSEST_DIF, skip update
            STR         2,0,CLOSEST_NUM ; Update closest number
            STR         4,0,CLOSEST_DIF ; Update smallest difference
SKIP_UPDATE AIR         1,1             ; Move to next memory location
            SIR         0,1             ; Decrement count
            JNZ         FIND_CLOSE      ; Continue if not done
            LDR         2,0,TARGET_NUM  ; Load target number into R2
            OUT         2,1             ; Print target number
            LDR         2,0,CLOSEST_NUM ; Load closest number into R2
            OUT         2,1             ; Print closest number
            LOC         1024
End:        HLT                         ; STOP