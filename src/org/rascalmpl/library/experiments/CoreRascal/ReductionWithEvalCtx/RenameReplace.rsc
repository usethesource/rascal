module experiments::CoreRascal::ReductionWithEvalCtx::RenameReplace

import experiments::CoreRascal::ReductionWithEvalCtx::AST;

@doc{Alpha-substitution: [ z / y ]}

@doc{The lambda expression part}
Exp rename(\true(), str y, str z) 						= \true();
Exp rename(\false(), str y, str z) 						= \false();
Exp rename(number(int n), str y, str z) 				= number(n);

Exp rename(id(x), str y, str z) 						= id(x == y ? z : x);
Exp rename(lambda(str x, Exp e), str y, str z) 			= (x == y) ? lambda(x, e) : lambda(x, rename(e, y, z));
Exp rename(apply(Exp e1, Exp e2), str y, str z) 		= apply(rename(e1, y, z), rename(e2, y, z));

Exp rename(add(Exp e1, Exp e2), str y, str z) 			= add(rename(e1, y, z), rename(e2, y, z));
Exp rename(eq(Exp e1, Exp e2), str y, str z) 			= eq(rename(e1, y, z), rename(e2, y, z));


Exp rename(assign(str x, Exp e), str y, str z) 			= assign(x == y ? z : x, rename(e, y, z)); 
Exp rename(ifelse(Exp e0, Exp e1, Exp e2), str y, str z)  
														= ifelse(rename(e0, y, z), rename(e1, y, z), rename(e2, y, z));

@doc{Extension with configurations that encapsulate semantics components, e.g, stores}
Exp rename(config(Exp exp, Store store), str y, str z) 	= config(rename(exp, y, z), store);

@doc{Extension with co-routines}
Exp rename(Exp::label(str x), str y, str z) 			= Exp::label(x == y ? z : x);
Exp rename(Exp::labeled(str x, Exp e), str y, str z) 	= Exp::labeled(x == y ? z : x, rename(e, y, z));
Exp rename(Exp::create(Exp e), str y, str z) 			= Exp::create(rename(e, y, z));
Exp rename(Exp::resume(Exp e1, Exp e2), str y, str z) 	= Exp::resume(rename(e1, y, z), rename(e2, y, z));
Exp rename(Exp::yield(Exp e), str y, str z) 			= Exp::yield(rename(e, y, z));

@doc{Extension with continuations}
Exp rename(Exp::abort(Exp e), str y, str z) 			= Exp::abort(rename(e, y, z));
Exp rename(Exp::callcc(Exp e), str y, str z) 			= Exp::callcc(rename(e, y, z));

@doc{Extension with constants and lists}
Exp rename(const(str id), str y, str z)             	= const(id);
Exp rename(lst(list[Exp] exps), str y, str z)       	= lst([ rename(exp, y, z) | exp <- exps ]);

@doc{Extension with recursion}
Exp rename(Y(Exp exp), str y, str z)                	= Y(rename(exp, y, z));

// Tests
test bool tst() = rename(Exp::id("x"), "x", "z") == Exp::id("z");
test bool tst() = rename(Exp::id("x"), "y", "z") == Exp::id("x");
test bool tst() = rename(Exp::lambda("x", id("x")), "x", "z") == Exp::lambda("x", id("x"));
test bool tst() = rename(Exp::lambda("x", id("y")), "y", "z") == Exp::lambda("x", id("z"));
test bool tst() = rename(Exp::assign("x", id("y")), "y", "z") == Exp::assign("x", id("z"));
test bool tst() = rename(Exp::assign("x", id("y")), "x", "z") == Exp::assign("z", id("y"));
test bool tst() = rename(Exp::apply(id("x"), id("y")), "x", "z") == Exp::apply(id("z"), id("y"));
test bool tst() = rename(Exp::eq(id("x"), id("y")), "x", "z") == Exp::eq(id("z"), id("y"));

@doc{Beta-substitution: [ v / y ]}

@doc{The lambda expression part}
Exp replace(\true(), str y, Exp v) 						= \true();
Exp replace(\false(), str y, Exp v) 					= \false();
Exp replace(number(int n), str y, Exp v) 				= number(n);


Exp replace(id(x), str y, Exp v) 						= x == y ? v : id(x);
Exp replace(lambda(str y, Exp e), str y, Exp v) 		= (x == y) ? lambda(x, e) :  lambda(x, replace(e, y, v));
Exp replace(apply(Exp e1, Exp e2), str y, Exp v) 		= apply(replace(e1, y, v), replace(e2, y, v));

Exp replace(add(Exp e1, Exp e2), str y, Exp v) 			= add(replace(e1, y, v), replace(e2, y, v));
Exp replace(eq(Exp e1, Exp e2), str y, Exp v) 			= eq(replace(e1, y, v), replace(e2, y, v));


Exp replace(assign(str x, Exp e), str y, Exp v) 		= assign(x, replace(e, y, v)); 
Exp replace(ifelse(Exp e0, Exp e1, Exp e2), str y, Exp v)  
														= ifelse(replace(e0, y, v), replace(e1, y, v), replace(e2, y, v));

@doc{Extension with configurations that encapsulate semantics components, e.g, stores}
Exp replace(config(Exp exp, Store store), str y, Exp v) 	
														= config(replace(exp, y, v), store); 

@doc{Extension with co-routines}

Exp replace(Exp::label(str x), str y, Exp v) 			= Exp::label(x);
Exp replace(Exp::labeled(str xx, Exp e), str y, Exp v) 	= Exp::labeled(x, replace(e, y, v));
Exp replace(Exp::create(Exp e), str y, Exp v) 			= Exp::create(replace(e, y, v));
Exp replace(Exp::resume(Exp e1, Exp e2), str y, Exp v) 	= Exp::resume(replace(e1, y, v), replace(e2, y, v));
Exp replace(Exp::yield(Exp e), str y, Exp v) 			= Exp::yield(replace(e, y, v));

@doc{Extension with continuations}
Exp replace(Exp::abort(Exp e), str y, Exp v) 			= Exp::abort(replace(e, y, v));
Exp replace(Exp::callcc(Exp e), str y, Exp v) 			= Exp::callcc(replace(e, y, v));

@doc{Extension with constants and lists}
Exp replace(const(str id), str y, Exp v)            	= const(id);
Exp replace(lst(list[Exp] exps), str y, Exp v)      	= lst([ replace(exp, y, v) | exp <- exps ]);

@doc{Extension with recursion}
Exp replace(Y(Exp exp), str y, Exp v)               	= Y(replace(exp, y, v));
