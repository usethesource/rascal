module tests::functionality::ConcretePatternTests2

import ParseTree;
layout Whitespace = [\ ]*;
lexical IntegerLiteral = [0-9]+; 
lexical Identifier = [a-z]+;

syntax Exp 
  = IntegerLiteral  
  | Identifier        
  | bracket "(" Exp ")"     
  > left Exp "*" Exp        
  > left Exp "+" Exp  
  | Exp "==" Exp      
  ;

syntax Stat 
   = Identifier ":=" Exp
   | "if" Exp "then" {Stat ";"}* "else" {Stat ";"}* "fi"
   ;

public test bool concreteMatch201() = (Exp) `1` := [Exp] "1";
public test bool concreteMatch202() = (Exp) `1*2` := [Exp] "1*2";
public test bool concreteMatch203() = (Exp) `1 *2` := [Exp] "1* 2";
public test bool concreteMatch204() = (Exp) `1==2` := [Exp] "1==2";

public test bool concreteMatch205() =   (Exp) `<Exp e>` := [Exp] "1";

public test bool concreteMatch206() = (Stat) `a:=1` := [Stat] "a:=1";
public test bool concreteMatch207() = (Stat) `<Identifier x> := <Exp e>` := [Stat] "a:=      1" && "<x>" == "a" && "<e>" == "1";
public test bool concreteMatch208() = (Stat) `<Identifier x> := <Exp e1> + <Exp e2>` := [Stat] "a:= 10 + 20" && "<x>" == "a" && "<e1>" == "10" && "<e2>" == "20";
public test bool concreteMatch209() = (Stat) `if x then a := 1 else b:=2 fi` := [Stat] "if x then a := 1 else b:=2 fi";
public test bool concreteMatch210() = (Stat) `if <Exp c> then <{Stat ";"}* th> else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi" && "<c>" == "x" && "<th>" == "a := 1;b:=2" && "<el>" == "c:=3";


public test bool concreteMatch211() = (Stat) `if <Exp c> then <{Stat ";"}* th> else <Identifier lhs>:=<Exp rhs> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi" && "<c>" == "x" && "<th>" == "a := 1;b:=2" && "<lhs>" == "c" && "<rhs>" == "3";
public test bool concreteMatch212() = (Stat) `if <Exp c> then a:=1;<{Stat ";"}* th> else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi" && "<c>" == "x" && "<th>" == "b:=2" && "<el>" == "c:=3";
public test bool concreteMatch213() = (Stat) `if <Exp c> then a:=1;<{Stat ";"}* th>;b:=2 else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi" && "<c>" == "x" && "<th>" == "" && "<el>" == "c:=3";
public test bool concreteMatch214() = (Stat) `if <Exp c> then a:=1;b:=2;<{Stat ";"}* th> else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi" && "<c>" == "x" && "<th>" == "" && "<el>" == "c:=3";
public test bool concreteMatch215() = (Stat) `if <Exp c> then <{Stat ";"}* th1>;a := 1;<{Stat ";"}* th2> else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi" && "<th1>" == "" && "<th2>" == "b:=2" && "<el>" == "c:=3";
public test bool concreteMatch216() = (Stat) `if <Exp c> then <{Stat ";"}* th1>;a := 1;<{Stat ";"}* th2>; d := 5 else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2;d:=5 else c:=3 fi" && "<th1>" == "" && "<th2>" == "b:=2" && "<el>" == "c:=3";
