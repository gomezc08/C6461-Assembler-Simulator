Label       Opcode      Operand         Comments
            LOC         6               ; BEGIN AT LOCATION 6
            Data        0               ; 6. Placeholder for numbers
Target:     Data        0               ; 7. Target number input by user
Closest:    Data        0               ; 8. Closest number to the target
Diff:       Data        65535           ; 9. Initial large difference value
            LDA         0,0,20          ; 10. Load 20 (count of numbers) into R0
            LDA         1,0,6           ; 11. Load start address of DataNum into R1
ReadLoop:   IN          2,0             ; 12. Read number from keyboard into R2
            STR         2,0,1           ; 13. Store number in memory at R1 address
            AIR         1,1             ; 14. Increment R1 to point to next memory address
            SIR         0,1             ; 15. Decrement count in R0
            JNE         0,0,ReadLoop    ; 16. If R0 != 0, repeat until all 20 numbers are read
            LDA         0,0,20          ; 17. Reload count (20) into R0
            LDA         1,0,6           ; 18. Reset R1 to start address of DataNum
PrintLoop:  LDR         2,0,1           ; 19. Load number from memory into R2
            OUT         2,1             ; 20. Output number to console printer
            AIR         1,1             ; 21. Move to next memory address
            SIR         0,1             ; 22. Decrement count in R0
            JNE         0,0,PrintLoop   ; 23. Repeat if R0 != 0
            IN          3,0             ; 24. Read target number into R3
            STR         3,0,Target      ; 25. Store target number in Target memory location
            LDA         0,0,20          ; 26. Reload count (20) into R0
            LDA         1,0,6           ; 27. Reset R1 to start address of DataNum
FindLoop:   LDR         2,0,1           ; 28. Load number from memory into R2
            SIR         4,3             ; 29. Calculate difference (R2 - R3), store in R4
            JGE         4,0,SkipAbs     ; 30. If R4 >= 0, skip absolute value calculation
            AIR         4,1             ; 31. Convert to positive (abs), only if R4 < 0
SkipAbs:    LDR         5,0,Diff        ; 32. Load current smallest difference from Diff
            JGE         4,5,NextNum     ; 33. If R4 >= Diff, skip updating closest
            STR         2,0,Closest     ; 34. Update Closest with current number
            STR         4,0,Diff        ; 35. Update Diff with new smallest difference
NextNum:    AIR         1,1             ; 36. Move to next memory location in DataNum
            SIR         0,1             ; 37. Decrement count in R0
            JNE         0,0,FindLoop    ; 38. Repeat if R0 != 0
            LDR         2,0,Target      ; 39. Load target number from memory into R2
            OUT         2,1             ; 40. Output target number
            LDR         2,0,Closest     ; 41. Load closest number from memory into R2
            OUT         2,1             ; 42. Output closest number
            LOC         1024            ; 43. Set end location for program
End:        HLT                         ; 44. Stop execution
