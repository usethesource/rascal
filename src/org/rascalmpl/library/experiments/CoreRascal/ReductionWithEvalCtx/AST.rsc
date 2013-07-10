module experiments::CoreRascal::ReductionWithEvalCtx::AST

@doc{The lambda expression part}
@doc{e = true | false | Num | Id | lambda x.e | e e | e + e | e == e | x := e | if e then e else e}
public data Exp = 
            \true()
          | \false()
          | number(int n)
          
          | id(str name)
          | lambda(str id, Exp exp)
          | apply(Exp exp1, Exp exp2)
          
          | add(Exp exp1, Exp exp2)
          | eq(Exp exp1, Exp exp2)
          
          | assign(str id, Exp exp)
          | ifelse(Exp exp1, Exp exp2, Exp exp3)    
          
          | config(Exp exp, Store store) // tuple of an expression and semantic components - <e,Store>
          ;

public alias Store = map[str,Exp];

public bool isValue(Exp::\true()) = true;
public bool isValue(Exp::\false()) = true;
public bool isValue(number(int n)) = true;
public bool isValue(lambda(str id, Exp exp)) = true;
public default bool isValue(Exp e) = false;

@doc{Extension with co-routines}
public data Exp =
		    label(str name)
          | labeled(str name, Exp exp)
          | create(Exp exp)
          | resume(Exp exp1, Exp exp2)
          | yield(Exp exp)
          ;

public bool isValue(label(str name)) = true;
