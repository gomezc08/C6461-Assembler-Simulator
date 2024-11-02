; Uses registers:
; R0 - Index for storing input values
; R1 - Current input value
; R2 - Target value
; R3 - Smallest difference found so far
; R4 - Closest number found
; R5 - Temporary register for differences

        LOC    6                ; Start at lower address
COUNT:  Data   20               ; Number of values to read
ARRAY:  Data   0                ; Start of numbers storage (20 words)
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
        Data   0
        Data   0
        Data   0
; Define constants and counters
COUNT:     Data  20                ; Number of values to read
TARGET:    Data  0                 ; Target number to compare against

; Main program - Start at lower address
START:     LDR    0,0,COUNT        ; Load counter (20) into R0
           LDA    1,0,6            ; Load base address of data (6) into R1 for offset

; Read numbers loop at a low address
RDLOOP:    IN     3,0              ; Read user input into R3
           STR    3,1,0            ; Store input at address [R1]
           AIR    1,1              ; Increment R1 to next memory address
           SIR    0,1              ; Decrement R0 (counter)
           JNE    0,0,RDLOOP       ; If R0 != 0, loop back to RDLOOP (should fit in 5 bits now)
End:    HLT                     ; Stop
