module experiments::CoreRascal::ReductionWithEvalCtx::Tests

import experiments::CoreRascal::ReductionWithEvalCtx::AST;
import experiments::CoreRascal::ReductionWithEvalCtx::EvalCtx;
import experiments::CoreRascal::ReductionWithEvalCtx::Reduction;
import experiments::CoreRascal::ReductionWithEvalCtx::ReductionWithEvalCtx;

import IO;

public Exp e1 = ifelse(Exp::eq(Exp::number(1), Exp::number(1)), Exp::add(Exp::number(2), Exp::number(3)), Exp::assign("x", Exp::number(4)));
public Exp e2 = ifelse(Exp::eq(Exp::number(1), Exp::number(0)), Exp::add(Exp::number(2), Exp::number(3)), Exp::assign("x", Exp::number(4)));

public test bool test1() {	 
	println("Exp: if 1 == 1 then 2 + 3 else x := 4");
	e1 = reduce(Exp::config(e1,()));
	println("Result: <e1>");
	return e1 == Exp::config(Exp::number(5), ());
}

public test bool test2() {	 
	println("Exp: if 1 == 0 then 2 + 3 else x := 4");
	e2 = reduce(Exp::config(e2,()));
	println("Result: <e2>");
	return e2 == Exp::config(Exp::number(4), ("x":Exp::number(4)));
}