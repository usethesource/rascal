module demo::lang::Pico::Eval

import demo::lang::Pico::Abstract;
import demo::lang::Pico::Load;

data PicoValue 
  = natval(int n) 
  | strval(str s) 
// highlight-next-line  
  | errorval(loc l, str msg); 

// highlight-next-line
alias VENV = map[PicoId, PicoValue]; 

@synopsis{Evaluate Expressions}
PicoValue evalExp(exp:natCon(int N), VENV env) = natval(N);

PicoValue evalExp(exp:strCon(str S), VENV env) = strval(S);

PicoValue evalExp(exp:id(PicoId Id), VENV env)  = 
    env[Id]?  ? env[Id] : errorval(exp.src, "Uninitialized variable <Id>");

PicoValue evalExp(exp:add(EXP E1, EXP E2), VENV env) = 
   (natval(n1) := evalExp(E1, env) && 
    natval(n2) := evalExp(E2, env)) ? natval(n1 + n2)
                                    : errorval(exp.src, "+ requires natural arguments");
  
PicoValue evalExp(exp:sub(EXP E1, EXP E2), VENV env) = 
   (natval(n1) := evalExp(E1, env) && 
    natval(n2) := evalExp(E2, env)) ? natval(n1 - n2)
                                    : errorval(exp.src, "- requires natural arguments");
                                                                     
PicoValue evalExp(exp:conc(EXP E1, EXP E2), VENV env) = 
   (strval(s1) := evalExp(E1, env) && 
    strval(s2) := evalExp(E2, env)) ? strval(s1 + s2)
                                    : errorval(exp.src, "|| requires string arguments");

@synopsis{Evaluate a statement}
VENV evalStat(stat:asgStat(PicoId Id, EXP Exp), VENV env) {
  env[Id] = evalExp(Exp, env);
  return env;
}
	
VENV evalStat(stat:ifElseStat(EXP Exp, 
                              list[STATEMENT] Stats1,
                              list[STATEMENT] Stats2),
              VENV env) =
  evalStats(evalExp(Exp, env) != natval(0) ? Stats1 : Stats2, env);

VENV evalStat(stat:whileStat(EXP Exp, 
                             list[STATEMENT] Stats1),
              VENV env) {
    while(evalExp(Exp, env) != natval(0)){
       env = evalStats(Stats1, env);
    }
    return env;
}

@synopsis{Evaluate a list of statements}
VENV evalStats(list[STATEMENT] Stats1, VENV env) {
  for(S <- Stats1){
      env = evalStat(S, env);
  }
  return env;
}
  
@synopsis{Evaluate declarations}
VENV evalDecls(list[DECL] Decls) =
    ( Id : (tp == demo::lang::Pico::Abstract::natural() ? natval(0) : strval(""))  
    | decl(PicoId Id, TYPE tp) <- Decls
    );

@synopsis{Evaluate a parsed Pico program}
VENV evalProgram(PROGRAM P){
  if(program(list[DECL] Decls, list[STATEMENT] Series) := P){
     VENV env = evalDecls(Decls);
     return evalStats(Series, env);
  } else
    throw "Cannot happen";
}

@synopsis{Parse and evaluate a Pico program}
VENV evalProgram(str txt) = evalProgram(load(txt));
    
