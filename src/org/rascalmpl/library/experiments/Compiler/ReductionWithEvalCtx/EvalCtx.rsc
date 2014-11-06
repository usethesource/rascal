module experiments::Compiler::ReductionWithEvalCtx::EvalCtx

//import experiments::Compiler::ReductionWithEvalCtx::AST;
//
//import IO;
//
//@doc{Extension of expressions with evaluation contexts}
//public data Exp = 
//			  C(Exp exp, Ctx ctx) // with evaluation contexts - C[e] 
//			;
//
//@doc{The Value data type}
//public data Value =
//			  nil()
//			| \true()
//			| \false()
//			| \num(int n)
//			| lambda(str id, Exp exp)
//			;
//
//public Value toValue(Exp::nil()) = Value::nil();
//public Value toValue(Exp::\true()) = Value::\true();
//public Value toValue(Exp::\false()) = Value::\false();
//public Value toValue(Exp::number(int n)) = Value::\num(n);
//public Value toValue(Exp::lambda(str id, Exp exp)) = Value::lambda(id, exp);
//public default Value toValue(Exp exp) { throw "unknown value: <exp>"; }
//
//public Exp toExp(Value::nil()) = Exp::nil();
//public Exp toExp(Value::\true()) = Exp::\true();
//public Exp toExp(Value::\false()) = Exp::\false();
//public Exp toExp(Value::\num(int n)) = Exp::number(n);
//public Exp toExp(Value::lambda(str id, Exp exp)) = Exp::lambda(id, exp);
//public default Exp toExp(Value val) { throw "unknown value: <val>"; }
//
//@doc{The evaluation context data type}
//public data Ctx = 
//			 hole() 			
//		   | apply(Ctx ctx, Exp exp2)
//		   | apply(Value \value, Ctx ctx)
//		   	
//		   | add(Ctx ctx, Exp exp2)
//		   | add(Exp exp1, Ctx ctx)
//		   
//		   | minus(Ctx ctx, Exp exp2)
//		   | minus(Exp exp1, Ctx ctx)
//		   
//		   | eq(Ctx ctx, Exp exp2)
//		   | eq(Value \value, Ctx ctx)
//		   
//		   | less(Ctx ctx, Exp exp2)
//		   | less(Value \value, Ctx ctx)
//		   
//		   | assign(str id, Ctx ctx)
//		   | ifelse(Ctx ctx, Exp exp2, Exp exp3)
//		   
//		   | block(Ctx ctx, list[Exp] restOfExps)
//		   
//		   | config(Ctx ctx, Store store)		   
//		   ;
//		   
//@doc{The splitting operation}
//public Exp split( Exp::apply(Exp exp1, Exp exp2) ) = 
//	C(exp2_, Ctx::apply(Value::lambda(id, exp), ctx)) 
//	when isValue(exp1) && Exp::lambda(str id, Exp exp) := exp1 && !isValue(exp2) && C(exp2_,ctx) := split(exp2);
//	
//public Exp split( Exp::apply(Exp exp1, Exp exp2) ) = 
//	C(exp1_, Ctx::apply(ctx, exp2)) 
//	when !isValue(exp1) && C(exp1_,ctx) := split(exp1);
//
//public Exp split( Exp::add(Exp exp1, Exp exp2) ) = 
//	C(exp2_, Ctx::add(exp1, ctx)) 
//	when isValue(exp1) && !isValue(exp2) && C(exp2_,ctx) := split(exp2);
//	
//public Exp split( Exp::add(Exp exp1, Exp exp2) ) = 
//	C(exp1_, Ctx::add(ctx, exp2)) 
//	when !isValue(exp1) && C(exp1_,ctx) := split(exp1);
//	
//public Exp split( Exp::minus(Exp exp1, Exp exp2) ) = 
//	C(exp2_, Ctx::minus(exp1, ctx)) 
//	when isValue(exp1) && !isValue(exp2) && C(exp2_,ctx) := split(exp2);
//	
//public Exp split( Exp::minus(Exp exp1, Exp exp2) ) = 
//	C(exp1_, Ctx::minus(ctx, exp2)) 
//	when !isValue(exp1) && C(exp1_,ctx) := split(exp1);
//
//public Exp split( Exp::eq(Exp exp1, Exp exp2) ) = 
//	C(exp2_, Ctx::eq(Value::\num(n), ctx)) 
//	when isValue(exp1) && Exp::number(int n) := exp1 && !isValue(exp2) && C(exp2_,ctx) := split(exp2);
//		
//public Exp split( Exp::eq(Exp exp1, Exp exp2) ) = 
//	C(exp1_, Ctx::eq(ctx, exp2)) 
//	when !isValue(exp1) && C(exp1_,ctx) := split(exp1);
//
//public Exp split( Exp::less(Exp exp1, Exp exp2) ) = 
//	C(exp2_, Ctx::less(Value::\num(n), ctx)) 
//	when isValue(exp1) && Exp::number(int n) := exp1 && !isValue(exp2) && C(exp2_,ctx) := split(exp2);
//		
//public Exp split( Exp::less(Exp exp1, Exp exp2) ) = 
//	C(exp1_, Ctx::less(ctx, exp2)) 
//	when !isValue(exp1) && C(exp1_,ctx) := split(exp1);
//
//public Exp split( Exp::assign(str id, Exp exp) ) = 
//	C(exp_, Ctx::assign(id, ctx)) 
//	when !isValue(exp) && C(exp_,ctx) := split(exp);
//
//public Exp split( Exp::ifelse(Exp exp1, Exp exp2, Exp exp3) ) = 
//	C(exp1_, Ctx::ifelse(ctx, exp2, exp3)) 
//	when !isValue(exp1) && C(exp1_,ctx) := split(exp1);
//	
//public Exp split( Exp::block([ Exp exp, *Exp rest ]) ) =
//	C(exp_, Ctx::block(ctx, rest)) 
//	when !isValue(exp) && C(exp_,ctx) := split(exp);
//
//public Exp split( Exp::config(Exp exp, Store store) ) = 
//	C(exp_, Ctx::config(ctx,store)) 
//	when !isValue(exp) && C(exp_,ctx) := split(exp);
//
//public default Exp split(Exp exp) = 
//	{ /*println("Default split: <exp> -\> <C(exp, Ctx::hole())>");*/ C(exp, Ctx::hole()); };
//
//@doc{The plugging operation}
//public Exp plug( C(Exp exp, Ctx::hole())) = exp;
//public Exp plug( C(Exp exp, Ctx::apply(Ctx ctx, Exp exp2)) ) = Exp::apply(plug(C(exp,ctx)), exp2);
//public Exp plug( C(Exp exp, Ctx::apply(Value::lambda(str id, Exp e), Ctx ctx)) ) = Exp::apply(Exp::lambda(id, e), plug(C(exp,ctx)));
//
//public Exp plug( C(Exp exp, Ctx::add(Ctx ctx, Exp exp2)) ) = Exp::add(plug(C(exp,ctx)), exp2);
//public Exp plug( C(Exp exp, Ctx::add(Exp exp1, Ctx ctx)) ) = Exp::add(exp1, plug(C(exp,ctx)));
//
//public Exp plug( C(Exp exp, Ctx::minus(Ctx ctx, Exp exp2)) ) = Exp::minus(plug(C(exp,ctx)), exp2);
//public Exp plug( C(Exp exp, Ctx::minus(Exp exp1, Ctx ctx)) ) = Exp::minus(exp1, plug(C(exp,ctx)));
//
//public Exp plug( C(Exp exp, Ctx::eq(Ctx ctx, Exp exp2)) ) = Exp::eq(plug(C(exp,ctx)), exp2);
//public Exp plug( C(Exp exp, Ctx::eq(Value::\num(int n), Ctx ctx)) ) = Exp::eq(Exp::number(n), plug(C(exp,ctx)));
//
//public Exp plug( C(Exp exp, Ctx::less(Ctx ctx, Exp exp2)) ) = Exp::less(plug(C(exp,ctx)), exp2);
//public Exp plug( C(Exp exp, Ctx::less(Value::\num(int n), Ctx ctx)) ) = Exp::less(Exp::number(n), plug(C(exp,ctx)));
//
//public Exp plug( C(Exp exp, Ctx::assign(str id, Ctx ctx)) ) = Exp::assign(id, plug(C(exp,ctx)));
//
//public Exp plug( C(Exp exp, Ctx::ifelse(Ctx ctx, Exp exp2, Exp exp3)) ) = Exp::ifelse(plug(C(exp,ctx)), exp2, exp3);
//
//public Exp plug( C(Exp exp, Ctx::block(Ctx ctx, list[Exp] exps)) ) = Exp::block([ plug(C(exp,ctx)) ] + exps);
//
//public Exp plug( C(Exp exp, Ctx::config(Ctx ctx, Store store)) ) = Exp::config(plug(C(exp,ctx)), store);
//public default Exp plug(Exp exp) { throw "unknown <exp>; "; }
//
//@doc{Extension with co-routines}
//public data Value =
//		    label(str l)
//		  | __dead()
//		  ;
//		  
//public Value toValue(Exp::label(str name)) = Value::label(name);
//public Value toValue(Exp::__dead()) = Value::__dead();
//
//public Exp toExp(Value::label(str name)) = Exp::label(name);
//public Exp toExp(Value::__dead()) = Exp::__dead();
//		   
//public data Ctx =
//		    labeled(str name, Ctx ctx)
//		  | create(Ctx ctx)
//		  | resume(Ctx ctx, Exp exp2)
//		  | resume(Value \value, Ctx ctx)
//		  | yield(Ctx ctx)
//		  | hasNext(Ctx ctx)
//		  ;
//		  
//public Exp split( Exp::labeled(str name, Exp exp) ) = 
//	C(exp_, Ctx::labeled(name, ctx)) 
//	when !isValue(exp) && C(exp_,ctx) := split(exp);
//	
//public Exp split( Exp::create(Exp exp) ) = 
//	C(exp_, Ctx::create(ctx))
//	when !isValue(exp) && C(exp_,ctx) := split(exp);
//	
//public Exp split( Exp::resume(Exp exp1, Exp exp2) ) = 
//	C(exp2_, Ctx::resume(Value::label(l), ctx)) 
//	when isValue(exp1) && Exp::label(str l) := exp1 && !isValue(exp2) && C(exp2_,ctx) := split(exp2);
//	
//public Exp split( Exp::resume(Exp exp1, Exp exp2) ) = 
//	C(exp1_, Ctx::resume(ctx, exp2)) 
//	when !isValue(exp1) && C(exp1_,ctx) := split(exp1);
//	
//public Exp split( Exp::yield(Exp exp) ) = 
//	C(exp_, Ctx::yield(ctx)) 
//	when !isValue(exp) && C(exp_,ctx) := split(exp);
//
//public Exp split( Exp::hasNext(Exp exp) ) = 
//	C(exp_, Ctx::hasNext(ctx)) 
//	when !isValue(exp) && C(exp_,ctx) := split(exp);
//
//public Exp plug( C(Exp exp, Ctx::labeled(str name, Ctx ctx)) ) = 
//	Exp::labeled(name, plug(C(exp,ctx)));
//	
//public Exp plug( C(Exp exp, Ctx::create(Ctx ctx)) ) = 
//	Exp::create(plug(C(exp,ctx)));
//	
//public Exp plug( C(Exp exp, Ctx::resume(Ctx ctx, Exp exp2)) ) = 
//	Exp::resume(plug(C(exp,ctx)), exp2);
//	
//public Exp plug( C(Exp exp, Ctx::resume(Value::label(str l), Ctx ctx)) ) = 
//	Exp::resume(Exp::label(l), plug(C(exp,ctx)));
//	
//public Exp plug( C(Exp exp, Ctx::yield(Ctx ctx)) ) = 
//	Exp::yield(plug(C(exp,ctx)));
//
//public Exp plug( C(Exp exp, Ctx::hasNext(Ctx ctx)) ) = 
//	Exp::hasNext(plug(C(exp,ctx)));
//
//@doc{Extension with continuations}
//public data Ctx = 
//		callcc(Ctx ctx)
//		;
//
//public Exp split( Exp::callcc(Exp exp) ) = 
//	C(exp_, Ctx::callcc(ctx)) 
//	when !isValue(exp) && C(exp_,ctx) := split(exp);
//
//public Exp plug( C(Exp exp, Ctx::callcc(ctx)) ) = 
//	Exp::callcc(plug(C(exp,ctx)));
//
//@doc{Extension with constants and lists}
//public data Value =
//			  const(str id)
//			| lst(list[Value] exps)
//			;
//
//public Value toValue(Exp::const(str id)) = Value::const(id);
//public Value toValue(Exp::lst(list[Exp] exps)) = Value::lst([ toValue(e) | Exp e <- exps ]);
//
//public Exp toExp(Value::const(str id)) = Exp::const(id);
//public Exp toExp(Value::lst(list[Exp] vals)) = Exp::lst([ toExp(val) | Value val <- vals ]);
//
//public data Ctx = 
//			 lst(list[Exp] head, Ctx ctx, list[Exp] tail)
//		   ;
//
//public Exp split( Exp::apply(Exp exp1, Exp exp2) ) = 
//	C(exp2_, Ctx::apply(Value::const(id), ctx)) 
//	when isValue(exp1) && Exp::const(str id) := exp1 && !isValue(exp2) && C(exp2_,ctx) := split(exp2);
//	
//public Exp split( Exp::lst([*Exp head, Exp exp, *Exp tail]) ) = 
//	C(exp_, Ctx::lst(head, ctx, tail)) 
//	when !isValue(exp) && C(exp_,ctx) := split(exp);
//
//public Exp plug( C(Exp exp, Ctx::apply(Value::const(str id), Ctx ctx)) ) = 
//	Exp::apply(Exp::const(id), plug(C(exp,ctx)));
//	
//public Exp plug( C(Exp exp, Ctx::lst(list[Exp] head, Ctx ctx, list[Exp] tail)) ) = 
//	Exp::lst(head + [ plug(C(exp,ctx)) ] + tail);
//
//@doc{Extension with recursion}
//public data Ctx =
//			Y(Ctx ctx)
//			;
//			
//public Exp split( Exp::Y(Exp exp) ) = 
//	C(exp_, Ctx::Y(ctx)) 
//	when !isValue(exp) && C(exp_,ctx) := split(exp);
//
//public Exp plug( C(Exp exp, Ctx::Y(ctx)) ) = 
//	Exp::Y(plug(exp,ctx));
//	
//@doc{Extension with exceptions}
//
//public data Ctx =
//			  \throw(Ctx ctx)
//			| \try(Ctx ctx, Catch \catch)
//			;
//
//public Exp split( Exp::\throw(Exp exp) ) =
//	C(exp_, Ctx::\throw(ctx))
//	when !isValue(exp) && C(exp_,ctx) := split(exp);
//
//// try{ throw(v) } catch x : e - redex
//public Exp split( Exp::\try(Exp exp, Catch \catch) ) =
//	C(exp_, Ctx::\try(ctx, \catch))
//	when !isValue(exp) 
//			&& !(Exp::\throw(Exp e) := exp && isValue(e)) 
//			&& C(exp_,ctx) := split(exp);
//	
//public Exp plug( C(Exp exp, Ctx::\throw(Ctx ctx)) ) = 
//	Exp::\throw(plug(C(exp,ctx)));
//	
//public Exp plug( C(Exp exp, Ctx::\try(Ctx ctx, Catch \catch)) ) = 
//	Exp::\try(plug(C(exp,ctx)), \catch);
//
//// Test that split and plug are inverse
//test bool testSplit(Exp e) = plug(split(e)) == e;
