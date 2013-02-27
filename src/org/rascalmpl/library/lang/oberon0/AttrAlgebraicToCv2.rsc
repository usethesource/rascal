@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Anastasia Izmaylova - A.Izmaylova@cwi.nl}
@doc{This is a malicious Oberon0ToC prettyprinter :-): it compiles to Oberon0 to C string replacing all the integer literals with their sum}
module lang::oberon0::AttrAlgebraicToCv2

import lang::oberon0::AST;
import lang::oberon0::utils::Parse;


import List;
import ParseTree;
import ParseTreeToAdt;
import util::Math;

import IO;
import Type;

public data Attr[&T1,&T2] = attr(&T2 (&T1) f); // two kinds of attributes: inherited (&T1 x) and synthesized (&T2 y); x -> <y,a>

public Attr[&T1,&T2] evalAttr(&T2 (&T1, list[&T2]) f, list[Attr[&T1,&T2]] phis, list[&T1 (&T1, list[&T2])] gs, bool circular)
	= attr(&T2 (&T1 x) {
		assert(size(phis) == size(gs)); 
		list[&T2] ys = [ phis[i].f(gs[i](x)) | int i <- [0..size(gs)]]; 
		if(circular)
			solve(ys) { ys = [ phis[i].f(gs[i](x,ys)) | int i <- [0..size(gs)]]; }; 
		return f(x,ys); });

@doc{Types of the attributes}
public alias I = int; // inherited attribute
public alias S = tuple[I \num ,str pp]; // synthesized attributes
public alias S1 = tuple[I \num,lrel[str,str] pp]; // synthesized attributes
public alias S2 = tuple[I \num,tuple[str,str] pp]; // synthesized attributes 

public I attrFunc(I vals...) = (!isEmpty(vals)) ? toInt(sum(vals)) : 0;

@doc{The function that defines the synthesized (second) attribute of a term based on the first attribute and the second attributes (ys) of the subterms; re-defined by some cases}
public S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]), pp([ y.pp | S y <- ys ], "")>;

@doc{The function that defines the inherited (first) attribute of a subterm based on the first attribute of the term and the second attributes (ys) of the subterms; it can introduce circularity; redefined by some cases}
public I g(I x, S ys...) = x;

private str pp(list[str] l, str sep) = "<for(int i <- [0..size(l)]){><if(l[i] != ""){><if(i != size(l) - 1 && l[i+1] != ""){><l[i]><sep><}else{><l[i]><}><}><}>";

public Attr[I,S] toC1(FModule[Attr[I,S],Attr[I,S],Attr[I,S]] n, type[Module] \type = #Module) { 
	Attr[I,S] decls = n.decls; Attr[I,S] body = n.body; 
	I g(I x, S ys...) = attrFunc([ y.\num | S y <- ys ]); // note: it introduces circularity
	return 	evalAttr(f, [ decls, body ], [ g, g ], true);
}
public Attr[I,S] toC2(FDeclarations[Attr[I,S],Attr[I,S],Attr[I,S]] n, type[Declarations] \type = #Declarations) {
	Attr[I,S] consts = n.consts; Attr[I,S] types = n.types; Attr[I,S] vars = n.vars;
	return evalAttr(f, [ consts, types, vars ], [ g, g, g ], false); 
}

public Attr[I,S] toC3(FConstDecl[Attr[I,S],Attr[I,S]] n, type[ConstDecl] \type = #ConstDecl) {
	Attr[I,S] name = n.name; Attr[I,S] val = n.\value; 
	S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]), "#define " + at(ys,0).pp + " " + at(ys,1).pp + ";\n">;
	return evalAttr(f, [ name, val ], [ g, g ], false); 
}

public Attr[I,S] toC4(FTypeDecl[Attr[I,S],Attr[I,S]] n, type[TypeDecl] \type = #TypeDecl) {
	Attr[I,S] t = n.\type; Attr[I,S] name = n.name;
	S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]), "typedef " + at(ys,0).pp + " " + at(ys,1).pp + ";\n">;
	return evalAttr(f, [ t, name ], [ g, g ], false); 
}

public Attr[I,S] toC5(FVarDecl[Attr[I,S],Attr[I,S]] n, type[VarDecl] \type = #VarDecl) { 
	Attr[I,S] t = n.\type; Attr[I,S] names = n.names;
	S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]), at(ys,0).pp + " " + at(ys,1).pp + ";\n">;
	return evalAttr(f, [ t, names ], [ g, g ], false); 
}
	 
public Attr[I,S] toC6(FType[Attr[I,S]] n, type[Type] \type = #Type) {
	Attr[I,S] name = n.name; 
	return evalAttr(f, [ name ], [ g ], false); 
}
		
