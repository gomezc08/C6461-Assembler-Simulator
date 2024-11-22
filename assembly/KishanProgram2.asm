Label       Opcode      Operand         Comments
            LOC         6               ; Start storing at location 6.
            Data        32              ; 6. Space ASCII value.
            Data        46              ; 7. Period (.) ASCII value.
            Data        0               ; 8. Count of sentence/ Sentence Number
            Data        0               ; 9. Count of number of matches
            Data        1               ; 10. Word/character length Count (should be 0 but used 2 since comaprsion is required in first iteration)
            Data        7               ; 11. Number of sentences (6 sentences + 1 for initialization purpose)
            Data        100             ; 12. Target word index = 100, 101, 102, ..., 100 + m where m is the number of characters in target word.
            Data        200             ; 13. Memory index = 200, 201, 202, ..., 200 + n where n is the number of characters in input.txt.
            Data        0               ; 14. Used to set new word length
            Data        1               ; 15. Default value of 1 (useful for resetting word number if we jump to new sentence).
            Data        34              ; 16. Address of WORD loop  
            Data        58              ; 17. Address of END LOOP
            Data        32              ; 18. Address of MATCH Loop
            IN          2,2             ; 18. Enter file and it is automatically stored starting at address 200.
            IN          2,3             ; 19. Enter target word and its automatically stored starting at address 100.
            
SENTENCE:   LDR         0,0,8           ; 20. Initial count of number of sentence loaded into Reg 0 to get sentence number
            AIR         0,1             ; 21. Increment the sentence number
            STR         0,0,8           ; 22. Store the current sentence number in location 8
            LDR         1,0,11          ; 23. Count for sentence
            SIR         1,1             ; 24. Reduce 1 from sentence count
            STR         1,0,11          ; 25. Store the count of sentences in 11
            JZ          1,0,17         ; 26 If no. of sentence is 0, jump to the end.

            LDR         2,0,9           ; 27. Load the match count in Reg 2 (initial value 0)
            LDR         3,0,10          ; 28. Lenth of the word from memory/file (initial value 1).
            TRR         2,3             ; 29. Check if the match count = the length of word from file
            JCC         4,0,17         ; 30. Jump to the end if the target word = length of word from file

MATCH:      AIR          2,1             ; 31. Add 1 if there is match between the word in memory and the target word(initially adds 1 to 9, so now both loc 9 and 10 have 1 value)
            STR          2,0,9           ; 32. Store the count of matching alphabets in location 9

WORD:       LDR         0,0,13,1        ; 32. Get the character of word from memory location 200
            LDR         1,0,13          ; 33. Index for the word from memory 200,201,202,....

            LDR         2,0,10          ; 34. get the length of word in Reg 2(initially 1)
            AIR         2,1             ; 35. Since we have first character of word, increment count of character by 1 (2,3,4...)
            STR         2,0,10          ; 36. Store the count value in location 10

            AIR         1,1             ; 37. Increment the index value of word from memory 200+1,201+1,...
            STR         1,0,13          ; 38. Store the index vlaue of word from memory at location 13 

            LDR         2,0,12,1        ; 39. Get the characters of word from target word/input
            LDR         3,0,12          ; 40. Index for the word from input 100, 101, 102, ...
            AIR         3,1             ; 41. Increment the index value for target word 100+1,101+1, 102+1...
            STR         3,0,12          ; 42. Store at location 12 the index value of target word  


            TRR         0,2             ; 43. Test if the ASCII value of Reg 0 and Reg 2 are equal
            JCC         4,0,18       ; 44. If the characters are equal, then jump to the match loop

            LDR         3,0,12          ; 45. Index for the target word, where we have reached by increasing
            SMR         3,0,9           ; 46. substract Index from number of matches if only few characters match  

            STR         3,0,12          ; 47. in next search process, the index will point to the start of target word  

            LDR         2,0,6           ; 48. Load ASCII of space into Reg 3
            TRR         0,3             ; 49. Compare if the new character is space
            JCC         4,0,16          ; 50. If space, it is a new word

            LDR         2,0,7           ; 51. Load ASCII of . in Reg 3
            TRR         0,3             ; 52. Compare if new character is .
            JCC         4,0,SENTENCE    ; 53. If true, jump to sentence
            LDR         3,0,10          ; 54. Load word count into Reg 3
            JNE         3,0,16        ; 55. If word count is greater than 3, jump to word.  

END:        OUT         2,1              ; 56. Ouput certain value
            HLT                         ; 57  . Stop execution 