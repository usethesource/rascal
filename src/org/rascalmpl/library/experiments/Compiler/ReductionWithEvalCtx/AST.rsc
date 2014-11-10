module experiments::Compiler::ReductionWithEvalCtx::AST

//@doc{The lambda expression part}
//public data Exp = 
//			nil()
//          | \true()
//          | \false()
//          | number(int n)         
//          | id(str name)
//          | lambda(str id, Exp exp)
//          | apply(Exp exp1, Exp exp2)
//          
//          | add(Exp exp1, Exp exp2)
//          | minus(Exp exp1, Exp exp2)
//          | eq(Exp exp1, Exp exp2)
//          | less(Exp exp1, Exp exp2)
//          
//          | assign(str id, Exp exp)
//          | ifelse(Exp exp1, Exp exp2, Exp exp3)
//          | \while(Exp cond, Exp body)
//		  ;
//
//@doc{Extension with configurations that encapsulate semantics components, e.g, stores}		    
//public data Exp =
//		 	config(Exp exp, Store store)
//          ;
//
//public alias Store = map[str,Exp];
//
//public bool isValue(nil()) = true;
//public bool isValue(\true()) = true;
//public bool isValue(\false()) = true;
//public bool isValue(number(int n)) = true;
//public bool isValue(lambda(str id, Exp exp)) = true;
//public default bool isValue(Exp e) = false;
//
//@doc{Extension with co-routines}
//public data Exp =
//		    label(str name)
//          | labeled(str name, Exp exp)
//          | create(Exp exp)
//          | resume(Exp exp1, Exp exp2)
//          | yield(Exp exp)
//          
//          | __dead()
//          | hasNext(Exp exp)
//          
//          | block(list[Exp] exps)
//          ;
//
//public bool isValue(label(str name)) = true;
//public bool isValue(__dead()) = true;
//
//@doc{Extension with continuations}
//public data Exp =
//			  abort(Exp exp)
//			| callcc(Exp exp)
//			;
//
//@doc{Extension with constants and lists}
//public data Exp = 
//			  const(str id)
//			| lst(list[Exp] exps)
//            ;
//public bool isValue(const(str id)) = true;
//public bool isValue(lst(list[Exp] exps)) = ( true | it && isValue(exp) | exp <- exps );
//
//@doc{Extension with recursion}
//public data Exp =
//			Y(Exp exp)
//			;
//
//@doc{Extension with exceptions}
//public data Exp =
//			  \throw(Exp exp)
//			| \try(Exp body, Catch \catch);
//			
//public data Catch = 
//			  \catch(str id, Exp body)
//			;
