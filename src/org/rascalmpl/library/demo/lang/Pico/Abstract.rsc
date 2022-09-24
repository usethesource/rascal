module demo::lang::Pico::Abstract

// highlight-next-line
public data TYPE = natural() | string(); 
	  
// highlight-next-line
public alias PicoId = str; 
	  
public data PROGRAM  
// highlight-next-line
    = program(list[DECL] decls, list[STATEMENT] stats);

public data DECL 
    = decl(PicoId name, TYPE tp);

public data EXP 
     = id(PicoId name)
     | natCon(int iVal)
     | strCon(str sVal)
     | add(EXP left, EXP right)
     | sub(EXP left, EXP right)
     | conc(EXP left, EXP right)
     ;
    
public data STATEMENT 
     = asgStat(PicoId name, EXP exp)
     | ifElseStat(EXP exp, list[STATEMENT] thenpart, list[STATEMENT] elsepart)
     | whileStat(EXP exp, list[STATEMENT] body)
     ;

// highlight-next-line
anno loc TYPE@location;
anno loc PROGRAM@location;
anno loc DECL@location;
anno loc EXP@location;
anno loc STATEMENT@location;

public alias Occurrence = tuple[loc location, PicoId name, STATEMENT stat]; // <5>
