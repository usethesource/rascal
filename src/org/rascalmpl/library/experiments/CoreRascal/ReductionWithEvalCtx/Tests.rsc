module experiments::CoreRascal::ReductionWithEvalCtx::Tests

import experiments::CoreRascal::ReductionWithEvalCtx::AST;
import experiments::CoreRascal::ReductionWithEvalCtx::Parse;
import experiments::CoreRascal::ReductionWithEvalCtx::EvalCtx;
import experiments::CoreRascal::ReductionWithEvalCtx::Reduction;
import experiments::CoreRascal::ReductionWithEvalCtx::ReductionWithEvalCtx;

import IO;

// Expect that input reduces to cfg
public bool expect(str input, Exp cfg) {
    println("Exp: <input>");
	e = reduce(Exp::config(parse(input),()));
	println("Result: <e>");
	return e == cfg;
}

test bool test1a() = expect("true", Exp::config(Exp::\true(), ()) );
test bool test1b() = expect("false", Exp::config(Exp::\false(), ()) );
test bool test1c() = expect("5", Exp::config(Exp::number(5), ()) );
test bool test1d() = expect("$L", Exp::config(Exp::label("$L"), ()) );
test bool test1e() = expect("x", Exp::config(Exp::id("x"), ()) );

test bool test2a() = expect("lambda(n) { n }", Exp::config(Exp::lambda("n", id("n")), ()) );
test bool test2b() = expect("lambda(n) { n }(5)", Exp::config(number(5), ("n_1": number(5))) );
test bool test2c() = expect("lambda(n) { n + 1 }(5)", Exp::config(number(6), ("n_1": number(5))) );
test bool test2d() = expect("lambda(n) { if n == 5 then n + 1 else n + 2 fi }(5)", Exp::config(number(6), ("n_1": number(5))) );
test bool test2e() = expect("lambda(n) { if n == 5 then n + 1 else n + 2 fi }(6)", Exp::config(number(8), ("n_1": number(6))) );

test bool test3() = expect("2 + 3", Exp::config(Exp::number(5), ()) );

test bool test4a() = expect("2 == 2", Exp::config(Exp::\true(), ()) );
test bool test4b() = expect("2 == 3", Exp::config(Exp::\false(), ()) );

test bool test5a() = expect("x := 3", Exp::config(Exp::\number(3), ("x" : number(3))) );

test bool test6a() = expect("if true then 3 else 4 fi", Exp::config(Exp::\number(3), ()) );
test bool test6b() = expect("if false then 3 else 4 fi", Exp::config(Exp::\number(4), ()) );
test bool test6c() = expect("if 1 == 1 then 3 else 4 fi", Exp::config(Exp::\number(3), ()) );
test bool test6d() = expect("if 1 == 2 then 3 else 4 fi", Exp::config(Exp::\number(4), ()) );
test bool test6e() = expect("if 1 == 1 then 2 + 3 else x := 4 fi",  Exp::config(Exp::number(5), ()) );
test bool test6f() = expect("if 1 == 0 then 2 + 3 else x := 4 fi",  Exp::config(Exp::number(4), ("x":Exp::number(4))) );


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
