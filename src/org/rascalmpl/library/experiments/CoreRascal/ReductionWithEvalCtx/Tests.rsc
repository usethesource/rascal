module experiments::CoreRascal::ReductionWithEvalCtx::Tests

import experiments::CoreRascal::ReductionWithEvalCtx::AST;
import experiments::CoreRascal::ReductionWithEvalCtx::Parse;
import experiments::CoreRascal::ReductionWithEvalCtx::EvalCtx;
import experiments::CoreRascal::ReductionWithEvalCtx::Reduction;
import experiments::CoreRascal::ReductionWithEvalCtx::ReductionWithEvalCtx;

import IO;

public Exp e1 = ifelse(Exp::eq(Exp::number(1), Exp::number(1)), Exp::add(Exp::number(2), Exp::number(3)), Exp::assign("x", Exp::number(4)));
public Exp e2 = ifelse(Exp::eq(Exp::number(1), Exp::number(0)), Exp::add(Exp::number(2), Exp::number(3)), Exp::assign("x", Exp::number(4)));

public bool expect(str input, Exp cfg) {
    println("Exp: <input>");
	e = reduce(Exp::config(parse(input),()));
	println("Result: <e>");
	return e == cfg;
}

public test bool test1() = 
	expect("if 1 == 1 then 2 + 3 else x := 4 fi", 
		   Exp::config(Exp::number(5), ())
		  );

public test bool test2() = 
	expect("if 1 == 0 then 2 + 3 else x := 4 fi", 
		   Exp::config(Exp::number(4), ("x":Exp::number(4)))
		  );

public test bool test3() = 
	expect("callcc(lambda (k) { k(42) })", 
		   Exp::config(Exp::number(42),("xvar":Exp::number(42), "k":Exp::lambda("xvar",Exp::abort(Exp::id("xvar")))))
		  );

// continuations
//public Exp e3 = Exp::callcc(Exp::lambda("k", Exp::apply(Exp::id("k"), Exp::number(42))));
//public test bool test3() {
//	println("Exp: callcc(lambda k . k 42)");
//	e3_ = reduce(Exp::config(e3,()));
//	println("Result: <e3_>");
//	return e3_ == Exp::config(Exp::number(42),("xvar":Exp::number(42), "k":Exp::lambda("xvar",Exp::abort(Exp::id("xvar")))));
//}

public test bool test4() = 
	expect("callcc(lambda (k) { k(42) + 10 })", 
		   Exp::config(Exp::number(42),("xvar":Exp::number(42), "k":Exp::lambda("xvar",Exp::abort(Exp::id("xvar")))))
		  );


//public Exp e4 = Exp::callcc(Exp::lambda("k", Exp::add(Exp::apply(Exp::id("k"), Exp::number(42)), Exp::number(10))));
//public test bool test4() {
//	println("Exp: callcc(lambda k . ((k 42) + 10))");
//	e4_ = reduce(Exp::config(e4,()));
//	println("Result: <e4_>");
//	return e4_ == Exp::config(Exp::number(42),("xvar":Exp::number(42), "k":Exp::lambda("xvar",Exp::abort(Exp::id("xvar")))));
//}

public test bool test5a() = 
	expect("callcc(lambda (k) { k(42) }) + 10", 
		    Exp::config(Exp::number(52),("xvar":Exp::number(42), "k":Exp::lambda("xvar",Exp::abort(Exp::add(Exp::id("xvar"), Exp::number(10))))))
		  );

//public Exp e5 = Exp::add(Exp::callcc(Exp::lambda("k", Exp::apply(Exp::id("k"), Exp::number(42)))), Exp::number(10));
//public test bool test5() {
//	println("Exp: callcc(lambda k . (k 42) ) + 10");
//	e5_ = reduce(Exp::config(e5,()));
//	println("Result: <e5_>");
//	return e5_ == Exp::config(Exp::number(52),("xvar":Exp::number(42), "k":Exp::lambda("xvar",Exp::abort(Exp::add(Exp::id("xvar"), Exp::number(10))))));
//}
