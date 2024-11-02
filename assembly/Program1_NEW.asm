    ; Uses registers:
    ; R0 - Temporary register for holding and updating the storage address
    ; R1 - Counter for the number of values to read
    ; R2 - Current input value
    ; R3 - Target value (not currently used)
    ; R4 - Smallest difference found so far (not currently used)
    ; R5 - Closest number found (not currently used)

            LOC    100                ; Start data storage at address 100
ARRAY:      Data   20                ; Start of numbers storage (20 words)
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
COUNT:      Data   20                ; Counter for number of values to read
TARGET:     Data   0                 ; Target value placeholder (not used here)
CURR_ADDR:  Data   100               ; Holds current address for data storage

START:      LDX    1,CURR_ADDR       ; Load starting address (100) into IX1
            LDR    1,0,COUNT         ; Load count (20) into R1

RDLOOP:     IN     2,0               ; Get input into R2
            STR    2,1,0             ; Store input at the address pointed by IX1

            LDR    0,0,CURR_ADDR     ; Load current storage address into R0
            AIR    0,1               ; Increment R0 to move to the next storage address
            STR    0,0,CURR_ADDR     ; Store the updated address back to CURR_ADDR
            LDX    1,CURR_ADDR       ; Update IX1 with the new address for the next loop

            SIR    1,1               ; Decrement R1 (count)
            STR    1,0,COUNT         ; Save the updated count back to COUNT

            JNE    1,0,RDLOOP        ; Loop if count is not zero
            JZ     0,1,End           ; Jump to End if count reaches zero

            LOC    4095
End:        HLT                      ; Stop
