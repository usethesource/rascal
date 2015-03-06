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

syntax F = left "-" F | "f" | right F "+";
syntax G = left (G "+" G | "-" G | "^" G) | "g" | right (G "-" G | G "+");

syntax Opt = Exp!com? "?";  
syntax Seq = (Exp!com Exp!number);
syntax Alt = (Exp!com | (Exp "," Exp));

test bool safeLeft() = F _ := parse(#F,"--f");
test bool safeRight() = F _ := parse(#F,"f++");
test bool safeGroupLeft() = G _ := parse(#G,"--g");
test bool safeGroupRight() = G _ := parse(#G,"g++");

Exp noBrackets(Exp e) = visit (e) { case (Exp) `(<Exp a>)` => a };
bool hasAmb(Tree x) = /amb(_) := x;

test bool \assoc() = noBrackets(parse(#Exp,"(a + b) + c")) == parse(#Exp, "a + b + c");

test bool \mutualAssoc1() = noBrackets(parse(#Exp,"(a - b) + c")) == parse(#Exp, "a - b + c");

test bool \mutualAssoc2() = noBrackets(parse(#Exp,"(a + b) - c")) == parse(#Exp, "a + b - c");

test bool \prio() = noBrackets(parse(#Exp,"(a*b)+c")) == parse(#Exp, "a*b+c");

test bool \safePrio1() = parse(#Exp,"a[a+b]") is ind;

test bool \safePrio2() = parse(#Exp,"a**b") is mul;

test bool \safePrio3() = parse(#Exp,"a*b*") is mul;

test bool \safePrio4() = parse(#Exp,"(a*)[b]") is ind;

test bool \transPrio() = noBrackets(parse(#Exp,"a,(b*c)")) == parse(#Exp,"a,b*c");
    
test bool \exceptNormal() = parse(#Exp,"a+[a]") is add;

test bool \exceptInList() = !hasAmb(parse(#Exp,"[a,a]"));

test bool \exceptInOpt() {
  try {
    parse(#Opt,"a,a?");
    return false;
  }
  catch value x: {
    return true;
  }
}

test bool \exceptInSeq1() {
  try {
    parse(#Seq,"a,a a");
    return false;
  }
  catch value x: {
    return true;
  }
}

test bool \exceptInSeq2() {
  try {
    parse(#Seq,"a+a 1");
    return false;
  }
  catch value x: {
    return true;
  }
}

test bool \exceptInAlt() = !hasAmb(parse(#Alt,"a,a"));