public Attr[I,S] toC7(FStatement[Attr[I,S],Attr[I,S],Attr[I,S],Attr[I,S1]] n, type[Statement] \type = #Statement) {
	switch(n) {
		case assign(Attr[I,S] var, Attr[I,S] exp) : {
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]), at(ys,0).pp + " = " + at(ys,1).pp + ";\n">;
			return evalAttr(f, [ var, exp ], [ g, g ], false);
		}
		case ifThen(Attr[I,S] condition, Attr[I,S] body, Attr[I,S1] elseIfs, Attr[I,S] elsePart) : {
			Attr[I,S] elseIfs_ = attr(S (I x) { S1 telseIfs = elseIfs.f(x); return <telseIfs.\num, "<for(tuple[str,str] elseIf <- telseIfs.pp){> else if (<elseIf[0]>) { <elseIf[1]> }<}>">; });
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]), "if (<at(ys,0).pp>) { <at(ys,1).pp> } <at(ys,2).pp> <if(at(ys,3).pp != "") {> else { <at(ys,3).pp> }<}>" + ";\n">;
			return evalAttr(f, [ condition, body, elseIfs_, elsePart ], [ g, g, g, g ], false);
		}
		case whileDo(Attr[I,S] condition, Attr[I,S] body) : {
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]), "while(<at(ys,0).pp>) { <at(ys,1).pp> }" + ";\n">;
			return evalAttr(f, [ condition, body ], [ g, g ], false);
		}
	}
}

public Attr[I,S] toC8(FExpression[Attr[I,S],Attr[I,S],Attr[I,S]] n, type[Expression] \type = #Expression) {
	switch(n) {
		case nat(Attr[I,S] val): { 
			return evalAttr(f, [ val ], [ g ], false); 
		}
		case \true(): { 
			S f(I x, S ys...) = <attrFunc(1), "<x>">; 
			return evalAttr(f, [], [], false); 
		}
		case \false(): { 
			S f(I x, S ys...) = <attrFunc(0), "<x>">; 
			return evalAttr(f, [], [], false); 
		}
		case lookup(Attr[I,S] var): { 
			return evalAttr(f, [ var ], [ g ], false); 
		}
		case neg(Attr[I,S] exp): { 
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]), "(-<at(ys,0).pp>)">; 
			return evalAttr(f, [ exp ], [ g ], false); 
		}
		case pos(Attr[I,S] exp): { 
			return evalAttr(f, [ exp ], [ g ], false); 
		}
		case not(Attr[I,S] exp): { 
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]), "(!<at(ys,0).pp>)">; 
			return evalAttr(f, [ exp ], [ g ], false); 
		}
		case mul(Attr[I,S] lhs, Attr[I,S] rhs): { 
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]), "(<at(ys,0).pp> * <at(ys,1).pp>)">; 
			return evalAttr(f, [ lhs, rhs ], [ g, g ], false); 
		}
		case div(Attr[I,S] lhs, Attr[I,S] rhs): { 
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]), "(<at(ys,0).pp> / <at(ys,1).pp>)">; 
			return evalAttr(f, [ lhs, rhs ], [ g, g ], false); 
		}
		case \mod(Attr[I,S] lhs, Attr[I,S] rhs): { 
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]),"(<at(ys,0).pp> % <at(ys,1).pp>)">; 
			return evalAttr(f, [ lhs, rhs ], [ g, g ], false); 
		}
		case amp(Attr[I,S] lhs, Attr[I,S] rhs): { 
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]),"(<at(ys,0).pp> && <at(ys,1).pp>)">; 
			return evalAttr(f, [ lhs, rhs ], [ g, g ], false); 
		}
		case add(Attr[I,S] lhs, Attr[I,S] rhs): { 
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]),"(<at(ys,0).pp> + <at(ys,1).pp>)">; 
			return evalAttr(f, [ lhs, rhs ], [ g, g ], false); 
		}
		case sub(Attr[I,S] lhs, Attr[I,S] rhs): { 
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]),"(<at(ys,0).pp> - <at(ys,1).pp>)">; 
			return evalAttr(f, [ lhs, rhs ], [ g, g ], false); 
		}
		case or(Attr[I,S] lhs, Attr[I,S] rhs): { 
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]),"(<at(ys,0).pp> || <at(ys,1).pp>)">; 
			return evalAttr(f, [ lhs, rhs ], [ g, g ], false); 
		}
		case eq(Attr[I,S] lhs, Attr[I,S] rhs): { 
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]),"(<at(ys,0).pp> == <at(ys,1).pp>)">; 
			return evalAttr(f, [ lhs, rhs ], [ g, g ], false); 
		}
		case neq(Attr[I,S] lhs, Attr[I,S] rhs): { 
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]),"(<at(ys,0).pp> != <at(ys,1).pp>)">; 
			return evalAttr(f, [ lhs, rhs ], [ g, g ], false); 
		}
		case lt(Attr[I,S] lhs, Attr[I,S] rhs): { 
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]),"(<at(ys,0).pp> \< <at(ys,1).pp>)">; 
			return evalAttr(f, [ lhs, rhs ], [ g, g ], false); 
		}
		case gt(Attr[I,S] lhs, Attr[I,S] rhs): { 
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]),"(<at(ys,0).pp> \> <at(ys,1).pp>)">; 
			return evalAttr(f, [ lhs, rhs ], [ g, g ], false); 
		}
		case leq(Attr[I,S] lhs, Attr[I,S] rhs): { 
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]),"(<at(ys,0).pp> \<= <at(ys,1).pp>)">; 
			return evalAttr(f, [ lhs, rhs ], [ g, g ], false); 
		}
		case geq(Attr[I,S] lhs, Attr[I,S] rhs): { 
			S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]),"(<at(ys,0).pp> \>= <at(ys,1).pp>)">; 
			return evalAttr(f, [ lhs, rhs ], [ g, g ], false); 
		}
	}
}

