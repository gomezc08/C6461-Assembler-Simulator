; Program to read 20 numbers and find closest to target
; Constants and data storage
LOC 6
Size: Data 20         ; Size of our array
Count: Data 0         ; Counter for input numbers
Temp: Data 0          ; Temporary storage
Current: Data 0       ; Current number being processed
Target: Data 0        ; Number to find closest to
Closest: Data 0       ; Closest number found
Diff: Data 0          ; Current difference
MinDiff: Data 999     ; Minimum difference found
Array: Data 0         ; Start of number array (20 words)

; Store addresses for indirect jumps
ProcAddr: Data 114    ; Address of Process routine
ReadAddr: Data 103    ; Address of ReadLoop routine
SearchAddr: Data 124  ; Address of Search routine
NextAddr: Data 138    ; Address of NextNum routine
DoneAddr: Data 142    ; Address of Done routine

; Main program
LOC 100
Start: LDR 0,0,Count    ; Initialize counter
      AIR 0,0           ; Set counter to 0
      STR 0,0,Count

; Read loop - Get 20 numbers
ReadLoop: LDR 0,0,Count    ; Load counter
         LDR 1,0,Size      ; Load size
         TRR 0,1          ; Compare counter to size
         JCC 3,0,Process   ; If equal, go to processing (check cc(3))

         IN 2,0            ; Get input from keyboard
         STR 2,0,Current   ; Store the number
         
         ; Store in array
         LDR 3,0,Count     ; Get current count
         STX 1,Count       ; Store count in index register 1
         STR 2,1,Array     ; Store number in array using IX1
         
         ; Print the number
         LDR 2,0,Current
         OUT 2,1           ; Print to console

         ; Increment counter
         LDR 0,0,Count
         AIR 0,1           ; Add 1 to counter
         STR 0,0,Count
         JMA 0,ReadAddr,1  ; Continue reading

; Process - Get target number and find closest
Process: IN 2,0            ; Get target number
         STR 2,0,Target    ; Store target
         OUT 2,1           ; Echo target number

         ; Initialize for search
         LDR 0,0,MinDiff
         STR 0,0,MinDiff   ; Reset minimum difference
         AIR 0,0           ; Clear R0 for counter
         STR 0,0,Count

; Search loop
Search:  LDR 0,0,Count     ; Load counter
         LDR 1,0,Size      ; Load size
         TRR 0,1          ; Compare counter to size
         JCC 3,0,Done     ; If equal, jump to Done (check cc(3))

         ; Calculate difference
         LDR 2,0,Target    ; Load target
         LDR 3,0,Count     ; Get current index
         STX 1,Count       ; Store count in index register 1
         LDR 3,1,Array     ; Load array[count] using IX1
         
         ; Find absolute difference
         SMR 3,0,Target    ; Subtract target
         STR 3,0,Diff      ; Store difference
         
         ; Compare with minimum
         LDR 1,0,MinDiff
         SMR 1,0,Diff     ; Compare differences
         JGE 1,0,NextAddr,1   ; If current >= min, skip (indirect)
         
         ; Update minimum and closest
         LDR 3,0,Count    ; Get current index
         STX 1,Count      ; Store count in index register 1
         LDR 3,1,Array    ; Load array[count] using IX1
         STR 3,0,Closest  ; Update closest
         LDR 3,0,Diff
         STR 3,0,MinDiff  ; Update minimum difference

NextNum: LDR 0,0,Count    ; Increment counter
         AIR 0,1
         STR 0,0,Count
         JMA 0,SearchAddr,1   ; Continue search (indirect)

; Print results
Done:    LDR 2,0,Target   ; Load target
         OUT 2,1          ; Print target
         LDR 2,0,Closest  ; Load closest
         OUT 2,1          ; Print closest number
         HLT              ; Stop program