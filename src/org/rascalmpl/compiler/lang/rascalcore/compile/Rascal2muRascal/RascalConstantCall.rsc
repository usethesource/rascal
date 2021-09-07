module lang::rascalcore::compile::Rascal2muRascal::RascalConstantCall

import lang::rascalcore::compile::muRascal::AST;
import ParseTree;
import String;
import List;
import Set;
import Map;
import Node;
import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;

/*
 *  Translate selected calls with constant arguments at compile time
 *  See lang::rascalcore::compile::muRascal::Primitives for constant folding of muPrimitives
 */

MuExp translateConstantCall(str name, list[MuExp] args) {
	return tcc(name, args);
}

// String
private MuExp tcc("size", [muCon(str s)]) = muCon(size(s));

// List
private MuExp tcc("size", [muCon(list[value] lst)]) = muCon(size(lst));
private  MuExp tcc("index", [muCon(list[value] lst)]) = muCon(index(lst));
private MuExp tcc("isEmpty", [muCon(list[value] lst)]) = muCon(isEmpty(lst));
private  MuExp tcc("reverse", [muCon(list[value] lst)]) = muCon(reverse(lst));
 
// Set 
private MuExp tcc("size", [muCon(set[value] st)]) = muCon(size(st));
private MuExp tcc("isEmpty", [muCon(set[value] st)]) = muCon(isEmpty(st));

// Map
private MuExp tcc("size", [muCon(map[value,value] mp)]) = muCon(size(mp));
private MuExp tcc("isEmpty", [muCon(map[value,value] mp)]) = muCon(isEmpty(mp));

// Node
private MuExp tcc("getName", [muCon(node nd)]) = muCon(getName(nd));
private MuExp tcc("getChildren", [muCon(node nd)]) = muCon(getChildren(nd));

// Type		
private MuExp tcc("subtype", [muCon(AType lhs), muCon(AType rhs)]) = muCon(asubtype(lhs, rhs));
private MuExp tcc("lub", [muCon(AType lhs), muCon(AType rhs)]) = muCon(alub(lhs, rhs));
private MuExp tcc("glb", [muCon(AType lhs), muCon(AType rhs)]) = muCon(aglb(lhs, rhs));
 
// Tree

private MuExp tcc("appl", [muCon(Production prod), muCon(list[Tree] args)]) = muCon(appl(prod, args));
private MuExp tcc("cycle", [muCon(AType symbol), muCon(int cycleLength)]) = muCon(cycle(symbol, cycleLength));
private MuExp tcc("char", [muCon(int character)]) = muCon(char(character));

// Production

private MuExp tcc("prod", [muCon(AType def), muCon(list[AType] symbols), muCon(set[Attr] attributes)]) = muCon(prod(def,symbols,attributes=attributes));

default MuExp tcc(str name, list[MuExp] args) { throw "NotConstant"; }