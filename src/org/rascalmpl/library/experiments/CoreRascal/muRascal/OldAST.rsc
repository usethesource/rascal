module experiments::CoreRascal::muRascal::AST

public data Exp = 
			nil()
          | \true()
          | \false()
          | number(int n)
          | fconst(str id)
          | label(str name)
          
          | var(str id, int scope, int pos)
          | lambda(str id, Exp exp)
          | apply(Exp exp1, Exp exp2)
                    
          | assign(str id, int scope, int pos, Exp exp)
          | ifelse(Exp exp1, Exp exp2, Exp exp3)
          | \while(Exp cond, Exp body)

          | labeled(str name, Exp exp)
          | create(Exp exp)
          | resume(Exp exp1, Exp exp2)
          | yield(Exp exp)
          
          | __dead()
          | hasNext(Exp exp)
          
          | block(list[Exp] exps)
		
		  | lst(list[Exp] exps)
		  
          | Y(Exp exp)
          
		  | \throw(Exp exp)
		  | \try(Exp body, Catch \catch);
			
public data Catch = 
			  \catch(str id, Exp body)
			;
