module demo::ConcretePico::Typecheck

import demo::ConcretePico::Programs;
import languages::pico::syntax::Pico;

//import demo::AbstractPico::Message;
import IO;
import UnitTest;

public data Message = message(str msg);
/*
 * Typechecker for Pico.
 */

alias Env = map[\PICO-ID, TYPE];


public list[Message] tcp(PROGRAM P) {
   if( [| begin declare <{\ID-TYPE "," }+ Decls>; <{STATEMENT ";"}+ Stats> end |] := P){  // TODO: + +
       println("tcp: ", Decls);
       println("tcp: Decls=<Decls>\nStats=<Stats>");
       Env Env = (Id : Type | [| <\PICO-ID Id> : <TYPE Type> |] <- Decls);
       println("Env = ", Env);
       return tcs(Stats, Env);
   }
   return [message("Malformed Pico program")];
}

public list[Message] tcs({STATEMENT ";"}+ Stats, Env Env){              // TODO: +
    return [tcst(S, Env) | STATEMENT S <- Stats];
}

public list[Message] tcst(STATEMENT Stat, Env Env) {
    println("tcst: ", Stat);
    switch (Stat) {
      case [| <\PICO-ID Id> := <EXP Exp> |]:
        if(Env[Id]?)
        	return requireType(Exp, Env[Id], Env);
        else {
            m = message("abc"); println(m);
            return [message("Undeclared variable <Id>")];
            }

      case [| if <EXP Exp> then <{STATEMENT ";"}* Stats1> 
                           else <{STATEMENT ";"}* Stats2>
              fi |]:
        return requireType(Exp, [|natural|], Env) + tcs(Stats1, Env) + tcs(Stats2, Env);

      case [| while <EXP Exp> do <{STATEMENT ";"}* Stats> od |]:
        return requireType(Exp, TYPE [|natural|], Env) + tcs(Stats, Env);
    }
    return [message("Unknown statement: <Stat>")];
}
 
public list[Message] requireType(EXP E, TYPE Type, Env Env) {
    switch (E) {
      case EXP[|<NatCon N>|]: if(Type == [|natural|]){ return []; } else fail;

      case EXP[|<StrCon S>|]: if(Type == [|string|]) 
                        { return []; }
                     else {
                         println("StrCOn case is failing");
                         fail;
                     }

      case EXP[|<\PICO-ID Id>|]: {
         TYPE Type2 = Env[Id];
         println("Type2 = <Type2>");
         if(Type2 == Type) { return []; } else fail;
      }

      case EXP[| <EXP E1> + <EXP E2> |]:
        if(Type == [|natural|]){
           return requireType(E1, [|natural|], Env) + 
                  requireType(E2, [|natural|], Env);
        } else fail;

      case EXP[| <EXP E1> - <EXP E2> |]:
        if(Type == [|natural|]){
           return requireType(E1, [|natural|], Env) + 
                  requireType(E2, [|natural|], Env);
        } else fail;

      case EXP[| <EXP E1> || <EXP E2> |]: 
        if(Type == [|string|]){
          return requireType(E1, [|string|], Env) + 
                 requireType(E2, [|string|], Env);
        } else fail;
        
      default: {
        return [message("Type error: expected <Type> got <E>")];
       }
    }
    
}

public bool test() {
  assertEqual(tcp([|begin declare x : natural; y := "a"  end|]), message("Undeclared variable y"));
  assertEqual(tcp([|begin declare x : natural; x := "a"  end|]), message("Type error: expected int got str"));
  assertEqual(tcp(small), []);
  assertEqual(tcp(fac), []);
  assertEqual(tcp(big), []);
  
  return report("ConcretePico::Typecheck");
}

