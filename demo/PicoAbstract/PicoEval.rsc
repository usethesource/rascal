module PicoEval

import PicoAbstractSyntax;
 
data PicoValue = intval(int) | strval(str);

alias VEnv = map[PicoId, PicoValue];

VEnv Env = ();

VEnv evalProgram(PROGRAM P){
    switch (P) {
      case program(list[DECL] Decls, list[STATEMENT] Series): {
           evalDecls(Decls);
           evalStatements(Series, Env);
      }
   }
}

void evalDecls(list[DECL] Decls){
    visit (Decls) {
      case decl(PicoId Id, string): { 
           Env[Id] = strval(""); 
      }
      case decl(PicoId Id, natural): { 
           Env[Id] = intval(0);  
      }
    };
}

void evalStatements(list[STATEMENT] Series){
    for(STATEMENT Stat : Series){
        evalStatement(Stat);
    }
}

void evalStatement(STATEMENT Stat){
    switch (Stat) {
      case asgStat(PicoId Id, EXP Exp): {
        Env[Id] = evalExp(Exp);
      }

      case ifStat(EXP Exp, list[STATEMENT] Stats1,
                            list[STATEMENT] Stats2):{
        if(evalExp(Exp, Env) != intval(0)){
           evalStatements(Stats1);
        } else {
          evalStatements(Stats2);
        }
      }

      case whileStat(EXP Exp, list[STATEMENT] Stats1): {
        if(evalExp(Exp, Env) != intval(0)){
          evalStatements(Stats1);
          evalStatement(Stat);
        }
      }
    }
}

PICO_VALUE evalExp(Exp exp) {
    switch (exp) {
      case natCon(int N): 
           return intval(toInt(unparseToString(N)));

      case strCon(str S): 
           return strval(unparseToString(S));

      case id(PicoId Id): 
           return Env[Id];

      case add(EXP E1, EXP E2):
           if(intval(int n1) := evalExp(E1) &&
              intval(int n2) := evalExp(E1){
              return intval(n1 + n2);
           }
      
      case sub(EXP E1, EXP E2):
            if(intval(int n1) := evalExp(E1) &&
               intval(int n2) := evalExp(E1)){
               return intval(n1 - n2);
           }
 
      case conc(EXP E1, EXP E2):
           if(strval(str s1) := evalExp(E1) &&
              strval(str s2) := evalExp(E2)){
              return strval(s1 + s2);
           }
   } 
}
