module experiments::Compiler::ReductionWithEvalCtx::ReductionWithEvalCtx

//import experiments::Compiler::ReductionWithEvalCtx::AST;
//import experiments::Compiler::ReductionWithEvalCtx::RenameReplace;
//import experiments::Compiler::ReductionWithEvalCtx::EvalCtx;
////extend experiments::CoreRascal::ReductionWithEvalCtx::Reduction;
//
//import IO;
//import List;
//import Type;
//
//// Counter for generating unique names
//public int counter = 0;
//public void resetCounter() { counter = 0; }
//public int incrementCounter() = { counter += 1; counter; };
//
//public int fvar = 0;
//public void resetFVar() { fvar = 0; }
//public int incrementFVar() = { fvar += 1; fvar; };
//
//
//public Exp step( Exp::add(Exp::number(n1), Exp::number(n2)) ) = Exp::number(n1 + n2);
//
//public Exp step( Exp::minus(Exp::number(n1), Exp::number(n2)) ) = Exp::number(n1 - n2);
//
//public Exp step( Exp::eq(Exp exp1, Exp exp2) ) = 
//	(exp1 == exp2) ? Exp::\true() : Exp::\false() 
//	when isValue(exp1) && isValue(exp2);
//	
//public Exp step( Exp::less(Exp exp1, Exp exp2) ) = 
//	(exp1 < exp2) ? Exp::\true() : Exp::\false() 
//	when isValue(exp1) && isValue(exp2);
//
//public Exp step( Exp::ifelse(Exp::\true(), Exp exp2, Exp exp3) ) = exp2;
//public Exp step( Exp::ifelse(Exp::\false(), Exp exp2, Exp exp3) ) = exp3; 
//
//public Exp step( Exp::\while(Exp cond, Exp body) ) = Exp::ifelse(cond, Exp::\block([ body, Exp::\while(cond, body) ]), nil());
//
//public Exp step( Exp::block([ Exp exp, *Exp rest ]) ) =
//	((!isEmpty(rest)) ? Exp::block(rest) : exp)
//	when isValue(exp);
//
//public Exp step( C(Exp::id(str name), Ctx::config(ctx, Store store)) ) = 
//	C(store[name], Ctx::config(ctx, store)) 
//	when (name in store);
//
//public Exp step( C(Exp::apply(Exp::lambda(str id, Exp exp1), Exp exp2), Ctx::config(ctx, Store store)) ) = 
//	{ str fresh = "fvar<incrementFVar()>"; store[fresh] = exp2; C(rename(exp1, id, fresh)/* alpha-substitution */, Ctx::config(ctx, store)); } 
//	when isValue(exp2);
//
//public Exp step( C(Exp::assign(str id, Exp exp), Ctx::config(ctx, Store store)) ) = 
//	{ store[id] = exp; C(exp, Ctx::config(ctx, store)); } 
//	when isValue(exp);
//
//@doc{Extension with co-routines}
//public Exp step( C(Exp::create(Exp exp), Ctx::config(ctx, Store store)) ) = 
//	{ 
//	  str l = "l<incrementCounter()>"; 
//	  store[l] = exp; 
//	  C(Exp::label(l), Ctx::config(ctx, store)); 
//	} 
//	when isValue(exp);
//	
//public Exp step( C(Exp::resume(Exp::label(str l), Exp exp2), Ctx::config(ctx, store)) ) = 
//	C(Exp::labeled(l, Exp::apply(store[l], exp2)), Ctx::config(ctx, store)) 
//	when isValue(exp2);
//	
//public Exp step( C(Exp::yield(Exp exp), Ctx::config(ctx, Store store)) ) =
//	{ 
//	  Ctx ctx1 = bottom-up-break visit(ctx) {
//					case Ctx::labeled(str l, Ctx ctx2) => 
//						{ 
//						  store[l] = Exp::lambda("xvar", plug(C(Exp::id("xvar"), ctx2))); 
//						  Ctx::hole(); 
//						}
//				 }; 
//	  C(exp, Ctx::config(ctx1,store));
//	}
//	when isValue(exp);
//	
//public Exp step( C(Exp::labeled(str l, Exp exp), Ctx::config(ctx, Store store)) ) = 
//	C(exp, Ctx::config(ctx, { store[l] = __dead(); store; })) 
//	when isValue(exp);
//	
//public Exp step( C(Exp::hasNext(Exp::label(str l)), Ctx::config(ctx, Store store)) ) =
//	C( (Exp::__dead() := store[l]) ? Exp::\false() : Exp::\true(), Ctx::config(ctx, store));
//
//@doc{Extension with continuations}
//public Exp step( C(Exp::abort(Exp exp), Ctx::config(ctx,store)) ) = 
//	C(exp, Ctx::config(Ctx::hole(), store));
//	
//public Exp step( C(Exp::callcc(Exp exp), Ctx::config(ctx,store)) ) = 
//	C(Exp::apply(exp, Exp::lambda("xvar", Exp::abort(plug(C(Exp::id("xvar"), ctx))))), Ctx::config(ctx, store)) 
//	when isValue(exp);
//
//@doc{Extension with constants and lists}
//public Exp step( Exp::lst(list[Exp] exps) ) = 
//	Exp::lst(exps) 
//	when isValue(Exp::lst(exps));
//public Exp step( C(Exp::apply(Exp::const(str id), Exp exp2), Ctx::config(ctx, Store store)) ) = 
//	C(delta(id, exp2), Ctx::config(ctx, store)) 
//	when isValue(exp2);
//
//@doc{Extension with recursion}
//public Exp step( C(Exp::Y(Exp exp), Ctx::config(ctx, Store store)) ) = 
//	C(Exp::apply(exp, Exp::lambda("xvar", Exp::apply(Exp::Y(exp), Exp::id("xvar")))), Ctx::config(ctx,store)) 
//	when isValue(exp);
//
//@doc{Extension with exceptions}
//public Exp step( C(Exp::\throw(Exp exp), Ctx::config(ctx1, Store store)) ) =
//	{ 
//	  println("\'Throw exception\' expression: getting a sub-context...");
//	  Ctx ctx2 = bottom-up-break visit(ctx1) {
//	  				// drops the sub-context up to the closest catch
//	  				case Ctx::\try(Ctx ctx, Catch \catch) => { println("Sub-context: <ctx>"); Ctx::\try(Ctx::hole(), \catch); } 
//	  			 };	
//	  println("Dropped the subcontext: <ctx2>");  
//	  C(Exp::\throw(exp), Ctx::config(ctx2, store)); 
//	}
//	when isValue(exp);
//
//public Exp step( C(Exp::\try(Exp exp, Catch::\catch(str id, Exp body)), Ctx::config(ctx, Store store)) ) =
//	C(exp, Ctx::config(ctx, store))
//	when isValue(exp);
//
//public Exp step( C(Exp::\try(Exp::\throw(Exp exp), Catch::\catch(str id, Exp body)), Ctx::config(ctx, Store store)) ) =
//	{ str fresh = "fvar<incrementFVar()>"; store[fresh] = exp; C(rename(body, id, fresh)/* alpha-substitution */, Ctx::config(ctx, store)); }
//	when isValue(exp);
//
//@doc{Characteristic rule}
//public Exp step( C(Exp exp, Ctx ctx) ) {
////	println("Default case of C[e]: <exp> <ctx>");
//	return if(!(Ctx::hole() := ctx)) C(reduce(exp), ctx); else C(exp, ctx);
//}
//
//public default Exp step( Exp exp ) {
////	println("Default reduction step: <exp>");
//	exp = split(exp);
////	println("Reduction step, splitting: <exp>");
//	exp = reduce(exp);
////	println("Reducing: <exp>");
//	exp = plug(exp);
////	println("Reduction step, plugging: <exp>");
//	return exp;
//}
//
//public Exp reduce(Exp e) {
//	Exp ee = e;
//	solve(ee) { ee = step(ee); }
//    return ee;
//}
//
//
//private Exp delta("_head", Exp::lst([ Exp head, *Exp tail ])) = head;
//private Exp delta("_tail", Exp::lst([ Exp head, *Exp tail ])) = Exp::lst(tail);
//private default Exp delta(str fname, Exp exp) = { throw "Delta is undefined on: \< <fname>, <exp> \>"; };
//
