module demo::lang::Pico::Typecheck

import Prelude;
import demo::lang::Pico::Abstract;
import demo::lang::Pico::Load;

// highlight-next-line
alias TENV = tuple[ map[PicoId, TYPE] symbols, list[tuple[loc l, str msg]] errors]; // <1>

// highlight-next-line
TENV addError(TENV env, loc l, str msg) = env[errors = env.errors + <l, msg>]; // <2>

// highlight-next-line
str required(TYPE t, str got) = "Required <getName(t)>, but got <got>"; // <3>
str required(TYPE t1, TYPE t2) = required(t1, getName(t2));             // <3>

@synopsis{Checking Expressions}
TENV checkExp(exp:natCon(int N), TYPE req, TENV env) = // <4>
  req := natural() ? env : addError(env, exp.src, required(req, "natural"));

TENV checkExp(exp:strCon(str S), TYPE req, TENV env) =
 req := string() ? env : addError(env, exp.src, required(req, "string"));

TENV checkExp(exp:id(PicoId Id), TYPE req, TENV env) { // <5>
  if(!env.symbols[Id]?)
     return addError(env, exp.src, "Undeclared variable <Id>");
  tpid = env.symbols[Id];
  return req := tpid ? env : addError(env, exp.src, required(req, tpid));
}

TENV checkExp(exp:add(EXP E1, EXP E2), TYPE req, TENV env) = // <6>
  natural() := req ? checkExp(E1, natural(), checkExp(E2, natural(), env))
                   : addError(env, exp.src, required(req, "natural"));
  
TENV checkExp(exp:sub(EXP E1, EXP E2), TYPE req, TENV env) = // <7>
  natural() := req ? checkExp(E1, natural(), checkExp(E2, natural(), env))
                   : addError(env, exp.src, required(req, "natural"));

TENV checkExp(exp:conc(EXP E1, EXP E2), TYPE req, TENV env) = // <8>  
  string() := req ? checkExp(E1, string(), checkExp(E2, string(), env))
                   : addError(env, exp.src, required(req, "string"));


@synopsis{Check a statement}
TENV checkStat(stat:asgStat(PicoId Id, EXP Exp), TENV env) { // <9>
  if(!env.symbols[Id]?)
     return addError(env, stat.src, "Undeclared variable <Id>");
  tpid = env.symbols[Id];
  return checkExp(Exp, tpid, env);
}
	
TENV checkStat(stat:ifElseStat(EXP Exp, // <10>
                              list[STATEMENT] Stats1,
                              list[STATEMENT] Stats2),
               TENV env){
    env0 = checkExp(Exp, natural(), env);
    env1 = checkStats(Stats1, env0);
    env2 = checkStats(Stats2, env1);
    return env2;
}

TENV checkStat(stat:whileStat(EXP Exp, 
                             list[STATEMENT] Stats1),
                 TENV env) {
    env0 = checkExp(Exp, natural(), env);
    env1 = checkStats(Stats1, env0);
    return env1;
}

@synopsis{Check a list of statements}
// highlight-next-line
TENV checkStats(list[STATEMENT] Stats1, TENV env) { // <11>
  for(S <- Stats1){
      env = checkStat(S, env);
  }
  return env;
}
  
@synopsis{Check declarations}
// highlight-next-line
TENV checkDecls(list[DECL] Decls) = // <12>
    <( Id : tp | decl(PicoId Id, TYPE tp) <- Decls), []>;

@synopsis{Check a Pico program}
// highlight-next-line
TENV checkProgram(program(list[DECL] Decls, list[STATEMENT] Series)) {  // <13>
    return checkStats(Series, checkDecls(Decls));
}

// highlight-next-line
list[tuple[loc l, str msg]] checkProgram(str txt) = checkProgram(load(txt)).errors; // <14>
    
