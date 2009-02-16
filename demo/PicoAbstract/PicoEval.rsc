module PicoEval

import PicoAbstractSyntax;
import PicoPrograms;
import UnitTest;
import IO;
 
data PicoValue = intval(int i) | strval(str s);

alias VEnv = map[PicoId, PicoValue];

VEnv Env = ();

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
      case decl(PicoId Id, string): { 
           Env[Id] = strval(""); 
      }
      case decl(PicoId Id, natural): { 
           Env[Id] = intval(0);  
      }
    };
}

void evalStatements(list[STATEMENT] Series){
    for(STATEMENT Stat : Series){
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

public int fac(int N)
{
	if(N <= 0)
		return 1;
	else
		return N * fac(N - 1);
}

public bool test(){
	assertEqual(evalExp(natCon(3)), intval(3));
	assertEqual(evalExp(add(natCon(3), natCon(4))), intval(7));
	assertEqual(evalExp(sub(natCon(7), natCon(4))), intval(3));
	assertEqual(evalExp(conc(strCon("abc"), strCon("def"))), strval("abcdef"));
	
	evalProgram(small); assertEqual(Env["s"], strval("###"));
	evalProgram(fac); assertEqual(Env["output"], intval(fac(13)));
	return report();
}
