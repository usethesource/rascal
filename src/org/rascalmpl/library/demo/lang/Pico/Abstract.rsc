module demo::lang::Pico::Abstract

// highlight-next-line
data TYPE(loc src=|unknown:///|) 
    = natural() 
    | string()
    ; 
	  
// highlight-next-line
alias PicoId = str; 
	  
data PROGRAM(loc src=|unknown:///|)
// highlight-next-line
    = program(list[DECL] decls, list[STATEMENT] stats);

data DECL(loc src=|unknown:///|) 
    = decl(PicoId name, TYPE tp);

data EXP(loc src=|unknown:///|) 
    = id(PicoId name)
    | natCon(int iVal)
    | strCon(str sVal)
    | add(EXP left, EXP right)
    | sub(EXP left, EXP right)
    | conc(EXP left, EXP right)
    ;
    
data STATEMENT(loc src=|unknown:///|)
    = asgStat(PicoId name, EXP exp)
    | ifElseStat(EXP exp, list[STATEMENT] thenpart, list[STATEMENT] elsepart)
    | whileStat(EXP exp, list[STATEMENT] body)
    ;

// highlight-next-line
alias Occurrence = tuple[loc src, PicoId name, STATEMENT stat]; 
