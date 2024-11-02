    ; Uses registers:
    ; R0 - Index for storing input values (starting at address 100)
    ; R1 - Counter (number of values to read)
    ; R2 - Current input value
    ; R3 - Target value
    ; R4 - Smallest difference found so far
    ; R5 - Closest number found

            LOC    100                ; Start at lower address
    ARRAY:  Data   20                ; Start of numbers storage (20 words)
            Data   10
            Data   30
            Data   0
            Data   0
            Data   0
            Data   0
            Data   0
            Data   0
            Data   0
            Data   0
            Data   0
            Data   0
            Data   0
            Data   0
            Data   0
            Data   0
            Data   0
            Data   0

            LOC    6
COUNT:      Data   20                
TARGET:     Data   0                 
TEMP:       Data   100              

START:      LDX    1,TEMP          ; IX1 gets 100
            LDA    0,0,0           ; Initialize R0 for offset
RDLOOP:     IN     2,0             ; Get input into R2
            STR    2,1,0           ; Store R2 at memory[0 + IX1]
            LDR    0,1,0           ; Load current IX1 value into R0
            AIR    0,1             ; Increment it
            STX    0,1,0           ; Store incremented value back to IX1
            LDR    3,0,COUNT       ; Get count
            SIR    3,1             ; Decrement count
            STR    3,0,COUNT       ; Save count
            JNE    3,0,RDLOOP      ; Loop if count not zero
            JZ     0,1,End

            LOC    4095
End:        HLT                     ; Stop