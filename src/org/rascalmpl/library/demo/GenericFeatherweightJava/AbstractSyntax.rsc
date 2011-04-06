@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
// This module defines the abstract syntax of Generic Featherweight Java.
module demo::GenericFeatherweightJava::AbstractSyntax
  
alias Name = str;

public data Type = typeVar(Name varName) | typeLit(Name className, list[Type] actuals);

alias FormalTypes = tuple[list[Type] vars, list[Type] bounds];
alias FormalVars  = tuple[list[Type] types, list[Name] names];

data Class  = class(Name className, FormalTypes formals, Type extends, FormalVars fields, Constr constr, list[Method] methods); 
data Constr = cons(FormalVars args, Super super, list[Init] inits); 
data Super  = super(list[Name] fields); 
data Init   = this(Name field);    
data Method = method(FormalTypes formalTypes, Type returnType, Name name, FormalVars formals, Expr expr); 

data Expr = var(Name varName) 
          | access(Expr receiver, Name fieldName) 
          | call  (Expr receiver, Name methodName, list[Type] actualTypes, list[Expr] actuals) 
          | new (Type class, list[Expr] actuals) 
          | cast(Type class, Expr expr);


