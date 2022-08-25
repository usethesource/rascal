@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@doc{
	Synopsis: Converts the concreate SMT Solver Response syntax to AST
}
@contributor{Jouke Stoel - stoel@cwi.nl (CWI)}

module lang::smtlib2::command::response::Implode

import ParseTree;

import lang::smtlib2::command::response::Parse;
import lang::smtlib2::command::response::Ast;

public Response toAst(str answer) = implode(#Response, parseResponse(answer));
