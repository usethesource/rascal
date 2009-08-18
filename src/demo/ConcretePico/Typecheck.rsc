module demo::ConcretePico::Typecheck

import languages::pico::syntax::Pico;  // Pico concrete syntax
import demo::AbstractPico::Message;    // Error messages

import demo::ConcretePico::Programs;   // Example programs

import IO;
import UnitTest;

/*
 * Typechecker for Pico.
 */
 
// TypeEnv: Type environments that map PICO-IDs to their declared TYPE
// Note that we define TypeEnvs as an alias (= abbreviation) for the more complex 
// type map[\PICO-ID, TYPE] in order to avoid repeating that type.
// Also note that we write \PICO-ID since the character - is not valid in
// Rascal identifiers and type names (but it is in SDF).

alias TypeEnv = map[\PICO-ID, TYPE];

TYPE naturalType = TYPE[|natural|];     // Two useful constants
TYPE stringType  = TYPE[|string|];

// checkProgram: typecheck a Pico program and return a list of error messages

public list[Message] checkProgram(PROGRAM P) {
   if( [| begin declare <{\ID-TYPE "," }* Decls>; <{STATEMENT ";"}* Stats> end |] := P){
   
       // Collect all declarations and put them in a type environment
       TypeEnv Env = (Id : Type | [| <\PICO-ID Id> : <TYPE Type> |] <- Decls);
       
       // Use the type environment to typecheck the program
       return checkStatements(Stats, Env);
   }
   return [message("Malformed Pico program")];
}

public list[Message] checkStatements({STATEMENT ";"}* Stats, TypeEnv Env){
    // Collect all errors produced by typechecking the statements
    return [checkStatement(S, Env) | STATEMENT S <- Stats];
}

// checkStatement: typecheck a statement

public list[Message] checkStatement(STATEMENT Stat, TypeEnv Env) {
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
         return requireType(Exp, naturalType, Env) 
                + checkStatements(Stats1, Env) 
                + checkStatements(Stats2, Env);

      case [| while <EXP Exp> do <{STATEMENT ";"}* Stats> od |]:
         return requireType(Exp, naturalType, Env) 
                + checkStatements(Stats, Env);
    }
    return [message("Unknown statement: <Stat>")];
}

list[Message] OK = [];                 // The empty list of error messages

// requireType: expression E should be of type Type in given type environment Env
 
public list[Message] requireType(EXP E, TYPE Type, TypeEnv Env) {

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
  assertEqual(checkProgram([|begin declare x : natural; x := 3  end|]), []);
  
  assertEqual(checkProgram([|begin declare x : natural; y := "a"  end|]), 
                  [message("Undeclared variable y")]);
                  
  assertEqual(checkProgram([|begin declare x : natural; x := "a"  end|]), 
                  [message("Type error: expected natural got \"a\"")]);
                  
  assertEqual(checkProgram([|begin declare x : natural; x := 2 + "a"  end|]), 
                  [message("Type error: expected natural got \"a\"")]);
                  
  assertEqual(checkProgram(small), []);
  
  assertEqual(checkProgram(fac), []);
  
  assertEqual(checkProgram(big), []);
  
  return report("ConcretePico::Typecheck");
}

