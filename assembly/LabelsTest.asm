Label       Opcode      Operand         ; Comments
            LOC         6               ; BEGIN AT LOCATION 6
            Data        10              ; Memory location 6 = 10
            Data        20              ; address 7 = 20
            Data        0               ; address 8 = 0
            LDR         2,0,6           ; R2 gets address 6 = 10
            LDR         0,0,7           ; R0 = 20
End:        HLT                         ; Stop program