@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Anastasia Izmaylova - A.Izmaylova@cwi.nl}
module lang::oberon0::AlgebraicImplode

import lang::oberon0::AST;
import lang::oberon0::utils::Parse;

import ParseTree;
import ParseTreeToAdt;

public FModule[Tree,Tree,Tree] parseToModule(Tree tree, type[Module] \type = #Module) = parseToAdt(#FModule[Tree,Tree,Tree], tree);
public FDeclarations[Tree,Tree,Tree] parseToDeclarations(Tree tree, type[Declarations] \type = #Declarations) = parseToAdt(#FDeclarations[Tree,Tree,Tree], tree);
public FConstDecl[Tree,Tree] parseToConstDecl(Tree tree, type[ConstDecl] \type = #ConstDecl) = parseToAdt(#FConstDecl[Tree,Tree], tree);
public FTypeDecl[Tree,Tree] parseToTypeDecl(Tree tree, type[TypeDecl] \type = #TypeDecl) = parseToAdt(#FTypeDecl[Tree,Tree], tree);
public FVarDecl[Tree,Tree] parseToVarDecl(Tree tree, type[VarDecl] \type = #VarDecl) = parseToAdt(#FVarDecl[Tree,Tree], tree);
public FType[Tree] parseToType(Tree tree, type[Type] \type = #Type) = parseToAdt(#FType[Tree], tree);
public FStatement[Tree,Tree,Tree,Tree] parseToStatement(Tree tree, type[Statement] \type = #Statement) = parseToAdt(#FStatement[Tree,Tree,Tree,Tree], tree);
public FExpression[Tree,Tree,Tree] parseToExpression(Tree tree, type[Expression] \type = #Expression) = parseToAdt(#FExpression[Tree,Tree,Tree], tree);
public FIdent[Tree] parseToIdent(Tree tree, type[Ident] \type = #Ident) = parseToAdt(#FIdent[Tree], tree);

public list[tuple[Tree,Tree]] parseToListOfStatement(Tree tree, type[list[Statement]] \type = #list[Statement]) = parseToList(tree);
public list[tuple[Tree,Tree]] parseToListOfConstDecl(Tree tree, type[list[ConstDecl]] \type = #list[ConstDecl]) = parseToList(tree);
public list[tuple[Tree,Tree]] parseToListOfTypeDecl(Tree tree, type[list[TypeDecl]] \type = #list[TypeDecl]) = parseToList(tree);
public list[tuple[Tree,Tree]] parseToListOfVarDecl(Tree tree, type[list[VarDecl]] \type = #list[VarDecl]) = parseToList(tree);
public list[tuple[Tree,Tree]] parseToListOfIdent(Tree tree, type[list[Ident]] \type = #list[Ident]) = parseToList(tree);
public list[tuple[Tree,Tree]] parseToListOfTupleOfExpressionAndListOfStatement(Tree tree, type[list[tuple[Expression,list[Statement]]]] \type = #list[tuple[Expression,list[Statement]]]) = parseToList(tree);

public tuple[Tree,Tree] parseToTupleOfExpressionAndListOfStatement(Tree tree, type[tuple[Expression,list[Statement]]] \type = #tuple[Expression,list[Statement]]) = parseToTuple(#tuple[Tree,Tree], tree);

public str parseToString(Tree tree, type[str] \type = #str) = parseToStr(tree);
public int parseToInteger(Tree tree, type[int] \type = #int) = parseToInt(tree);

public Module test0(loc l) {
	Tree ob01 = parse(l);
	
	// Tree to adt with the java implode
Module m01 = implode(#Module, ob01);
	
	// Tree to adt with the algebraic visit based implode
	Module (Tree) f = fvisit[<#Module>, <parseToModule, parseToDeclarations, parseToConstDecl, parseToTypeDecl, parseToVarDecl, parseToType, parseToStatement, parseToExpression, parseToIdent, parseToListOfStatement, parseToListOfConstDecl, parseToListOfTypeDecl, parseToListOfVarDecl, parseToListOfIdent, parseToListOfTupleOfExpressionAndListOfStatement, parseToTupleOfExpressionAndListOfStatement, parseToString, parseToInteger>];
	Module m010 = f(ob01);
	
	assert(m01 == m010);
	
	return m010;
}