@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@doc{
	Synopsis: Grammar of the SMTLIBv2 response
}
@contributor{Jouke Stoel - stoel@cwi.nl (CWI)}

module lang::smtlib2::command::response::Syntax

extend lang::std::Layout;

start syntax Response
	= response: CheckSat sat
	| response: GetValue model
	| response: GetUnsatCore unsatCore
	;
	
syntax CheckSat 
	= sat: "sat" 
	| unsat: "unsat" 
	| unknown: "unknown"
	;
	
syntax GetUnsatCore = unsatCore: "(" Label* labels ")";

syntax GetValue = foundValues:"(" Model* models ")";
syntax Model = model:"(" Expr var Expr val ")";

syntax Expr
	= customFunctionCall: "(" Expr functionName Expr* params ")"
	| lit: Literal lit
	| var: Id varName
	;

syntax Literal
	= intVal:Int int
	| boolVal: Bool b
	;

lexical Int = '-'? [0-9]+ !>> [0-9];

lexical Bool = "true" | "false";

keyword Keywords = "true" | "false";

lexical Id = ([a-z A-Z 0-9 _.] !<< [a-z A-Z][a-z A-Z 0-9 _.]* !>> [a-z A-Z 0-9 _.]) \Keywords;
lexical Label = [a-z A-Z 0-9 _.$%|:/,?#\[\]] !<< [a-z A-Z 0-9 _.$%|:/,?#\[\]]* !>> [a-z A-Z 0-9 _.$%|:/,?#\[\]];
