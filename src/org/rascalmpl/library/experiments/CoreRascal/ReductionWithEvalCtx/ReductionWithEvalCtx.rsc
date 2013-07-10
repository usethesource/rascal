module experiments::CoreRascal::ReductionWithEvalCtx::ReductionWithEvalCtx

import experiments::CoreRascal::ReductionWithEvalCtx::AST;
import experiments::CoreRascal::ReductionWithEvalCtx::EvalCtx;
extend experiments::CoreRascal::ReductionWithEvalCtx::Reduction;

import IO;
import Type;

public Exp step( Exp::add(number(n1), number(n2)) ) = Exp::number(n1 + n2);
public Exp step( Exp::eq(number(n1), number(n2)) ) = (n1 == n2) ? Exp::\true() : Exp::\false();

public Exp step( Exp::ifelse(Exp::\true(), Exp exp2, Exp exp3) ) = exp2;
public Exp step( Exp::ifelse(Exp::\false(), Exp exp2, Exp exp3) ) = exp3; 

public Exp step( C(Exp::id(str name), Ctx::config(ctx, store)) ) = C(store[name], Ctx::config(ctx, store)) when (name in store);
public Exp step( C(Exp::apply(Exp::lambda(str id, Exp exp1), Exp exp2), Ctx::config(ctx, store)) ) = { store_ = store + (id:exp2); C(exp2, Ctx::config(ctx, store_)); } when isValue(exp2);
public Exp step( C(Exp::assign(str id, Exp exp), Ctx::config(ctx, store)) ) = { println(typeOf(store)); store_ = store + (id:exp); C(exp, Ctx::config(ctx, store_)); } when isValue(exp);
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




