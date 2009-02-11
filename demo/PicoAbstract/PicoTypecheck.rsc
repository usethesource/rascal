module PicoTypecheck

import PicoAbstractSyntax;
import Error;
import IO;
import PicoPrograms;
import UnitTest;

alias Env = map[PicoId,TYPE];

public list[Error] tcp(PROGRAM P) {
    if(program(list[DECL] Decls, list[STATEMENT] Stats) := P){
           Env Env = (Id : Type | decl(PicoId Id, TYPE Type): Decls);
           return tcs(Stats, Env);
    }
    return [error("Malformed Pico program")];
}

public list[Error] tcs(list[STATEMENT] Stats, Env Env){
    list[Error] errors = [];
    for(STATEMENT S : Stats){
    	errors = [errors, tcst(S, Env)];
    }
    return errors;
}

public list[Error] tcst(STATEMENT Stat, Env Env) {
    switch (Stat) {
      case asgStat(PicoId Id, EXP Exp):
        return requireType(Exp, Env[Id], Env);  // TODO: undefined variable

      case ifStat(EXP Exp, list[STATEMENT] Stats1, list[STATEMENT] Stats2):
        return requireType(Exp, natural, Env) + tcs(Stats1, Env) + tcs(Stats2, Env);

      case whileStat(EXP Exp, list[STATEMENT] Stats): 
        return requireType(Exp, natural, Env) + tcs(Stats, Env);
    }
    return [error("Unknown statement: <Stat>")];
}
 
public list[Error] requireType(EXP E, TYPE Type, Env Env) {
    switch (E) {
      case natCon(int N): if(Type == natural){ return []; } else fail;

      case strCon(str S): if(Type == string) { return []; } else fail;

      case id(PicoId Id): {
         TYPE Type2 = Env[Id];
         if(Type2 == Type) { return []; } else fail;
      }

      case add(EXP E1, EXP E2):
        if(Type == natural){
           return requireType(E1, natural, Env) + 
                  requireType(E1, natural, Env);
        } else fail;

      case sub(EXP E1, EXP E2):
        if(Type == natural){
           return requireType(E1, natural, Env) + 
                  requireType(E1, natural, Env);
        } else fail;

      case conc(EXP E1, EXP E2): 
        if(Type == string){
          return requireType(E1, string, Env) + 
                 requireType(E1, string, Env);
        } else fail;
      
      }
    
      return [error("Type error: expected <Type> got <E>")];
}

public bool test(){

	assertEqual(requireType(natCon(3), natural, ()), []);
	assertEqual(requireType(strCon("a"), string, ()), []);
	assertEqual(requireType(id("x"), string, ("x" : string)), []);
	assertEqual(requireType(id("x"), string, ("x" : natural)), [error("Type error: expected string() got id(\"x\")")]);

   PROGRAM small =
   program([decl("x", natural), decl("s", string)],
        [ asgStat("x", natCon(1)) ,
         whileStat(id("x"),
                    [ asgStat("x", sub(id("x"), natCon(1))),
                      asgStat("s", conc(id("s"), strCon("#")))
                    ]
                   ) 
        ]
       );
  assertEqual(tcp(small), []);
  assertEqual(tcp(fac), []);
  assertEqual(tcp(big), []);
  return report();
}