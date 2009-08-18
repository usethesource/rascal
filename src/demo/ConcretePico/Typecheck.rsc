module demo::ConcretePico::Typecheck

import languages::pico::syntax::Pico;  // Pico concrete syntax
import demo::AbstractPico::Message;    // Error messages

import demo::ConcretePico::Programs;   // Example programs

import IO;
import UnitTest;

/*
 * Typechecker for Pico.
 */

alias Env = map[\PICO-ID, TYPE];        // Mapping from PICO-ID to TYPEs
                                        // Note that we write \PICO-ID since the character - is not valid in
                                        // Rascal identifiers and type names (but it is in SDF).

TYPE naturalType = TYPE[|natural|];     // Two useful constants
TYPE stringType  = TYPE[|string|];

// tcp: typecheck a Pico program and return a list of error messages

public list[Message] tcp(PROGRAM P) {
   if( [| begin declare <{\ID-TYPE "," }* Decls>; <{STATEMENT ";"}* Stats> end |] := P){
       // Collect all declarations and put them in a type environment
       Env Env = (Id : Type | [| <\PICO-ID Id> : <TYPE Type> |] <- Decls);
       // Use the type environment to typecheck the program
       return tcs(Stats, Env);
   }
   return [message("Malformed Pico program")];
}

public list[Message] tcs({STATEMENT ";"}* Stats, Env Env){
    // Collect all errors produced by typechecking the statements
    return [tcst(S, Env) | STATEMENT S <- Stats];
}

// tcst: typecheck a statement

public list[Message] tcst(STATEMENT Stat, Env Env) {
    switch (Stat) {
      case [| <\PICO-ID Id> := <EXP Exp> |]:
         if(Env[Id]?)
            return requireType(Exp, Env[Id], Env);
         else {
            return [message("Undeclared variable <Id>")];
         }

      case [| if <EXP Exp> then <{STATEMENT ";"}* Stats1> 
                           else <{STATEMENT ";"}* Stats2>
              fi |]:
         return requireType(Exp, naturalType, Env) + tcs(Stats1, Env) + tcs(Stats2, Env);

      case [| while <EXP Exp> do <{STATEMENT ";"}* Stats> od |]:
         return requireType(Exp, naturalType, Env) + tcs(Stats, Env);
    }
    return [message("Unknown statement: <Stat>")];
}

list[Message] OK = [];                 // The empty list of error messages

// requireType: expression E should be of type Type in given type environment Env
 
public list[Message] requireType(EXP E, TYPE Type, Env Env) {

    switch (E) {
      case EXP[|<NatCon N>|]: 
         if(Type == naturalType) return OK; else fail;

      case EXP[|<StrCon S>|]:
         if(Type == stringType) return OK; else fail;  

      case EXP[|<\PICO-ID Id>|]: {
         if(Env[Id]?){
            if(Env[Id] == Type){
        	   return OK;
            } else fail;
         } else
            return [message("Undeclared variable <Id>")];
      }

      case [| <EXP E1> + <EXP E2> |]:
         if(Type == naturalType){
            return requireType(E1, naturalType, Env) + 
                   requireType(E2, naturalType, Env);
         } else fail;

      case [| <EXP E1> - <EXP E2> |]:
         if(Type == naturalType){
            return requireType(E1, naturalType, Env) + 
                   requireType(E2, naturalType, Env);
         } else fail;

      case [| <EXP E1> || <EXP E2> |]: 
         if(Type == stringType){
            return requireType(E1, stringType, Env) + 
                   requireType(E2, stringType, Env);
         } else fail;
        
      default: {
         return [message("Type error: expected <Type> got <E>")];
      }
    } 
}

public bool test() {
  assertEqual(tcp([|begin declare x : natural; x := 3  end|]), []);
  
  assertEqual(tcp([|begin declare x : natural; y := "a"  end|]), 
                  [message("Undeclared variable y")]);
                  
  assertEqual(tcp([|begin declare x : natural; x := "a"  end|]), 
                  [message("Type error: expected natural got \"a\"")]);
                  
  assertEqual(tcp([|begin declare x : natural; x := 2 + "a"  end|]), 
                  [message("Type error: expected natural got \"a\"")]);
                  
  assertEqual(tcp(small), []);
  
  assertEqual(tcp(fac), []);
  
  assertEqual(tcp(big), []);
  
  return report("ConcretePico::Typecheck");
}

