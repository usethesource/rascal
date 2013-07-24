module experiments::CoreRascal::ReductionWithEvalCtx::EvalCtx

import experiments::CoreRascal::ReductionWithEvalCtx::AST;

import IO;

@doc{Extension of expressions with evaluation contexts}
public data Exp = 
			  C(Exp exp, Ctx ctx) // with evaluation contexts - C[e] 
			;

@doc{The Value data type}
public data Value =
			  \true()
			| \false()
			| \num(int n)
			| lambda(str id, Exp exp)
			;

@doc{The evaluation context data type}
@doc{C = [] | C e | v C | C + e | e + C | C == e | v == C | x := C | if C then e else e | <C, Store>}
public data Ctx = 
			 hole() 			
		   | apply(Ctx ctx, Exp exp2)
		   | apply(Value \value, Ctx ctx)
		   	
		   | add(Ctx ctx, Exp exp2)
		   | add(Exp exp1, Ctx ctx)
		   | eq(Ctx ctx, Exp exp2)
		   | eq(Value \value, Ctx ctx) 
		   
		   | assign(str id, Ctx ctx)
		   | ifelse(Ctx ctx, Exp exp2, Exp exp3)
		   
		   | config(Ctx ctx, Store store)		   
		   ;
		   
@doc{The splitting operation}
public Exp split( Exp::apply(Exp exp1, Exp exp2) ) = 
	C(exp2_, Ctx::apply(Value::lambda(id, exp), ctx)) 
	when isValue(exp1) && Exp::lambda(str id, Exp exp) := exp1 && !isValue(exp2) && C(exp2_,ctx) := split(exp2);
	
public Exp split( Exp::apply(Exp exp1, Exp exp2) ) = 
	C(exp1_, Ctx::apply(ctx, exp2)) 
	when !isValue(exp1) && C(exp1_,ctx) := split(exp1);

public Exp split( Exp::add(Exp exp1, Exp exp2) ) = 
	C(exp2_, Ctx::add(exp1, ctx)) 
	when isValue(exp1) && !isValue(exp2) && C(exp2_,ctx) := split(exp2);
	
public Exp split( Exp::add(Exp exp1, Exp exp2) ) = 
	C(exp1_, Ctx::add(ctx, exp2)) 
	when !isValue(exp1) && C(exp1_,ctx) := split(exp1);

public Exp split( Exp::eq(Exp exp1, Exp exp2) ) = 
	C(exp2_, Ctx::eq(Value::\num(n), ctx)) 
	when isValue(exp1) && Exp::number(int n) := exp1 && !isValue(exp2) && C(exp2_,ctx) := split(exp2);
		
public Exp split( Exp::eq(Exp exp1, Exp exp2) ) = 
	C(exp1_, Ctx::eq(ctx, exp2)) 
	when !isValue(exp1) && C(exp1_,ctx) := split(exp1);

public Exp split( Exp::assign(str id, Exp exp) ) = 
	C(exp_, Ctx::assign(id, ctx)) 
	when !isValue(exp) && C(exp_,ctx) := split(exp);

public Exp split( Exp::ifelse(Exp exp1, Exp exp2, Exp exp3) ) = 
	C(exp1_, Ctx::ifelse(ctx, exp2, exp3)) 
	when !isValue(exp1) && C(exp1_,ctx) := split(exp1);

public Exp split( Exp::config(Exp exp, Store store) ) = 
	C(exp_, Ctx::config(ctx,store)) 
	when !isValue(exp) && C(exp_,ctx) := split(exp);

public default Exp split(Exp exp) = { /*println("Default split: <exp> -\> <C(exp, Ctx::hole())>");*/ C(exp, Ctx::hole()); };

@doc{The plugging operation}
public Exp plug( C(Exp exp, Ctx::hole())) = exp;
public Exp plug( C(Exp exp, Ctx::apply(Ctx ctx, Exp exp2)) ) = Exp::apply(plug(C(exp,ctx)), exp2);
public Exp plug( C(Exp exp, Ctx::apply(Value::lambda(str id, Exp e), Ctx ctx)) ) = Exp::apply(Exp::lambda(id, e), plug(C(exp,ctx)));

public Exp plug( C(Exp exp, Ctx::add(Ctx ctx, Exp exp2)) ) = Exp::add(plug(C(exp,ctx)), exp2);
public Exp plug( C(Exp exp, Ctx::add(Exp exp1, Ctx ctx)) ) = Exp::add(exp1, plug(C(exp,ctx)));

public Exp plug( C(Exp exp, Ctx::eq(Ctx ctx, Exp exp2)) ) = Exp::eq(plug(C(exp,ctx)), exp2);
public Exp plug( C(Exp exp, Ctx::eq(Value::\num(int n), Ctx ctx)) ) = Exp::eq(Exp::number(n), plug(C(exp,ctx)));

public Exp plug( C(Exp exp, Ctx::assign(str id, Ctx ctx)) ) = Exp::assign(id, plug(C(exp,ctx)));

public Exp plug( C(Exp exp, Ctx::ifelse(Ctx ctx, Exp exp2, Exp exp3)) ) = Exp::ifelse(plug(C(exp,ctx)), exp2, exp3);

