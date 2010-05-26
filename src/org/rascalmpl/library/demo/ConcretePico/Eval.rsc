module demo::ConcretePico::Eval

import languages::pico::syntax::Pico;   // Pico concrete syntax
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

alias ValueEnv = map[\PICO-ID, PICO_VALUE];                                   

TYPE naturalType = (TYPE) `natural`;     // Two useful constants
TYPE stringType  = (TYPE) `string`;

// evalProgram: evaluate a Pico program and return a value environment

public ValueEnv evalProgram(PROGRAM P){

   if( ` begin declare <{\ID-TYPE "," }* Decls>; <{STATEMENT ";"}* Stats> end ` := P){

       return evalStatements(Stats, evalDecls(Decls));
   } else
       throw IllegalArgument(P);
}

// evalDecls: evaluate all declaration and retrun a value environment with default initalizations

ValueEnv evalDecls({\ID-TYPE "," }* Decls){
    ValueEnv Env = ();
    
    for(`<\PICO-ID Id> : <TYPE Type>` <- Decls){
        Env[Id] = (Type == naturalType) ? intval(0) : strval(""); 
    }
    return Env;
}

ValueEnv evalStatements({STATEMENT ";"}* Series, ValueEnv Env){
    for(STATEMENT Stat <- Series){
        Env = evalStatement(Stat, Env);
    }
    return Env;
}

ValueEnv evalStatement(STATEMENT Stat, ValueEnv Env){
    switch (Stat) {
      case `<\PICO-ID Id> := <EXP Exp>`: {
        Env[Id] = evalExp(Exp, Env);
        return Env;
      }

      case `if <EXP Exp> then <{STATEMENT ";"}* Stats1> 
                         else <{STATEMENT ";"}* Stats2> fi`:{
        if(evalExp(Exp, Env) == intval(0)){
          return evalStatments(Stats1, Env);
        } else {
          return evalStatements(Stats2, Env);
        }
      }

      case `while <EXP Exp> do <{STATEMENT ";"}* Stats> od`: {
        if(evalExp(Exp, Env) == intval(0)){
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

PICO_VALUE evalExp(EXP exp, ValueEnv Env) {
    switch (exp) {
      case (EXP) `<NatCon N>`: 
           return intval(toInt("<N>"));

      case (EXP) `<StrCon S>`: {
           if(/"<sval:.*>"/ := "<S>")
              return strval(sval);
           else
              println("Ill formed string value");
      }

      case (EXP) `<\PICO-ID Id>`: 
           return Env[Id];

      case `<EXP exp1> + <EXP exp2>`:
           if(intval(int n1) := evalExp(exp1, Env) &&
              intval(int n2) := evalExp(exp2, Env)){
              return intval(n1 + n2);
           } else fail;
      
      case `<EXP exp1> - <EXP exp2>`:
            if(intval(int n1) := evalExp(exp1, Env) &&
               intval(int n2) := evalExp(exp2, Env)){
               return intval(n1 - n2);
           } else fail;
 
      case `<EXP exp1> || <EXP exp2>`:
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
 if(` begin declare <{\ID-TYPE "," }* Decls>; <{STATEMENT ";"}* Stats> end ` := `begin declare x : natural; x := 10 end`)
   println("Decl = <Decls>, Stats = <Stats>");
 else
   println("fails");
}

test E := evalProgram(`begin declare x : natural; x := 10 end`) && E[`x`] == intval(10);
   
test E := evalProgram(`begin declare x : natural, y : natural; x := 10; y := x + 1 end`) && E[`x`] == intval(10) && E[`y`] == intval(11);
   
test E := evalProgram(`begin declare x : string, y : string; x := "a"; y := x || "b" end`) && E[`x`] == strval("a") && E[`y`] == strval("ab");
   
test E := evalProgram(small) && E[`s`] == strval("##########");
   
test E := evalProgram(fac) && E[`output`] == intval(3628800);

