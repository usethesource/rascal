@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@synopsis{Synopsis: AST for the SMTLIBv2 Ints theory}
@contributor{Jouke Stoel - stoel@cwi.nl (CWI)}

module lang::smtlib2::theory::ints::Ast

data Sort = \int();

data Expr
	= neg(Expr val)
	| sub(Expr lhs, Expr rhs)
	| add(Expr lhs, Expr rhs)
	| mul(Expr lhs, Expr rhs)
	| div(Expr lhs, Expr rhs)
	| \mod(Expr lhs, Expr rhs)
	| abs(Expr val)
	| lte(Expr lhs, Expr rhs)
	| lt (Expr lhs, Expr rhs)
	| gte(Expr lhs, Expr rhs)
	| gt (Expr lhs, Expr rhs)
	;

data Literal
	= intVal(int i)
	;