public Attr[I,S] toC9(FIdent[Attr[I,S]] n, type[Ident] \type = #Ident) {
	Attr[I,S] name = n.name;
	S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]), "<for(S y <- ys){><y.pp><}>">;
	return evalAttr(f, [ name ], [ g ], false);
}

public Attr[I,S] toC10(lrel[Attr[I,S],Attr[I,S]] n, type[list[Statement]] \type = #list[Statement]) {
	if([<Attr[I,S] el0, Attr[I,S] rest>] := n) {
		S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]), pp([ y.pp | S y <- ys ], "")>;
		return evalAttr(f, [ el0, rest ], [ g, g ], false);
	}
	return attr(S (I x) { return <0,"">; });
}

public Attr[I,S] toC11(lrel[Attr[I,S],Attr[I,S]] n, type[list[ConstDecl]] \type = #list[ConstDecl]) {
	if([<Attr[I,S] el0, Attr[I,S] rest>] := n) {
		S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]), pp([ y.pp | S y <- ys ], "")>;
		return evalAttr(f, [ el0, rest ], [ g, g ], false);
	}
	return attr(S (I x) { return <0,"">; });
}

public Attr[I,S] toC12(lrel[Attr[I,S],Attr[I,S]] n, type[list[TypeDecl]] \type = #list[TypeDecl]) {
	if([<Attr[I,S] el0, Attr[I,S] rest>] := n)
		return evalAttr(f, [ el0, rest ], [ g, g ], false);
	return attr(S (I x) { return <0,"">; });
}

public Attr[I,S] toC13(lrel[Attr[I,S],Attr[I,S]] n, type[list[VarDecl]] \type = #list[VarDecl]) {
	if([<Attr[I,S] el0, Attr[I,S] rest>] := n)
		return evalAttr(f, [ el0, rest ], [ g, g ], false);
	return attr(S (I x) { return <0,"">; });
}

public Attr[I,S] toC14(lrel[Attr[I,S],Attr[I,S]] n, type[list[Ident]] \type = #list[Ident]) { 
	if([<Attr[I,S] el0, Attr[I,S] rest>] := n) {
		S f(I x, S ys...) = <attrFunc([ y.\num | S y <- ys ]), pp([ y.pp | S y <- ys ], ",")>;
		return evalAttr(f, [ el0, rest ], [ g, g ], false);
	}
	return attr(S (I x) { return <0,"">; });
}

public Attr[I,S1] toC15(lrel[Attr[I,S2],Attr[I,S1]] n, type[lrel[Expression,list[Statement]]] \type = #lrel[Expression,list[Statement]]) {
	if([<Attr[I,S2] el0, Attr[I,S1] rest>] := n)
			return attr(S1 (I x) { return <attrFunc(el0.f(x/*note:g(x,ys)=x*/).\num + rest.f(x/*note:g(x,ys)=x*/).\num), [el0.f(x/*note:g(x,ys)=x*/).pp] + rest.f(x/*note:g(x,ys)=x*/).pp>; });
	return attr(S1 (I x) { return <0,[]>; });
}

public Attr[I,S2] toC16(tuple[Attr[I,S],Attr[I,S]] n, type[tuple[Expression,list[Statement]]] \type = #tuple[Expression,list[Statement]]) {
	Attr[I,S] el1 = n[0]; Attr[I,S] el2 = n[1];
	return attr(S2 (I x) { return <attrFunc(el1.f(x).\num, el2.f(x).\num), <el1.f(x).pp, el2.f(x).pp>>; });
}

public Attr[I,S] toC17(str s, type[str] \type = #str) {
	return attr(S (I x) {
		str pp = "";
		switch(s) {
			case "INTEGER": pp = "int";
			case "BOOLEAN": pp = "int";
			default: pp = s;
		}
		return <attrFunc(0), pp>;
	});
}
public Attr[I,S] toC18(int i, type[int] \type = #int) = attr(S (I x) { return <attrFunc(i), "<x>">; });

public &T at(list[&T] l, int i) {
int j = 0;
for(&T el <- l)
if(j == i) return el; else j = j + 1;
throw "IndexOutOfBounds(<i>)";
}

public str test0(loc l) {
	Tree ob01 = parse(l);
	Module m = implode(#Module,ob01);
	Attr[I,S] (Module) f = fvisit[<#Module>, <toC1,toC2,toC3,toC4,toC5,toC6,toC7,toC8,toC9,toC10,toC11,toC12,toC13,toC14,toC15,toC16,toC17,toC18>];
	Attr[I,S] m010 = f(m);
	return m010.f(0).pp;
}
