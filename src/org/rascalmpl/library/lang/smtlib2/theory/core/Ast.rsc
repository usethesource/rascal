@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@synopsis{Synopsis: AST for the SMTLIBv2 Core theory (boolean logic)}
@contributor{Jouke Stoel - stoel@cwi.nl (CWI)}

module lang::smtlib2::theory::core::Ast

data Sort = \bool();

data Expr
	= \not(Expr val)
	| implies(Expr lhs, Expr rhs)
	| and(Expr lhs, Expr rhs)
	| or(Expr lhs, Expr rhs)
	| xor(Expr lhs, Expr rhs)
	| eq(Expr lhs, Expr rhs)
	| distinct(Expr lhs, Expr rhs)
	| ite(Expr condition, Expr whenTrue, Expr whenFalse)   
	;
	
data Literal = boolVal(bool b);
