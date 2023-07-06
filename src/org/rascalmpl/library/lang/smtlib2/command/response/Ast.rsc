@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@synopsis{Synopsis: Represents the AST of the SMT solver response}
@contributor{Jouke Stoel - stoel@cwi.nl (CWI)}

module lang::smtlib2::command::response::Ast

data Response
	= response(CheckSat sat)
	| response(GetUnsatCore unsatCore)
	| response(GetValue model)
	| none()
	;

data CheckSat
	= sat()
	| unsat()
	| unknown()
	;
	
data GetUnsatCore = unsatCore(list[str] labels);
	
data GetValue = foundValues(list[Model] models);
data Model = model(Expr var, Expr val);

data Expr;
