module demo::ConcretePico::Typecheck

import zoo::pico::syntax::Main;  // Pico concrete syntax
//import demo::ConcretePico::Programs;   // Example programs

import IO;
import Message;
import ParseTree;

/*
 * Typechecker for Pico.
 */
 
// TypeEnv: Type environments that map Pico IDs to their declared TYPE
// Note that we define TypeEnvs as an alias (= abbreviation) for the more complex 
// type map[Id, Type] in order to avoid repeating that type.

alias TypeEnv = map[ID, TYPE];

TYPE naturalType = (TYPE)`natural`;     // Two useful constants
TYPE stringType  = (TYPE)`string`;

// checkProgram: typecheck a Pico program and return a list of error messages

public list[Message] checkProgram(PROGRAM P) {
   if( (PROGRAM) `begin declare <{IDTYPE "," }* Decls>; <{STATEMENT ";"}* Stats> end` := P){
   
       // Collect all declarations and put them in a type environment
       TypeEnv Env = (Id : Type | (IDTYPE) `<ID Id> : <TYPE Type>` <- Decls);
       
       // Use the type environment to typecheck the program
       return checkStatements(Stats, Env);
   }
   return [error(P@\loc, "Malformed Pico program")];
}

public list[Message] checkStatements({STATEMENT ";"}* Stats, TypeEnv Env){
    // Collect all errors produced by typechecking the statements
    return [checkStatement(S, Env) | STATEMENT S <- Stats];
}

// checkStatement: typecheck a statement

public list[Message] checkStatement(STATEMENT Stat, TypeEnv Env) {
    switch (Stat) {
      case (STATEMENT) `<ID id>:=<EXP exp>`:
         if(Env[id]?)
            return requireType(Exp, Env[id], Env);
         else {
            pos = Stat@\loc;
            return [error(Stat@\loc, "Undeclared variable <Id>")];
         }

      case (STATEMENT) `if <EXP Exp> then <{STATEMENT ";"}* Stats1> else <{STATEMENT ";"}* Stats2> fi`:
         return requireType(Exp, naturalType, Env) 
                + checkStatements(Stats1, Env) 
                + checkStatements(Stats2, Env);

      case (STATEMENT) `while <EXP Exp> do <{STATEMENT ";"}* Stats> od`:
         return requireType(Exp, naturalType, Env) 
                + checkStatements(Stats, Env);
    }
    return [error(Stat@\loc, "Unknown statement: <Stat>")];
}

list[Message] OK = [];                 // The empty list of error messages

// requireType: expression E should be of type Type in given type environment Env
 
public list[Message] requireType(EXP E, TYPE Type, TypeEnv Env) {

    switch (E) {
      case (EXP)`<NAT N>`: 
         if(Type == naturalType) return OK; else fail;

      case (EXP)`<STR S>`:
         if(Type == stringType) return OK; else fail;  

      case (EXP)`<ID Id>`: {
         if(Env[Id]?){
            if(Env[Id] == Type){
        	   return OK;
            } else fail;
         } else
            return [error(Id@\loc, "Undeclared variable <Id>")];
      }

      case (EXP) `<EXP E1> + <EXP E2>`:
         if(Type == naturalType){
            return requireType(E1, naturalType, Env) + 
                   requireType(E2, naturalType, Env);
         } else fail;

      case (EXP) `<EXP E1> - <EXP E2>`:
         if(Type == naturalType){
            return requireType(E1, naturalType, Env) + 
                   requireType(E2, naturalType, Env);
         } else fail;

      case (EXP) `<EXP E1> || <EXP E2>`: 
         if(Type == stringType){
            return requireType(E1, stringType, Env) + 
                   requireType(E2, stringType, Env);
         } else fail;
        
      default: {
         return [error(E@\loc, "Expected type <Type> but got <E>")];
      }
    } 
}

  
test checkProgram((PROGRAM) `begin declare x : natural; x := 3  end`) == [];  
 
test [/"Undeclared variable y"] := checkProgram((PROGRAM) `begin declare x : natural; y := "a"  end`);
  
test [/"Expected type natural but got \"a\""] := checkProgram((PROGRAM) `begin declare x : natural; x := "a"  end`); 
              
test [/"Expected type natural but got \"a\""] := checkProgram((PROGRAM) `begin declare x : natural; x := 2 + "a"  end`);
              
test checkProgram(small) == [];
  
test [/"Expected type natural but got \"abc\"", /"Expected type string but got 3"] := checkProgram(exampleTypeErrors); 
             
test checkProgram(fac) == [];
  
test checkProgram(big) == [];