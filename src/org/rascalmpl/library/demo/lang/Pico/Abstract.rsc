module demo::lang::Pico::Abstract

// highlight-next-line
data TYPE(loc src=|unknown:///|) // <1>
    = natural() 
    | string()
    ; 
	  
// highlight-next-line
alias PicoId = str; // <2>
	  
data PROGRAM(loc src=|unknown:///|) // <4>
// highlight-next-line
    = program(list[DECL] decls, list[STATEMENT] stats); // <3> 

data DECL(loc src=|unknown:///|) // <4>
    = decl(PicoId name, TYPE tp);

data EXP(loc src=|unknown:///|) // <4>
    = id(PicoId name)
    | natCon(int iVal)
    | strCon(str sVal)
    | add(EXP left, EXP right)
    | sub(EXP left, EXP right)
    | conc(EXP left, EXP right)
    ;
    
data STATEMENT(loc src=|unknown:///|) // <4>
    = asgStat(PicoId name, EXP exp)
    | ifElseStat(EXP exp, list[STATEMENT] thenpart, list[STATEMENT] elsepart)
    | whileStat(EXP exp, list[STATEMENT] body)
    ;

// highlight-next-line
alias Occurrence = tuple[loc src, PicoId name, STATEMENT stat]; // <5>
