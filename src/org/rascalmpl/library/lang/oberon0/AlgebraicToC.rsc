@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Anastasia Izmaylova - A.Izmaylova@cwi.nl}
module lang::oberon0::AlgebraicToC

import lang::oberon0::AST;
import lang::oberon0::utils::Parse;

import ParseTree;
import ParseTreeToAdt;

import IO;

public str toC(\mod(str _, str decls, str body, str _)) = decls + body;

public str toC(decls(str consts, str types, str vars)) = consts + types + vars;

public str toC(constDecl(str name, str \value)) = "#define " + name + " " + \value;

public str toC(typeDecl(str name, str \type)) = "typedef " + \type + " " + name;

public str toC(varDecl(str names, str \type)) = \type + " " + names;
 
public str toC(user("INTEGER")) = "int";
public str toC(user("BOOLEAN")) = "int"; 
public default str toC(user(str name)) = name;

public str toC(assign(str var, str exp)) = var + " = " + exp;
public str toC(ifThen(str condition, str body, list[tuple[str,str]] elseIfs, str elsePart)) = "if (<condition>) { <body> } <for(tuple[str,str] elseIf <- elseIfs){> else if (<elseIf[0]>) { <elseIf[1]> } <}> <if(elsePart != ""){> else { <elsePart> } <}>";
public str toC(whileDo(str condition, str body)) = "while(<condition>) { <body> }";

public str toC(nat(int val)) = "<val>";
public str toC(FExpression::\true()) = "1";
public str toC(FExpression::\false()) = "0";
public str toC(lookup(str var)) = var;
public str toC(neg(str exp)) = "(-<exp>)";
public str toC(pos(str exp)) = exp;
public str toC(not(str exp)) = "(!<exp>)";
public str toC(mul(str lhs, str rhs)) = "(<lhs> * <rhs>)";
public str toC(div(str lhs, str rhs)) = "(<lhs> / <rhs>)";
public str toC(FExpression::\mod(str lhs, str rhs)) = "(<lhs> % <rhs>)";
public str toC(amp(str lhs, str rhs)) = "(<lhs> && <rhs>)";
public str toC(add(str lhs, str rhs)) = "(<lhs> + <rhs>)";
public str toC(sub(str lhs, str rhs)) = "(<lhs> - <rhs>)";
public str toC(or(str lhs, str rhs)) = "(<lhs> || <rhs>)";
public str toC(FExpression::eq(str lhs, str rhs)) = "(<lhs> == <rhs>)";
public str toC(FExpression::neq(str lhs, str rhs)) = "(<lhs> != <rhs>)";
public str toC(lt(str lhs, str rhs)) = "(<lhs> \< <rhs>)";
public str toC(gt(str lhs, str rhs)) = "(<rhs> \< <lhs>)";
public str toC(leq(str lhs, str rhs)) = "(<lhs> \<= <rhs>)";
public str toC(geq(str lhs, str rhs)) = "(<rhs> \<= <lhs>)";
public default str toC(FExpression[str,str,str] n) { throw "incomplete"; }

public str toC(id(str name)) = name;

public str toC([ <str el0, str rest> ], type[list[Statement]] _) = el0 + ";\n" + rest;

public str toC([ <str el0, str rest> ], type[list[ConstDecl]] _) = el0 + "\n" + rest;

public str toC([ <str el0, str rest> ], type[list[TypeDecl]] _) = el0 + ";\n" + rest;

public str toC([ <str el0, str rest> ], type[list[VarDecl]] _) = el0 + ";\n" + rest;

public str toC([ <str el0, str rest> ], type[list[Ident]] _) = el0 + ";" + rest;

// Test
public str test0(loc l) {
	Tree ob01 = parse(l);
	Module m = implode(#Module,ob01);
	str (Module) f = fvisit[#Module, toC];
	str m010 = f(m);
	
	return m010;
}
