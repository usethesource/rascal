@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@synopsis{Synopsis: The SMTLIBv2 Command AST. This is the basic AST needed to construct SMTLIBv2 constraint problems. Sorts, Expressions and Literals are implemented in the different theories}
@contributor{Jouke Stoel - stoel@cwi.nl (CWI)}

module lang::smtlib2::command::Ast

data Script = script(list[Command] commands);

data Command 
	= setLogic(Logic logic)
	| setOption(Option option)
	| setInfo(Info info)
	| declareSort(str name, int arity)
	| defineSort(str name, list[str] sorts, list[Sort] types)
	| declareFunction(str name, list[Sort] params, Sort returnType)
	| defineFunction(str name, list[SortedVar] sParams, Sort returnType, Expr body)
	| \assert(Expr expr)
	| checkSatisfiable()
	| getValue(list[Expr] exprs)
	| getUnsatCore()
	| push(int nr)
	| pop(int nr)
	| exit()
	;

data SortedVar = sortedVar(str name, Sort sort);

// Sorts are filled by the different theories	
data Sort = custom(str name);

// Expressions are filled by the different theories
data Expr 
	= var(str name)
	| lit(Literal lit)
	| named(Expr labeledExpr, str label)
	| customFunctionCall(str name, list[Expr] params)
	;

data Literal; 
	
data Option
	= printSuccess(bool val)
	| regularOutputChannel(str channel)
	| diagnosticOutputChannel(str channel)
	| expandDefinitions(bool val)
	| interactiveMode(bool val)
	| produceProofs(bool val)
	| produceUnsatCores(bool val)
	| produceModels(bool val)
	| produceAssignments(bool val)
	| randomSeed(int seed)
	| verbosity(int level)
	;

data Logic = logic();
data Info = info();
