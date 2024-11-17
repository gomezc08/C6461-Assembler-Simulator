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
            Data        0               ; 26. Reserve for new number
            Data        20              ; 27. i = 19,18,17,..,0.
            Data        6               ; 28. memory = 6,7,8,...,25.
            Data        0               ; 29. store the AND value (initially 0)
            Data        55              ; 30 Store the location of the loop
            
LOOP:      LDR         0,0,27          ; 31. TREATING R0 as i. R0 = 19 at first.
            LDR         1,0,28          ; 32. TREATING R1 as where in memory we are.. R1 = 6
            IN          2,0             ; 33. R2 = User input.
            OUT         2,1             ; 34. output it.
            STR         2,0,28,1        ; 35. store whatever user typed in into memory[i]
            SIR         0,1             ; 36. R0 -= 1
            AIR         1,1             ; 37. R1 += 1
            STR         0,0,27          ; 38. update memory 27.
            STR         1,0,28          ; 39. update memory 28.
            JNE         0,1,LOOP       ; 40. If R0 (i) is NOT 0, jump back to start of loop.

            SIR         1,6             ; 41 Substract 6 from reg 1 to make it 20
            AIR         0,6             ; 42 Make the memory location 6
            STR         0,0,28          ; 43
            STR         1,0,27          ; 44 Again, make the count 20//
            
            IN          2,0             ; 45. special number will be stored in R3 the whole time.
            STR         2,0,26          ; 46. Store the number to be compared into memory at reg 2.

            LDR         0,0,27          ; 47Counter for substraction(load 20 value in reg 0).
            LDR         1,0,28       ; 48 loads the value of location 6//
            SMR         2,0,28,1        ; 49 subtract given number from memory address(Reg 2 - Memory location 6)
            STR         2,0,29          ; 50 Store into location 29 the difference (calculated and stored at 3)
            AIR         1,1             ; 51 Increment the count which indicates at the 20 numbers we scanned
            SIR         0,1             ; 52 Substract 1 from 20 
            STR         0,0,27          ; 53 Store contents of register 0 into memroy location 27.
            STR         1,0,28          ; 54 store contents of register 1 into memory location 28.

     
NEWLOOP:    LDR        2,0,26        ; 55 Load the unique number to be substracted in register 2   
            SMR         2,0,28,1        ; 56 subtract given number from memory address (Reg 2 - Memory loc 7.8.9....20)
            LDR         3,0,29          ; 57 Load register 3 with contents of memory 29
            AND         2,3             ; 58 AND between 2 and 3
            STR         2,0,29          ; 59 Load the AND value at location 29
            AIR         1,1             ; 60 Increment the count which indicates at the 20 numbers we scanned
            SIR         0,1             ; 61 Substract 1 from 20 every time
            STR         0,0,27          ; 62 Send value of 0 to 27    
            LDR         2,0,26          ; 63 Load reg 2 from memory 26(last value)
            LDR         3,0,29          ; 64 move  and value to reg 3
            STR         0,0,27          ; 65 Send new reduced value to memory loc 27 from reg 0.
            STR         1,0,28          ; 66 Send new reduced value to memory loc 28 from reg 0.
            JNE         0,1,30,1       ; 67 If R0 (i) is NOT 0, jump back to start of loop.

            SMR         2,0,29          ; 72 Substract the given number with the final AND value
            OUT         2,1             ; 73
            
            LOC         1024            ; 76 End program section
End:        HLT                         ; 77 Stop execution 