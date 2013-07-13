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
    e_in = parse(input);
    println("parsed!");
    e = reduce(Exp::config(e_in,()));
	println("Result: <e>");
	return e == cfg;
}

public bool expectModulo(str input, Exp cfg) {
    println("Exp: <input>");
    e_in = parse(input);
    println("parsed!");
	e = reduce(Exp::config(e_in,()));
	println("Result: <e.exp>; Store: <e.store>");
	return e.exp == cfg.exp;
}

public test bool test1() = 
	expect("if 1 == 1 then 2 + 3 else x := 4 fi", 
		   Exp::config(parse("5"), ())
		  );

public test bool test2() = 
	expect("if 1 == 0 then 2 + 3 else x := 4 fi", 
		   Exp::config(parse("4"), ("x":parse("4")))
		  );

// continuations
public test bool test3() = 
	expectModulo("callcc(lambda (k) { k(42) })", 
		   Exp::config(parse("42"),("xvar":parse("42"), "k":parse("lambda (xvar) { abort(xvar) }")))
		  );

public test bool test4() = 
	expectModulo("callcc(lambda (k) { k(42) + 10 })", 
		   Exp::config(parse("42"),("xvar":parse("42"), "k":parse("lambda (xvar) { abort(xvar) }")))
		  );


public test bool test4a() = 
	expectModulo("callcc(lambda (k) { k(42) }) + 10", 
		    Exp::config(parse("52"),("xvar":parse("42"), "k":parse("lambda (xvar) { abort(xvar + 10) }")))
		  );

// lists
public test bool test6() =
	expect("[ 1 + 2, 3 + 4 ]", Exp::config(parse("[ 3, 7 ]"), ()));

// recursion
// performs summation of all the numbers in a list until zero is encountered 
public test bool test7() {
	str fsum = "Y( lambda (self) { lambda (l) { if l == [] 
													then 1 
													else if _head(l) == 0 
															then 0 
															else _head(l) + self(_tail(l)) 
														 fi 
												fi } } )";
	bool t1 = expectModulo("<fsum>( [ 1, 2, 3, 0, 5 ] )",
				 Exp::config(parse("6"), ()));
	bool t2 = expectModulo("<fsum>( [ 1, 2, 3, 4, 5 ] )",
				 Exp::config(parse("16"), ()));
	return t1 && t2;
}
// recursion + continuations
// performs summation of all the numbers in a list if the list does not have zero elements, otherwise jumps from the recursion
public test bool test9() {
	str fsum0 = "Y( lambda (self) { lambda (k) { lambda (l) { if l == [] 
																then 1 
																else if _head(l) == 0 
																		then k(0) 
																		else _head(l) + self(k)(_tail(l)) 
															 		 fi 
												    		  fi } } } )";
	str fsum1 = "lambda (ls) { callcc( lambda (k) { <fsum0>(k)(ls) } ) }";
	str rsum1 = "(<fsum1>)([ 1, 2, 3, 0, 5])";
	str rsum2 = "(<fsum1>)([ 1, 2, 3, 4, 5])";											    
	return expectModulo(rsum1, Exp::config(parse("0"), ()))
			&& expectModulo(rsum2, Exp::config(parse("16"), ()));
}
