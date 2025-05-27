module ExpressionGrammars

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

syntax Opt = Exp!com? "?";  
syntax Seq = (Exp!com Exp!number);
syntax Alt = (Exp!com | (Exp "," Exp));

public Exp noBrackets(Exp e) = visit (e) { case (Exp) `(<Exp a>)` => a };
public bool hasAmb(Tree x) = /amb(_) := x;

public test bool \assoc() = noBrackets(parse(#Exp,"(a + b) + c")) == parse(#Exp, "a + b + c");

public test bool \mutualAssoc1() = noBrackets(parse(#Exp,"(a - b) + c")) == parse(#Exp, "a - b + c");

public test bool \mutualAssoc2() = noBrackets(parse(#Exp,"(a + b) - c")) == parse(#Exp, "a + b - c");

public test bool \prio() = noBrackets(parse(#Exp,"(a*b)+c")) == parse(#Exp, "a*b+c");

public test bool \safePrio1() = parse(#Exp,"a[a+b]") is ind;

public test bool \safePrio2() = parse(#Exp,"a**b") is mul;

public test bool \safePrio3() = parse(#Exp,"a*b*") is mul;

public test bool \safePrio4() = parse(#Exp,"(a*)[b]") is ind;

public test bool \transPrio() = noBrackets(parse(#Exp,"a,(b*c)")) == parse(#Exp,"a,b*c");
    
public test bool \exceptNormal() = parse(#Exp,"a+[a]") is add;

public test bool \exceptInList() = !hasAmb(parse(#Exp,"[a,a]"));

public test bool \exceptInOpt() {
  try {
    parse(#Opt,"a,a?");
    return false;
  }
  catch value x: {
    return true;
  }
}

public test bool \exceptInSeq1() {
  try {
    parse(#Seq,"a,a a");
    return false;
  }
  catch value x: {
    return true;
  }
}

public test bool \exceptInSeq2() {
  try {
    parse(#Seq,"a+a 1");
    return false;
  }
  catch value x: {
    return true;
  }
}

public test bool \exceptInAlt() = !hasAmb(parse(#Alt,"a,a"));