@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Anastasia Izmaylova - A.Izmaylova@cwi.nl}
module lang::oberon0::AttrAlgebraicToC

import lang::oberon0::AST;
import lang::oberon0::utils::Parse;


import List;
import ParseTree;
import ParseTreeToAdt;
import util::Math;

import IO;

public alias AttrPP = tuple[int,str] (int); // two attributes, int x; (inherited) and int y; (synthesized) x -> <y,pp>
public int attrFunc(int vals...) = (!isEmpty(vals)) ? max(vals) : 0;

public AttrPP toC1(FModule[AttrPP,AttrPP,AttrPP] n, type[Module] \type = #Module) { 
	AttrPP decls = n.decls; AttrPP body = n.body; 
	int y = 0;
	str pp = "";
	solve(y) {
		tuple[int,str] declspart = decls(y);
		tuple[int,str] bodypart = body(y);
		y = attrFunc(declspart[0], bodypart[0]);
		pp = declspart[1] + bodypart[1];
	};
	return tuple[int,str] (int x) { return <y, pp>; }; 
}
public AttrPP toC2(FDeclarations[AttrPP,AttrPP,AttrPP] n, type[Declarations] \type = #Declarations)
 	= tuple[int,str] (int x) { 
 	tuple[int,str] consts = n.consts(x); tuple[int,str] types = n.types(x); tuple[int,str] vars = n.vars(x); 
 	return <attrFunc(consts[0], types[0], vars[0]), consts[1] + types[1] + vars[1]>; 
 	};

public AttrPP toC3(FConstDecl[AttrPP,AttrPP] n, type[ConstDecl] \type = #ConstDecl)
= 	tuple[int,str] (int x) { 
tuple[int,str] name = n.name(x); tuple[int,str] val = n.\value(x); 
return <attrFunc(name[0], val[0]), "#define " + name[1] + " " + val[1]>; 
};

public AttrPP toC4(FTypeDecl[AttrPP,AttrPP] n, type[TypeDecl] \type = #TypeDecl)
	= tuple[int,str] (int x) { 
	tuple[int,str] t = n.\type(x); tuple[int,str] name = n.name(x); 
	return <attrFunc(t[0], name[0]), "typedef " + t[1] + " " + name[1]>; 
	};

public AttrPP toC5(FVarDecl[AttrPP,AttrPP] n, type[VarDecl] \type = #VarDecl)
	= tuple[int,str] (int x) { 
		tuple[int,str] t = n.\type(x); 	tuple[int,str] names = n.names(x); 
	return <attrFunc(t[0], names[0]), t[1] + " " + names[1]>; 
	};
	 
public AttrPP toC6(FType[AttrPP] n, type[Type] \type = #Type)
	= tuple[int,str] (int x) { 
		tuple[int,str] name = n.name(x); 
		return <attrFunc(name[0]), name[1]>; 
		};
		
public AttrPP toC7(FStatement[AttrPP,AttrPP,AttrPP,list[tuple[AttrPP,AttrPP]]] n, type[Statement] \type = #Statement) {
	switch(n) {
		case assign(AttrPP var, AttrPP exp) :
			return tuple[int,str] (int x) { tuple[int,str] var_ = var(x); tuple[int,str] exp_ = exp(x); return <attrFunc(var_[0], exp_[0]), var_[1] + " = " + exp_[1]>; 
			};
		
		case ifThen(AttrPP condition, AttrPP body, list[tuple[AttrPP,AttrPP]] elseIfs, AttrPP elsePart) :
			return tuple[int,str] (int x) { 
			tuple[int,str] condition_ = condition(x); tuple[int,str] body_ = body(x); tuple[int,str] elsePart_ = elsePart(x);
				return <attrFunc(condition_[0], body_[0], toInt(attrFunc([ elseIf[0](x)[0] + elseIf[1](x)[0] | tuple[AttrPP, AttrPP] elseIf <- elseIfs ])), elsePart_[0]), "if (<condition_[1]>) { <body_[1]> } <for(tuple[AttrPP,AttrPP] elseIf <- elseIfs){> else if (<elseIf[0](x)[1]>) { <elseIf[1](x)[1]> } <}> <if(elsePart_[1] != ""){> else { <elsePart_[1]> } <}>">; 
				};
	
		case whileDo(AttrPP condition, AttrPP body) : 
			return tuple[int,str] (int x) {
			tuple[int,str] condition_ = condition(x); tuple[int,str] body_ = body(x);
			return <attrFunc(condition_[0], body_[0]), "while(<condition_[1]>) { <body_[1]> }">; };
	}
}

public AttrPP toC8(FExpression[AttrPP,AttrPP,AttrPP] n, type[Expression] \type = #Expression) {
	switch(n) {
		case nat(AttrPP val): return tuple[int,str] (int x) { tuple[int,str] val_ = val(x); return <attrFunc(val_[0]), val_[1]>; };
		case \true(): return tuple[int,str] (int x) { return <attrFunc(1), "<x>">; };
		case \false(): return tuple[int,str] (int x) { return <attrFunc(0), "<x>">; };
		case lookup(AttrPP var): return tuple[int,str] (int x) { tuple[int,str] var_ = var(x); return <attrFunc(var_[0]), var_[1]>; };
		case neg(AttrPP exp): return tuple[int,str] (int x) { tuple[int,str] exp_ = exp(x); return <attrFunc(exp_[0]), "(-<exp_[1]>)">; };
		case pos(AttrPP exp): return tuple[int,str] (int x) { tuple[int,str] exp_ = exp(x); return <attrFunc(exp_[0]), exp_[1]>; };
		case not(AttrPP exp): return tuple[int,str] (int x) { tuple[int,str] exp_ = exp(x); return <attrFunc(exp_[0]), "(!<exp_[1]>)">; };
		case mul(AttrPP lhs, AttrPP rhs): return tuple[int,str] (int x) { tuple[int,str] lhs_ = lhs(x); tuple[int,str] rhs_ = rhs(x); return <attrFunc(lhs_[0], rhs_[0]), "(<lhs_[1]> * <rhs_[1]>)">; };
		case div(AttrPP lhs, AttrPP rhs): return tuple[int,str] (int x) { tuple[int,str] lhs_ = lhs(x); tuple[int,str] rhs_ = rhs(x); return <attrFunc(lhs_[0], rhs_[0]), "(<lhs_[1]> / <rhs_[1]>)">; };
		case \mod(AttrPP lhs, AttrPP rhs): return tuple[int,str] (int x) { tuple[int,str] lhs_ = lhs(x); tuple[int,str] rhs_ = rhs(x); return <attrFunc(lhs_[0], rhs_[0]), "(<lhs_[1]> % <rhs_[1]>)">; };
		case amp(AttrPP lhs, AttrPP rhs): return tuple[int,str] (int x) { tuple[int,str] lhs_ = lhs(x); tuple[int,str] rhs_ = rhs(x); return <attrFunc(lhs_[0], rhs_[0]), "(<lhs_[1]> && <rhs_[1]>)">; };
		case add(AttrPP lhs, AttrPP rhs): return tuple[int,str] (int x) { tuple[int,str] lhs_ = lhs(x); tuple[int,str] rhs_ = rhs(x); return <attrFunc(lhs_[0], rhs_[0]), "(<lhs_[1]> + <rhs_[1]>)">; };
		case sub(AttrPP lhs, AttrPP rhs): return tuple[int,str] (int x) { tuple[int,str] lhs_ = lhs(x); tuple[int,str] rhs_ = rhs(x); return <attrFunc(lhs_[0], rhs_[0]), "(<lhs_[1]> - <rhs_[1]>)">; };
		case or(AttrPP lhs, AttrPP rhs): return tuple[int,str] (int x) { tuple[int,str] lhs_ = lhs(x); tuple[int,str] rhs_ = rhs(x); return <attrFunc(lhs_[0], rhs_[0]), "(<lhs_[1]> || <rhs_[1]>)">; };
		case eq(AttrPP lhs, AttrPP rhs): return tuple[int,str] (int x) { tuple[int,str] lhs_ = lhs(x); tuple[int,str] rhs_ = rhs(x); return <attrFunc(lhs_[0], rhs_[0]), "(<lhs_[1]> == <rhs_[1]>)">; };
		case neq(AttrPP lhs, AttrPP rhs): return tuple[int,str] (int x) { tuple[int,str] lhs_ = lhs(x); tuple[int,str] rhs_ = rhs(x); return <attrFunc(lhs_[0], rhs_[0]), "(<lhs_[1]> != <rhs_[1]>)">; };
		case lt(AttrPP lhs, AttrPP rhs): return tuple[int,str] (int x) { tuple[int,str] lhs_ = lhs(x); tuple[int,str] rhs_ = rhs(x); return <attrFunc(lhs_[0], rhs_[0]), "(<lhs_[1]> \< <rhs_[1]>)">; };
		case gt(AttrPP lhs, AttrPP rhs): return tuple[int,str] (int x) { tuple[int,str] lhs_ = lhs(x); tuple[int,str] rhs_ = rhs(x); return <attrFunc(lhs_[0], rhs_[0]), "(<rhs_[1]> \< <lhs_[1]>)">; };
		case leq(AttrPP lhs, AttrPP rhs): return tuple[int,str] (int x) { tuple[int,str] lhs_ = lhs(x); tuple[int,str] rhs_ = rhs(x); return <attrFunc(lhs_[0], rhs_[0]), "(<lhs_[1]> \<= <rhs_[1]>)">; };
		case geq(AttrPP lhs, AttrPP rhs): return tuple[int,str] (int x) { tuple[int,str] lhs_ = lhs(x); tuple[int,str] rhs_ = rhs(x); return <attrFunc(lhs_[0], rhs_[0]), "(<rhs_[1]> \<= <lhs_[1]>)">; };
	}
}

public AttrPP toC9(FIdent[AttrPP] n, type[Ident] \type = #Ident)
	= tuple[int,str] (int x) { tuple[int,str] name = n.name(x); return <attrFunc(name[0]), name[1]>; };

public AttrPP toC10(list[tuple[AttrPP,AttrPP]] n, type[list[Statement]] \type = #list[Statement]) {
	return tuple[int,str] (int x) { return ([<AttrPP el0, AttrPP rest>] := n) ? { tuple[int,str] el0_ = el0(x); tuple[int,str] rest_ = rest(x); <attrFunc(el0_[0], rest_[0]), el0_[1] + ";\n" + rest_[1]>; } : <0,"">; };
}
public AttrPP toC11(list[tuple[AttrPP,AttrPP]] n, type[list[ConstDecl]] \type = #list[ConstDecl]) {
	return tuple[int,str] (int x) { return ([<AttrPP el0, AttrPP rest>] := n) ? { tuple[int,str] el0_ = el0(x); tuple[int,str] rest_ = rest(x); <attrFunc(el0_[0], rest_[0]), el0_[1] + "\n" + rest_[1]>; } : <0,"">; };
}
public AttrPP toC12(list[tuple[AttrPP,AttrPP]] n, type[list[TypeDecl]] \type = #list[TypeDecl]) {
	return tuple[int,str] (int x) { return ([<AttrPP el0, AttrPP rest>] := n) ? { tuple[int,str] el0_ = el0(x); tuple[int,str] rest_ = rest(x); <attrFunc(el0_[0], rest_[0]), el0_[1] + ";\n" + rest_[1]>; } : <0,"">; };
}
public AttrPP toC13(list[tuple[AttrPP,AttrPP]] n, type[list[VarDecl]] \type = #list[VarDecl]) {
	return tuple[int,str] (int x) { return ([<AttrPP el0, AttrPP rest>] := n) ? { tuple[int,str] el0_ = el0(x); tuple[int,str] rest_ = rest(x); <attrFunc(el0_[0], rest_[0]), el0_[1] + ";\n" + rest_[1]>; } : <0,"">; };
}
public AttrPP toC14(list[tuple[AttrPP,AttrPP]] n, type[list[Ident]] \type = #list[Ident]) { 
	return tuple[int,str] (int x) { return ([<AttrPP el0, AttrPP rest>] := n) ? { tuple[int,str] el0_ = el0(x); tuple[int,str] rest_ = rest(x); <attrFunc(el0_[0], rest_[0]), el0_[1] + ";" + rest_[1]>; } : <0,"">; };
}
public list[tuple[AttrPP,AttrPP]] toC15(list[tuple[tuple[AttrPP,AttrPP],list[tuple[AttrPP,AttrPP]]]] n, type[list[tuple[Expression,list[Statement]]]] \type = #list[tuple[Expression,list[Statement]]]) = [ *([el0] + rest) | <tuple[AttrPP,AttrPP] el0, list[tuple[AttrPP,AttrPP]] rest> <- n ];

public tuple[AttrPP,AttrPP] toC16(tuple[AttrPP,AttrPP] n, type[tuple[Expression,list[Statement]]] \type = #tuple[Expression,list[Statement]]) = n;

public AttrPP toC17(str s, type[str] \type = #str) {
	return tuple[int,str] (int x) {
		str pp = "";
		switch(s) {
			case "INTEGER": pp = "int";
			case "BOOLEAN": pp = "int";
			default: pp = s;
		}
		return <attrFunc(0), pp>;
	};
}
public AttrPP toC18(int i, type[int] \type = #int) = tuple[int,str] (int x) { return <attrFunc(i), "<x>">; };

public str test0(loc l) {
	Tree ob01 = parse(l);
	Module m = implode(#Module,ob01);
	AttrPP (Module) f = fvisit[<#Module>, <toC1,toC2,toC3,toC4,toC5,toC6,toC7,toC8,toC9,toC10,toC11,toC12,toC13,toC14,toC15,toC16,toC17,toC18>];
	AttrPP m010 = f(m);
	return m010(10)[1];
}
