module demo::AbstractPico::Eval

import demo::AbstractPico::AbstractSyntax;
import demo::AbstractPico::Programs;
import IO;

/*
 * Evaluator for Pico.
 */
 
 // Define the run-time representation of values
 
data PicoValue = intval(int i) | strval(str s);

// Define the global execution state as a map from identifiers to values

alias VEnv = map[PicoId, PicoValue];

// The global environment

public VEnv Env = ();

// The actual evaluation functions

public void evalProgram(PROGRAM P){
    Env = ();
    switch (P) {
      case program(list[DECL] Decls, list[STATEMENT] Series): {
           evalDecls(Decls);
           evalStatements(Series);
      }
   }
}

void evalDecls(list[DECL] Decls){
    visit (Decls) {
      case decl(PicoId Id, string()): { 
           println("adding <Id> : string");
           Env[Id] = strval(""); 
      }
      case decl(PicoId Id, natural()): {
           println("adding <Id> : natural") ;
           Env[Id] = intval(0);  
      }
    };
}

void evalStatements(list[STATEMENT] Series){
    for(STATEMENT Stat <- Series){
        evalStatement(Stat);
    }
}

void evalStatement(STATEMENT Stat){
    switch (Stat) {
      case asgStat(PicoId Id, EXP Exp): {
        Env[Id] = evalExp(Exp);
      }

      case ifStat(EXP Exp, list[STATEMENT] Stats1,
                            list[STATEMENT] Stats2):{
        if(evalExp(Exp) != intval(0)){
           evalStatements(Stats1);
        } else {
          evalStatements(Stats2);
        }
      }

      case whileStat(EXP Exp, list[STATEMENT] Stats1): {
        if(evalExp(Exp) != intval(0)){
          evalStatements(Stats1);
          evalStatement(Stat);
        }
      }
    }
}

PicoValue evalExp(EXP exp) {
    switch (exp) {
      case natCon(int N): 
           return intval(N);

      case strCon(str S): 
           return strval(S);

      case id(PicoId Id): 
           return Env[Id];

      case add(EXP E1, EXP E2):
           if((intval(int n1) := evalExp(E1)) && intval(int n2) := evalExp(E2)) {
                return intval(n1 + n2);
           }
      
      case sub(EXP E1, EXP E2):
            if((intval(int n1) := evalExp(E1)) && intval(int n2) := evalExp(E2)){
               return intval(n1 - n2);
           }
 
      case conc(EXP E1, EXP E2):
           if((strval(str s1) := evalExp(E1)) && strval(str s2) := evalExp(E2)){
              return strval(s1 + s2);
           }
   } 
}

public int myFac(int N)
{
	if(N <= 0)
		return 1;
	else
		return N * myFac(N - 1);
}

// Tests

test evalExp(natCon(3)) == intval(3);
test evalExp(add(natCon(3), natCon(4))) == intval(7);
test evalExp(sub(natCon(7), natCon(4))) == intval(3);
test evalExp(conc(strCon("abc"), strCon("def"))) == strval("abcdef");

bool testSmall(){
	evalProgram(small); return Env["s"] == strval("###");
}

test testSmall();

bool testFac(){	
	evalProgram(fac); return Env["output"] == intval(myFac(13));
}

test testFac();

