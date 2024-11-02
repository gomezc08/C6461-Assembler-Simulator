Label       Opcode      Operand         ; Comments
            LOC         6               ; BEGIN AT LOCATION 6
Counter:    Data        20              ; Initialize counter to 20
Zero:       Data        0               ; Constant zero for comparison
            LDR         1,0,Counter     ; Load counter value into R1
Loop1:      OUT         1,1             ; Print current counter value
            SIR         1,1             ; Decrement counter by 1
            STR         1,0,Counter     ; Store updated counter
            LDR         2,0,Zero        ; Load zero for comparison
            TRR         1,2             ; Compare counter with zero
            JCC         3,0,End         ; If equal (EQUALORNOT), jump to End
            JMA         0,Loop1         ; Otherwise, jump back to Loop1
End:        HLT                         ; Stop program