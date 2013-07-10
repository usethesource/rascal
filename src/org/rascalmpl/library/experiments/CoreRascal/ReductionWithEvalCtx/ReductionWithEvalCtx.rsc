module experiments::CoreRascal::ReductionWithEvalCtx::ReductionWithEvalCtx

import experiments::CoreRascal::ReductionWithEvalCtx::AST;
import experiments::CoreRascal::ReductionWithEvalCtx::EvalCtx;
extend experiments::CoreRascal::ReductionWithEvalCtx::Reduction;

import IO;
import Type;

public int counter = 0;
public void resetCounter() { counter = 0; }
public int incrementCounter() = { counter += 1; counter; };

public Exp step( Exp::add(number(n1), number(n2)) ) = Exp::number(n1 + n2);
public Exp step( Exp::eq(number(n1), number(n2)) ) = (n1 == n2) ? Exp::\true() : Exp::\false();

public Exp step( Exp::ifelse(Exp::\true(), Exp exp2, Exp exp3) ) = exp2;
public Exp step( Exp::ifelse(Exp::\false(), Exp exp2, Exp exp3) ) = exp3; 

public Exp step( C(Exp::id(str name), Ctx::config(ctx, Store store)) ) = C(store[name], Ctx::config(ctx, store)) when (name in store);
public Exp step( C(Exp::apply(Exp::lambda(str id, Exp exp1), Exp exp2), Ctx::config(ctx, Store store)) ) = { store[id] = exp2; C(exp1, Ctx::config(ctx, store)); } when isValue(exp2);
public Exp step( C(Exp::assign(str id, Exp exp), Ctx::config(ctx, Store store)) ) = { store[id] = exp; C(exp, Ctx::config(ctx, store)); } when isValue(exp);

@doc{Extension with co-routines}
public Exp step( C(Exp::create(Exp exp), Ctx::config(ctx, Store store)) ) = { str l = "l<incrementCounter()>"; store[l] = exp; C(Exp::label(l), Ctx::config(ctx, store)); } when isValue(exp);
public Exp step( C(Exp::resume(Exp::label(str l), Exp exp2), Ctx::config(ctx, store)) ) = C(Exp::labeled(l, Exp::apply(store[l], exp2)), Ctx::config(ctx, store)) when isValue(exp2);
public Exp step( C(Exp::labeled(str l, Exp exp), Ctx::config(ctx1, Store store)) ) = { store[l] = Exp::lambda("xvar", plug(C(Exp::id("xvar"), ctx2))); C(e, Ctx::config(ctx1,store)); } when !isValue(exp) && C(Exp::yield(Exp e), Ctx ctx2) := exp && !Ctx::config(_,_) := ctx2 && isValue(e);
public Exp step( C(Exp::labeled(str l, Exp exp), Ctx::config(ctx, Store store)) ) = C(exp, Ctx::config(ctx, store)) when isValue(exp);

@doc{Extension with continuations}
public Exp step( C(Exp::abort(Exp exp), Ctx::config(ctx,store)) ) = C(exp, Ctx::config(Ctx::hole(), store));
public Exp step( C(Exp::callcc(Exp exp), Ctx::config(ctx,store)) ) = C(Exp::apply(exp, Exp::lambda("xvar", Exp::abort(plug(C(Exp::id("xvar"), ctx))))), Ctx::config(ctx, store)) when isValue(exp);

public Exp step( C(Exp exp, Ctx ctx) ) {
	println("Default case of C[e]: <exp> <ctx>");
	return if(!(Ctx::hole() := ctx)) C(reduce(exp), ctx); /* characteristic rule */ else C(exp, ctx);
}

public default Exp step( Exp exp ) {
	println("Default reduction step: <exp>");
	exp = split(exp);
	println("Reduction step, splitting: <exp>");
	exp = reduce(exp);
	println("Reducing: <exp>");
	exp = plug(exp);
	println("Reduction step, plugging: <exp>");
	return exp;
}

public Exp reduce(Exp e) {
	Exp ee = e;
	solve(ee) { ee = step(ee); }
    return ee;
}




