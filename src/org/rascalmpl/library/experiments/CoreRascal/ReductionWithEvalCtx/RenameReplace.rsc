module experiments::CoreRascal::ReductionWithEvalCtx::RenameReplace

import experiments::CoreRascal::ReductionWithEvalCtx::AST;

@doc{The lambda expression part}
Exp rename(Exp::\true(), str y, str z) 					= Exp::\true();
Exp rename(Exp::\false(), str y, str z) 					= Exp::\false();
Exp rename(Exp::number(int n), str y, str z) 			= Exp::number(n);

Exp rename(Exp::id(x), str y, str z) 					= Exp::id(x == y ? z : x);
Exp rename(Exp::lambda(str x, Exp e), str y, str z) 	= (x == y) ? Exp::lambda(x, e) :  Exp::lambda(x, rename(e, y, z));
Exp rename(Exp::apply(Exp e1, Exp e2), str y, str z) 	= Exp::apply(rename(e1, y, z), rename(e2, y, z));

Exp rename(Exp::add(Exp e1, Exp e2), str y, str z) 		= Exp::add(rename(e1, y, z), rename(e2, y, z));
Exp rename(Exp::eq(Exp e1, Exp e2), str y, str z) 		= Exp::eq(rename(e1, y, z), rename(e2, y, z));

Exp rename(Exp::assign(str x, Exp e), str y, str z) 	= Exp::assign(x == y ? z : x, rename(e, y, z)); 
Exp rename(Exp::ifelse(Exp e0, Exp e1, Exp e2), str y, str z)  
														= Exp::ifelse(rename(e0, y, z), rename(e1, y, z), rename(e1, y, z));

// Exp rename(config(Exp exp, Stire store), str y, str z) 	= ?

@doc{Extension with co-routines}
Exp rename(Exp::label(str x), str y, str z) 			= Exp::label(x == y ? z : x);
Exp rename(Exp::labeled(str x, Exp e), str y, str z) 	= Exp::labeled(x == y ? z : x, rename(e, y, z));
Exp rename(Exp::create(Exp e), str y, str z) 			= Exp::create(rename(e, y, z));
Exp rename(Exp::resume(Exp e1, Exp e2), str y, str z) 	= Exp::resume(rename(e1, y, z), rename(e2, y, z));
Exp rename(Exp::yield(Exp e), str y, str z) 			= Exp::yield(rename(e, y, z));

@doc{Extension with continuations}
Exp rename(Exp::abort(Exp e), str y, str z) 			= Exp::abort(rename(e, y, z));
Exp rename(Exp::callcc(Exp e), str y, str z) 			= Exp::callcc(rename(e, y, z));

test bool tst() = rename(Exp::id("x"), "x", "z") == Exp::id("z");
test bool tst() = rename(Exp::id("x"), "y", "z") == Exp::id("x");
test bool tst() = rename(Exp::lambda("x", id("x")), "x", "z") == Exp::lambda("x", id("x"));
test bool tst() = rename(Exp::lambda("x", id("y")), "y", "z") == Exp::lambda("x", id("z"));
test bool tst() = rename(Exp::assign("x", id("y")), "y", "z") == Exp::assign("x", id("z"));
test bool tst() = rename(Exp::assign("x", id("y")), "x", "z") == Exp::assign("z", id("y"));
test bool tst() = rename(Exp::apply(id("x"), id("y")), "x", "z") == Exp::apply(id("z"), id("y"));
test bool tst() = rename(Exp::eq(id("x"), id("y")), "x", "z") == Exp::eq(id("z"), id("y"));


@doc{The lambda expression part}
Exp replace(Exp::\true(), str y, Exp v) 				= Exp::\true();
Exp replace(Exp::\false(), str y, Exp v) 				= Exp::\false();
Exp replace(Exp::number(int n), str y, Exp v) 			= Exp::number(n);

Exp replace(Exp::id(x), str y, Exp v) 					= x == y ?v : Exp::id(x);
Exp replace(Exp::lambda(str y, Exp e), str y, Exp v) 	= (x == y) ? Exp::lambda(x, e) :  Exp::lambda(x, replace(e, y, v));
Exp replace(Exp::apply(Exp e1, Exp e2), str y, Exp v) 	= Exp::apply(replace(e1, y, v), replace(e2, y, v));

Exp replace(Exp::add(Exp e1, Exp e2), str y, Exp v) 	= Exp::add(replace(e1, y, v), replace(e2, y, v));
Exp replace(Exp::eq(Exp e1, Exp e2), str y, Exp v) 		= Exp::eq(replace(e1, y, v), replace(e2, y, v));

Exp replace(Exp::assign(str x, Exp e), str y, Exp v) 	= Exp::assign(x, replace(e, y, v)); 
Exp replace(Exp::ifelse(Exp e0, Exp e1, Exp e2), str y, Exp v)  
														= Exp::ifelse(replace(e0, y, v), replace(e1, y, v), replace(e1, y, v));

// Exp replace(config(Exp vxp, Stire store), str y, Exp v) 	= ?

@doc{Extension with co-routines}
Exp replace(Exp::label(str x), str y, Exp v) 			= Exp::label(x);
Exp replace(Exp::labeled(str xx, Exp e), str y, Exp v) 	= Exp::labeled(x, replace(e, y, v));
Exp replace(Exp::create(Exp e), str y, Exp v) 			= Exp::create(replace(e, y, v));
Exp replace(Exp::resume(Exp e1, Exp e2), str y, Exp v) 	= Exp::resume(replace(e1, y, v), replace(e2, y, v));
Exp replace(Exp::yield(Exp e), str y, Exp v) 			= Exp::yield(replace(e, y, v));

@doc{Extension with continuations}
Exp replace(Exp::abort(Exp e), str y, Exp v) 			= Exp::abort(replace(e, y, v));
Exp replace(Exp::callcc(Exp e), str y, Exp v) 			= Exp::callcc(replace(e, y, v));
