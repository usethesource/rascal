module demo::lang::Pico::Assembly

import demo::lang::Pico::Abstract;

public data Instr =
  dclNat(PicoId Id)  | dclStr(PicoId Id) 
| pushNat(int intCon)| pushStr(str strCon)
| rvalue(PicoId Id) | lvalue(PicoId Id)
| pop() | copy() |  assign() | add2() | sub2() | conc2()
| label(str label) | go(str  label)
| gotrue(str label) | gofalse(str label);