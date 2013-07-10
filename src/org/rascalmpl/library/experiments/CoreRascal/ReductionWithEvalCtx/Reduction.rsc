module experiments::CoreRascal::ReductionWithEvalCtx::Reduction

import experiments::CoreRascal::ReductionWithEvalCtx::AST;

@doc{Reduction rules}
public Exp step( Exp::add(number(n1), number(n2)) ) = Exp::number(n1 + n2);
public Exp step( Exp::eq(number(n1), number(n2)) ) = (n1 == n2) ? Exp::\true() : Exp::\false();

public Exp step( Exp::ifelse(Exp::\true(), Exp exp2, Exp exp3) ) = exp2;
public Exp step( Exp::ifelse(Exp::\false(), Exp exp2, Exp exp3) ) = exp3; 
