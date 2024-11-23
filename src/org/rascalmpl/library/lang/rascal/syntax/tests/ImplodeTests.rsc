@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@synopsis{Tests the potential clashes among value constructors of different adts, plus, the identified clash with: bool eq(value, value);}
module lang::rascal::\syntax::tests::ImplodeTests

import lang::rascal::\syntax::tests::ImplodeTestGrammar;
import ParseTree;
import Exception;

public data Num(loc src=|unknown:///|, map[int,list[str]] comments = ());
public data Exp(loc src=|unknown:///|, map[int,list[str]] comments = ()) = id(str name) | eq(Exp e1, Exp e2) | number(Num n);
public Exp number(Num::\int("0")) = Exp::number(Num::\int("01"));

public data Number(loc src=|unknown:///|, map[int,list[str]] comments = ());
public data Expr(loc src=|unknown:///|, map[int,list[str]] comments = ()) = id(str name) | eq(Expr e1, Expr e2) | number(Number n);
public Expr number(Number::\int("0")) = Expr::number(Number::\int("02"));

public Exp implodeExp(str s) = implode(#Exp, parseExp(s));
public Exp implodeExpLit1() = implode(#Exp, expLit1());
public Exp implodeExpLit2() = implode(#Exp, expLit2());

public Expr implodeExpr(str s) = implode(#Expr, parseExp(s));
public Expr implodeExprLit1() = implode(#Expr, exprLit1());
public Expr implodeExprLit2() = implode(#Expr, exprLit2());


// ---- test1 ----

test bool test11() { try return Exp::id(_) := implodeExp("a"); catch ImplodeError(_): return false;}

@IgnoreCompiler{TODO}
test bool test12() { try return Exp::number(Num::\int("0")) := implodeExp("0"); catch ImplodeError(_): return false;}

test bool test13() { try return Exp::eq(Exp::id(_),Exp::id(_)) := implodeExp("a == b"); catch ImplodeError(_): return false;}

@IgnoreCompiler{TODO}
test bool test14() { try return Exp::eq(Exp::number(Num::\int("0")), Exp::number(Num::\int("1"))) := implodeExp("0 == 1"); catch ImplodeError(_): return false;}

test bool test15() { try return  Expr::id(_) := implodeExpr("a"); catch ImplodeError(_): return false;}

@IgnoreCompiler{TODO}
test bool test16() { try return Expr::number(Number::\int("0")) := implodeExpr("0"); catch ImplodeError(_): return false;}

test bool test17() { try return Expr::eq(Expr::id(_),Expr::id(_)) := implodeExpr("a == b"); catch ImplodeError(_): return false;}

@IgnoreCompiler{TODO}
test bool test18() { try return Expr::eq(Expr::number(Number::\int("0")), Expr::number(Number::\int("1"))) := implodeExpr("0 == 1"); catch ImplodeError(_): return false;}

// ---- test2 ----

test bool test21() { try return Exp::eq(Exp::id("a"),Exp::id("b")) := implodeExpLit1(); catch ImplodeError(_): return false;}

test bool test22() { try return Exp::eq(Exp::id("a"),Exp::number(Num::\int("11"))) := implodeExpLit2(); catch ImplodeError(_): return false;}

test bool test23() { try return Expr::eq(Expr::id("a"),Expr::id("b")) := implodeExprLit1(); catch ImplodeError(_): return false;}

test bool test24() { try return  Expr::eq(Expr::id("a"),Expr::number(Number::\int("11"))) := implodeExprLit2(); catch ImplodeError(_): return false;}
