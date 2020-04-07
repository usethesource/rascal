@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascal::\syntax::tests::ExpressionGrammars

import ParseTree;

lexical Id = [a-z] !<< [a-z]+ !>> [a-z];
lexical Num = [0-9]+;
layout W = [\ \t\n\r]*;

syntax Exp 
  = id: Id
  | number: Num
  | lst: "[" {Exp!com ","}*  "]"
  | ind: Exp!clop!clos "[" {Exp!com ","}+ "]"
  > clop: Exp "+"
  | clos: Exp "*"
  > left mul: Exp "*" Exp
  > left 
    ( add: Exp "+" Exp
    | sub: Exp "-" Exp
    ) 
  > bracket "(" Exp exp ")" 
  | com: Exp "," Exp
  ;

syntax Exp = left( add: Exp "+" Exp | minmin: Exp "--" Exp);

// "modular" extensions in the priority relation
syntax Exp = :mul > left Exp "/" Exp > :add;
syntax Exp = :add > Exp "." Exp;

syntax F = left "-" F | "f" | right F "+";
syntax G = left (G "+" G | "-" G | "^" G) | "g" | right (G "-" G | G "+");

syntax Opt = Exp!com? "?";  
syntax Seq = (Exp!com Exp!number);
syntax Alt = (Exp!com | (Exp "," Exp));

Exp removeBrackets(Exp e) = visit(e) {
  case (Exp) `(<Exp b>)` => b
};

test bool ext1() = (Exp) `e+e.e` := removeBrackets((Exp) `(e+e).e`);
test bool ext2() = (Exp) `e*e/e+e` := removeBrackets((Exp) `((e*e)/e)+e`);
test bool ext3() = (Exp) `e*e--e` := removeBrackets((Exp) `(e*e)--e`);
test bool ext4() = (Exp) `e*e.e` := removeBrackets((Exp) `(e*e).e`);
test bool ext5() = (Exp) `e+e.e` := removeBrackets((Exp) `(e+e).e`);
test bool ext6() = (Exp) `e--e-e` := removeBrackets((Exp) `(e--e)-e`);
test bool ext7() = (Exp) `e-e--e` := removeBrackets((Exp) `(e-e)--e`);

test bool safeLeft() = F _ := parse(#F,"--f");
test bool safeRight() = F _ := parse(#F,"f++");
test bool safeGroupLeft() = G _ := parse(#G,"--g");
test bool safeGroupRight() = G _ := parse(#G,"g++");

Exp noBrackets(Exp e) = visit (e) { case (Exp) `(<Exp a>)` => a };
bool hasAmb(Tree x) = /amb(_) := x;

test bool \assoc() = x := noBrackets(parse(#Exp,"(a + b) + c")) when x := parse(#Exp, "a + b + c");

test bool \mutualAssoc1() = x := noBrackets(parse(#Exp,"(a - b) + c")) when x := parse(#Exp, "a - b + c");

test bool \mutualAssoc2() = x := noBrackets(parse(#Exp,"(a + b) - c")) when x := parse(#Exp, "a + b - c");

test bool \prio() = x := noBrackets(parse(#Exp,"(a*b)+c")) when x := parse(#Exp, "a*b+c");

test bool \safePrio1() = parse(#Exp,"a[a+b]") is ind;

test bool \safePrio2() = parse(#Exp,"a**b") is mul;

test bool \safePrio3() = parse(#Exp,"a*b*") is mul;

test bool \safePrio4() = parse(#Exp,"(a*)[b]") is ind;

test bool \transPrio() = x := noBrackets(parse(#Exp,"a,(b*c)")) when x := parse(#Exp,"a,b*c");
    
test bool \exceptNormal() = parse(#Exp,"a+[a]") is add;

test bool \exceptInList() = !hasAmb(parse(#Exp,"[a,a]"));

test bool \exceptInOpt() {
  try {
    parse(#Opt,"a,a?");
    return false;
  }
  catch value _: {
    return true;
  }
}

test bool \exceptInSeq1() {
  try {
    parse(#Seq,"a,a a");
    return false;
  }
  catch value _: {
    return true;
  }
}

test bool \exceptInSeq2() {
  try {
    parse(#Seq,"a+a 1");
    return false;
  }
  catch value _: {
    return true;
  }
}

test bool \exceptInAlt() = !hasAmb(parse(#Alt,"a,a"));
