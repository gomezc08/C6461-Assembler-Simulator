Label       Opcode      Operand         Comments
            LOC         6               ; Start storing at location 6.
            Data        200             ; 6. Memory index = 200, 201, 202, ..., 200 + n where n is the number of characters in input.txt.
            Data        100             ; 7. Target word index = 100, 101, 102, ..., 100 + m where m is the number of characters in target word.
            Data        1               ; 8. Word tracker - keeps track what word we are on in the sentence (default is the 1st word).
            Data        1               ; 9. Sentence tracker - keeps track what sentence we are on in input.txt (default is the 1st sentence).
            Data        46              ; 10. Period (.) ASCII value.
            Data        32              ; 11. Space ASCII value.
            Data        1               ; 12. Default value of 1 (useful for resetting word number if we jump to new sentence).
            IN          2,2             ; 10. Enter file and it is automatically stored starting at address 100.
            IN          2,3             ; 11. Enter target word and its automatically stored starting at address 10.
            LDR         0,0,6           ; R0 = Memory index.
            LDR         1,0,7           ; R1 = Target word index.

MAINLOOP:   LDR         0,0,6           ; 12. R0 = Memory index.
            LDR         1,0,7           ; 13. R1 = Target word index.
            LDR         2,0,6,1         ; 14. R2 = Mem[Memory index].
            LDR         3,0,7,1         ; 15. R3 = Mem[Target word index].
            JMA         0,COMPARE       ; 16. Force a jump to compare (we will return to MAINLOOP if the two words arent the same).

PROCEED:    LDR         1,0,7           ; reset R1 (target index back to beginning)!
            LDR         0,0,6           ; R0 = NEW Memory index.
            AIR         0,1             ; increment R2 by 1.
            STR         0,0,6           ; Memory[6] = NEW Memory index.
            LDR         2,0,6,1         ; R2 = NEW Mem[Memory index].
; if its a period.
            SMR         2,0,10          ; R2 - Period (.) ASCII.
            JZ          2,0,PERIOD      ; WE ARE AT A SPACE. 

; else if its a space.
            LDR         2,0,6,1         ; R2 = NEW Mem[Memory index].
            SMR         2,0,11          ; R2 - Space ASCII.
            JZ          2,0,SPACE       ; WE ARE AT A PERIOD.
; else
            JMA         0,PROCEED       ; Keep incrementing.

PERIOD:     LDR         3,0,9           ; R3 = Sentence number.
            AIR         3,1             ; Increment the sentence number by 1.
            STR         3,0,9           ; Store new sentence number.
            LDR         3,0,12          ; R3 = 1.
            STR         3,0,8           ; since we hit a period, we need to move word number back to 1 since we are back to start of a sentence.
; since we are currently on a period, we need to move to first character of next word before jumping back to mainloop...
            AIR         0,1             ; R0 = Memory index + 1.
            AIR         0,1             ; Typically is looks like: "Hello. Chris". so we have a period followed by a space so need to increment twice.
            JMA         0,MAINLOOP      ; jump back to mainloop!

SPACE:      LDR         3,0,8           ; R3 = Word number.
            AIR         3,1             ; Increment the word number by 1.
            STR         3,0,9           ; Store new word number.
; since we are currently on a space, we need to move to first character of next word before jumping back to mainloop...
            AIR         0,1             ; R0 = Memory index + 1.
            JMA         0,MAINLOOP      ; jump back to mainloop!
           
           

; if R2 == Space and R3 = Period (.) AKA if we finish comparing both strings and they both reached the end...
COMPARE:    SMR         2,0,11          ; R2 = 32 (space) - R2.
            SMR         3,0,10          ; R3 = 46 (period) - R3.
            JZ          2,CHECK2        ; I hope this ins't confusion... CHECK2 checks if R3 is also 0. in this case, we have reached the end.

; else if Word[i] != target[j]...
COMPARE2:   LDR         2,0,6,1         ; R2 = Mem[Memory index].
            LDR         3,0,7,1         ; R3 = Mem[Target word index]. 
            JNE         2,3,PROCEED

; else we continue checking (update pointers/values and call back to compare)...
            LDR         0,0,6           ; R0 = Memory index.
            LDR         1,0,7           ; R1 = Target word index.
            AIR         0,1             ; R1++
            AIR         1,1             ; R2++
            STR         0,0,6           ; Store new memory index.
            STR         1,0,7           ; Store new target word index.
            LDR         2,0,6,1         ; R2 = NEW Mem[Memory index].
            LDR         3,0,7,1         ; R3 = NEW Mem[Target word index].
            JMA         0,COMPARE

CHECK2:     JZ          3,CONCLUSION    ; yay we found everything!!!
            JMA         0,COMPARE2      ; if we actually have't found everything haha, jump back to next if condition.    

CONCLUSION: LDR         1,0,12          ; start at beginning of character.
            STR         1,0,7           ; update reset of target word index.
            LDR         0,8             ; R0 = word number.
            OUT         0,1             ; Print word number.
            LDR         0,9             ; R0 = sentence number.
            OUT         0,1             ; Print sentnece number.
            JMA         0,OUTPUT        ; self explanatory.

OUTPUT:     ; TODO.
            
            HLT                         ; Stop execution 