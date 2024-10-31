Label       Opcode      Operand         Comments
            LOC         6               ; Start storing numbers at location 6
            Data        0               ; 6. Reserve 20 locations for our numbers
            Data        0               ; 7. Reserve 20 locations for our numbers
            Data        0               ; 8. Reserve 20 locations for our numbers
            Data        0               ; 9. Reserve 20 locations for our numbers
            Data        0               ; 10. Reserve 20 locations for our numbers
            Data        0               ; 11. Reserve 20 locations for our numbers
            Data        0               ; 12. Reserve 20 locations for our numbers
            Data        0               ; 13. Reserve 20 locations for our numbers
            Data        0               ; 14. Reserve 20 locations for our numbers
            Data        0               ; 15. Reserve 20 locations for our numbers
            Data        0               ; 16. Reserve 20 locations for our numbers
            Data        0               ; 17. Reserve 20 locations for our numbers
            Data        0               ; 18. Reserve 20 locations for our numbers
            Data        0               ; 19. Reserve 20 locations for our numbers
            Data        0               ; 20. Reserve 20 locations for our numbers
            Data        0               ; 21. Reserve 20 locations for our numbers
            Data        0               ; 22. Reserve 20 locations for our numbers
            Data        0               ; 23. Reserve 20 locations for our numbers
            Data        0               ; 24. Reserve 20 locations for our numbers
            Data        0               ; 25. Reserve 20 locations for our numbers
            Data        20              ; 26. i = 19,18,17,..,0.
            Data        6               ; 27. memory = 6,7,8,...,25.
            Data        32              ; 28. memory = end.
LOOP1:      LDR         0,0,26          ; 29. TREATING R0 as i. R0 = 19 at first.
            LDR         1,0,27          ; 30. TREATING R1 as where in memory we are.. R1 = 6
            IN          2,0             ; 31. R2 = User input.
            OUT         2,1             ; 32. output it.
            STR         2,0,27,1        ; 33. store whatever user typed in into memory[i]
            SIR         0,1             ; 34. R0 -= 1
            AIR         1,1             ; 35. R1 += 1
            STR         0,0,26          ; 36. update memory 26.
            STR         1,0,27          ; 37. update memory 27.
            JNE         0,1,LOOP1       ; 38. If R0 (i) is NOT 0, jump back to start of loop.
            IN          3,0             ; 39. special number will be stored in R3 the whole time.
            LOC         1024            ; End program section
End:        HLT                         ; Stop execution