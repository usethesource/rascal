module demo::PicoConcrete::PicoTypecheck

import languages/pico/syntax/Pico;
import demo::PicoAbstract::Message;
import IO;
import UnitTest;

/*
 * Typechecker for Pico.
 */

alias Env = map[PicoId,TYPE];

public list[Message] tcp(PROGRAM P) {
    if([| begin <DECLS Decls> <{STATEMENT ";"}* Stats> end |] := P){
           Env Env = (Id : Type | [| <PicoId Id> : <TYPE Type> |] <- Decls);
           return tcs(Stats, Env);
    }
    return [message("Malformed Pico program")];
}

public list[Message] tcs(list[STATEMENT] Stats, Env Env){
    list[Message] messages = [];
    for(STATEMENT S <- Stats){
    	messages = [messages, tcst(S, Env)];
    }
    return messages;
}

public list[Message] tcst(STATEMENT Stat, Env Env) {
    switch (Stat) {
      case [| <PicoId Id> : <EXP Exp>):
        return requireType(Exp, Env[Id], Env);  // TODO: undefined variable

      case [| if <EXP Exp> then <{STATEMENT ";"}* Stats1> 
                           else <{STATEMENT ";"}* Stats1>
              fi |]:
        return requireType(Exp, natural, Env) + tcs(Stats1, Env) + tcs(Stats2, Env);

      case [| while <EXP Exp> do <{STATEMENT ";"}* Stats1> od |]:
        return requireType(Exp, natural, Env) + tcs(Stats, Env);
    }
    return [message("Unknown statement: <Stat>")];
}
 
public list[Message] requireType(EXP E, TYPE Type, Env Env) {
    switch (E) {
      case NatCon N: if(Type == natural){ return []; } else fail;

      case StrCon S: if(Type == string) { return []; } else fail;

      case PicoId Id: {
         TYPE Type2 = Env[Id];
         if(Type2 == Type) { return []; } else fail;
      }

      case [| <EXP E1> + <EXP E2> |]:
        if(Type == natural){
           return requireType(E1, natural, Env) + 
                  requireType(E2, natural, Env);
        } else fail;

      case [| <EXP E1> - <EXP E2> |]:
        if(Type == natural){
           return requireType(E1, natural, Env) + 
                  requireType(E2, natural, Env);
        } else fail;

      case [| <EXP E1> || <EXP E2> |]: 
        if(Type == string){
          return requireType(E1, string, Env) + 
                 requireType(E2, string, Env);
        } else fail;
      
      }
    
      return [message("Type error: expected <Type> got <E>")];
}

public bool test(){

	assertEqual(requireType(natCon(3), natural, ()), []);
	assertEqual(requireType(strCon("a"), string, ()), []);
	assertEqual(requireType(id("x"), string, ("x" : string)), []);
	assertEqual(requireType(id("x"), string, ("x" : natural)), [message("Type error: expected string() got id(\"x\")")]);

   PROGRAM mySmall =
   program([decl("x", natural), decl("s", string)],
        [ asgStat("x", natCon(1)) ,
         whileStat(id("x"),
                    [ asgStat("x", sub(id("x"), natCon(1))),
                      asgStat("s", conc(id("s"), strCon("#")))
                    ]
                   ) 
        ]
       );
  assertEqual(tcp(mySmall), []);
  assertEqual(tcp(fac), []);
  assertEqual(tcp(big), []);
  return report("PicoTypecheck");
}