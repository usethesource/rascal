module PicoTypecheck

import PicoAbstractSyntax;
import Error;
import IO;

alias Env = map[PicoId,TYPE];

list[Error] tcp(PROGRAM P) {
    switch (P) {
      case program(list[DECL] Decls, list[STATEMENT] Series): {
           Env Env = (Id : Type | decl(PicoId Id, TYPE Type): Decls);
           return [ tcst(S, Env) | STATEMENT S : Series ];
      }
    }
    return [];
}

list[Error] tcst(STATEMENT Stat, Env Env) {
    switch (Stat) {
      case asgStat(PicoId Id, EXP Exp): {
        TYPE Type = Env[Id];
        return type_of(Exp, Type, Env);
      }

      case ifStat(EXP Exp, list[STATEMENT] Stats1,
                            list[STATEMENT] Stats2):
        return type_of(Exp, natural, Env) + 
               tcs(Stats1, Env) + tcs(Stats2, Env);

      case whileStat(EXP Exp, list[STATEMENT] Stats1): 
        return type_of(Exp, natural, Env) + tcs(Stats, Env);
    }
    return [];
}
 
list[Error] type_of(EXP E, TYPE Type, Env Env) {
    switch (E) {
      case natCon(int N): if(Type == natural){ return []; }

      case strCon(str S): if(Type == string) { return []; }

      case id(PicoId Id): {
         TYPE Type2 = Env(Id);
         if(Type2 == Type) { return []; }
      }

      case add(EXP E1, EXP E2):
        if(Type == natural){
           return type_of(E1, natural, Env) + 
                  type_of(E1, natural, Env);
        }

      case sub(EXP E1, EXP E2):
        if(Type == natural){
           return type_of(E1, natural, Env) + 
                  type_of(E1, natural, Env);
        }

      case conc(EXP E1, EXP E2): 
        if(Type == string){
          return type_of(E1, string, Env) + 
                 type_of(E1, string, Env);
        }
    
      default: return [error("Incorrect type")];
    }
}

public void test(){
   PROGRAM small =
   program([decl("x", natural), decl("y", string)],
        [ asgStat("x", natCon(1)),
          whileStat(id("x"),
                    [ asgStat("x", sub(id("x"), natCon(1))),
                      asgStat("s", conc(id("s"), strCon("#")))
                    ]
                   )
        ]
       );
   list[Error] errors = tcp(small);
   println("Errors:\n <errors>");
}