; Uses registers:
; R0 - Current input/working number
; R1 - Used for difference calculation
; R2 - Smallest difference found so far
; R3 - Closest number found

        LOC    20               ; Start at lower address
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
TARGET: Data   0                ; Target number to compare against
MINDIF: Data   700             ; Initial large difference value
RESULT: Data   0               ; Closest number found

; Main program
Start:  LDR    0,0,COUNT        ; Load counter
        LDX    1,0              ; Initialize X1 to 0

; Read numbers loop
RDLOOP: IN     3,0              ; Read from keyboard
        STR    3,1,ARRAY        ; Store in array
        AIR    1,1              ; Increment index
        AIR    0,-1            ; Decrement counter (use AIR with negative)
        JNE    0,0,RDLOOP       ; Jump if not zero

; Print the numbers
        LDR    0,0,COUNT       ; Reset counter
        LDX    1,0             ; Reset index
PRLOOP: LDR    3,1,ARRAY       ; Load number
        OUT    3,1             ; Print it
        AIR    1,1             ; Increment index
        SOB    0,0,PRLOOP      ; Loop for all numbers

; Get target
        IN     3,0             ; Read target
        STR    3,0,TARGET      ; Store it
        OUT    3,1             ; Echo target

; Find closest
        LDX    1,0             ; Reset index
        LDR    0,0,COUNT       ; Reset counter
        LDR    2,0,MINDIF      ; Load initial difference

FNDLP:  LDR    1,1,ARRAY       ; Load current number
        LDR    3,0,TARGET      ; Load target
        SMR    1,0,TARGET      ; Subtract target
        JGE    1,0,CHKDIF      ; If positive, check it
        NOT    1               ; Make positive
        AIR    1,1             ; Add 1

CHKDIF: TRR    1,2             ; Compare with min diff
        JGE    1,0,NEXT        ; If not smaller, next

        STR    1,0,MINDIF      ; Update min difference
        LDR    3,1,ARRAY       ; Get original number
        STR    3,0,RESULT      ; Save as closest

NEXT:   AIR    1,1             ; Next index
        SOB    0,0,FNDLP       ; Continue if not done

        LDR    3,0,RESULT      ; Load result
        OUT    3,1             ; Print closest number

End:    HLT                    ; Stop