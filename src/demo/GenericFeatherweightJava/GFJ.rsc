// This module defines the abstract syntax of Generic Featherweight Java.
module demo::GenericFeatherweightJava::GFJ
  
alias Name = str;

data Type = typeVar(Name variableName) | typeLit(Name className, list[Type] actuals); // N, C

alias FormalTypes = tuple[list[Type] vars, list[Type] bounds];
alias FormalVars  = tuple[list[Type] types, list[Name] names];

data Class = class(Name className, FormalTypes formals, Type extends, FormalVars fields, Constr constr, list[Method] methods); 
data Constr = cons(FormalVars args, Super super, list[Init] inits); 
data Super  = super(list[Name] fields); 
data Init   = this(Name field);    
data Method = method(FormalTypes formalTypes, Type returnType, Name name, FormalVars formals, e e); 

data Expr = var(Name varName) 
          | access(Expr receiver, Name fieldName) 
          | call(Expr receiver, Name methodName, list[Type] actualTypes, list[Expr] actuals) 
          | new(Type class, list[Expr] actuals) 
          | cast(Type class, Expr expr) 
          | this;  
       
