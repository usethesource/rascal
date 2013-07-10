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
	e1_ = reduce(Exp::config(e1,()));
	println("Result: <e1_>");
	return e1_ == Exp::config(Exp::number(5), ());
}

public test bool test2() {	 
	println("Exp: if 1 == 0 then 2 + 3 else x := 4");
	e2_ = reduce(Exp::config(e2,()));
	println("Result: <e2_>");
	return e2_ == Exp::config(Exp::number(4), ("x":Exp::number(4)));
}

// continuations
public Exp e3 = Exp::callcc(Exp::lambda("k", Exp::apply(Exp::id("k"), Exp::number(42))));
public test bool test3() {
	println("Exp: callcc(lambda k . k 42)");
	e3_ = reduce(Exp::config(e3,()));
	println("Result: <e3_>");
	return e3_ == Exp::config(Exp::number(42),("xvar":Exp::number(42), "k":Exp::lambda("xvar",Exp::abort(Exp::id("xvar")))));
}