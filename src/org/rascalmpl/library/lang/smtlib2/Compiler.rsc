@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@doc{
	Synopsis: Translate the SMTLIBv2 AST to string so that it can be interpreted by a SMTLIBv2 compliant solver 
}
@contributor{Jouke Stoel - stoel@cwi.nl (CWI)}

module lang::smtlib2::Compiler

import lang::smtlib2::command::Ast;
import lang::smtlib2::theory::core::Ast;
import lang::smtlib2::theory::ints::Ast;

 
list[str] toString(Script script) = [toString(command) | command <- script.commands];

// Commands
str toString(setLogic(Logic logic)) = "(not yet implemented)";
str toString(setOption(Option option)) = "(set-option <toString(option)>)";
str toString(setInfo(Info info)) = "(not yet implemented)";
str toString(declareSort(str name, int arity)) = "(not yet implemented)";
str toString(defineSort(str name, list[str] sorts, list[Sort] types)) = "(not yet implemented)";
str toString(declareFunction(str name, list[Sort] params, Sort returnType)) = "(declare-fun <name> (<toString(params)>) <toString(returnType)>)";
str toString(defineFunction(str name, list[SortedVar] params, Sort returnType, Expr body)) = "(define-fun <name> (<toString(params)>) <toString(returnType)> <toString(body)>)";
str toString(\assert(Expr expr)) = "(assert <toString(expr)>)";
str toString(checkSatisfiable()) = "(check-sat)";
str toString(getValue(exprs)) = "(get-value (<(""| "<it> <toString(expr)>" | expr <- exprs )>))";
str toString(getUnsatCore()) = "(get-unsat-core)";
str toString(push(nr)) = "(push <nr>)";
str toString(pop(nr)) = "(pop <nr>)";
str toString(exit()) = "(exit)";
default str toString(Command command) = "(unkown command)";

// Options
str toString(interactiveMode(val)) = ":interactive-mode <val>";
str toString(printSuccess(bool val)) = ":print-success <val>";
str toString(regularOutputChannel(channel)) = ":regular-output-channel <channel>";
str toString(diagnosticOutputChannel(str channel)) = ":diagnostic-output-channel <channel>";
str toString(expandDefinitions(bool val)) = ":expand-definitions <val>";
str toString(produceProofs(bool val)) = ":produce-proofs <val>";
str toString(produceUnsatCores(bool val)) = ":produce-unsat-cores <val>";
str toString(produceModels(bool val)) = ":produce-models <val>";
str toString(produceAssignments(bool val)) = ":produce-assignments <val>";
str toString(randomSeed(int seed)) = ":random-seed <seed>";
str toString(verbosity(int level)) = ":verbosity <level>";

// Sorts
str toString(list[SortedVar] params) = ("" | "<it> (<param.name> <toString(param.sort)>)" | param <- params); 
str toString(list[Sort] sorts) = ("" | "<it> <toString(sort)>" | sort <- sorts); 
str toString(\int()) = "Int";
str toString(\bool())= "Bool";

// Literals
str toString(boolVal(b)) = b ? "true" : "false";
str toString(intVal(i)) = "<i>";	

// Expr
str toString(var(str name)) = "<name>";
str toString(lit(Literal lit)) = toString(lit);
str toString(named(labeledExpr, label)) = "(! <toString(labeledExpr)> :named <label>)";
str toString(customFunctionCall(functionName, params)) = "(<functionName> <("" | "<it> <toString(param)>" | param <- params)>)";

// From core
str toString(\not(val)) = "(not <toString(val)>)";
str toString(implies(lhs, rhs)) = "(=\> <toString(lhs)> <toString(rhs)>)";
str toString(and(lhs, rhs)) = "(and <toString(lhs)> <toString(rhs)>)";
str toString(or(lhs, rhs)) = "(or <toString(lhs)> <toString(rhs)>)";
str toString(xor(lhs, rhs)) = "(xor <toString(lhs)> <toString(rhs)>)";
str toString(eq(lhs, rhs)) = "(= <toString(lhs)> <toString(rhs)>)";
str toString(distinct(lhs, rhs)) = "(distinct <toString(lhs)> <toString(rhs)>)";
str toString(ite(condition, whenTrue, whenFalse)) = "(ite <toString(condition)> <toString(whenTrue)> <toString(whenFalse)>)";   

// From ints
str toString(neg(val)) = "(- <toString(val)>)";
str toString(sub(lhs, rhs)) = "(- <toString(lhs)> <toString(rhs)>)";
str toString(add(lhs, rhs)) = "(+ <toString(lhs)> <toString(rhs)>)";
str toString(mul(lhs, rhs)) = "(* <toString(lhs)> <toString(rhs)>)";
str toString(div(lhs, rhs)) = "(div <toString(lhs)> <toString(rhs)>)";
str toString(\mod(lhs, rhs)) = "(mod <toString(lhs)> <toString(rhs)>)";
str toString(abs(val)) = "(abs <toString(val)>)";
str toString(lte(lhs, rhs)) = "(\<= <toString(lhs)> <toString(rhs)>)";
str toString(lt (lhs, rhs)) = "(\< <toString(lhs)> <toString(rhs)>)";
str toString(gte(lhs, rhs)) = "(\>= <toString(lhs)> <toString(rhs)>)";
str toString(gt (lhs, rhs)) = "(\> <toString(lhs)> <toString(rhs)>)";
