Label       Opcode      Operand         Comments
            LOC         6               ; Start storing at location 6.
            Data        200             ; 6. Memory index = 200, 201, 202, ..., 200 + n where n is the number of characters in input.txt.
            Data        100             ; 7. Target word index = 100, 101, 102, ..., 100 + m where m is the number of characters in target word.
            Data        1               ; 8. Word tracker - keeps track what word we are on in the sentence (default is the 1st word).
            Data        1               ; 9. Sentence tracker - keeps track what sentence we are on in input.txt (default is the 1st sentence).
            Data        46              ; 10. Period (.) ASCII value.
            Data        32              ; 11. Space ASCII value.
            Data        1               ; 12. Default value of 1 (useful for resetting word number if we jump to new sentence).
            IN          2,2             ; 13. Enter file and it is automatically stored starting at address 100.
            IN          2,3             ; 14. Enter target word and its automatically stored starting at address 10.
            LDR         0,0,6           ; R0 = Memory index.
            LDR         1,0,7           ; R1 = Target word index.
            
MAINLOOP:   LDR         0,0,6           ; 12. R0 = Memory index.
            LDR         1,0,7           ; 13. R1 = Target word index.
            LDR         2,0,6,1         ; 14. R2 = Mem[Memory index].
            LDR         3,0,7,1         ; 15. R3 = Mem[Target word index].

            HLT                         ; Stop execution 