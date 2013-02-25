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

public str toC1(FModule[str,str,str] n, type[Module] \type = #Module) = n.decls + n.body;
public str toC2(FDeclarations[str,str,str] n, type[Declarations] \type = #Declarations) = n.consts + n.types + n.vars;
public str toC3(FConstDecl[str,str] n, type[ConstDecl] \type = #ConstDecl) = "#define " + n.name + " " + n.\value;
public str toC4(FTypeDecl[str,str] n, type[TypeDecl] \type = #TypeDecl) = "typedef " + n.\type + " " + n.name;
public str toC5(FVarDecl[str,str] n, type[VarDecl] \type = #VarDecl) = n.\type + " " + n.names; 
public str toC6(FType[str] n, type[Type] \type = #Type) = n.name;
public str toC7(FStatement[str,str,str,list[tuple[str,str]]] n, type[Statement] \type = #Statement) {
	switch(n) {
		case assign(str var, str exp) : return var + " = " + exp;
		case ifThen(str condition, str body, list[tuple[str,str]] elseIfs, str elsePart) : 
			return "if (<condition>) { <body> } <for(tuple[str,str] elseIf <- elseIfs){> else if (<elseIf[0]>) { <elseIf[1]> } <}> <if(elsePart != ""){> else { <elsePart> } <}>";
		case whileDo(str condition, str body) : 
			return "while(<condition>) { <body> }";
	}
}

public str toC8(FExpression[str,str,str] n, type[Expression] \type = #Expression) {
	switch(n) {
		case nat(str val): return val;
		case \true(): return "1";
		case \false(): return "0";
		case lookup(str var): return var;
		case neg(str exp): return "(-<exp>)";
		case pos(str exp): return exp;
		case not(str exp): return "(!<exp>)";
		case mul(str lhs, str rhs): return "(<lhs> * <rhs>)";
		case div(str lhs, str rhs): return "(<lhs> / <rhs>)";
		case \mod(str lhs, str rhs): return "(<lhs> % <rhs>)";
		case amp(str lhs, str rhs): return "(<lhs> && <rhs>)";
		case add(str lhs, str rhs): return "(<lhs> + <rhs>)";
		case sub(str lhs, str rhs): return "(<lhs> - <rhs>)";
		case or(str lhs, str rhs): return "(<lhs> || <rhs>)";
		case eq(str lhs, str rhs): return "(<lhs> == <rhs>)";
		case neq(str lhs, str rhs): return "(<lhs> != <rhs>)";
		case lt(str lhs, str rhs): return "(<lhs> \< <rhs>)";
		case gt(str lhs, str rhs): return "(<rhs> \< <lhs>)";
		case leq(str lhs, str rhs): return "(<lhs> \<= <rhs>)";
		case geq(str lhs, str rhs): return "(<rhs> \<= <lhs>)";
	}
}

public str toC9(FIdent[str] n, type[Ident] \type = #Ident) = n.name;

public str toC10(list[tuple[str,str]] n, type[list[Statement]] \type = #list[Statement]) = ([<str el0, str rest>] := n) ? el0 + ";\n" + rest : "";
public str toC11(list[tuple[str,str]] n, type[list[ConstDecl]] \type = #list[ConstDecl]) = ([<str el0, str rest>] := n) ? el0 + "\n" + rest : "";
public str toC12(list[tuple[str,str]] n, type[list[TypeDecl]] \type = #list[TypeDecl]) = ([<str el0, str rest>] := n) ? el0 + ";\n" + rest : "";
public str toC13(list[tuple[str,str]] n, type[list[VarDecl]] \type = #list[VarDecl]) = ([<str el0, str rest>] := n) ? el0 + ";\n" + rest : "";
public str toC14(list[tuple[str,str]] n, type[list[Ident]] \type = #list[Ident]) = ([<str el0, str rest>] := n) ? el0 + ";" + rest : "";
public list[tuple[str,str]] toC15(list[tuple[tuple[str,str],list[tuple[str,str]]]] n, type[list[tuple[Expression,list[Statement]]]] \type = #list[tuple[Expression,list[Statement]]]) = [ *([el0] + rest) | <tuple[str,str] el0, list[tuple[str,str]] rest> <- n ];

public tuple[str,str] toC16(tuple[str,str] n, type[tuple[Expression,list[Statement]]] \type = #tuple[Expression,list[Statement]]) = n;

public str toC17(str s, type[str] \type = #str) {
	switch(s) {
		case "INTEGER": return "int";
		case "BOOLEAN": return "int";
		default: return s;
	}
}
public str toC18(int i, type[int] \type = #int) = "<i>";

public str test0(loc l) {
	Tree ob01 = parse(l);
	Module m = implode(#Module,ob01);
	str (Module) f = fvisit[<#Module>, <toC1,toC2,toC3,toC4,toC5,toC6,toC7,toC8,toC9,toC10,toC11,toC12,toC13,toC14,toC15,toC16,toC17,toC18>];
	str m010 = f(m);
	
	return m010;
}
