module demo::ConcretePico::Eval

import languages::pico::syntax::Pico;
import demo::ConcretePico::Programs;

import IO;
import UnitTest;

/*
 * Evaluator for Pico
 */
 
data PICO_VALUE = intval(int ival) | strval(str sval);

alias VEnv = map[\PICO-ID, PICO_VALUE];

public VEnv evalProgram(PROGRAM P){

   if( [| begin declare <{\ID-TYPE "," }* Decls>; <{STATEMENT ";"}* Stats> end |] := P){
       VEnv Env = evalDecls(Decls);
       println("Declaration env:", Env);
       return evalStatements(Stats, Env);
   }
}

VEnv evalDecls({\ID-TYPE "," }* Decls){
    VEnv Env = ();
    println("evalDecls: ", Decls);
    for([|<\PICO-ID Id> : <TYPE Type>|] <- Decls){
        println("in loop", Id);
        Env[Id] = (Type == [|natural|]) ? intval(0) : strval(""); 
    }
    return Env;
}

VEnv evalStatements({STATEMENT ";"}* Series, VEnv Env){
    for(STATEMENT Stat <- Series){
        Env = evalStatement(Stat, Env);
    }
    return Env;
}

VEnv evalStatement(STATEMENT Stat, VEnv Env){
    println(Stat);
    switch (Stat) {
      case [| <\PICO-ID Id> := <EXP Exp> |]: {
        Env[Id] = evalExp(Exp, Env);
        return Env;
      }

      case [| if <EXP Exp> then <{STATEMENT ";"}* Stats1> 
                       else <{STATEMENT ";"}* Stats2> fi |]:{
        if(evalExp(Exp, Env) != intval(0)){
          return evalStatments(Stats1, Env);
        } else {
          return evalStatements(Stats2, Env);
        }
      }

      case [| while <EXP Exp> do <{STATEMENT ";"}* Stats> od |]: {
        if(evalExp(Exp, Env) != intval(0)){
          return Env;
        } else {
          VEnv Env2 = evalStatements(Stats, Env);
          return evalStatement(Stat, Env2);
        }
      }
      default: return Env;
    }
}

PICO_VALUE evalExp(EXP exp, VEnv Env) {
    switch (exp) {
      case [| <NatCon N> |]: 
           return intval(toInt(unparseToString(N)));

      case [| <StrCon S> |]: 
           return strval(unparseToString(S));

      case [| <\PICO-ID Id> |]: 
           return Env[Id];

      case <EXP exp1> + <EXP exp2>:
           if(intval(int n1) := evalExp(exp1, Env) &&
              intval(int n2) := evalExp(exp2, Env)){
              return intval(n1 + n2);
           } else fail;
      
      case <EXP exp1> - <EXP exp2>:
            if(intval(int n1) := evalExp(exp1, Env) &&
               intval(int n2) := evalExp(exp2, Env)){
               return intval(n1 - n2);
           } else fail;
 
      case <EXP exp1> || <EXP exp2>:
           if(strval(str s1) := evalExp(exp1, Env) &&
              strval(str s2) := evalExp(exp2, Env)){
              return strval(s1 + s2);
           } else fail;
   } 
}

public bool test(){
   println(evalProgram(small));
   println(evalProgram(fac));
   return true;
}
