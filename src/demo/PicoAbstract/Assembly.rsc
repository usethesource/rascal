module demo::PicoAbstract::Assembly

import demo::PicoAbstract::PicoAbstractSyntax;
import Integer;
import UnitTest;

public data Instr =
      dclNat(PicoId Id)
    | dclStr(PicoId Id)
	| pushNat(int intCon)
	| pushStr(str strCon)
	| rvalue(PicoId Id)
	| lvalue(PicoId Id)
	| pop()
	| copy()
	| assign()
	| add()
	| sub()
	| mul()
	| label(str label)
	| go(str  label)
	| gotrue(str label)
	| gofalse(str label)
	;

private int nLabel = 0;

private str nextLabel(){
	nLabel += 1;
	return "L" + toString(nLabel);
}

public list[Instr] compileProgram(PROGRAM P){
    nLabel = 0;
    if(program(list[DECL] Decls, list[STATEMENT] Series) := P){
           return [compileDecls(Decls), compileStatements(Series)];
    } else
       throw Exception("Cannot happen");
}

private list[Instr] compileDecls(list[DECL] Decls){
    return [ (type == natural()) ? dclNat(Id) : dclStr(Id)  | decl(PicoId Id, TYPE type) <- Decls];
}

private list[Instr] compileStatements(list[STATEMENT] Stats){
  return [ compileStatement(S) | S <- Stats ];
}

private list[Instr] compileStatement(STATEMENT Stat){

   switch (Stat) {
      case asgStat(PicoId Id, EXP Exp):
        return [lvalue(Id), compileExp(Exp), assign()];

      case ifStat(EXP Exp, list[STATEMENT] Stats1,
                           list[STATEMENT] Stats2):{
        nextLab = nextLabel();
        falseLab = nextLabel();
        
        return [compileExp(Exp), 
                gofalse(falseLab), 
                compileStatements(Stats1),  
                go(nextLab), 
                label(falseLab), compileStatements(Stats2), 
                label(nextLab)];
      }

      case whileStat(EXP Exp, list[STATEMENT] Stats1): {
        entryLab = nextLabel();
        nextLab = nextLabel();
        return [label(entryLab), compileExp(Exp), 
                gofalse(nextLab), 
                compileStatements(Stats1), 
                go(entryLab), 
                label(nextLab)];
      }
    }
}

private list[Instr] compileExp(EXP exp) {
    switch (exp) {
      case natCon(int N): 
           return [pushNat(N)];

      case strCon(str S): 
           return [pushStr(S)];

      case id(PicoId Id): 
           return [rvalue(Id)];

      case add(EXP E1, EXP E2):
           return [compileExp(E1), compileExp(E2), add];
      
      case sub(EXP E1, EXP E2):
            return [compileExp(E1), compileExp(E2), sub];
 
      case conc(EXP E1, EXP E2):
           return [compileExp(E1), compileExp(E2), conc];
   } 
}

public bool test(){
  P = program([],[]);
  R = [];
  assertEqual(compileProgram(P), R);
  
  P = program([decl("x", natural())], [ifStat(natCon(5), [asgStat("x", natCon(3))], [asgStat("x", natCon(4))])]);
       
   R = [dclNat("x"),
        pushNat(5),
        gofalse("L4"),
        lvalue("x"),
        pushNat(3),
        assign(),
        go("L3"),
        label("L4"),
        lvalue("x"),
        pushNat(4),
        assign(),
        label("L3")];
  assertEqual(compileProgram(P), R);
  
  P = program([decl("x", natural())], [whileStat(natCon(5), [asgStat("x", natCon(3))])]);
       
  R = [dclNat("x"),
       label("L3"),
       pushNat(5),
       gofalse("L4"),
       lvalue("x"),
       pushNat(3),
       assign(),
       go("L3"),
       label("L4")];
       
   assertEqual(compileProgram(P), R);
   
   
   P = program([decl("x", string())], [whileStat(natCon(5), [asgStat("x", strCon("abc"))])]);
       
  R = [dclStr("x"),
       label("L3"),
       pushNat(5),
       gofalse("L4"),
       lvalue("x"),
       pushStr("abc"),
       assign(),
       go("L3"),
       label("L4")];
       
   assertEqual(compileProgram(P), R);
  return report();
}
