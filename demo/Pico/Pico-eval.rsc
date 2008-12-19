module Pico-eval
import pico/syntax/Pico;
 
data PICO_VALUE intval(int) | strval(str);

type map[PICO-ID, PICO_VALUE] VEnv;

VEnv evalProgram(PROGRAM P){
    switch (P) {
      case begin <DECLS Decls> <{STATEMENT ";"}* Series> end: {
          VEnv Env = evalDecls(Decls);
          return evalStatements(Series, Env);
      }
   }
}

VEnv evalDecls(DECLS Decls){
    VEnv Env = {};
    visit (Decls) {
      case <PICO-ID Id> : string: { 
           Env[Id] = strval(""); 
           return Env;
      }
      case <PICO-ID Id> : natural: { 
           Env[Id] = intval(0);  
           return Env;
      }
    };
    return Env;
}

VEnv evalStatements({STATEMENT ";"}* Series, VEnv Env){
    switch (Series) {
      case <STATEMENT Stat>; <{STATEMENT ";"}* Series2>: {
        Env Env2 = evalStatement(Stat, Env);
        return evalStatements(Series2, Env2);
      }
      default: return Env;
    }
}

VEnv evalStatement(STATEMENT Stat, VEnv Env){
    switch (Stat) {
      case [| <PICO-ID Id> = <EXP Exp> |]: {
        Env[Id] = evalExp(Exp, Env);
        return Env;
      }

      case if <EXP Exp> then <{STATEMENT ";"}* Stats1> 
                       else <{STATEMENT ";"}* Stats1> fi:{
        if(evalExp(Exp, Env) != intval(0)){
          return evalStatments(Stats1, Env);
        } else {
          return evalStatements(Stats2, Env);
        }
      }

      case while <EXP Exp> do <{STATEMENT ";"}* Stats1> od: {
        if(evalExp(Exp, Env) != intval(0)){
          return Env;
        } else {
          VEnv Env2 = evalStatements(Stats1, Env);
          return evalStatement(Stat, Env2);
        }
      }
      default: return Env;
    }
}

PICO_VALUE evalExp(Exp exp, VEnv Env) {
    switch (exp) {
      case NatCon[| <NatCon N> |]: 
           return intval(toInt(unparseToString(N)));

      case StrCon[| <StrCon S> |]: 
           return strval(unparseToString(S));

      case PICO-ID[| <PICO-ID Id> |]: 
           return Env[Id];

      case <EXP exp1> + <EXP exp2>:
           if(intval(int n1) ~~ evalExp(exp1, Env) &&
              intval(int n2) ~~ evalExp(exp2, Env)){
              return intval(n1 + n2);
           }
      
      case <EXP exp1> - <EXP exp2>:
            if(intval(int n1) ~~ evalExp(exp1, Env) &&
               intval(int n2) ~~ evalExp(exp2, Env)){
               return intval(n1 - n2);
           }
 
      case <EXP exp1> || <EXP exp2>:
           if(strval(str s1) ~~ evalExp(exp1, Env) &&
              strval(str s2) ~~ evalExp(exp2, Env)){
              return strval(s1 + s2);
           }
   } 
}
