module Typecheck

import Pico-syntax;
import Errors;

type map[PICO-ID,TYPE] Env;

list[Error] tcp(PROGRAM P) {
    switch (P) {
      case begin <DECLS Decls> <{STATEMENT ";"}* Series> end: {
           Env Env = {<Id, Type> | 
                      [| <PICO-ID Id> : <TYPE Type> |] : Decls};
           return [ tcst(S, Env) | Stat S : Series ];
      }
    }
    return [];
}

list[Error] tcst(Stat Stat, Env Env) {
    switch (Stat) {
      case [| <PICO-ID Id> = <EXP Exp>|]: {
        TYPE Type = Env[Id];
        return type_of(Exp, Type, Env);
      }

      case if <EXP Exp> then <{STATEMENT ";"}* Stats1> 
                   else <{STATEMENT ";"}* Stats1> fi:
        return type_of(Exp, natural, Env) + 
               tcs(Stats1, Env) + tcs(Stats2, Env);

      case while <EXP Exp> do <{STATEMENT ";"}* Stats1> od: 
        return type_of(Exp, natural, Env) + tcs(Stats, Env);
    }
    return [];
}
 
list[Error] type_of(Exp E, TYPE Type, Env Env) {
    switch (E) {
      case <NatCon N>: if(Type == natural){ return []; }

      case <StrCon S>: if(Type == string) { return []; }

      case <PICO-ID Id>: {
         TYPE Type2 = Env(Id);
         if(Type2 == Type) { return []; }
      }

      case <EXP E1> + <EXP E2>:
        if(Type == natural){
           return type_of(E1, natural, Env) + 
                  type_of(E1, natural, Env);
        }

      case <EXP E1> - <EXP E2>:
        if(Type == natural){
           return type_of(E1, natural, Env) + 
                  type_of(E1, natural, Env);
        }

      case <EXP E1> || <EXP E2>: 
        if(Type == string){
          return type_of(E1, string, Env) + 
                 type_of(E1, string, Env);
        }
    
      default: return [error("Incorrect type")];
    }
}