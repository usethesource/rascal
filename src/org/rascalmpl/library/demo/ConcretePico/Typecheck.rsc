@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module demo::ConcretePico::Typecheck

import lang::pico::\syntax::Main;  // Pico concrete syntax
import demo::ConcretePico::Programs;   // Example programs

import IO;
import Message;
import ParseTree;

/*
 * Typechecker for Pico.
 */
 
// TypeEnv: Type environments that map Pico IDs to their declared TYPE
// Note that we define TypeEnvs as an alias (= abbreviation) for the more complex 
// type map[Id, Type] in order to avoid repeating that type.

alias TypeEnv = map[Id, Type];

Type naturalType = (Type)`natural`;     // Two useful constants
Type stringType  = (Type)`string`;

// checkProgram: typecheck a Pico program and return a list of error messages

public list[Message] checkProgram(Program P) {
   if( (Program) `begin declare <{IdType "," }* Decls>; <{Statement ";"}* Stats> end` := P){
   
       // Collect all declarations and put them in a type environment
       TypeEnv Env = (Id : Type | (IdType) `<Id Id> : <Type Type>` <- Decls);
       
       // Use the type environment to typecheck the program
       return checkStatements(Stats, Env);
   }
   return [error(P@\loc, "Malformed Pico program")];
}

public list[Message] checkStatements({Statement ";"}* Stats, TypeEnv Env){
    // Collect all errors produced by typechecking the statements
    return [checkStatement(S, Env) | Statement S <- Stats];
}

// checkStatement: typecheck a statement

public list[Message] checkStatement(Statement Stat, TypeEnv Env) {
    switch (Stat) {
      case (Statement) `<Id id>:=<Expression exp>`:
         if(Env[id]?)
            return requireType(exp, Env[id], Env);
         else {
            pos = Stat@\loc;
            return [error(Stat@\loc, "Undeclared variable <Id>")];
         }

      case (Statement) `if <Expression Exp> then <{Statement ";"}* Stats1> else <{Statement ";"}* Stats2> fi`:
         return requireType(Exp, naturalType, Env) 
                + checkStatements(Stats1, Env) 
                + checkStatements(Stats2, Env);

      case (Statement) `while <Expression Exp> do <{Statement ";"}* Stats> od`:
         return requireType(Exp, naturalType, Env) 
                + checkStatements(Stats, Env);
    }
    return [error(Stat@\loc, "Unknown statement: <Stat>")];
}

list[Message] OK = [];                 // The empty list of error messages

// requireType: expression E should be of type Type in given type environment Env
 
public list[Message] requireType(Expression E, Type Type, TypeEnv Env) {

    switch (E) {
      case (Expression)`<Natural N>`: 
         if(Type == naturalType) return OK; else fail;

      case (Expression)`<String S>`:
         if(Type == stringType) return OK; else fail;  

      case (Expression)`<Id Id>`: {
         if(Env[Id]?){
            if(Env[Id] == Type){
        	   return OK;
            } else fail;
         } else
            return [error(Id@\loc, "Undeclared variable <Id>")];
      }

      case (Expression) `<Expression E1> + <Expression E2>`:
         if(Type == naturalType){
            return requireType(E1, naturalType, Env) + 
                   requireType(E2, naturalType, Env);
         } else fail;

      case (Expression) `<Expression E1> - <Expression E2>`:
         if(Type == naturalType){
            return requireType(E1, naturalType, Env) + 
                   requireType(E2, naturalType, Env);
         } else fail;

      case (Expression) `<Expression E1> || <Expression E2>`: 
         if(Type == stringType){
            return requireType(E1, stringType, Env) + 
                   requireType(E2, stringType, Env);
         } else fail;
        
      default: {
         return [error(E@\loc, "Expected type <Type> but got <E>")];
      }
    } 
}

  
test checkProgram((Program) `begin declare x : natural; x := 3  end`) == [];  
 
test [/"Undeclared variable y"] := checkProgram((Program) `begin declare x : natural; y := "a"  end`);
  
test [/"Expected type natural but got \"a\""] := checkProgram((Program) `begin declare x : natural; x := "a"  end`); 
              
test [/"Expected type natural but got \"a\""] := checkProgram((Program) `begin declare x : natural; x := 2 + "a"  end`);
              
test checkProgram(small) == [];
  
test [/"Expected type natural but got \"abc\"", /"Expected type string but got 3"] := checkProgram(exampleTypeErrors); 
             
test checkProgram(fac) == [];
  
test checkProgram(big) == [];
