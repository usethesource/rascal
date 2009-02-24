module PicoAbstractSyntax
/*
 * The abstract syntax of our favourite toy language
 */
public data TYPE =
	  natural | string;
	  
public alias PicoId = str;

public data EXP = 
      id(PicoId name)
    | natCon(int iVal)
    | strCon(str sVal)
    | add(EXP left, EXP right)
    | sub(EXP left, EXP right)
    | conc(EXP left, EXP right)
    ;
    
public data STATEMENT =
	  asgStat(PicoId name, EXP exp)
	| ifStat(EXP exp, list[STATEMENT] thenpart, list[STATEMENT] elsepart)
	| whileStat(EXP exp, list[STATEMENT] body)
	;
    
public data DECL =
	  decl(PicoId name, TYPE tp);

public data PROGRAM =
	  program(list[DECL] decls, list[STATEMENT] stats);


data POS = pos(int n);

anno POS pos on EXP, STATEMENT;