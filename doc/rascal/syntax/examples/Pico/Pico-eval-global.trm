module Pico-eval

import pico/syntax/Pico;

data PICO_VALUE intval(int) | strval(str);

type map[PICO-ID, PICO_VALUE] VEnv;

VEnv Env = {};

void evalProgram(PROGRAM P){
    switch(P) {
      case begin <DECLS Decls> <{STATEMENT ";"}* Series> end: {
          evalDecls(Decls);
          evalStatements(Series);
      }
   }
}

VEnv evalDecls(DECLS Decls){
    global Venv Env;
    visit (Decls) {
      case <PICO-ID Id> : string:  Env[Id] = strval("");
      case <PICO-ID Id> : natural: Env[Id] = intval(0);
    };
    return Env;
}

void evalStatements({STATEMENT ";"}* Series){
    switch (Series) {
      case <STATEMENT Stat>; <{STATEMENT ";"}* Series2>: {
        evalStatement(Stat);
        evalStatements(Series2);
        return;
      }
      default: return;
    }
}

void evalStatement(STATEMENT Stat){
    global Venv Env;
    switch (Stat) {
      case [| <PICO-ID Id> = <EXP Exp> |]: {
        Env[Id] = evalExp(Exp);
        return;
      }

      case if <EXP Exp> then <{STATEMENT ";"}* Stats1> 
                   else <{STATEMENT ";"}* Stats1> fi:{
        if(evalExp(Exp) != intval(0)) {
          evalStatements(Stats1);
          return;
        } else {
          evalStatements(Stats2);
          return;
        }
      }

      case while <EXP Exp> do <{STATEMENT ";"}* Stats1> od:{
        if(evalExp(Exp) != intval(0)){
          return;
        } else {
          evalStatements(Stats1);
          evalStatement(Stat);
          return;
        }
      }
    }
};

PICO_VALUE evalExp(Exp exp) {
    global Venv Env;
    switch (exp) {
      case <NatCon N>: intval(toInt(unparseToString(N)));

      case <StrCon S>: return strval(unparseToString(S));

      case <PICO-ID Id>: return Env[Id];

      case <EXP exp1> + <EXP exp2>:
           if(intval(int n1) ~~ evalExp(exp1) &&
              intval(int n2) ~~ evalExp(exp2)){
              return intval(n1 + n2);
           }

      case <EXP exp1> - <EXP exp2>:
           if(intval(int n1) ~~ evalExp(exp1) &&
              intval(int n2) ~~ evalExp(exp2)){
              return intval(n1 - n2);
           }
   
      case <EXP exp1> || <EXP exp2>:
           if(strval(str s1) ~~ evalExp(exp1) &&
              strval(str s2) ~~ evalExp(exp2)){
              return strval(s1 + s2);
           }
   } 
}
