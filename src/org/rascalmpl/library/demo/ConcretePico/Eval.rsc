@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::ConcretePico::Eval

import lang::pico::\syntax::Main;   // Pico concrete syntax
import demo::ConcretePico::Programs;    // Example programs

import String;
import IO;
import String;
import Exception;
import Benchmark;

/*
 * Evaluator for Pico
 */

// PICO_VALUE: the runtime representation of Pico values

data PICO_VALUE = intval(int ival) | strval(str sval);

// ValueEnv: Value environments that map PICO-IDs to their runtime PICO_VALUE
// Note that we define ValueEnv as an alias (= abbreviation) for the more complex 
// type map[\PICO-ID, PICO_VALUE] in order to avoid repeating that type.
// Also note that we write \PICO-ID since the character - is not valid in
// Rascal identifiers and type names (but it is in SDF).

alias ValueEnv = map[Id, PICO_VALUE];                                   

Type naturalType = (Type) `natural`;     // Two useful constants
Type stringType  = (Type) `string`;

// evalProgram: evaluate a Pico program and return a value environment

public ValueEnv evalProgram(Program P){

   if( `begin declare <{IdType "," }* Decls>; <{Statement ";"}* Stats> end` := P){

       return evalStatements(Stats, evalDecls(Decls));
   } else
       throw IllegalArgument(P);
}

// evalDecls: evaluate all declaration and retrun a value environment with default initalizations

ValueEnv evalDecls({IdType "," }* Decls){
    ValueEnv Env = ();
    
    for(`<Id i> : <Type t>` <- Decls) {
        Env[id] = (t == naturalType) ? intval(0) : strval(""); 
    }
    return Env;
}

ValueEnv evalStatements({Statement ";"}* Series, ValueEnv Env){
    for(Statement Stat <- Series){
        Env = evalStatement(Stat, Env);
    }
    return Env;
}

ValueEnv evalStatement(Statement Stat, ValueEnv Env){
    switch (Stat) {
      case `<Id id> := <Expression exp>`: {
        Env[id] = evalExp(exp, Env);
        return Env;
      }

      case `if <Expression exp> then <{Statement ";"}* Stats1> 
                         else <{Statement ";"}* Stats2> fi`:{
        if(evalExp(exp, Env) == intval(0)){
          return evalStatments(Stats1, Env);
        } else {
          return evalStatements(Stats2, Env);
        }
      }

      case `while <Expression exp> do <{Statement ";"}* Stats> od`: {
        if(evalExp(exp, Env) == intval(0)){
          return Env;
        } else {
          ValueEnv Env2 = evalStatements(Stats, Env);
          return evalStatement(Stat, Env2);
        }
      }
      default: return Env;
    }
}

// evalExp: evaluate an expression and return its value

PICO_VALUE evalExp(Expression exp, ValueEnv Env) {
    switch (exp) {
      case (Expression) `<Natural N>`: 
           return intval(toInt("<N>"));

      case (Expression) `<String S>`: {
           if(/"<sval:.*>"/ := "<S>")
              return strval(sval);
           else
              println("Ill formed string value");
      }

      case (Expression) `<Id id>`: 
           return Env[id];

      case `<Expression exp1> + <Expression exp2>`:
           if(intval(int n1) := evalExp(exp1, Env) &&
              intval(int n2) := evalExp(exp2, Env)){
              return intval(n1 + n2);
           } else fail;
      
      case `<Expression exp1> - <Expression exp2>`:
            if(intval(int n1) := evalExp(exp1, Env) &&
               intval(int n2) := evalExp(exp2, Env)){
               return intval(n1 - n2);
           } else fail;
 
      case `<Expression exp1> || <Expression exp2>`:
           if(strval(str s1) := evalExp(exp1, Env) &&
              strval(str s2) := evalExp(exp2, Env)){
              return strval(s1 + s2);
           } else fail;
   } 
}

public void reports(){
		s = currentTimeMillis();
		E = evalProgram(fac);
		//assert E[`output`] == intval(3628800);
		println("fac: <currentTimeMillis() - s> millis");
}

public void tst(){
 println("********** TST ***********");
 if(` begin declare <{(Id ":" Type) "," }* Decls>; <{Statement ";"}* Stats> end ` := `begin declare x : natural; x := 10 end`)
   println("Decl = <Decls>, Stats = <Stats>");
 else
   println("fails");
}

test E := evalProgram(`begin declare x : natural; x := 10 end`) && E[`x`] == intval(10);
   
test E := evalProgram(`begin declare x : natural, y : natural; x := 10; y := x + 1 end`) && E[`x`] == intval(10) && E[`y`] == intval(11);
   
test E := evalProgram(`begin declare x : string, y : string; x := "a"; y := x || "b" end`) && E[`x`] == strval("a") && E[`y`] == strval("ab");
   
test E := evalProgram(small) && E[`s`] == strval("##########");
  
  
// TMP fail, since fac(50) is computed in example program
test E := evalProgram(fac) && E[`output`] == intval(3628800);

