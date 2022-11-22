module demo::lang::Pico::Compile

import Prelude;
import demo::lang::Pico::Abstract;
import demo::lang::Pico::Assembly;
import demo::lang::Pico::Load;

// highlight-next-line
alias Instrs = list[Instr]; // <1>

@synopsis{Compile expressions to stackmachine instructions}
// highlight-next-line
Instrs compileExp(natCon(int N)) = [pushNat(N)]; // <2>

Instrs compileExp(strCon(str S)) = [pushStr(substring(S,1,size(S)-1))];

Instrs compileExp(id(PicoId Id)) = [rvalue(Id)];

// highlight-next-line
Instrs compileExp(add(EXP E1, EXP E2)) // <3>
  = [*compileExp(E1), *compileExp(E2), add2()];

Instrs compileExp(sub(EXP E1, EXP E2)) 
  = [*compileExp(E1), *compileExp(E2), sub2()];

Instrs compileExp(conc(EXP E1, EXP E2)) 
  = [*compileExp(E1), *compileExp(E2), conc2()];
  
// highlight-next-line
private int nLabel = 0; // <4>

@synopsis{Unique label generation}
private str nextLabel() {
  nLabel += 1;
  return "L<nLabel>";
}

@synopsis{Compile a statement}
Instrs compileStat(asgStat(PicoId Id, EXP Exp)) 
  = [lvalue(Id), *compileExp(Exp), assign()];
	
// highlight-next-line
Instrs compileStat(ifElseStat(EXP Exp,                   // <5>
                              list[STATEMENT] Stats1,
                              list[STATEMENT] Stats2)){
  
  elseLab = nextLabel();
  endLab = nextLabel();  
  return [*compileExp(Exp), 
          gofalse(elseLab), 
          *compileStats(Stats1),  
          go(endLab), 
          label(elseLab), 
          *compileStats(Stats2), 
          label(endLab)];
}

Instrs compileStat(whileStat(EXP Exp, 
                             list[STATEMENT] Stats1)) {
  entryLab = nextLabel();
  endLab = nextLabel();
  return [label(entryLab), 
          *compileExp(Exp), 
          gofalse(endLab), 
          *compileStats(Stats1), 
          go(entryLab), 
          label(endLab)];
}

@synopsis{Compile a list of statements}
Instrs compileStats(list[STATEMENT] Stats1) 
// highlight-next-line
  = [ *compileStat(S) | S <- Stats1 ]; // <6>
  
@synopsis{Compile declarations}
Instrs compileDecls(list[DECL] Decls) =
// highlight-next-line
  [ ((tp == natural()) ? dclNat(Id) : dclStr(Id)) |  // <7>
    decl(PicoId Id, TYPE tp) <- Decls
  ];

@synopsis{Compile a Pico program}
// highlight-next-line
Instrs compileProgram(PROGRAM P){  // <8>
  nLabel = 0;
  if (program(list[DECL] Decls, list[STATEMENT] Series) := P) {
     return [*compileDecls(Decls), *compileStats(Series)];
  } else {
    throw "Cannot happen";
  }
}

Instrs compileProgram(str txt) = compileProgram(load(txt));

