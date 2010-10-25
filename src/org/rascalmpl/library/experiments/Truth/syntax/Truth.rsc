module experiments::Truth::Truth

syntax Truth = \true  : True t
             | \false : False f
             ;
             
syntax True = \true      : "true"
            | \not       : "not" False f
            > left \and  : True lt "and" True rt
            | left \or   : True lt "or" True rt
            |      \or   : False lf "or" True rt
            |      \or   : True lt "or" False rf
            ;
            
syntax False = \false    : "false"
             | \not      : "not" True t
             > left \and : False lf "and" False rf
             |      \and : True  lt "and" False rf
             |      \and : False lf "and" True  rt
             | left \or  : False lf "or" False  rf
             ;