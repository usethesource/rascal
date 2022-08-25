@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@doc{Tests the potential clashes among value constructors of different adts, plus, the identified clash with: bool eq(value, value);}
module lang::rascal::\syntax::tests::ImplodeTestGrammar

import ParseTree;

lexical Id = [a-z] !<< [a-z]+ !>> [a-z];
layout W = [\ \t\n\r]*;

lexical Num = \int: [0-9]+;
syntax Exp 
	= id: Id name
	| number: Num n
	| non-assoc eq: Exp e1 "==" Exp e2;
	

lexical Number = \int: [0-9]+;
syntax Expr 
	= id: Id name
	| number: Number n
	| non-assoc eq: Expr e1 "==" Expr e2;


public Exp parseExp(str s) = parse(#Exp, s);
public Exp expLit1() = (Exp) `a == b`;
public Exp expLit2() = (Exp) `a == 11`;

public Expr parseExpr(str s) = parse(#Expr, s);
public Expr exprLit1() = (Expr) `a == b`;
public Expr exprLit2() = (Expr) `a == 11`;
