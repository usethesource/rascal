module lang::rascalcore::compile::Examples::Tst3

data Instruction
    = GOTO(str lbl) 
    | LABEL(str lbl)
    ;

list[Instruction] stmts(
  [*Instruction pre, 
   //TRYCATCH(Type \typ1, str from, str to, str handler1, handlers=hs),
   LABEL(from),
   *Instruction block,  
   LABEL(to),
   Instruction jump, // no jump to after the final handler, look elsewhere
   LABEL(handler1),
   //exp(load(str var)),
   *Instruction catch1,
   LABEL(str \join),
   *Instruction post
  ]) 
  = []//stmts([*pre, stat(\try([asm(stmts(exprs(tryJoins([*block, jump], \join))))],tryJoins([*hs,\catch(\typ1, var, [asm(stmts(catch1))])], \join))), LABEL(\join), *post])
  // find any GOTO jump to after the final handler:
  when /GOTO(\join) := block || /GOTO(\join) := block
  ;  