public Exp plug( C(Exp exp, Ctx::config(Ctx ctx, Store store)) ) = Exp::config(plug(C(exp,ctx)), store);
public default Exp plug(Exp exp) { throw "unknown <exp>; "; }

@doc{Extension with co-routines}
public data Value =
		    label(str l)
		  | __dead()
		  ;
		   
public data Ctx =
		    labeled(str name, Ctx ctx)
		  | create(Ctx ctx)
		  | resume(Ctx ctx, Exp exp2)
		  | resume(Value \value, Ctx ctx)
		  | yield(Ctx ctx)
		  | hasNext(Ctx ctx)
		  ;
		  
public Exp split( Exp::labeled(str name, Exp exp) ) = 
	C(exp_, Ctx::labeled(name, ctx)) 
	when !isValue(exp) && C(exp_,ctx) := split(exp);
	
public Exp split( Exp::create(Exp exp) ) = 
	C(exp_, Ctx::create(ctx))
	when !isValue(exp) && C(exp_,ctx) := split(exp);
	
public Exp split( Exp::resume(Exp exp1, Exp exp2) ) = 
	C(exp2_, Ctx::resume(Value::label(l), ctx)) 
	when isValue(exp1) && Exp::label(str l) := exp1 && !isValue(exp2) && C(exp2_,ctx) := split(exp2);
	
public Exp split( Exp::resume(Exp exp1, Exp exp2) ) = 
	C(exp1_, Ctx::resume(ctx, exp2)) 
	when !isValue(exp1) && !isValue(exp2) && C(exp1_,ctx) := split(exp1);
	
public Exp split( Exp::yield(Exp exp) ) = 
	C(exp_, Ctx::yield(ctx)) 
	when !isValue(exp) && C(exp_,ctx) := split(exp);

public Exp split( Exp::hasNext(Exp exp) ) = 
	C(exp_, Ctx::hasNext(ctx)) 
	when !isValue(exp) && C(exp_,ctx) := split(exp);

public Exp plug( C(Exp exp, Ctx::labeled(str name, Ctx ctx)) ) = 
	Exp::labeled(name, plug(C(exp,ctx)));
	
public Exp plug( C(Exp exp, Ctx::create(Ctx ctx)) ) = 
	Exp::create(plug(C(exp,ctx)));
	
public Exp plug( C(Exp exp, Ctx::resume(Ctx ctx, Exp exp2)) ) = 
	Exp::resume(plug(C(exp,ctx)), exp2);
	
public Exp plug( C(Exp exp, Ctx::resume(Value::label(str l), Ctx ctx)) ) = 
	Exp::resume(Exp::label(l), plug(C(exp,ctx)));
	
public Exp plug( C(Exp exp, Ctx::yield(Ctx ctx)) ) = 
	Exp::yield(plug(C(exp,ctx)));

public Exp plug( C(Exp exp, Ctx::hasNext(Ctx ctx)) ) = 
	Exp::hasNext(plug(C(exp,ctx)));

@doc{Extension with continuations}
public data Ctx = 
		callcc(Ctx ctx)
		;

public Exp split( Exp::callcc(Exp exp) ) = 
	C(exp_, Ctx::callcc(ctx)) 
	when !isValue(exp) && C(exp_,ctx) := split(exp);

public Exp plug( C(Exp exp, Ctx::callcc(ctx)) ) = 
	Exp::callcc(plug(C(exp,ctx)));

@doc{Extension with constants and lists}
public data Value =
			  const(str id)
			| lst(list[Value] exps)
			;

public data Ctx = 
			 lst(list[Exp] head, Ctx ctx, list[Exp] tail)
		   ;

public Exp split( Exp::apply(Exp exp1, Exp exp2) ) = 
	C(exp2_, Ctx::apply(Value::const(id), ctx)) 
	when isValue(exp1) && Exp::const(str id) := exp1 && !isValue(exp2) && C(exp2_,ctx) := split(exp2);
	
public Exp split( Exp::lst([*Exp head, Exp exp, *Exp tail]) ) = 
	C(exp_, Ctx::lst(head, ctx, tail)) 
	when !isValue(exp) && C(exp_,ctx) := split(exp);

public Exp plug( C(Exp exp, Ctx::apply(Value::const(str id), Ctx ctx)) ) = 
	Exp::apply(Exp::const(id), plug(C(exp,ctx)));
	
public Exp plug( C(Exp exp, Ctx::lst(list[Exp] head, Ctx ctx, list[Exp] tail)) ) = 
	Exp::lst(head + [ plug(C(exp,ctx)) ] + tail);

@doc{Extension with recursion}
public data Ctx =
			Y(Ctx ctx)
			;
			
public Exp split( Exp::Y(Exp exp) ) = 
	C(exp_, Ctx::Y(ctx)) 
	when !isValue(exp) && C(exp_,ctx) := split(exp);

public Exp plug( C(Exp exp, Ctx::Y(ctx)) ) = 
	Exp::Y(plug(exp,ctx));

// Test that split and plug are inverse
test bool testSplit(Exp e) = plug(split(e)) == e;
