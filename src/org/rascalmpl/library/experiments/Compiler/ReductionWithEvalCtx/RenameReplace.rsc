module experiments::Compiler::ReductionWithEvalCtx::RenameReplace

//import experiments::Compiler::ReductionWithEvalCtx::AST;
//
//@doc{Alpha-substitution: [ z / y ]}
//
//@doc{The lambda expression part}
//Exp rename(nil(), str y, str z)                         = nil();
//Exp rename(\true(), str y, str z) 						= \true();
//Exp rename(\false(), str y, str z) 						= \false();
//Exp rename(number(int n), str y, str z) 				= number(n);
//
//Exp rename(id(x), str y, str z) 						= id(x == y ? z : x);
//Exp rename(lambda(str x, Exp e), str y, str z) 			= (x == y) ? lambda(x, e) : lambda(x, rename(e, y, z));
//Exp rename(apply(Exp e1, Exp e2), str y, str z) 		= apply(rename(e1, y, z), rename(e2, y, z));
//
//Exp rename(add(Exp e1, Exp e2), str y, str z) 			= add(rename(e1, y, z), rename(e2, y, z));
//Exp rename(minus(Exp e1, Exp e2), str y, str z) 		= minus(rename(e1, y, z), rename(e2, y, z));
//Exp rename(eq(Exp e1, Exp e2), str y, str z) 			= eq(rename(e1, y, z), rename(e2, y, z));
//Exp rename(less(Exp e1, Exp e2), str y, str z) 			= less(rename(e1, y, z), rename(e2, y, z));
//
//Exp rename(assign(str x, Exp e), str y, str z) 			= assign(x == y ? z : x, rename(e, y, z)); 
//Exp rename(ifelse(Exp e0, Exp e1, Exp e2), str y, str z)  
//														= ifelse(rename(e0, y, z), rename(e1, y, z), rename(e2, y, z));
//
//Exp rename(\while(Exp cond, Exp body), str y, str z)    = \while(rename(cond, y, z), rename(body, y, z));
//														
//Exp rename(block(list[Exp] exps), str y, str z)         = block([ rename(exp, y, z) | Exp exp <- exps ]);
//
//@doc{Extension with configurations that encapsulate semantics components, e.g, stores}
//Exp rename(config(Exp exp, Store store), str y, str z) 	= config(rename(exp, y, z), store);
//
//@doc{Extension with co-routines}
//Exp rename(label(str x), str y, str z) 			    = Exp::label(x == y ? z : x);
//Exp rename(labeled(str x, Exp e), str y, str z) 	= Exp::labeled(x == y ? z : x, rename(e, y, z));
//Exp rename(create(Exp e), str y, str z) 			= Exp::create(rename(e, y, z));
//Exp rename(resume(Exp e1, Exp e2), str y, str z) 	= Exp::resume(rename(e1, y, z), rename(e2, y, z));
//Exp rename(yield(Exp e), str y, str z) 			    = Exp::yield(rename(e, y, z));
//Exp rename(hasNext(Exp e), str y, str z) 			= Exp::hasNext(rename(e, y, z));
//
//@doc{Extension with continuations}
//Exp rename(abort(Exp e), str y, str z) 			    = Exp::abort(rename(e, y, z));
//Exp rename(callcc(Exp e), str y, str z) 			= Exp::callcc(rename(e, y, z));
//
//@doc{Extension with constants and lists}
//Exp rename(const(str id), str y, str z)             	= const(id);
//Exp rename(lst(list[Exp] exps), str y, str z)       	= lst([ rename(exp, y, z) | exp <- exps ]);
//
//@doc{Extension with recursion}
//Exp rename(Y(Exp exp), str y, str z)                	= Y(rename(exp, y, z));
//
//@doc{Extension with exception handling}
//Exp rename(\throw(Exp exp), str y, str z)               = \throw(rename(exp, y, z));
//Exp rename(\try(Exp body, Catch \catch), str y, str z)  = \try(rename(body, y, z), rename(\catch, y, z));
//Catch rename(\catch(str id, Exp body), str y, str z)    = (id == y) ? \catch(id, body) : \catch(id, rename(body, y, z));
//
//// Tests
//test bool tst() = rename(Exp::id("x"), "x", "z") == Exp::id("z");
//test bool tst() = rename(Exp::id("x"), "y", "z") == Exp::id("x");
//test bool tst() = rename(Exp::lambda("x", id("x")), "x", "z") == Exp::lambda("x", id("x"));
//test bool tst() = rename(Exp::lambda("x", id("y")), "y", "z") == Exp::lambda("x", id("z"));
//test bool tst() = rename(Exp::assign("x", id("y")), "y", "z") == Exp::assign("x", id("z"));
//test bool tst() = rename(Exp::assign("x", id("y")), "x", "z") == Exp::assign("z", id("y"));
//test bool tst() = rename(Exp::apply(id("x"), id("y")), "x", "z") == Exp::apply(id("z"), id("y"));
//test bool tst() = rename(Exp::eq(id("x"), id("y")), "x", "z") == Exp::eq(id("z"), id("y"));
//
//@doc{Beta-substitution: [ v / y ]}
//
//@doc{The lambda expression part}
//Exp replace(nil(), str y, Exp v)                        = nil();
//Exp replace(\true(), str y, Exp v) 						= \true();
//Exp replace(\false(), str y, Exp v) 					= \false();
//Exp replace(number(int n), str y, Exp v) 				= number(n);
//
//
//Exp replace(id(x), str y, Exp v) 						= x == y ? v : id(x);
//Exp replace(lambda(str y, Exp e), str y, Exp v) 		= (x == y) ? lambda(x, e) :  lambda(x, replace(e, y, v));
//Exp replace(apply(Exp e1, Exp e2), str y, Exp v) 		= apply(replace(e1, y, v), replace(e2, y, v));
//
//Exp replace(add(Exp e1, Exp e2), str y, Exp v) 			= add(replace(e1, y, v), replace(e2, y, v));
//Exp replace(minus(Exp e1, Exp e2), str y, Exp v) 		= minus(replace(e1, y, v), replace(e2, y, v));
//Exp replace(eq(Exp e1, Exp e2), str y, Exp v) 			= eq(replace(e1, y, v), replace(e2, y, v));
//Exp replace(less(Exp e1, Exp e2), str y, Exp v) 		= less(replace(e1, y, v), replace(e2, y, v));
//
//Exp replace(assign(str x, Exp e), str y, Exp v) 		= assign(x, replace(e, y, v)); 
//Exp replace(ifelse(Exp e0, Exp e1, Exp e2), str y, Exp v)  
//														= ifelse(replace(e0, y, v), replace(e1, y, v), replace(e2, y, v));
//														
//Exp replace(\while(Exp cond, Exp body), str y, Exp v)   = \while(replace(cond, y, v), replace(body, y, v));
//
//Exp replace(block(list[Exp] exps), str y, str z)        = block([ replace(exp, y, z) | Exp exp <- exps ]);
//
//@doc{Extension with configurations that encapsulate semantics components, e.g, stores}
//Exp replace(config(Exp exp, Store store), str y, Exp v) 	
//														= config(replace(exp, y, v), store); 
//
//@doc{Extension with co-routines}
//
//Exp replace(label(str x), str y, Exp v) 			= Exp::label(x);
//Exp replace(labeled(str xx, Exp e), str y, Exp v) 	= Exp::labeled(x, replace(e, y, v));
//Exp replace(create(Exp e), str y, Exp v) 			= Exp::create(replace(e, y, v));
//Exp replace(resume(Exp e1, Exp e2), str y, Exp v) 	= Exp::resume(replace(e1, y, v), replace(e2, y, v));
//Exp replace(yield(Exp e), str y, Exp v) 			= Exp::yield(replace(e, y, v));
//Exp replace(hasNext(Exp e), str y, Exp v) 			= Exp::hasNext(hasNext(e, y, v));
//
//@doc{Extension with continuations}
//Exp replace(abort(Exp e), str y, Exp v) 			= Exp::abort(replace(e, y, v));
//Exp replace(callcc(Exp e), str y, Exp v) 			= Exp::callcc(replace(e, y, v));
//
//@doc{Extension with constants and lists}
//Exp replace(const(str id), str y, Exp v)            	= const(id);
//Exp replace(lst(list[Exp] exps), str y, Exp v)      	= lst([ replace(exp, y, v) | exp <- exps ]);
//
//@doc{Extension with recursion}
//Exp replace(Y(Exp exp), str y, Exp v)               	= Y(replace(exp, y, v));
//
//@doc{Extension with exception handing}
//Exp replace(\throw(Exp exp), str y, Exp v)              = \throw(replace(exp, y, v));
//Exp replace(\try(Exp body), Catch \catch, str y, Exp v) = \try(replace(body, y, v), replace(\catch, y, v));
//Catch replace(\catch(str id, Exp body), str y, Exp v)   = (id == y) ? \catch(id, body) : \catch(id, replace(body, y, v)); 
