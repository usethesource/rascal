module experiments::Compiler::ReductionWithEvalCtx::Tests

import experiments::Compiler::ReductionWithEvalCtx::AST;
import experiments::Compiler::ReductionWithEvalCtx::Parse;
import experiments::Compiler::ReductionWithEvalCtx::EvalCtx;
import experiments::Compiler::ReductionWithEvalCtx::Reduction;
import experiments::Compiler::ReductionWithEvalCtx::ReductionWithEvalCtx;

import IO;

// Expect that input reduces to cfg
public bool expect(str input, Exp cfg) {
	resetCounter();
	resetFVar();
    println("Exp: <input>");
    e_in = parse(input);
    println("parsed!");
    e = reduce(Exp::config(e_in,()));
	println("Result: <e>");
	return e == cfg;
}

public bool expectModulo(str input, Exp cfg) {
	resetCounter();
	resetFVar();
    println("Exp: <input>");
    e_in = parse(input);
    println("parsed!");
	e = reduce(Exp::config(e_in,()));
	println("Result: <e.exp>; Store: <e.store>");
	return e.exp == cfg.exp;
}

test bool test1a() = expect("true", Exp::config(Exp::\true(), ()) );
test bool test1b() = expect("false", Exp::config(Exp::\false(), ()) );
test bool test1c() = expect("5", Exp::config(Exp::number(5), ()) );
test bool test1d() = expect("$L", Exp::config(Exp::label("$L"), ()) );
test bool test1e() = expect("x", Exp::config(Exp::id("x"), ()) );

test bool test2a() = expect("lambda(n) { n }", Exp::config(Exp::lambda("n", id("n")), ()) );
test bool test2b() = expect("lambda(n) { n }(5)", Exp::config(number(5), ("fvar1": number(5))) );
test bool test2c() = expect("lambda(n) { n + 1 }(5)", Exp::config(number(6), ("fvar1": number(5))) );
test bool test2d() = expect("lambda(n) { if n == 5 then n + 1 else n + 2 fi }(5)", Exp::config(number(6), ("fvar1": number(5))) );
test bool test2e() = expect("lambda(n) { if n == 5 then n + 1 else n + 2 fi }(6)", Exp::config(number(8), ("fvar1": number(6))) );

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

// recursion + continuations
// continuation-passing form
public test bool test9a() {
	// f(6) = 6 + 7 + 8 + 9 + 1 (== 31)
	str f = "Y( lambda (self) { lambda (k) { 
					'lambda (n) { if n == 10 
									'then k(1) 
									'else k(n + callcc( lambda (k1) { self(k1)(n + 1) } ))
								 'fi } } } )";
	str input = "callcc( lambda (k) { <f>(k)(6) } ) + 10";
	return expectModulo(input, Exp::config(parse("41"), ()));
}

// block expression
public test bool test10() {
	str input = "{ y := 2; x := 1 + y; x }";
	return expectModulo(input, Exp::config(parse("3"), ()));
}

// exceptions
public test bool test11() {
	str input1 = "try { { x := 1; throw(x); x + 10 } } catch y : if(x == 1) then 101 else throw(y) fi";
	str input2 = "try { try { { x := 1; throw(x); x + 10 } } catch y : if(x == 0) then 101 else throw(y) fi } catch y : if(y == 1) then 102 else throw(y) fi";
	str input3 = "try { { x := 1; throw(throw(x)); x + 10 } } catch y : if(x == 1) then 101 else throw(y) fi";
	str input4 = "try { { x := 1; throw(x)(x); x + 10 } } catch y : if(x == 1) then 101 else throw(y) fi";
	str input5 = "try { { x := 1; x + 10 } } catch y : if(x == 1) then 101 else throw(y) fi";
	
	return expectModulo(input1, Exp::config(parse("101"), ()))
			&& expectModulo(input2, Exp::config(parse("102"), ()))
			&& expectModulo(input3, Exp::config(parse("101"), ()))
			&& expectModulo(input4, Exp::config(parse("101"), ()))
			&& expectModulo(input5, Exp::config(parse("11"), ()));
}

// re-implement test9 using exceptions
public test bool test11a() {
	str fsum0 = "Y( lambda (self) { lambda (l) { if l == [] 
													'then 1 
													'else if _head(l) == 0 
															'then throw(0) 
															'else _head(l) + self(_tail(l)) 
														 'fi 
												'fi } } )";
	str fsum1 = "lambda (ls) { try { <fsum0>(ls) } catch x : x }";
	str rsum1 = "(<fsum1>)([ 1, 2, 3, 0, 5])";
	str rsum2 = "(<fsum1>)([ 1, 2, 3, 4, 5])";											    
	return expectModulo(rsum1, Exp::config(parse("0"), ()))
			&& expectModulo(rsum2, Exp::config(parse("16"), ()));
}

// while
public test bool test12() {
	str input1 = "{ n := 1; b := true; while(b){ { n := n + 1; b := false } }; n }";
	return expectModulo(input1, Exp::config(parse("2"), ()));
}

// co-routines and 'count-down' example
public test bool test13() {
	str input1 = "2 - 1";
	str input2 = "2 \< 1";
	// 6 + 5 + 4 + 3 + 2 + 1 + 0 (== 21)
	str input3 = "{ 
				   'countdown := lambda (n) { 
										'{ 
										   'while(0 \< n) { 
												'{ 
												  'yield(n); 
												  'n := n - 1 
												'} 
											'}; 
										   '0 
										 '} 
								 '}; 
				   'c := create(countdown); 
				   'count := resume(c,6);
				   'while(hasNext(c)) { count := count + resume(c, nil) };
				   'count
				   '}";
				   
	return expectModulo(input1, Exp::config(parse("1"), ()))
			&& expectModulo(input2, Exp::config(parse("false"), ()))
			&& expectModulo(input3, Exp::config(parse("21"), ()));
}